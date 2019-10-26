package GHObjects;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.*;

public class GHCommit {
    private String sha;
    private Boolean isFIC, shouldBeConsidered;
    private RevCommit revCommit;
    private List<GHFile> changedFiles;
    private List<GHCommit> fixerCommits, fixedCommits;
    private Set<String> fixerShas;

    public GHCommit(RevCommit commit) {
        revCommit = commit;
        sha = commit.getName();
        isFIC = false;
        shouldBeConsidered = false;
        changedFiles = new ArrayList<>();
        fixerCommits = new ArrayList<>();
        fixedCommits = new ArrayList<>();
        fixerShas = new HashSet<>();
    }

    public RevCommit getRevCommit() {
        return revCommit;
    }

    public void addChangedFile(GHFile ghFile) {
        changedFiles.add(ghFile);
    }

    public String getSha() {
        return sha;
    }

    public List<GHFile> getChangedFiles() {
        return changedFiles;
    }

    public Boolean isFIC() {
        return isFIC;
    }

    public void setFIC(Boolean FIC) {
        isFIC = FIC;
    }

    @Override
    public String toString() {
        return "GHCommit{" +
                "sha='" + sha + '\'' +
                ", message='" + revCommit.getShortMessage() + '\'' +
                '}';
    }

    public void addFixShas(Set<String> shas) {
        fixerShas.addAll(shas);
    }

    public void addChangedFiles(List<GHFile> ghFiles) {
        changedFiles.addAll(ghFiles);
    }

    public GHFile findGHFile(String filePath) {
        for(GHFile file : changedFiles){
            if(file.getPath().equals(filePath))
                return file;
        }
        return null;
    }

    public void setConsideration() {
        shouldBeConsidered = true;
    }

    public Boolean shouldBeConsidered(){
        return shouldBeConsidered;
    }

    public Date getDate(){
        Date date = revCommit.getCommitterIdent().getWhen();
        return date;
    }

    public double getAverageLOC() {
        int total=0, number=0;
        for(GHFile ghFile : changedFiles){
            int loc = ghFile.getLoc();
            if(loc==-1)
                continue;
            total += loc;
            number++;
        }

        if(number==0)
            return -1d;
        return (double)total/number;
    }

    public double getAverageCyc() {
        double total=0d, number=0;
        for(GHFile ghFile : changedFiles){
            double avgCyc = ghFile.getAvgCyc();
            if(avgCyc==0d)
                continue;
            total += avgCyc;
            number++;
        }

        if(number==0)
            return -1d;
        return total/number;
    }
}
