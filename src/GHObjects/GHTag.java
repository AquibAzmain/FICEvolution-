package GHObjects;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GHTag implements Comparable<GHTag> {
    private String version;
    private Ref ref;
    private List<GHCommit> commits;
    private String separator;

    public GHTag(Ref ref) {
        this.ref = ref;
//        extractVersion(ref.getName());
        commits = new ArrayList<>();
    }

    public void extractVersion(String name){
        name = name.substring(10);
        if(!name.startsWith("v"))
            return;
        for(String v : Arrays.asList(name.substring(1).split("\\."))){
            if(!v.matches("^[0-9]+$"))
                return;
        }
        version = name.substring(1);
    }

    public String getVersion() {
        return version;
    }

    public Ref getRef() {
        return ref;
    }

    public void setRef(Ref ref) {
        this.ref = ref;
    }

    public List<GHCommit> getCommits() {
        return commits;
    }

    public void setCommits(List<GHCommit> commits) {
        this.commits = commits;
    }

    @Override
    public String toString() {
        return "GHTag{" +
                "version='" + version + '\'' +
                ", ref=" + ref.getName() +
                '}';
    }

    @Override
    public int compareTo(GHTag tag2) {
        String version1 = getVersion();
        String version2 = tag2.getVersion();

        String[] nos1 = version1.split(separator);
        String[] nos2 = version2.split(separator);
        int maxlen = nos1.length;

        if(nos1.length<nos2.length){
            nos1 = fillEmptyElements(nos1, nos2);
            maxlen = nos2.length;
        }
        else if(nos1.length>nos2.length){
            nos2 = fillEmptyElements(nos2, nos1);
        }

//        System.out.println(version1 + " " + version2);
//        printSplitString(fnos1);
//        printSplitString(fnos2);

        for(int i=0; i<maxlen; i++){
            int v1 = Integer.parseInt(nos1[i]);
            int v2 = Integer.parseInt(nos2[i]);

            if(v1!=v2)
                return v1-v2;
        }

        return version1.compareTo(version2);
    }

    private String[] fillEmptyElements(String[] strs1, String[] strs2) {
        String[] fstrs = new String[strs2.length];
        for(int i=0; i<strs1.length; i++){
            fstrs[i] = strs1[i];
        }
        for(int i=strs1.length; i<strs2.length; i++){
            fstrs[i] = "0";
        }
        return fstrs;
    }

    private void printSplitString(String[] strings) {
        System.out.print("\t");
        for(int i=0; i<strings.length; i++){
            System.out.print(strings[i] + " ");
        }
        System.out.println();
    }

    public ObjectId getObjectId(Repository repository) throws IOException {
        Ref peeledRef = repository.getRefDatabase().peel(ref);
        if(peeledRef.getPeeledObjectId() != null) {
            return peeledRef.getPeeledObjectId();
        } else {
            return ref.getObjectId();
        }
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
