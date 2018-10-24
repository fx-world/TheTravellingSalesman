package de.fxworld.thetravelingsalesman;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BranchBoundSolver<T> implements ISolver<T> {

    private IProblem<T> problem;

    public BranchBoundSolver(IProblem<T> problem) {
        this.problem = problem;
    }

    @Override
    public Path solve() {
        return calculateShortestPath(problem);
    }

    @Override
    public IProblem<T> getProblem() {
        return problem;
    }

    protected Path calculateShortestPath(IProblem<T> problem) {
    	List<Integer> leftToVisit = new ArrayList<Integer>();
    	
    	for (int i = 0; i < problem.getLocationsCount(); i++) {
    		leftToVisit.add(i);
    	}
    	
        return calculateShortestPath(problem, new Path(problem), leftToVisit);
    }

    protected Path calculateShortestPath(IProblem<T> problem, Path startPath, List<Integer> leftToVisit) {
        Path bestPath = null;

        if (leftToVisit.size() == 1) {
            bestPath = startPath.to(leftToVisit.get(0));

            problem.setBestPath(bestPath);

        } else {
            List<Path> nextPaths = new ArrayList<Path>();
            Path globalBestPath = problem.getBestPath();

            for (int nextLocation : leftToVisit) {
                Path nextPath = startPath.to(nextLocation);

                if (globalBestPath == null || nextPath.getLength() < globalBestPath.getLength()) {
                    nextPaths.add(nextPath);
                }
            }

            Collections.sort(nextPaths);

            for (Path nextPath : nextPaths) {
                List<Integer> newLeftToVisit = new ArrayList<>(leftToVisit);
                Integer nextLocation = nextPath.getLast();
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
