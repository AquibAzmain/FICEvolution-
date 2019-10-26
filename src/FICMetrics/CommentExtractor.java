package FICMetrics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;



public class CommentExtractor {
	
	private static final String FILE_PATH = "G:/Eclipse/Metrics/Test/ReversePolishNotation.java";
	private static String TEST_FILE = "G:/Eclipse/Metrics/Test/LineNumbers.txt";
	private static Scanner fileScanner;
	static List<String> FICLines= new ArrayList<String>();
	
	public static void main(String[] args) throws Exception {
		extractComment(FILE_PATH);
	}
	
	public static void extractComment(String FILE_PATH) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(TEST_FILE));
		String st;
		while ((st = br.readLine()) != null)
			FICLines.add(st);
		br.close();
		
		System.out.println("File Name: "+FILE_PATH);
		
		JavaParser.getStaticConfiguration().setAttributeComments(true);
		CompilationUnit compilationUnit = JavaParser.parse(new FileInputStream(FILE_PATH));
		List<Comment> comments = compilationUnit.getComments();
		
		
		Integer numberOfComments = comments.size();
		int comment_FIC = 0;
		for (Comment comment : comments) {
			//System.out.print("Comment: "+comment.toString());
			//.out.println("Comment Type: "+detectTypeOfComment(comment));
			int commentStartLineNumber = comment.getBegin().get().line;
			int commentEndLineNumber = comment.getEnd().get().line;
			for (String number : FICLines) {
			    if (Integer.parseInt(number)>=commentStartLineNumber && Integer.parseInt(number)<=commentEndLineNumber) {
			    	comment_FIC++;
			    }
			}
		}
		System.out.println("Total FIC:"+ FICLines.size());
		System.out.println("Comment FIC:"+ comment_FIC);
		
		JavaParser.getStaticConfiguration().setAttributeComments(false);
		Integer lineOfCode = getLineOfCode(FILE_PATH);
		
        System.out.println("File LOC: "+ lineOfCode);
        System.out.println("Number of Comments: "+numberOfComments);
        System.out.println("Comment Percentage: "+((numberOfComments*100)/lineOfCode)+"%");
	}
	
	@SuppressWarnings("unused")
	private static String detectTypeOfComment (Comment comment) {
		if(comment.isBlockComment()) return "Block Comment";
		if(comment.isJavadocComment()) return "Java Doc Comment";
		if(comment.isLineComment()) return "Line Comment";
		return "Cannot Identify Comment Type";
	}
	
	private static int getLineOfCode(String file) throws FileNotFoundException {
		File f = new File(file);
		fileScanner = new Scanner(f);

        int lineNumber = 0;
        while(fileScanner.hasNextLine()){
            fileScanner.nextLine();
            lineNumber++;
        }
        return lineNumber;
	}

}
