import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NodeTester {
	public static void main(String[] args) throws IOException {
		List<ArrayList<Double>> labelsList = new ArrayList<ArrayList<Double>>();
		List<ArrayList<Double>> featuresList = new ArrayList<ArrayList<Double>>();
		double[][] labels;
		double[][] features;

		// Opens webpage
		URL prostateData = new URL("https://www.cs.ubc.ca/~nando/340-2012/lectures/prostate.data");
		BufferedReader in = new BufferedReader(new InputStreamReader(prostateData.openStream()));

		int row = 0;
		String line;
		while ((line = in.readLine()) != null) {
			// Skips first line
			if (row > 0) {
				// Splits string into an array of numbers
				String[] tokens = line.split("\\s");
				featuresList.add(new ArrayList<Double>());
				int index = 0;
				// Adds each number individually and converts to double
				for (int i = 0; i < tokens.length; i++) {
					if (!tokens[i].equals("")) {
						// Pick a column to be the labels
						if (index == 1) {
							ArrayList<Double> tmp = new ArrayList<Double>();
							tmp.add(Double.parseDouble((tokens[i])));
							labelsList.add(tmp);
						} else {// Everything else is a feature
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

		// Picks a random starting parameter estimate
		double[][] start = new double[8][1];
		for (int i = 0; i < 8; i++) {
			start[i][0] = 100.0 * (Math.random() - 0.5);
		}

		// Constructs a non-faulty node with the data
		Node node = new Node(labels, features, false);
		// Iterates 200 times and prints the loss at each step. If it explodes,
		// you may have to decrease the learning rate
		node.step(start, 0.000001, 200);
		// Best possible loss is something like 14.4
	}

	private static double[][] listToMatrix(List<ArrayList<Double>> mat) {
		if (mat.size() > 0 && mat.get(0).size() > 0) {
			double[][] out = new double[mat.size()][mat.get(0).size()];
			for (int i = 0; i < out.length; i++) {
				for (int j = 0; j < out[0].length; j++) {
					out[i][j] = mat.get(i).get(j);
				}
			}
			return out;
		}
		return new double[][] { {} };
	}

	public static void printMatrix(double[][] mat) {
		for (double[] row : mat) {
			for (double el : row) {
				System.out.print(el + " ");
			}
			System.out.println();
		}
	}
}
