package GHAnalyzer;

import GHObjects.GHCommit;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FICCollector {
    private final String FIC_DIR = "fix_inducing_commits/";
    private final String FIC_EXT = "_commits.txt";

    private Set<String> ficShas;

    public FICCollector(){
        ficShas = new HashSet<>();
    }

    public void listFicShas(String projectName) throws IOException {
        File listFile = new File(FIC_DIR + projectName + FIC_EXT);

        if(listFile.exists()){
            FileReader fr=new FileReader(listFile);
            BufferedReader br=new BufferedReader(fr);
            String line;
            while((line=br.readLine())!=null)
            {
                ficShas.add(line);
            }
            fr.close();
        }
    }

    public void labelFics(List<GHCommit> commits){
        for(GHCommit commit : commits){
            if(ficShas.contains(commit.getSha()))
                commit.setFIC(true);
        }
    }
}
