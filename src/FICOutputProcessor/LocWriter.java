package FICOutputProcessor;

import GHObjects.GHCommit;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LocWriter {
    private static final String RESULT_DIR = "results/";
    private static final String FILE_NAME = "/loc.csv";
    List<Double> ficLocs, nonFicLocs;

    public LocWriter() {
        ficLocs = new ArrayList<>();
        nonFicLocs = new ArrayList<>();
    }

    public void extractLocs(List<GHCommit> commits){
        for(GHCommit commit : commits){
            double avgLoc = commit.getAverageLOC();
//            System.out.println("\t" + commit.getSha() + " " + avgLoc);
            if(avgLoc<0)
                continue;

            if(commit.isFIC())
                ficLocs.add(avgLoc);
            else
                nonFicLocs.add(avgLoc);
        }
    }

    public void printLocResult(String filePath) throws FileNotFoundException {
        System.out.println("<Printing LOC Result>");

        String fileName = RESULT_DIR + filePath + FILE_NAME;
        PrintWriter csvContentWriter = new PrintWriter(fileName);

        csvContentWriter.println("FIC,Non_FIC");

        int max = getMaxListSize();
        for(int i=0; i<max; i++){
            String ficLoc = getSentiFromList(ficLocs, i);
            String nonFicLoc = getSentiFromList(nonFicLocs, i);

            csvContentWriter.println(ficLoc + "," + nonFicLoc);
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
        if(ficLocs.size()>nonFicLocs.size())
            return ficLocs.size();
        return nonFicLocs.size();
    }

}
