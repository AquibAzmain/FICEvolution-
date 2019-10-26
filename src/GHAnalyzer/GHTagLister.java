package GHAnalyzer;

import GHObjects.GHCommit;
import GHObjects.GHTag;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GHTagLister {
    private List<GHTag> tags = new ArrayList<>();
    private Git git;
    private Repository repository;
    private String projectName;

    public GHTagLister(Git git, Repository repository, String projectName) {
        this.git = git;
        this.repository = repository;
        this.projectName = projectName;
    }

    public List<GHTag> getTags() {
        List<GHTag> reverseTags = new ArrayList<>();
        reverseTags.addAll(tags.subList(1, tags.size()));
        Collections.reverse(reverseTags);
        return reverseTags;
    }

    public void listAndSortTags() throws GitAPIException {
        List<Ref> refs  = git.tagList().call();
        System.out.println(refs.size());

        for (Ref ref : refs) {
            GHTag ghTag = new GHTag(ref);
            String version = VersionExtractor.extractVersionForProject(projectName, ref.getName());
            System.out.println(ref.getName() + " " + version);
            if(version != null) {
                ghTag.setVersion(version);
                ghTag.setSeparator(VersionExtractor.extractSeparatorForProject(projectName));
                tags.add(ghTag);
            }
        }

        Collections.sort(tags);
    }

    public void assignCommitsToTags() throws IOException, GitAPIException {
        GHTag previousTag = tags.get(0);
        for(GHTag tag : tags.subList(1, tags.size())){
            ObjectId from = previousTag.getObjectId(repository);
            ObjectId to = tag.getObjectId(repository);

//            System.out.println(tag);
            int count = 0;
            List<GHCommit> commits = new ArrayList<>();
            for(RevCommit commit :git.log().addRange(from, to).call()){
                commits.add(new GHCommit(commit));
//                System.out.println("\t" + commit.getName());
                count++;
            }
            tag.setCommits(commits);
            previousTag = tag;
//            System.out.println("\tCOUNT: " + count);
        }
    }

    public List<GHCommit> getAllCommits() {
        List<GHCommit> commits = new ArrayList<>();
        for(GHTag tag : tags.subList(1, tags.size())){
            for(GHCommit commit : tag.getCommits()){
                commits.add(commit);
            }
        }
        return commits;
    }
}
