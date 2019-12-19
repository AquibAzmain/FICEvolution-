package GHAnalyzer;

import GHObjects.GHCommit;
import GHObjects.GHTag;
import GHObjects.GHTimeChunk;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChunkProcessor {

    public static final int CHUNK_NUM = 3;
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public void processChunks(GHTag tag){
        System.out.println("\t" + tag);

        List<GHCommit> commits = tag.getCommits();
        commits.sort(commitDateComparator);

        DateTime startDate = new DateTime(commits.get(0).getDate()).minusSeconds(1);
        DateTime endDate = new DateTime(commits.get(commits.size()-1).getDate()).plusSeconds(1);

//        System.out.println("\t" + startDate + " X " + endDate);

        for(GHCommit commit : commits){
            String fic = "-";
            if(commit.isFIC())
                fic = "*";
//            System.out.print(fic + "(" + new DateTime(commit.getDate()) + ") ");
        }
//        System.out.println();

        List<Interval> intervals = getTimeChunks(startDate, endDate);
        List<GHTimeChunk> chunks = new ArrayList<>();
        for(Interval interval : intervals){
//            System.out.print("{" + interval.getStart() + "," + interval.getEnd() + "} ");
            GHTimeChunk chunk = new GHTimeChunk(interval.getStart(), interval.getEnd());
            chunk.addCommits(commits);
            chunks.add(chunk);
        }
//        System.out.println();


        for(GHTimeChunk chunk : chunks){
            System.out.print("{" + chunk.getCommits().size() + "," +
                    chunk.getFicNum() + "," +
                    chunk.getFicRatio() + "} ");
        }
        System.out.println();

        int num = 0;
        for(GHTimeChunk chunk : chunks){
            num += chunk.getCommits().size();
        }
        if(num!=commits.size())
            System.out.println("\tOMFG " + num + " " + commits.size());

        tag.setChunks(chunks);
    }

    private List<Interval> getTimeChunks(DateTime startDate, DateTime endDate) {
        List<Interval> list = new ArrayList<>();

        long millis = startDate.getMillis();
        long chunkSize = (endDate.getMillis() - startDate.getMillis()) / CHUNK_NUM;

        for(int i = 0; i < CHUNK_NUM; ++i) {
            list.add(new Interval(millis, millis += chunkSize));
        }

        return list;
    }

    private Comparator<? super GHCommit> commitDateComparator = new Comparator<GHCommit>() {
        @Override
        public int compare(GHCommit ghCommit1, GHCommit ghCommit2) {
            return ghCommit1.getDate().compareTo(ghCommit2.getDate());
        }
    };
}
