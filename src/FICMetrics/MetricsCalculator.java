package FICMetrics;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;


public class MetricsCalculator {
	public List<GHMethod> countCylomaticComplexity(String fileContent, List<Integer> fileLines) {
		List<GHMethod> FICMethods = calculateCC(fileContent, fileLines);
		System.out.println("FIC Methods:"+FICMethods);
		return FICMethods;
	}

	public List<Integer> countCylomaticComplexityForPreviousVersion(String fileContentNew, String fileContentPrevious,
			List<Integer> fileLines) {
		List<GHMethod> FICMethodsNew = calculateCC(fileContentNew, fileLines);
		List<GHMethod> FICMethodsPrevious = calculateCC(fileContentPrevious, fileLines);
//		System.out.println(FICMethodsNew);
//		System.out.println(FICMethodsPrevious);
		List<GHMethod> FilteredMethods = new ArrayList<GHMethod>();
		for (int i = 0; i < FICMethodsNew.size(); i++) {			
			for (int j =0; j< FICMethodsPrevious.size();j++) {
				if (FICMethodsNew.get(i).getMethodName().equals(FICMethodsPrevious.get(j).getMethodName())) {
					GHMethod method = new GHMethod(FICMethodsPrevious.get(j).getMethodName(), FICMethodsPrevious.get(j).getCyclomaticComplexity());
					FilteredMethods.add(method);
				}
			}	
		}

		List<Integer> cycs = new ArrayList<>();
		for(GHMethod ghMethod : FilteredMethods)
			cycs.add(ghMethod.getCyclomaticComplexity());

		return cycs;

	}

	public List<GHMethod> calculateCC(String fileContent, List<Integer> fileLines) {
		CompilationUnit cu = null;
		try{
			cu = JavaParser.parse(fileContent);
		} catch (ParseProblemException ppe) {
			System.out.println("\n PROBLEM CONTENT:\n" + fileContent);
		}
		Counter counter = new Counter(fileLines);
		counter.visit(cu, fileContent);
		return counter.FICMethods;

	}

	public int countLOC(String fileContent, List<Integer> changedLines) {
		Counter counter = new Counter();
		CompilationUnit cu = JavaParser.parse(fileContent);
		JavaParser.getStaticConfiguration().setAttributeComments(true);
		Integer lineOfCode = counter.getLineOfCodeWithoutBlackLine(cu.toString());

		List<Comment> comments = cu.getComments();
		int comment_FIC = 0;
		for (Comment comment : comments) {
			int commentStartLineNumber = comment.getBegin().get().line;
			int commentEndLineNumber = comment.getEnd().get().line;
			for (int number : changedLines) {
				if (number>=commentStartLineNumber && number<=commentEndLineNumber) {
					comment_FIC++;
				}
			}
		}

		if(changedLines.size()==comment_FIC)
			return -1;
		return lineOfCode;
	}

	public boolean invalidFile(String fileContent) {
		CompilationUnit cu = null;
		try{
			cu = JavaParser.parse(fileContent);
		} catch (ParseProblemException ppe) {
			printInvalidContent(fileContent);
		}
		if(cu==null)
			return true;
		return false;
	}

	private void printInvalidContent(String fileContent) {
		for(String line : fileContent.split("\n"))
			System.out.println("\t\t\t\t" + line);
	}
}
