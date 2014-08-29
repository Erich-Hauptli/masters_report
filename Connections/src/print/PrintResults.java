package print;

import java.util.ArrayList;
import java.util.TreeSet;

public class PrintResults implements Print_Interface{
	/*  Prints out the high level node interconnect data.  */
	public void print_connection_data(String common_field, String common_field_value, TreeSet<String> ids, ArrayList<String> Connects, ArrayList<String> Order){
		/*  Pass in the search value, the array lists containing the node ordering, and the node interconnects.  Also pass in the list of user ids for data gathered.  */
		
        String printout_header = "For " + ids.size() + " users who had " + common_field +  " = ";		//Prints data about the query
        printout_header = printout_header + common_field_value + " at some point in their career.\n";
        System.out.println(printout_header);
		
        System.out.println("Edge Transitions");		//Prints out data gathered about the transtitions between nodes
        System.out.println("----------------");
        for (String result : Connects){
        	System.out.println(result);
        }
        
        System.out.println("\n\nNode Ordering");	//Prints out data about the order the nodes should be displayed
        System.out.println("-------------");
        for(int i=0; i<Order.size(); i++){
        	for (String result : Order){
        		String[] result_split = result.split("\\s*,\\s*");
        		if(Integer.parseInt(result_split[1]) == i){				//Sort into numerical order
        			System.out.println(result);
        		}
        	}
        }   
	}
	
	/*  Prints out the data about a particular node.  */
	public void print_node_data(ArrayList<String> node_data){
		/*  Pass in the array list containing the data about a particular node.  */
		String header = null;
		for(String node : node_data){	//Step through the array list
			//System.out.println(node);
			String[] split = node.split("\\s*,\\s*");	//Split each line on commas
			if(!split[1].equalsIgnoreCase("")){			//Ensure the 2nd piece of data isn't blank
				if(!split[0].equalsIgnoreCase(header)){	//Check to see if entering a new column of data for a node
					System.out.println("\n" + split[0] + "\n-----------------");	//If we are, print out column header and then data
					System.out.println(split[1] + ": " + split[2]);
					header = split[0];
				}else{
					System.out.println(split[1] + ": " + split[2]);					//Otherwise, just print out data
				}
			}
		}
	}
}
