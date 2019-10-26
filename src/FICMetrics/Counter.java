package FICMetrics;

import static com.github.javaparser.JavaParser.parseBodyDeclaration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Counter extends VoidVisitorAdapter<String> {

	public int FinalComplexity = 0;
	public int TotalCC = 0;
	private List<Integer> FICLines;
	public List<GHMethod> FICMethods = new ArrayList<GHMethod>();

	public Counter(List<Integer> FICLines) {
		this.FICLines = FICLines;
	}

	public Counter() {
	}

	public void visit(MethodDeclaration md, String fileContent) {
		Set<String> FICMethodList = new HashSet<String>();
		int methodStartLineNumber = md.getBegin().get().line;
		int methodEndLineNumber = md.getEnd().get().line;
		for (int number : FICLines) {
			if (number >= methodStartLineNumber && number <= methodEndLineNumber) {
				FICMethodList.add(md.getNameAsString());
			}
		}
		if (FICMethodList.contains(md.getNameAsString())) {
			if (!md.isAbstract()) {
				String methodBody = md.getChildNodes().get(md.getChildNodes().size() - 1).toString();
				if (methodBody.startsWith("{")) {
					BodyDeclaration<?> methodToParse = parseBodyDeclaration(methodBody);
					FinalComplexity = processSwitch(methodToParse) + processCondition(methodToParse)
							+ processLoop(methodToParse) + 1;
					TotalCC = TotalCC + FinalComplexity;
					GHMethod method = new GHMethod(md.getNameAsString(), FinalComplexity);
					this.FICMethods.add(method);
				} else {
					FinalComplexity = 0;
					GHMethod method = new GHMethod(md.getNameAsString(), FinalComplexity);
					this.FICMethods.add(method);

				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	static int processCondition(Node node) {
		int complexity = 0;
		for (IfStmt ifStmt : node.getChildNodesByType(IfStmt.class)) {
			complexity++;
			if (ifStmt.getElseStmt().isPresent()) {
				if (ifStmt.getElseStmt().get() instanceof IfStmt) {
				}
			}
		}
		return complexity;
	}

	@SuppressWarnings("deprecation")
	static int processSwitch(Node node) {
		int complexity = 0;
		for (SwitchStmt switchStmt : node.getChildNodesByType(SwitchStmt.class)) {
			if (switchStmt.isSwitchStmt()) {
				complexity = complexity + switchStmt.getChildNodes().size() - 1;
			}
		}
		return complexity;
	}

	@SuppressWarnings("deprecation")
	static int processLoop(Node node) {
		int complexity = 0;
		for (ForStmt forStmt : node.getChildNodesByType(ForStmt.class)) {
			if (forStmt.isForStmt()) {
				complexity++;
			}
		}
		for (ForeachStmt foreachStmt : node.getChildNodesByType(ForeachStmt.class)) {
			if (foreachStmt.isForeachStmt()) {
				complexity++;
			}
		}
		for (WhileStmt whileStmt : node.getChildNodesByType(WhileStmt.class)) {
			if (whileStmt.isWhileStmt()) {
				complexity++;
			}
		}
		for (DoStmt doStmt : node.getChildNodesByType(DoStmt.class)) {
			if (doStmt.isDoStmt()) {
				complexity++;
			}
		}

		return complexity;
	}

	public int getLineOfCodeWithoutBlackLine(String codeToBeAnalysed) {
		InputStream codeStream = new ByteArrayInputStream(codeToBeAnalysed.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(codeStream));
		int i = 0;
		boolean isEOF = false;
		do {
			String t = "";
			try {
				t = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (t != null) {
				isEOF = true;
				t = t.replaceAll("\\n|\\t|\\s", "");
				if ((!t.equals("")) && (!t.startsWith("//"))) {
					i = i + 1;
				}
			} else {
				isEOF = false;
			}
		} while (isEOF);
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

}
