public class Main {
    /* 2(1 + ǫ)q ≤ k ≤ m
     *	m = num of working machines(node)
     *  q = num of faulty (upto q)
     *  ǫ = batch size constant, (ǫ > 0)
     */
    static int numOfNodes;      // m
    static int faultyNodeLB;    // q
    //double batchSizeConst = 0;  // ǫ, if required later

    static double learningRate;

    // termination conditions (depends on the model we will use?)
    static int maximumIter;	    // maximum # of iteration allowed
    static double absTolerance; // most tolerable loss

    // init system params here
    private static void init() {
        // all accessible from other classes
        numOfNodes = 10;
        faultyNodeLB = 0;
        learningRate = 0.0000001;
        maximumIter = 18000;
        absTolerance = 0;
    }

    public static void main(String[] args) {
        init();

        // initialize parameter server
        ParameterServer paramServer =
            new ParameterServer(numOfNodes, faultyNodeLB);

        Tester tester = new Tester();
        try {
            tester.loadData();
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }

        // parse out data and allocate to nodes
        //@TODO as a function of ParameterServer
        Node[] nodes = tester.distributeData(numOfNodes);
        paramServer.setNodes(nodes);

        tester.singleNodeTest();

        tester.testBGD(nodes, learningRate, maximumIter);
    }



    public static void test(ParameterServer paramServer) {

        for (int round = 0; round < maximumIter; ++round) {
            /*
            paramServer.nextTimeStep();
            paramServer.print();
            */
        }
    }
}
