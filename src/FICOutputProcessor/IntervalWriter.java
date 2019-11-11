package FICOutputProcessor;

import GHObjects.GHCommit;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class IntervalWriter {
    private static final String RESULT_DIR = "results/";
    private static final String FILE_NAME = "/intv.csv";
    List<Long> ficIntv, nonFicIntv;
    public IntervalWriter() {
        ficIntv = new ArrayList<>();
        nonFicIntv = new ArrayList<>();
    }

    public void extractIntv(List<GHCommit> commits){
        commits.sort(commitDateComparator);
        Date lastCommitDate=null;

        for(GHCommit commit : commits){
            Date currentDate = commit.getDate();

            if(lastCommitDate!=null){
                long dateDiff = extractDateDiff(lastCommitDate, currentDate);

                if(commit.isFIC())
                    ficIntv.add(dateDiff);
                else
                    nonFicIntv.add(dateDiff);
            }

            lastCommitDate = currentDate;
        }
    }

    private Long extractDateDiff(Date previousDate, Date currentDate){
        long diffInMillies = Math.abs(currentDate.getTime() - previousDate.getTime());
        long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }

    private Comparator<? super GHCommit> commitDateComparator = new Comparator<GHCommit>() {
        @Override
        public int compare(GHCommit ghCommit1, GHCommit ghCommit2) {
            return ghCommit1.getDate().compareTo(ghCommit2.getDate());
        }
    };

    public void printIntvResult(String filePath) throws FileNotFoundException {
        System.out.println("<Printing Interval Result>");

        String fileName = RESULT_DIR + filePath + FILE_NAME;
        PrintWriter csvContentWriter = new PrintWriter(fileName);

        csvContentWriter.println("FIC,Non_FIC");

        int max = getMaxListSize();
        for(int i=0; i<max; i++){
            String ficIntv = getSentiFromList(this.ficIntv, i);
            String nonFicIntv = getSentiFromList(this.nonFicIntv, i);

            csvContentWriter.println(ficIntv + "," + nonFicIntv);
        }

        csvContentWriter.close();
    }

    private String getSentiFromList(List<Long> locList, int index) {
        if(index >= locList.size())
            return "";
        return locList.get(index)+"";
    }

    private int getMaxListSize() {
        if(ficIntv.size()> nonFicIntv.size())
            return ficIntv.size();
        return nonFicIntv.size();
    }

}
