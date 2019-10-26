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
            case "apache/hadoop":
                return versionExtraction4(tagName);
            case "SeleniumHQ/selenium":
                return versionExtraction5(tagName);
            case "spring-projects/spring-framework":
                return versionExtraction6(tagName);
            case "apache/tomcat":
                return versionExtraction1(tagName);
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
            case "apache/hadoop":
                return "\\.";
            case "SeleniumHQ/selenium":
                return "\\.";
            case "spring-projects/spring-framework":
                return "\\.";
            case "apache/tomcat":
                return "\\.";
        }

        return version;
    }

    private static String versionExtraction6(String name) {
        name = name.substring(10);
        if(!name.startsWith("v") || !name.endsWith("RELEASE"))
            return null;
        for(String v : Arrays.asList(name.substring(1, name.length()-7).split("\\."))){
            if(!v.matches("^[0-9]+$"))
                return null;
        }
        return name.substring(5);
    }

    private static String versionExtraction5(String name) {
        name = name.substring(10);
        if(!name.startsWith("Selenium"))
            return null;
        for(String v : Arrays.asList(name.substring(9).split("\\."))){
            if(!v.matches("^[0-9]+$"))
                return null;
        }
        return name.substring(5);
    }

    private static String versionExtraction4(String name) {
        name = name.substring(10);
        if(!name.startsWith("rel/release"))
            return null;
        for(String v : Arrays.asList(name.substring(12).split("\\."))){
            if(!v.matches("^[0-9]+$"))
                return null;
        }
        return name.substring(5);
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
