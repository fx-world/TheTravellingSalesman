import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PathFinder {

    public static void main(String[] args) throws IOException {
        new PathFinder().run();
    }

    protected void run() throws IOException {

        IProblem problem1 = new Problem2D("tspadata1.txt");
        testSolvers(problem1);

        IProblem problem2 = new Problem2D("tspadata2.txt");
        testSolvers(problem2);

        IProblem problem3 = new Problem2D("tspadata3.txt");
        testSolvers(problem3);

        for (int i = 5; i <= 20; i++) {
            System.out.println("Problem with size " + i);
            IProblem problem = createProblem(i);

            testSolvers(problem);
        }
    }

    protected void testSolvers(IProblem problem) {
        List<ISolver> solvers = new ArrayList<>();
        solvers.add(new DummySolver(problem));
        solvers.add(new AntSolver(problem));
        solvers.add(new ParallelBranchBoundSolver(problem));
        //solvers.add(new ParallelBranchBoundSolver(problem, 50));

        for (ISolver solver : solvers) {
            //problem.resetBestPath();

            long from = System.currentTimeMillis();
            Path path = solver.solve();
            long to = System.currentTimeMillis();

            System.out.println(solver.toString() + " best=" + path.getLength() + " time=" + (to - from));
        }

        System.out.println("");
    }

    protected IProblem createProblem(int count) {
        IProblem result = null;
        int[][] distances = createDistances(count);
        List<Location> locations = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            locations.add(new Location(i, "L" + i));
        }
        
        //result = new Problem2D(locations, distances);
        result = new ProblemArray(locations, distances);
                
        return result;
    }

    protected int[][] createDistances(int count) {
        int[][] result = new int[count][];
        Random rand = new Random(42);

        for (int i = 0; i < count; i++) {
            result[i] = new int[count];
            for (int j = 0; j < count; j++) {
                result[i][j] = rand.nextInt(5000);
            }
        }

        return result;
    }

}
