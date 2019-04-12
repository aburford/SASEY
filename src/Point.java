import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Point extends Circle {
	double x;
	double y;
	boolean faulty;
	double[][] columnVector;
	public Point(double x, double y, double radius, boolean faulty) {
		super(x, y, radius, faulty ? Color.RED : Color.BLACK);
		this.x = x;
		this.y = y;
		this.faulty = faulty;
		columnVector = new double[][]{{x},{y}};
	}
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
		setCenterX(x);
		setCenterY(y);
	}
	public double[][] getColumnVector() {
		columnVector[0][0] = x;
		columnVector[1][0] = y;
		return columnVector;
	}
	public void toggleFaulty() {
		faulty = !faulty;
		setFill(faulty ? Color.RED : Color.BLACK);
	}
}
