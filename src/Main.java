import FICOutputProcessor.CycWriter;
import FICOutputProcessor.FrequencyWriter;
import FICOutputProcessor.IntervalWriter;
import FICOutputProcessor.LocWriter;
import GHAnalyzer.*;
import GHObjects.GHCommit;
import GHObjects.GHTag;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private Git git;
    private Repository repository;
    private List<GHTag> tags;
    private List<GHCommit> allCommits;

    public static void main(String[] args) throws IOException, GitAPIException {
        Main main = new Main();
        main.run();
    }

    private void run() throws IOException, GitAPIException {
        List<String> projects = getProjects();
        for(String project : projects){
            System.out.println("BEGIN ANALYSIS for " + project);
            init();
            collectData(project);
            outputData(project);
        }
    }

    private void outputData(String project) throws FileNotFoundException {
        String projectName = project.split("/")[1];
        outputLoc(projectName);
        outputCyc(projectName);
        outputInterval(projectName);
        outputFreq(projectName);
    }

    private void outputFreq(String project) throws FileNotFoundException {
        FrequencyWriter frequencyWriter = new FrequencyWriter();
        for(GHTag tag : tags){
            frequencyWriter.extractFreq(tag);
        }
        frequencyWriter.printFreqResult(project);
    }

    private void outputInterval(String project) throws FileNotFoundException {
        IntervalWriter intervalWriter = new IntervalWriter();
//        intervalWriter.extractIntv(allCommits);
        List<GHCommit> tempCommits = new ArrayList<>();
        for(GHTag tag : tags)
            tempCommits.addAll(tag.getCommits());
        intervalWriter.extractIntv(tempCommits);
        intervalWriter.printIntvResult(project);
    }

    private void outputCyc(String project) throws FileNotFoundException {
        CycWriter cycWriter = new CycWriter();
//        cycWriter.extractLocs(allCommits);
        for(GHTag tag : tags)
            cycWriter.extractCycs(tag.getCommits());
        cycWriter.printCycResult(project);
    }

    private void outputLoc(String project) throws FileNotFoundException {
        LocWriter locWriter = new LocWriter();
//        locWriter.extractLocs(allCommits);
        for(GHTag tag : tags)
            locWriter.extractLocs(tag.getCommits());
        locWriter.printLocResult(project);
    }

    private void collectData(String project) throws IOException, GitAPIException {
        openRepo(project);
        extractTags(project);
        labelFICs(project);
//            classifyCommits(project);
        analyzeCommits();
        analyzeMetrics();
    }

    private void init() {
        git = null;
        repository = null;
        tags = new ArrayList<>();
        allCommits = new ArrayList<>();
    }

    private void labelFICs(String project) throws IOException {
        FICCollector ficCollector = new FICCollector();
        ficCollector.listFicShas(project.split("/")[1]);
        ficCollector.labelFics(allCommits);
    }

    private void analyzeMetrics() {
        System.out.println("\n\nMETRICS ANALYSIS INITIATED: ");

        MetricsAnalyzer metricsAnalyzer = new MetricsAnalyzer();
        for(GHTag tag : tags) {
            metricsAnalyzer.extractMetrics(tag.getCommits());
        }
    }

    private void analyzeCommits() throws IOException {
        System.out.println("\n\nCOMMIT ANALYSIS INITIATED: ");

        CommitAnalyzer commitAnalyzer = new CommitAnalyzer(repository);
        for(GHTag tag : tags) {
            commitAnalyzer.analyzeCommits(tag);
        }
    }

    private void classifyCommits(String projectPath) {
        CommitClassifier commitClassifier = new CommitClassifier();
        commitClassifier.importCommitData(projectPath);
        commitClassifier.classifyCommits(tags);
    }

    private void extractTags(String projectName) throws GitAPIException, IOException {
        GHTagLister tagLister = new GHTagLister(git, repository, projectName);
        tagLister.listAndSortTags();
        tagLister.assignCommitsToTags();
        tags = tagLister.getTags();
        allCommits = tagLister.getAllCommits();
    }

    private void openRepo(String projectPath) throws IOException, GitAPIException {
        GHProjectProcessor projectProcessor = new GHProjectProcessor();
        projectProcessor.openRepo(projectPath);
        git = projectProcessor.getGit();
        repository = projectProcessor.getRepository();
    }

    private List<String> getProjects() {
        return Arrays.asList(
//                "google/guava"
//                "mockito/mockito"
//                "apache/commons-lang"
//                "apache/hadoop"
//                "elastic/elasticsearch"
//                "SeleniumHQ/selenium"
//                "spring-projects/spring-framework"
                "apache/tomcat"
        );

    }
}
