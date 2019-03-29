public class Node {

	double[][] labels;
	double[][] features;
	boolean faulty;

	/**
	 * @param labels - should be a column vector (one column, many rows)
	 * @param features - should be a matrix with the same number of columns as theta has rows
	 */
	public Node(double[][] labels, double[][] features, boolean faulty) {
		this.labels = labels;
		this.features = features;
		this.faulty = faulty;
	}
	
	/**
	 * Calculates gradient of loss function at parameter estimate theta based on available data
	 * @param theta - should be a column vector (one column, many rows)
	 * @return gradient column vector
	 */
	public double[][] gradient(double[][] theta) {
		if(faulty) {
			//Do something bad. Make sure to return.
		}
		return MH.add(MH.scale(MH.multiply(MH.transpose(features), labels), -2.0), MH.scale(MH.multiply(MH.multiply(MH.transpose(features), features), theta), 2.0));
	}

	/**
	 * Calculates the sum of the squares of the errors in each dimension based on available data
	 * @param theta - should be a column vector (one column, many rows)
	 * @return sum of squared error (scalar)
	 */
	public double loss(double[][] theta) {
		double[][] modelPrediction = MH.multiply(features, theta);//Model's prediction of the label vector
		double[][] errorVector = MH.add(labels, MH.scale(modelPrediction, -1.0));//The real label vector minus the model's prediction
		//Dotting the error vector with itself corresponds to adding up the squares of the errors in each dimension
		return MH.dot(errorVector, errorVector);
	}
	
	
	// testing purposes only
	public double[][] step(double[][] startTheta, double learningRate, int steps) {
		double[][] currentTheta = startTheta;
		double[][] grad;
		for(int i = 0; i < steps; i++) {
			grad = gradient(currentTheta);
			currentTheta = MH.add(currentTheta, MH.scale(grad, -learningRate));
			System.out.println(loss(currentTheta));
		}
		return currentTheta;
	}
}
