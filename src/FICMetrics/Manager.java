package FICMetrics;

import java.util.ArrayList;
import java.util.List;

public class Manager {
	
	public static void main(String[] args) {
		List<Integer> FICLines= new ArrayList<Integer>();
		FICLines.add(20);
		FICLines.add(50);
		FICLines.add(100);
		FICLines.add(105);
		
		MetricsCalculator mc = new MetricsCalculator();
		mc.countCylomaticComplexityForPreviousVersion(fileContentNewVersion, fileContentPreviousVersion, FICLines);
		//mc.countCylomaticComplexity(fileContentNewVersion, FICLines);
	}
	
	private static String fileContentNewVersion = "package Test;\r\n" + 
			"\r\n" + 
			"import java.util.Stack;\r\n" + 
			"import java.util.stream.Stream;\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"/**\r\n" + 
			" * A Simple Reverse Polish Notation calculator with memory function.\r\n" + 
			" */\r\n" + 
			"public class ReversePolishNotation {\r\n" + 
			"\r\n" + 
			"    // What does this do?\r\n" + 
			"    public static int ONE_BILLION = 1000000000;\r\n" + 
			"\r\n" + 
			"    private double memory = 0;\r\n" + 
			"\r\n" + 
			"    /**\r\n" + 
			"     * Takes reverse polish notation style string and returns the resulting calculation.\r\n" + 
			"     *\r\n" + 
			"     * @param input mathematical expression in the reverse Polish notation format\r\n" + 
			"     * @return the calculation result\r\n" + 
			"     */\r\n" + 
			"    public Double calc(String input) {\r\n" + 
			"\r\n" + 
			"        String[] tokens = input.split(\" \");\r\n" + 
			"        Stack<Double> numbers = new Stack<>();\r\n" + 
			"        String sample = \"sdadada\";\r\n" + 
			"//abc\r\n" + 
			"        Stream.of(tokens).forEach(t -> {\r\n" + 
			"            double a;\r\n" + 
			"            double b;\r\n" + 
			"            switch(t){\r\n" + 
			"                case \"+\":\r\n" + 
			"                    b = numbers.pop();\r\n" + 
			"                    a = numbers.pop();\r\n" + 
			"                    numbers.push(a + b);\r\n" + 
			"                    break;\r\n" + 
			"                case \"/\":\r\n" + 
			"                    b = numbers.pop();\r\n" + 
			"                    a = numbers.pop();\r\n" + 
			"                    numbers.push(a / b);\r\n" + 
			"                    break;\r\n" + 
			"                case \"-\":\r\n" + 
			"                    b = numbers.pop();\r\n" + 
			"                    a = numbers.pop();\r\n" + 
			"                    numbers.push(a - b);\r\n" + 
			"                    break;\r\n" + 
			"                case \"*\":\r\n" + 
			"                    b = numbers.pop();\r\n" + 
			"                    a = numbers.pop();\r\n" + 
			"                    numbers.push(a * b);\r\n" + 
			"                    break;\r\n" + 
			"                default:\r\n" + 
			"                    numbers.push(Double.valueOf(t));\r\n" + 
			"            }\r\n" + 
			"            \r\n" + 
			"            switch(t){\r\n" + 
			"            case \"+\":\r\n" + 
			"                b = numbers.pop();\r\n" + 
			"                a = numbers.pop();\r\n" + 
			"                numbers.push(a + b);\r\n" + 
			"                break;\r\n" + 
			"            case \"/\":\r\n" + 
			"                b = numbers.pop();\r\n" + 
			"                a = numbers.pop();\r\n" + 
			"                numbers.push(a / b);\r\n" + 
			"                break;\r\n" + 
			"            case \"-\":\r\n" + 
			"                b = numbers.pop();\r\n" + 
			"                a = numbers.pop();\r\n" + 
			"                numbers.push(a - b);\r\n" + 
			"                break;\r\n" + 
			"            case \"*\":\r\n" + 
			"                b = numbers.pop();\r\n" + 
			"                a = numbers.pop();\r\n" + 
			"                numbers.push(a * b);\r\n" + 
			"                break;\r\n" + 
			"            default:\r\n" + 
			"                numbers.push(Double.valueOf(t));\r\n" + 
			"        }\r\n" + 
			"        });\r\n" + 
			"        return numbers.pop();\r\n" + 
			"    }\r\n" + 
			"\r\n" + 
			"    /**\r\n" + 
			"     * Memory Recall uses the number in stored memory, defaulting to 0.\r\n" + 
			"     *\r\n" + 
			"     * @return the double\r\n" + 
			"     */\r\n" + 
			"    public double memoryRecall(){\r\n" + 
			"    	int x =0;\r\n" + 
			"    	if (x>0)System.out.println(x);\r\n" + 
			"    	else System.out.println(0);\r\n" + 
			"        return memory;\r\n" + 
			"    }\r\n" + 
			"\r\n" + 
			"    /**\r\n" + 
			"     * Memory Clear sets the memory to 0.\r\n" + 
			"     */\r\n" + 
			"    public void memoryClear(){\r\n" + 
			"        memory = 0;\r\n" + 
			"    }\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"    public void memoryStore(double value){\r\n" + 
			"        memory = value;\r\n" + 
			"    }\r\n" + 
			"}";
	
	
	private static String fileContentPreviousVersion = "package Test;\r\n" + 
			"\r\n" + 
			"import java.util.Stack;\r\n" + 
			"import java.util.stream.Stream;\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"/**\r\n" + 
			" * A Simple Reverse Polish Notation calculator with memory function.\r\n" + 
			" */\r\n" + 
			"public class ReversePolishNotation {\r\n" + 
			"\r\n" + 
			"    // What does this do?\r\n" + 
			"    public static int ONE_BILLION = 1000000000;\r\n" + 
			"\r\n" + 
			"    private double memory = 0;\r\n" + 
			"\r\n" + 
			"    /**\r\n" + 
			"     * Takes reverse polish notation style string and returns the resulting calculation.\r\n" + 
			"     *\r\n" + 
			"     * @param input mathematical expression in the reverse Polish notation format\r\n" + 
			"     * @return the calculation result\r\n" + 
			"     */\r\n" + 
			"    public Double calc(String input) {\r\n" + 
			"\r\n" + 
			"        String[] tokens = input.split(\" \");\r\n" + 
			"        Stack<Double> numbers = new Stack<>();\r\n" + 
			"        String sample = \"sdadada\";\r\n" + 
			"//abc\r\n" + 
			"        Stream.of(tokens).forEach(t -> {\r\n" + 
			"            double a;\r\n" + 
			"            double b;\r\n" + 
			"            switch(t){\r\n" + 
			"                case \"+\":\r\n" + 
			"                    b = numbers.pop();\r\n" + 
			"                    a = numbers.pop();\r\n" + 
			"                    numbers.push(a + b);\r\n" + 
			"                    break;\r\n" + 
			"                case \"/\":\r\n" + 
			"                    b = numbers.pop();\r\n" + 
			"                    a = numbers.pop();\r\n" + 
			"                    numbers.push(a / b);\r\n" + 
			"                    break;\r\n" + 
			"                case \"-\":\r\n" + 
			"                    b = numbers.pop();\r\n" + 
			"                    a = numbers.pop();\r\n" + 
			"                    numbers.push(a - b);\r\n" + 
			"                    break;\r\n" + 
			"                case \"*\":\r\n" + 
			"                    b = numbers.pop();\r\n" + 
			"                    a = numbers.pop();\r\n" + 
			"                    numbers.push(a * b);\r\n" + 
			"                    break;\r\n" + 
			"                default:\r\n" + 
			"                    numbers.push(Double.valueOf(t));\r\n" + 
			"            }\r\n" + 
			"            \r\n" + 
			"            switch(t){\r\n" + 
			"            case \"+\":\r\n" + 
			"                b = numbers.pop();\r\n" + 
			"                a = numbers.pop();\r\n" + 
			"                numbers.push(a + b);\r\n" + 
			"                break;\r\n" + 
			"            case \"/\":\r\n" + 
			"                b = numbers.pop();\r\n" + 
			"                a = numbers.pop();\r\n" + 
			"                numbers.push(a / b);\r\n" + 
			"                break;\r\n" + 
			"            case \"-\":\r\n" + 
			"                b = numbers.pop();\r\n" + 
			"                a = numbers.pop();\r\n" + 
			"                numbers.push(a - b);\r\n" + 
			"                break;\r\n" + 
			"            case \"*\":\r\n" + 
			"                b = numbers.pop();\r\n" + 
			"                a = numbers.pop();\r\n" + 
			"                numbers.push(a * b);\r\n" + 
			"                break;\r\n" + 
			"            default:\r\n" + 
			"                numbers.push(Double.valueOf(t));\r\n" + 
			"        }\r\n" + 
			"        });\r\n" + 
			"        return numbers.pop();\r\n" + 
			"    }\r\n" + 
			"\r\n" + 
			"    /**\r\n" + 
			"     * Memory Recall uses the number in stored memory, defaulting to 0.\r\n" + 
			"     *\r\n" + 
			"     * @return the double\r\n" + 
			"     */\r\n" + 
			"    public double memoryRecall(){\r\n" + 
			"    	int x =0;\r\n" + 
			"    	if (x>0)System.out.println(x);\r\n" + 
			"    	else System.out.println(0);\r\n" + 
			"        return memory;\r\n" + 
			"    }\r\n" + 
			"\r\n" + 
			"    /**\r\n" + 
			"     * Memory Clear sets the memory to 0.\r\n" + 
			"     */\r\n" + 
			"    public void memoryClear(){\r\n" + 
			"        memory = 0;\r\n" + 
			"		if (x>0)System.out.println(x);\r\n" + 
			"    	else System.out.println(0);"+
			"    }\r\n" + 
			"\r\n" + 
			"\r\n" +  
			"}";
}
