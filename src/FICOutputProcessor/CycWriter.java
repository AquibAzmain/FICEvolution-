package FICOutputProcessor;

import GHObjects.GHCommit;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CycWriter {
    private static final String RESULT_DIR = "results/";
    private static final String FILE_NAME = "/cyc.csv";
    List<Double> ficCycs, nonFicCycs;

    public CycWriter() {
        ficCycs = new ArrayList<>();
        nonFicCycs = new ArrayList<>();
    }

    public void extractCycs(List<GHCommit> commits){
        for(GHCommit commit : commits){
            double avgCyc = commit.getAverageCyc();
//            System.out.println("\t" + commit.getSha() + " " + avgCyc);
            if(avgCyc<0)
                continue;

            if(commit.isFIC())
                ficCycs.add(avgCyc);
            else
                nonFicCycs.add(avgCyc);
        }
    }

    public void printCycResult(String filePath) throws FileNotFoundException {
        System.out.println("<Printing Cyc Result>");

        String fileName = RESULT_DIR + filePath + FILE_NAME;
        PrintWriter csvContentWriter = new PrintWriter(fileName);

        csvContentWriter.println("FIC,Non_FIC");

        int max = getMaxListSize();
        for(int i=0; i<max; i++){
            String ficCyc = getSentiFromList(ficCycs, i);
            String nonFicCyc = getSentiFromList(nonFicCycs, i);

            csvContentWriter.println(ficCyc + "," + nonFicCyc);
        }

        csvContentWriter.close();
    }

    private String getSentiFromList(List<Double> locList, int index) {
        if(index >= locList.size())
            return "";
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        return numberFormat.format(locList.get(index));
    }

    private int getMaxListSize() {
        if(ficCycs.size()> nonFicCycs.size())
            return ficCycs.size();
        return nonFicCycs.size();
    }

}
