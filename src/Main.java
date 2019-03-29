import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    /* 2(1 + ǫ)q ≤ k ≤ m
     *	m = num of working machines(node)
     *  q = num of faulty (upto q)
     *  ǫ = batch size constant, (ǫ > 0)
     */
    static int numOfNodes;      // m
    static int faultyNodeLB;    // q
    //double batchSizeConst = 0;  // ǫ, if required later

    static double learningRate;

    // termination conditions (depends on the model we will use?)
    static int maximumIter;	    // maximum # of iteration allowed
    static double absTolerance; // most tolerable loss

    // init system params here
    private static void init() {
        // all accessible from other classes
        numOfNodes = 10;
        faultyNodeLB = 0;
        learningRate = 0.0000001;
        maximumIter = 18000;
        absTolerance = 0;
    }

    public static void main(String[] args) {
        init();
        
        // LOAD DATA
        ArrayList<ArrayList<Double>> labelsList = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> featuresList = new ArrayList<ArrayList<Double>>();
		URL prostateData;
		BufferedReader in;
		int row = 0;
		String line;
		try {
			// open webpage
			prostateData = new URL("https://www.cs.ubc.ca/~nando/340-2012/lectures/prostate.data");
			in = new BufferedReader(new InputStreamReader(prostateData.openStream()));
			while((line = in.readLine()) != null) {
				//Skips first line
				if(row > 0) {
					//Splits string into an array of numbers
					String[] tokens = line.split("\\s");
					featuresList.add(new ArrayList<Double>());
					int index = 0;
					//Adds each number individually and converts to double
					for(int i = 0; i < tokens.length; i++) {
						if(!tokens[i].equals("")) {
							//Pick a column to be the labels
							if(index == 1) {
								ArrayList<Double> tmp = new ArrayList<Double>();
								tmp.add(Double.parseDouble((tokens[i])));
								labelsList.add(tmp);
							} else {//Everything else is a feature
								featuresList.get(row - 1).add(Double.parseDouble((tokens[i])));
							}
							index++;
						}
					}
				}
				row++;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		double[][] labels = MH.listToMatrix(labelsList);
		double[][] features = MH.listToMatrix(featuresList);
		
		// DISTRIBUTE DATA
		Node[] nodes = new Node[numOfNodes];
        //evenly distributed (last node gets k data, k <= dataPerNode)
        int dataPerNode = labels.length / nodes.length + 1;
        //distribute upto (m-1)th node
        for (int i = 0; i < nodes.length - 1; ++i) {
            nodes[i] = new Node(
                    Arrays.copyOfRange(labels, (dataPerNode * i), dataPerNode * (i + 1)),
                    Arrays.copyOfRange(features, (dataPerNode * i), dataPerNode * (i + 1)),
                    false );
        }
        // last node gets the rest
        nodes[nodes.length - 1] = new Node(
                Arrays.copyOfRange(labels, dataPerNode * (nodes.length - 1), labels.length),
                Arrays.copyOfRange(features, dataPerNode * (nodes.length - 1), features.length),
                false );
        
        
        // INITIALIZE PARAMETER SERVER
        // ParameterServer(int faultyNodeLB, int featureLength, int labelLength, double learningRate, Node[] nodes) {
        ParameterServer paramServer = new ParameterServer(faultyNodeLB, labels[0].length, features[0].length, learningRate, nodes);

        // ITERATE TIMESTEPS
        for (int i = 0; i < maximumIter; i++) {
        	System.out.println("Total loss: " + paramServer.nextTimeStep());
        }
    }
}
