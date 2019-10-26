package GHAnalyzer;

import GHObjects.GHCommit;
import GHObjects.GHFile;
import GHObjects.GHTag;

import java.io.File;
import java.util.*;

public class CommitClassifier {
    private Set<String> ficShas;
    private Map<String, Set<String>> shaToFixMap;
    private List<FixedFile> fixedFiles;

    public CommitClassifier(){
        ficShas = new HashSet<>();
        shaToFixMap = new HashMap<>();
        fixedFiles = new ArrayList<>();
    }

    public void importCommitData(String projectPath) {
        File folder = new File(projectPath);

        for (File f : folder.listFiles()) {
            if(f.isDirectory()) {
                String sha = f.getName();
                extractFixedFiles(f, sha);
            }
        }
    }

    private void extractFixedFiles(File folder, String sha) {
        for (File f : folder.listFiles()) {
            if(f.isDirectory()) {
                extractChanges(f, sha);
            }
        }
    }

    private void extractChanges(File folder, String fixSha) {
        for (File f : folder.listFiles()) {
            if(f.isFile()) {
                String[] strs = f.getName().split(":");
                String ficSha = strs[2];
                FixedFile fixedFile = findFixedFile(folder.getName(), ficSha);
                fixedFile.addLines((extractLineNumbers(strs[0])));

                ficShas.add(ficSha);
                addToFixMap(fixSha, ficSha);
            }
        }
    }

    private FixedFile findFixedFile(String name, String ficSha) {
        FixedFile newFixedFile = new FixedFile(name, ficSha);

        for(FixedFile fixedFile : fixedFiles){
            if(fixedFile.equals(newFixedFile))
                return fixedFile;
        }

        return newFixedFile;
    }

    private List<Integer> extractLineNumbers(String str) {
        List<Integer> nums = new ArrayList<>();
        for(String num : Arrays.asList(str.split("_"))){
            nums.add(Integer.parseInt(num));
        }
        return nums;
    }

    private void addToFixMap(String fixSha, String ficSha) {
        if(!shaToFixMap.containsKey(ficSha))
            shaToFixMap.put(ficSha, new HashSet<>());
        Set<String> fixShas = shaToFixMap.get(ficSha);
        fixShas.add(fixSha);
        shaToFixMap.put(ficSha, fixShas);
    }

    public void classifyCommits(List<GHTag> tags){
        for(GHTag tag : tags){
            for(GHCommit commit : tag.getCommits()){
                String sha = commit.getSha();
                if(ficShas.contains(sha)) {
                    commit.setFIC(true);
                    commit.addFixShas(shaToFixMap.get(sha));
                    addChangedFilesToCommit(commit);
                }
            }
        }
    }

    private void addChangedFilesToCommit(GHCommit commit) {
        String sha = commit.getSha();
        for(FixedFile fixedFile : fixedFiles){
            if(fixedFile.getFicSha().equals(sha)){
                GHFile ghFile = new GHFile(fixedFile.getFilePath(), fixedFile.getFixedLines());
                commit.addChangedFile(ghFile);
            }
        }
    }
}

class FixedFile{
    String filePath;
    String ficSha;
    List<Integer> fixedLines;

    FixedFile(String name, String sha) {
        filePath = name;
        ficSha = sha;
        fixedLines = new ArrayList<>();
    }

    void addLines(List<Integer> lines){
        fixedLines.addAll(lines);
    }

    boolean equals(FixedFile f2) {
        if(f2.filePath.equals(filePath) && f2.ficSha.equals(ficSha))
            return true;
        return false;
    }

    String getFicSha() {
        return ficSha;
    }

    String getFilePath() {
        return filePath;
    }

    List<Integer> getFixedLines() {
        return fixedLines;
    }
}
