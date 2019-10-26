package GHObjects;

import java.util.ArrayList;
import java.util.List;

public class GHFile {
    private String path;
    private List<Integer> changedLines;
    private int loc;
    private List<Integer> cycs;
    private String currentContent, previousContent;
    private int beginA, beginB, endA, endB;

    public GHFile(String filePath) {
        path = filePath;
        changedLines = new ArrayList<>();
        cycs = new ArrayList<>();
    }

    public GHFile(String filePath, List<Integer> fixedLines) {
        this(filePath);
        changedLines.addAll(fixedLines);
    }

    public void setCurrentContent(String currentContent) {
        this.currentContent = currentContent;
    }

    public void setPreviousContent(String previousContent) {
        this.previousContent = previousContent;
    }

    public void addChangedLines(int start, int finish) {
        for(int i=start; i<=finish; i++){
            changedLines.add(i);
        }
    }

    public void setAB(int beginA, int beginB, int endA, int endB){
        this.beginA = beginA;
        this.beginB = beginB;
        this.endA = endA;
        this.endB = endB;
    }

    public String getPath() {
        return path;
    }

    public List<Integer> getChangedLines() {
        return changedLines;
    }

    public String getCurrentContent() {
        return currentContent;
    }

    public String getPreviousContent() {
        return previousContent;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public void setCycs(List<Integer> cycs) {
        this.cycs = cycs;
    }

    public int getLoc() {
        return loc;
    }

    public double getAvgCyc(){
        int total=0, number=0;
        for(Integer cyc : cycs){
            total += cyc;
            number++;
        }

        if(number==0)
            return 0d;
        return (double) total/number;
    }

    @Override
    public String toString() {
        return "GHFile{" +
                "path='" + path + '\'' +
                "changed lines='" + changedLines + '\'' +
                "current content=\n" + outputContent(currentContent) +
                "previous content=\n" + outputContent(previousContent) +
                '}';
    }

    private String outputContent(String fileContent) {
        String output = "";
        int count = 1;
        for(String line : fileContent.split("\n")) {
            output += "\t\t\t\t\t" + count + " " + line + "\n";
            count++;
        }
        return output;
    }
}
