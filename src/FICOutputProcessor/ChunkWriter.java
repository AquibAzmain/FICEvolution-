package FICOutputProcessor;

import GHObjects.GHTag;
import GHObjects.GHTimeChunk;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChunkWriter {
    private static final String RESULT_DIR = "results/";
    private static final String FILE_NAME = "/chunks.csv";
    List<TagChunks> tagChunksList;

    public ChunkWriter() {
        tagChunksList = new ArrayList<>();
    }

    public void extractChunks(GHTag tag){
        TagChunks tagChunks = new TagChunks(tag.getVersion());
        for(GHTimeChunk chunk : tag.getChunks()){
            tagChunks.addChunkFicRatio(chunk.getFicRatio());
        }
        tagChunksList.add(tagChunks);
    }

    public void printChunkResult(String filePath) throws FileNotFoundException {
        System.out.println("<Printing Chunk Result>");

        String fileName = RESULT_DIR + filePath + FILE_NAME;
        PrintWriter csvContentWriter = new PrintWriter(fileName);

        csvContentWriter.println("Version, Early, Middle, Late");

        for(TagChunks tagChunks : tagChunksList){
            csvContentWriter.println(tagChunks.getInfo());
        }

        csvContentWriter.close();
    }
}

class TagChunks{
    String tagName;
    List<String> chunkFicRatios;

    TagChunks(String version) {
        tagName = version;
        chunkFicRatios = new ArrayList<>();
    }

    public String getInfo() {
        String info = tagName;
        for(String ratio : chunkFicRatios){
            info += "," + ratio;
        }
        return info;
    }

    @Override
    public String toString() {
        return "TagInfo{" +
                "tagName='" + tagName + '\'' +
                '}';
    }

    public void addChunkFicRatio(double ficRatio) {
        DecimalFormat df = new DecimalFormat("#.##");
        chunkFicRatios.add(df.format(ficRatio));
    }
}
