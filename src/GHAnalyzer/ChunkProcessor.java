package GHAnalyzer;

import GHObjects.GHCommit;
import GHObjects.GHTag;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChunkProcessor {

    public static final int CHUNK_NUM = 10;

    public void processChunks(GHTag tag){
        List<GHCommit> commits = tag.getCommits();
        commits.sort(commitDateComparator);

        DateTime startDate = new DateTime(commits.get(0).getDate());
        DateTime endDate = new DateTime(commits.get(commits.size()-1).getDate());

        List<Interval> list = getTimeChunks(startDate, endDate);

        System.out.println("\t" + tag);
        Interval interval = list.get(list.size()-1);
        if(interval.toDurationMillis()!=0){
            System.out.println("AAAAAAAAAAAA");
        }

//        for(Interval interval : list){
//            System.out.println("\t\t" + interval.getStart() + " - " +
//                    interval.getEnd() + " - " +
//                    interval.toDurationMillis());
//        }
    }

    private List<Interval> getTimeChunks(DateTime startDate, DateTime endDate) {
        List<Interval> list = new ArrayList<>();

        long millis = startDate.getMillis();
        long chunkSize = (endDate.getMillis() - startDate.getMillis()) / CHUNK_NUM;

        for(int i = 0; i < CHUNK_NUM; ++i) {
            list.add(new Interval(millis, millis += chunkSize));
        }

        list.add(new Interval(millis, endDate.getMillis()));

        return list;
    }

    private Comparator<? super GHCommit> commitDateComparator = new Comparator<GHCommit>() {
        @Override
        public int compare(GHCommit ghCommit1, GHCommit ghCommit2) {
            return ghCommit1.getDate().compareTo(ghCommit2.getDate());
        }
    };
}
