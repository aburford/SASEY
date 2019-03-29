import java.util.List;

public class Batch {
	private Node[] nodes;
	public Batch(List<Node> nodes) {
		this.nodes = (Node[]) nodes.toArray();
	}
	
	//Iterates Weiszfeld's algorithm until the change in distance is less than minChange
	public double[][] getGeometricMedian(double minChange, double[][] parameterEstimate) {
		double[][] lastMedian;
		double[][] currentMedian = new double[parameterEstimate.length][1];
		for(int i = 0; i < nodes.length; i++) {
			currentMedian = MH.add(currentMedian, nodes[i].gradient(parameterEstimate));
		}
		currentMedian = MH.scale(currentMedian, 1.0 / nodes.length);
		do {
			lastMedian = currentMedian.clone();
			currentMedian = step(currentMedian, parameterEstimate);
		} while(MH.distance(lastMedian, currentMedian) > minChange);
		return currentMedian;
	}
	
	//One step of Weiszfeld
	private double[][] step(double[][] startPosition, double[][] parameterEstimate) {
		double weightSum = 0.0;
		double[][] weightedTotal = new double[nodes.length][1];
		double weight = 0.0;
		double[][] grad;
		for(int i = 0; i < nodes.length; i++) {
			grad = nodes[i].gradient(parameterEstimate);
			weight = 1.0 / MH.distance(startPosition, grad);
			weightSum += weight;
			weightedTotal = MH.add(weightedTotal, MH.scale(grad, weight));
		}
		return MH.scale(weightedTotal, 1.0 / weightSum);
	}
	
	public double totalLoss(double[][] model) {
		double totalLoss = 0;
		for (Node n : nodes) {
			totalLoss += n.loss(model);
		}
		return totalLoss;
	}
}
