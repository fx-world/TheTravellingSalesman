import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BranchBoundSolver implements ISolver {

    private IProblem problem;

    public BranchBoundSolver(IProblem problem) {
        this.problem = problem;
    }

    @Override
    public Path solve() {
        return calculateShortestPath(problem);
    }

    @Override
    public IProblem getProblem() {
        return problem;
    }

    protected Path calculateShortestPath(IProblem problem) {
        return calculateShortestPath(problem, new Path(problem), problem.getLocations());
    }

    protected Path calculateShortestPath(IProblem problem, Path startPath, List<Location> leftToVisit) {
        Path bestPath = null;

        if (leftToVisit.size() == 1) {
            bestPath = startPath.to(leftToVisit.get(0));

            problem.setBestPath(bestPath);

        } else {
            List<Path> nextPaths = new ArrayList<Path>();
            Path globalBestPath = problem.getBestPath();

            for (Location nextLocation : leftToVisit) {
                Path nextPath = startPath.to(nextLocation);

                if (globalBestPath == null || nextPath.getLength() < globalBestPath.getLength()) {
                    nextPaths.add(nextPath);
                }
            }

            Collections.sort(nextPaths);

            for (Path nextPath : nextPaths) {
                List<Location> newLeftToVisit = new ArrayList<>(leftToVisit);
                Location nextLocation = nextPath.getLast();
                newLeftToVisit.remove(nextLocation);

                Path path = calculateShortestPath(problem, nextPath, newLeftToVisit);

                if (path != null && (bestPath == null || path.getLength() < bestPath.getLength())) {
                    bestPath = path;
                }
            }
        }

        return bestPath;
    }
}
