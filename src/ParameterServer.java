import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ParameterServer {
	Node[] nodes;
	int q;
	double[][] model;
	int m;
	double learningRate;
	Batch[] batches;

	public ParameterServer(int faultyNodeLB, int featureLength, int labelLength, double learningRate, Node[] nodes) {
		q = faultyNodeLB;
		this.learningRate = learningRate;
		model = new double[featureLength][labelLength];
		this.nodes = nodes;
		// Picks a random starting parameter estimate
		while (featureLength-- > 0) {
			for (int i = 0; i < labelLength; i++) {
				// between -50 and 50
				model[featureLength][i] = 100.0 * (Math.random() - 0.5);
			}
		}
		System.out.println(Arrays.deepToString(model));
		// "2(1 + ǫ)q ≤ k ≤ m for any arbitrary but fixed constant ǫ > 0"
		// so I'm not sure if this is right:

		// int batchSize = 2 * q;
		// batchSize = m / k,
		// have to make sure if k = 2*q works later
		int batchSize = q == 0 ? nodes.length : m / (2 * q);
		batchSize = nodes.length / 2;
		this.batches = generateBatches(batchSize);

	}

	// return the average loss after this time step
	public double nextTimeStep() {
		// compute the mean of medians
		double[][] meanGradient = new double[model.length][model[0].length];
		double avgLoss = 0;
		for (Batch b : batches) {
			meanGradient = MH.add(meanGradient, b.getGeometricMedian(10, model));
			avgLoss += b.totalLoss(model);
		}
		meanGradient = MH.scale(meanGradient, 1 / batches.length);

		// update the model based on the average gradient we calculated
		model = MH.add(model, MH.scale(meanGradient, -learningRate));
		avgLoss /= nodes.length;
		return avgLoss;
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
