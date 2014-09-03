package insights;

public class Insights_Test {
	public static void main(String[] args) throws Exception {
		Insights insight = new Insights();
		
		String result = insight.ClusterResults("education.arff");
		insight.ParseResults(result);
 
	}
}
