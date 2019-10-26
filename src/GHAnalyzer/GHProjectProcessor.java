package GHAnalyzer;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;

public class GHProjectProcessor {
    public static final String ANALYSIS_PROJECTS_DIR = "analysis_projects";

    private Git git;
    private Repository repository;

    public GHProjectProcessor() {
    }

    public void openRepo(String projectPath) throws IOException, GitAPIException {
        String projectName = projectPath.split("/")[1];

        String filePath = "../" + ANALYSIS_PROJECTS_DIR + "/" + projectName + "/";
        File index = new File(filePath);
        if (index.exists()) {
            System.out.println("Hmm");
            git = Git.open(new File(filePath));
            git.checkout();
        }
        else {
            System.out.println("Cloning project ...");
            git = Git.cloneRepository()
                    .setURI( "https://github.com/" + projectPath + ".git" )
                    .setDirectory(new File(filePath))
                    .call();
        }

        repository = git.getRepository();
    }

    public Git getGit() {
        return git;
    }

    public Repository getRepository() {
        return repository;
    }
}
