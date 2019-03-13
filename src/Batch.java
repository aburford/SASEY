import java.util.List;

public class Batch {
	private Node[] nodes;
	public Batch(List<Node> nodes) {
		this.nodes = (Node[]) nodes.toArray();
	}
	
	public double[] getGeometricMedian(){
		return new double[]{0};
	}
}
