package FICOutputProcessor;

import GHObjects.GHCommit;
import GHObjects.GHTag;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FrequencyWriter {
    private static final String RESULT_DIR = "results/";
    private static final String FILE_NAME = "/freq.csv";
    List<TagInfo> tagInfos;

    public FrequencyWriter() {
        tagInfos = new ArrayList<>();
    }

    public void extractFreq(GHTag tag){
        TagInfo tagInfo = new TagInfo(tag.getVersion());
        for(GHCommit commit : tag.getCommits()){
            if(commit.isFIC())
                tagInfo.addFIC();
            else
                tagInfo.addNonFIC();
        }
        tagInfos.add(tagInfo);
    }

    public void printFreqResult(String filePath) throws FileNotFoundException {
        System.out.println("<Printing Freq Result>");

        String fileName = RESULT_DIR + filePath + FILE_NAME;
        PrintWriter csvContentWriter = new PrintWriter(fileName);

        csvContentWriter.println("Version, FIC frequency, Non FIC frequency, FIC ration");

        for(TagInfo tagInfo : tagInfos){
            csvContentWriter.println(tagInfo.getInfo());
        }

        csvContentWriter.close();
    }
}

class TagInfo{
    String tagName;
    int ficCount, nonFicCount;

    TagInfo(String version) {
        tagName = version;
        ficCount = 0;
        nonFicCount = 0;
    }


    public void addFIC() {
        ficCount++;
    }


    public void addNonFIC() {
        nonFicCount++;
    }

    public String getInfo() {
        return tagName + ',' +
                ficCount + ',' +
                nonFicCount + ',' +
                getRatio();
    }

    private Double getRatio() {
        return (double)ficCount/(ficCount+nonFicCount);
    }

    @Override
    public String toString() {
        return "TagInfo{" +
                "tagName='" + tagName + '\'' +
                ", ficCount=" + ficCount +
                ", nonFicCount=" + nonFicCount +
                '}';
    }
}
