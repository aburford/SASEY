import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) {
		// parse out data and allocate to nodes
		try (Stream<String> stream = Files.lines(Paths.get("data/prostate.data"))) {
			stream.toArray();
			// initialize ParameterServer
			
			// initialize nodes
			int numOfNodes = 100;
			Node[] nodes = new Node[numOfNodes];
			while (--numOfNodes >= 0)
				// allocate parsed data into nodes
				nodes[numOfNodes] = new Node();
			
			// server.setNodes(nodes)
			
			// loop through time steps with server.nextTimeStep()
		} catch (IOException e) {
			System.out.println("working directory should be root of git repo");
			e.printStackTrace();
		}
		
	}

}
