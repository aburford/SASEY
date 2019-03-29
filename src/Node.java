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
	 * Calculates the sum of the squares of the errors in each dimension based on available data
	 * @param theta - should be a column vector (one column, many rows)
	 * @return sum of squared error (scalar)
	 */
	public double loss(double[][] theta) {
		double[][] modelPrediction = multiply(features, theta);//Model's prediction of the label vector
		double[][] errorVector = add(labels, scale(modelPrediction, -1.0));//The real label vector minus the model's prediction
		//Dotting the error vector with itself corresponds to adding up the squares of the errors in each dimension
		return dot(errorVector, errorVector);
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
		return add(scale(multiply(transpose(features), labels), -2.0), scale(multiply(multiply(transpose(features), features), theta), 2.0));
	}

	//Testing purposes only
	public double[][] step(double[][] startTheta, double learningRate, int steps) {
		double[][] currentTheta = startTheta;
		double[][] grad;
		for(int i = 0; i < steps; i++) {
			grad = gradient(currentTheta);
			currentTheta = add(currentTheta, scale(grad, -learningRate));
			System.out.println(loss(currentTheta));
		}
		return currentTheta;
	}

	//Scales all elements of matrix
	public static double[][] scale(double[][] mat, double factor) {
		double[][] out = new double[mat.length][mat[0].length];
		for(int i = 0; i < mat.length; i++) {
			for(int j = 0; j < mat[0].length; j++) {
				out[i][j] = mat[i][j] * factor;
			}
		}
		return out;
	}

	//Transposes matrix
	private static double[][] transpose(double[][] mat) {
		double[][] out = new double[mat[0].length][mat.length];
		for(int i = 0; i < mat.length; i++) {
			for(int j = 0; j < mat[0].length; j++) {
				out[j][i] = mat[i][j];
			}
		}
		return out;
	}

	//Adds two matrices
	public static double[][] add(double[][] mat1, double[][] mat2) throws ArithmeticException {
		if(mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			throw new ArithmeticException("Dimensions do not match");
		}
		double[][] out = new double[mat1.length][mat1[0].length];
		for(int i = 0; i < mat1.length; i++) {
			for(int j = 0; j < mat1[0].length; j++) {
				out[i][j] = mat1[i][j] + mat2[i][j];
			}
		}
		return out;
	}

	//Matrix multiplication
	private static double[][] multiply(double[][] mat1, double[][] mat2) {
		if(mat1[0].length != mat2.length) {
			throw new ArithmeticException("Dimensions do not match");
		}
		double[][] out = new double[mat1.length][mat2[0].length];
		for(int i = 0; i < out.length; i++) {
			for(int j = 0; j < out[0].length; j++) {
				out[i][j] = weight(mat1, mat2, i, j);
			}
		}
		return out;
	}

	//Matrix multiplication helper function. Dots one row of left matrix with one column of right matrix
	private static double weight(double[][] mat1, double[][] mat2, int row, int col) {
		if(mat1[row].length != mat2.length) {
			throw new ArithmeticException("Dimensions do not match");
		}
		double out = 0;
		for(int i = 0; i < mat1[0].length; i++) {
			out += mat1[row][i] * mat2[i][col];
		}
		return out;
	}

	//Dots two vectors
	private static double dot(double[][] mat1, double[][] mat2) {
		return weight(transpose(mat1), mat2, 0, 0);
	}
}
