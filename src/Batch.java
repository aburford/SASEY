import java.util.List;

public class Batch {
	private Node[] nodes;
	public Batch(List<Node> nodes) {
		this.nodes = (Node[]) nodes.toArray();
	}
	
	public double[][] getGeometricMedian(double[][] model){
		// TODO calculate geometric median
		for (Node n : nodes) {
			n.gradient(model);
		}
		return new double[][]{{0}};
	}
	
	public double totalLoss(double[][] model) {
		double totalLoss = 0;
		for (Node n : nodes) {
			totalLoss += n.loss(model);
		}
		return totalLoss;
	}
}
