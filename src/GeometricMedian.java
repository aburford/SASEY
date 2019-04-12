import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class GeometricMedian extends Application {
	ArrayList<Point> points = new ArrayList<Point>();
	Pane p = new Pane();
	Circle median = new Circle();
	boolean dragging = false;
	Polygon hull = new Polygon();

	@Override
	public void start(Stage primaryStage) throws Exception {
		p.setOnMouseClicked((e) -> {
			// prevent new circle creation when a different circle is clicked
			System.out.println(e.getTarget());
			// System.out.println(e.getEventType());
			if (e.getTarget() instanceof Circle)
				return;
			if (dragging) {
				dragging = false;
				return;
			}
			Point c = new Point(e.getX(), e.getY(), 5, e.isShiftDown());
			c.setOnMouseDragged((eDrag) -> {
				dragging = true;
				c.setLocation(eDrag.getSceneX(), eDrag.getSceneY());
				displayMedianAndHull();
			});
			c.setOnMouseClicked((eClick) -> {
				if (dragging) {
					dragging = false;
					return;
				}
				if (eClick.isShiftDown()) {
					c.toggleFaulty();
					displayHull();
				} else
					removePoint(c);
			});
			points.add(c);
			p.getChildren().add(c);
			displayMedianAndHull();
			median.setVisible(true);
		});
		median.setRadius(5);
		median.setVisible(false);
		p.getChildren().add(median);
		hull.setFill(Color.TRANSPARENT);
		p.getChildren().add(hull);
		Label info = new Label("Shift click to designate faulty node");
		Button clear = new Button("Clear");
		clear.setLayoutY(20);
		clear.setOnAction((e) -> {
			for (Point point : points) {
				p.getChildren().remove(point);
			}
			points.clear();
			displayHull();
			median.setVisible(false);
		});
		p.getChildren().add(info);
		p.getChildren().add(clear);
		Scene sc = new Scene(p, 900, 600);
		primaryStage.setScene(sc);
		primaryStage.show();
	}
	
	public void removePoint(Point point) {
		p.getChildren().remove(point);
		points.remove(point);
		if (points.size() == 0)
			median.setVisible(false);
		else
			displayMedianAndHull();
	}
	
	public void displayHull() {
		ArrayList<Point> nonFaulty = new ArrayList<Point>(points.size());
		for (Point p : points) {
			if (!p.faulty)
				nonFaulty.add(p);
		}
		ArrayList<Point> hullPoints = QuickHull.quickHull(nonFaulty);
		Double[] coordinates = new Double[hullPoints.size() * 2];
		int i = 0;
		for (Point p : hullPoints) {
			coordinates[i++] = p.x;
			coordinates[i++] = p.y;
		}
		p.getChildren().remove(hull);
		hull = new Polygon();
		hull.getPoints().addAll(coordinates);
		hull.setFill(Color.TRANSPARENT);
		hull.setStroke(Color.BLACK);
		p.getChildren().add(0,hull);
		Color g = new Color(0, 1, 0, 0.8);
		Color b = new Color(0, 0, 1, 0.8);
		median.setFill(hull.contains(median.getCenterX(), median.getCenterY()) ? g : b);
	}

	// Iterates Weiszfeld's algorithm until the change in distance is less than
	// minChange
	public void displayMedianAndHull() {
		double[][] lastMedian;
		double[][] currentMedian = new double[2][1];
		for (Point c : points) {
			currentMedian = MH.add(currentMedian, c.getColumnVector());
		}
		currentMedian = MH.scale(currentMedian, 1.0 / points.size());
		// System.out.print("Starting geometric median calculation...\t");
		do {
			lastMedian = currentMedian.clone();
			currentMedian = step(currentMedian);
			// System.out.println("Geometric median error:" +
			// MH.distance(lastMedian, currentMedian));
		} while (MH.distance(lastMedian, currentMedian) > 0.1);
		// System.out.println("finished");
		median.setCenterX(currentMedian[0][0]);
		median.setCenterY(currentMedian[1][0]);
		median.toFront();
		displayHull();
	}

	// One step of modified Weizfeld algorithm
	private double[][] step(double[][] y) {
		boolean flag = false;
		int index = -1;
		double[][] vector;
		for (int i = 0; i < points.size(); i++) {
			vector = points.get(i).getColumnVector();
			for (int j = 0; j < y.length; j++) {
				if (y[j][0] == vector[j][0]) {
					flag = true;
					index = i;
					break;
				}
			}
		}
		double[][] R = new double[2][1];
		double weightSum = 0.0;
		double[][] weightedTotal = new double[2][1];
		double weight = 0.0;
		for (int i = 0; i < points.size(); i++) {
			if (!flag || index != i) {
				vector = points.get(i).getColumnVector();
				weight = 1.0 / MH.distance(y, vector);
				weightSum += weight;
				weightedTotal = MH.add(weightedTotal, MH.scale(vector, weight));
				R = MH.add(R, MH.scale(MH.add(vector, MH.scale(y, -1.0)), 1.0 / weight));
			}
		}
		double[][] tofY = MH.scale(weightedTotal, 1.0 / weightSum);
		double rofY = Math.sqrt(MH.dot(R, R));
		return MH.add(MH.scale(tofY, Math.abs(1 - 1.0 / rofY)), MH.scale(y, Math.min(1, 1.0 / rofY)));
	}

	public static void main(String[] args) {
		launch(args);
	}

}
