package insights;

public class Insights_Test {
	public static void main(String[] args) throws Exception {
		
		long start_time = System.nanoTime();
		Insights insight = new Insights();
		
		String result = insight.ClusterResults("education.arff");
		insight.ParseResults(result);
		
		long end_time = System.nanoTime();
		
		long total_time = end_time - start_time;
		System.out.println("Weka Time: " + total_time);
 
	}
}
