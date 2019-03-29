import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

// Modified NodeTester.java
public class Tester {
    List<ArrayList<Double>> labelsList;
    List<ArrayList<Double>> featuresList;
    double[][] labels;
    double[][] features;

    public void init() throws IOException{
        loadData();
        //something else later
    }

    public void singleNodeTest() {
    	double[][] start = getRandomParam();

        //Constructs a non-faulty node with the data
        Node node = new Node(labels, features, false);
        //Iterates 200 times and prints the loss at each step. If it explodes, you may have to decrease the learning rate
        node.step(start, 0.000001, 200);
        //Best possible loss is something like 14.4
    }

    /**
     * Tests Batch Gradient Descent.
     */
    public void testBGD(Node[] nodes, double learningRate, int iter) {
    	// features * theta == labels
    	double[][] serverParam = getRandomParam();	//parameter @ParameterServer

    	for (int round = 0; round < iter; ++round) {
            System.out.println("===============================================================");
            System.out.println("@Round" + (round + 1));
            double[][] gradientSum = new double[8][1]; //for mean calculation

            for (int i = 0; i < nodes.length; ++i) {
                gradientSum = MH.add(gradientSum, nodes[i].gradient(serverParam));
                System.out.println("[Node " + (i + 1) + "] cost = " + nodes[i].loss(serverParam));
            }
            gradientSum = MH.scale(gradientSum, nodes.length); //get mean of gradients
            serverParam = MH.add(serverParam, MH.scale(gradientSum, -learningRate));
            System.out.println("===============================================================");
        }
    }

    /**
     * Distribute features and labels into nodes.
     */
    public Node[] distributeData(int numOfNodes) {
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

        return nodes;
    }

    public void loadData() throws IOException{
        labelsList = new ArrayList<ArrayList<Double>>();
		featuresList = new ArrayList<ArrayList<Double>>();

		//Opens webpage
		URL prostateData = new URL("https://www.cs.ubc.ca/~nando/340-2012/lectures/prostate.data");
    		BufferedReader in = new BufferedReader(new InputStreamReader(prostateData.openStream()));

		int row = 0;
		String line;
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
		labels = listToMatrix(labelsList);
		features = listToMatrix(featuresList);
    }

    private double[][] getRandomParam() {
        //Picks a random starting parameter estimate
        double[][] start = new double[8][1];
        for(int i = 0; i < 8; i++) {
            start[i][0] = 100.0 * (Math.random() - 0.5);
        }
        return start;
    }

	private double[][] listToMatrix(List<ArrayList<Double>> mat) {
		if(mat.size() > 0 && mat.get(0).size() > 0) {
			double[][] out = new double[mat.size()][mat.get(0).size()];
			for(int i = 0; i < out.length; i++) {
				for(int j = 0; j < out[0].length; j++) {
					out[i][j] = mat.get(i).get(j);
				}
			}
			return out;
		}
		return new double[][]{{}};
	}

	public void printMatrix(double[][] mat) {
		for(double[] row : mat) {
			for(double el : row) {
				System.out.print(el + " ");
			}
			System.out.println();
		}
	}
}
