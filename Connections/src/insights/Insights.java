package insights;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DensityBasedClusterer;
import weka.clusterers.EM;
import weka.core.Instances;

public class Insights implements Insights_Interface{

	  public String ClusterResults(String filename) {
		    ClusterEvaluation eval;
		    Instances               data = null;
		    String[]                options;
		    DensityBasedClusterer   cl;    

		    try {
				data = new Instances(new BufferedReader(new FileReader(filename)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    // normal
		    //System.out.println("\n--> normal");
		    options    = new String[2];
		    options[0] = "-t";
		    options[1] = filename;
		    //System.out.println(
		    //    ClusterEvaluation.evaluateClusterer(new EM(), options));
		    String Cluster = null;
			try {
				Cluster = ClusterEvaluation.evaluateClusterer(new EM(), options);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    // manual call
		    //System.out.println("\n--> manual");
		    cl   = new EM();
		    try {
				cl.buildClusterer(data);
			    eval = new ClusterEvaluation();
			    eval.setClusterer(cl);
			    eval.evaluateClusterer(new Instances(data));
			    //System.out.println("# of clusters: " + eval.getNumClusters());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    return Cluster;
		  }
	  public void	ParseResults(String cluster_data){
		  System.out.println(cluster_data);
	  }
}
