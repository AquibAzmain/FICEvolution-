package FICMetrics;

public class GHMethod {
	private String methodName;
	private int cyclomaticComplexity;
	
	public GHMethod(String methodName, int cyclomaticComplexity) {
		super();
		this.methodName = methodName;
		this.cyclomaticComplexity = cyclomaticComplexity;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public int getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}
	
	public void setCyclomaticComplexity(int cyclomaticComplexity) {
		this.cyclomaticComplexity = cyclomaticComplexity;
	}
	@Override
	public String toString() {
		return "[Method Name=" + methodName + ", Cyclomatic Complexity=" + cyclomaticComplexity + "]";
	}
}
