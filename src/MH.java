import java.util.ArrayList;
import java.util.List;

// MatrixHelper (MH for short)
public class MH {
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
	public static double[][] transpose(double[][] mat) {
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
	public static double[][] multiply(double[][] mat1, double[][] mat2) {
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
	public static double weight(double[][] mat1, double[][] mat2, int row, int col) {
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
	public static double dot(double[][] mat1, double[][] mat2) {
		return weight(transpose(mat1), mat2, 0, 0);
	}
	
	public static double[][] listToMatrix(List<ArrayList<Double>> mat) {
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
}
