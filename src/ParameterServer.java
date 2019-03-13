import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParameterServer {
	Node[] nodes;
	int q;
	int[][][] model;

	public ParameterServer(int numOfNodes, int faultyNodeLB) {
		nodes = new Node[numOfNodes];
		while (--numOfNodes >= 0)
			nodes[numOfNodes] = new Node();
		q = faultyNodeLB;
	}

	public void nextTimeStep() {
		// "2(1 + ǫ)q ≤ k ≤ m for any arbitrary but fixed constant ǫ > 0"
		// so I'm not sure if this is right:
		int batchSize = 2 * q;

		Batch[] batches = generateBatches(batchSize);

		// compute the mean of medians
		// int[] mean = new int[];
		// for (Batch b : batches) {
		// mean += b.getGeometricMedian();
		// }
		// mean /= batches.length;

		// update the model based on the average gradient we calculated
		// backpropagation is hard, maybe use this: https://deeplearning4j.org

	}

	private Batch[] generateBatches(int batchSize) {
		int numOfBatches = nodes.length / batchSize + nodes.length % batchSize == 0 ? 0 : 1;
		@SuppressWarnings("unchecked")
		List<Node>[] batchLists = new ArrayList[numOfBatches];
		for (int i = 0; i < batchLists.length; i++) {
			batchLists[i] = new ArrayList<>(batchSize);
		}
		int batchI = 0;

		// shuffle the nodes array for random batching
		Random r = new Random();
		Node tmp;
		for (int i = 0; i < nodes.length; i++) {
			int rand = r.nextInt(nodes.length);
			tmp = nodes[rand];
			nodes[rand] = nodes[i];
			nodes[i] = tmp;
		}

		// distribute nodes evenly into batches
		for (Node n : nodes) {
			batchLists[batchI].add(n);
			batchI++;
			batchI %= batchLists.length;
		}

		// copy into a Batch array
		Batch[] batches = new Batch[numOfBatches];
		for (int i = 0; i < batches.length; i++) {
			batches[i] = new Batch(batchLists[i]);
		}
		return batches;
	}
}
