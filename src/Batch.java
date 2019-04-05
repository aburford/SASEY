import java.util.List;

public class Batch {
	private Node[] nodes;
	public Batch(List<Node> nodes) {
		this.nodes = new Node[nodes.size()];
		int i = 0;
		for (Node n : nodes)
			this.nodes[i++] = n;
	}
	
	//Iterates Weiszfeld's algorithm until the change in distance is less than minChange
	public double[][] getGeometricMedian(double minChange, double[][] parameterEstimate) {
		double[][] lastMedian;
		double[][] currentMedian = new double[parameterEstimate.length][1];
		for(int i = 0; i < nodes.length; i++) {
			currentMedian = MH.add(currentMedian, nodes[i].gradient(parameterEstimate));
		}
		currentMedian = MH.scale(currentMedian, 1.0 / nodes.length);
		System.out.print("Starting geometric median calculation...\t");
		do {
			lastMedian = currentMedian.clone();
			currentMedian = step(currentMedian, parameterEstimate);
			System.out.println("Geometric median error:" + MH.distance(lastMedian, currentMedian));
			System.out.println("Geometric median minChange" + minChange);
			System.out.printf("Geometric median continue:%b", MH.distance(lastMedian, currentMedian) > minChange);
		} while(MH.distance(lastMedian, currentMedian) > minChange);
		System.out.println("finished");
		return currentMedian;
	}
	
	//One step of modified Weizfeld algorithm
	private double[][] step(double[][] y, double[][] parameterEstimate) {
		boolean flag = false;
		int index = -1;
		double[][] grad;
		for(int i = 0; i < nodes.length; i++) {
			grad = nodes[i].gradient(parameterEstimate);
			for(int j = 0; j < y.length; j++) {
				if(y[j][0] == grad[j][0]) {
					flag = true;
					index = i;
					break;
				}
			}
		}
		double[][] R = new double[parameterEstimate.length][1];
		double weightSum = 0.0;
		double[][] weightedTotal = new double[parameterEstimate.length][1];
		double weight = 0.0;
		for(int i = 0; i < nodes.length; i++) {
			if(!flag || index != i) {
				grad = nodes[i].gradient(parameterEstimate);
				weight = 1.0 / distance(y, grad);
				weightSum += weight;
				weightedTotal = MH.add(weightedTotal, MH.scale(grad, weight));
				R = MH.add(R, MH.scale(MH.add(grad, MH.scale(y, -1.0)), 1.0 / weight));
			}
		}
		double[][] tofY = MH.scale(weightedTotal, 1.0 / weightSum);
		double rofY = Math.sqrt(MH.dot(R, R));
		return MH.add(MH.scale(tofY, Math.abs(1 - 1.0 / rofY)), MH.scale(y, Math.min(1, 1.0 / rofY)));
	}
	
	public double totalLoss(double[][] model) {
		double totalLoss = 0;
		for (Node n : nodes) {
			totalLoss += n.loss(model);
		}
		return totalLoss;
	}
}
