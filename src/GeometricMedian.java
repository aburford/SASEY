import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class GeometricMedian extends Application {
	ArrayList<Circle> points = new ArrayList<Circle>();
	Pane p = new Pane();
	Circle median = new Circle();
	boolean dragging = false;
	@Override
	public void start(Stage primaryStage) throws Exception {
		p.setOnMouseClicked((e) -> {
			// prevent new circle creation when a different circle is clicked
			System.out.println(e.getTarget());
//			System.out.println(e.getEventType());
			if (e.getTarget() != p)
				return;
			if (dragging) {
				dragging = false;
				return;
			}
			// TODO: designate node as faulty or not with right vs left click and display convex hull of non faulty
			Circle c = new Circle(e.getX(), e.getY(), 5, Color.BLACK);
			c.setOnMouseDragged((eDrag) -> {
				dragging = true;
				c.setCenterX(eDrag.getSceneX());
				c.setCenterY(eDrag.getSceneY());
				displayGeometricMedian();
			});
			c.setOnMouseClicked((eClick) -> {
				if (dragging) {
					dragging = false;
					return;
				}
				System.out.println("circle clicked");
				p.getChildren().remove(c);
				points.remove(c);
				if (points.size() == 0)
					median.setVisible(false);
				else
					displayGeometricMedian();
			});
			points.add(c);
			p.getChildren().add(c);
			displayGeometricMedian();
			median.setVisible(true);
		});
		median.setRadius(5);
		median.setFill(Color.BLUE);
		median.setVisible(false);
		p.getChildren().add(median);
		Scene sc = new Scene(p, 900, 600);
		primaryStage.setScene(sc);
		primaryStage.show();
	}

	// Iterates Weiszfeld's algorithm until the change in distance is less than
	// minChange
	public void displayGeometricMedian() {
		double[][] lastMedian;
		double[][] currentMedian = new double[2][1];
		for (Circle c : points) {
			currentMedian = MH.add(currentMedian, new double[][]{{c.getCenterX()},{c.getCenterY()}});
		}
		currentMedian = MH.scale(currentMedian, 1.0 / p.getChildren().size());
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
	}

	// One step of Weiszfeld
	private double[][] step(double[][] startPosition) {
		double weightSum = 0.0;
		double[][] weightedTotal = new double[2][1];
		double weight = 0.0;
		double[][] vector;
		for (Circle c : points) {
			vector = new double[][]{{c.getCenterX()},{c.getCenterY()}};
			weight = 1.0 / MH.distance(startPosition, vector);
			weightSum += weight;
			weightedTotal = MH.add(weightedTotal, MH.scale(vector, weight));
		}
		return MH.scale(weightedTotal, 1.0 / weightSum);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
