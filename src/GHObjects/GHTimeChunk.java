package GHObjects;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class GHTimeChunk {
    private DateTime start, end;
    private List<GHCommit> commits;
    private int ficNum;

    public GHTimeChunk(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
        ficNum = 0;
        commits = new ArrayList<>();
    }

    public int getFicNum() {
        return ficNum;
    }

    public void addCommits(List<GHCommit> ghCommits) {
        for(GHCommit commit : ghCommits){
            DateTime postDate = new DateTime(commit.getDate());
            if(postDate.isBefore(start) || postDate.isEqual(start))
                continue;
            if(postDate.isAfter(end))
                return;
            commits.add(commit);
            if(commit.isFIC())
                ficNum++;
        }
    }

    public void addCommit(GHCommit ghCommit) {
        commits.add(ghCommit);
        if (ghCommit.isFIC())
            ficNum++;
    }

    public double getFicRatio(){
        if(commits.size()==0)
            return 0d;
        return (double) ficNum/commits.size();
    }

    public List<GHCommit> getCommits() {
        return commits;
    }
}
