package GHAnalyzer;

import java.util.Arrays;

public class VersionExtractor {
    public static String extractVersionForProject(String projectName, String tagName){
        String version = null;
        switch (projectName) {
            case "mockito/mockito":
                return versionExtraction1(tagName);
            case "google/guava":
                return versionExtraction1(tagName);
            case "apache/commons-lang":
                return versionExtraction2(tagName);
            case "elastic/elastic-search":
                return versionExtraction3(tagName);
        }

        return version;
    }

    public static String extractSeparatorForProject(String projectName){
        String version = null;
        switch (projectName) {
            case "mockito/mockito":
                return "\\.";
            case "google/guava":
                return "\\.";
            case "apache/commons-lang":
                return "_";
            case "elastic/elastic-search":
                return "_";
        }

        return version;
    }

    private static String versionExtraction3(String name) {
        name = name.substring(10);
        if(!name.startsWith("Elasticsearch "))
            return null;
        for(String v : Arrays.asList(name.substring(14).split("\\."))){
            if(!v.matches("^[0-9]+$"))
                return null;
        }
        return name.substring(5);
    }

    private static String versionExtraction2(String name) {
        name = name.substring(10);
        if(!name.startsWith("LANG"))
            return null;
        for(String v : Arrays.asList(name.substring(5).split("\\_"))){
            if(!v.matches("^[0-9]+$"))
                return null;
        }
        return name.substring(5);
    }

    private static String versionExtraction1(String name) {
        name = name.substring(10);
        if(!name.startsWith("v"))
            return null;
        for(String v : Arrays.asList(name.substring(1).split("\\."))){
            if(!v.matches("^[0-9]+$"))
                return null;
        }
        return name.substring(1);
    }
}
