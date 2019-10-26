package GHAnalyzer;

import GHObjects.GHCommit;
import GHObjects.GHFile;
import GHObjects.GHTag;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.IOException;
import java.util.List;

public class CommitAnalyzer {
    private Repository repository;

    public CommitAnalyzer(Repository repository) {
        this.repository = repository;
    }

    public void analyzeCommits(GHTag tag) throws IOException {
        for(GHCommit commit : tag.getCommits()){
            analyzeCommitDiffs(commit);
        }
    }

    private void analyzeCommitDiffs(GHCommit commit) throws IOException {
        RevCommit revCommit = commit.getRevCommit();
        if(revCommit.getParentCount()>0){
            ObjectId parentCommitId = revCommit.getParent(0).getTree();
            ObjectId childCommitId = revCommit.getTree();
            diffWithFormatter(parentCommitId, childCommitId, commit);
        }
    }

    private void diffWithFormatter(ObjectId parentCommitId, ObjectId currentCommitId, GHCommit commit) throws IOException {
        DiffFormatter df = new DiffFormatter(System.out);
        df.setRepository(repository);
        df.setDiffComparator(RawTextComparator.DEFAULT);
        df.setDetectRenames(true);
        List<DiffEntry> diffs = df.scan(parentCommitId, currentCommitId);

        for (DiffEntry diff : diffs) {
            String filePath = diff.getNewPath();

            if(!filePath.endsWith(".java") || isFileCreation(filePath, commit.getRevCommit().getParent(0)))
                continue;

//            GHFile ghFile;
//            if(!commit.isFIC()) {
//                ghFile = new GHFile(filePath);
//                extractChangedLines(df, diff, ghFile);
//            }
//            else{
//                ghFile = commit.findGHFile(filePath);
//            }

            GHFile ghFile = new GHFile(filePath);
            extractChangedLines(df, diff, ghFile);
            ghFile.setCurrentContent(extractFileContent(commit.getRevCommit(), ghFile.getPath()));
            ghFile.setPreviousContent(extractFileContent(commit.getRevCommit().getParent(0), ghFile.getPath()));
            commit.addChangedFile(ghFile);

            commit.setConsideration();
        }
    }

    private boolean isFileCreation(String filePath, RevCommit revCommit) throws IOException {
        RevTree tree = revCommit.getTree();
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        treeWalk.setFilter(PathFilter.create(filePath));
        if (!treeWalk.next())
            return true;
        return false;
    }

    private void extractChangedLines(DiffFormatter df, DiffEntry diff, GHFile ghFile) throws IOException {
        for (Edit edit : df.toFileHeader(diff).toEditList()) {
            int start = edit.getBeginA()+1;
            int finish = edit.getEndA();

            Edit.Type type = edit.getType();
            if(type.equals(Edit.Type.INSERT)){
                start = edit.getBeginB()+1;
                finish = edit.getEndB();
            }
            else if(type.equals(Edit.Type.REPLACE)){
                finish = edit.getEndB();
            }

            ghFile.setAB(edit.getBeginA(), edit.getBeginB(), edit.getEndA(), edit.getEndB());

            ghFile.addChangedLines(start, finish);
        }
    }

    private String extractFileContent(RevCommit commit, String filePath) throws IOException {
        String content = "";
        RevTree tree = commit.getTree();

        // now try to find a specific file
        try (TreeWalk treeWalk = new TreeWalk(repository)) {
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            treeWalk.setFilter(PathFilter.create(filePath));

            if (!treeWalk.next()) {
                System.out.println("Did not find expected file \'" + filePath + "\'");
            }

            ObjectId objectId = treeWalk.getObjectId(0);
            ObjectLoader loader = repository.open(objectId);

            content = new String(loader.getBytes());
        }

        return content;
    }
}
