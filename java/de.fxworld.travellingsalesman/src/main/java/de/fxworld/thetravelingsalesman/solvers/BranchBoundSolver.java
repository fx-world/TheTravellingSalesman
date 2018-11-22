package de.fxworld.thetravelingsalesman.solvers;
import java.util.*;

import de.fxworld.thetravelingsalesman.*;

public class BranchBoundSolver<T> implements ISolver<T> {

    private IProblem<T> problem;

    public BranchBoundSolver(IProblem<T> problem) {
        this.problem = problem;
    }

    @Override
    public IPath<T> solve() {
        return calculateShortestPath(problem);
    }

    @Override
    public IProblem<T> getProblem() {
        return problem;
    }

    protected IPath<T> calculateShortestPath(IProblem<T> problem) {
    	List<Integer> leftToVisit = new ArrayList<Integer>();
    	
    	for (int i = 0; i < problem.getLocationsCount(); i++) {
    		leftToVisit.add(i);
    	}
    	
        return calculateShortestPath(problem, problem.createPath(), leftToVisit);
    }

    protected IPath<T> calculateShortestPath(IProblem<T> problem, IPath<T> startPath, List<Integer> leftToVisit) {
        IPath<T> bestPath = null;

        if (leftToVisit.size() == 1) {
            bestPath = startPath.to(leftToVisit.get(0).intValue());

            problem.setBestPath(bestPath);

        } else {
            List<IPath<T>> nextPaths = new ArrayList<>();
            IPath<T> globalBestPath = problem.getBestPath();

            for (int nextLocation : leftToVisit) {
            	IPath<T> nextPath = startPath.to(nextLocation);

                if (nextPath.isBetter(globalBestPath)) {
                    nextPaths.add(nextPath);
                }
            }

            Collections.sort(nextPaths);

            for (IPath<T> nextPath : nextPaths) {
                List<Integer> newLeftToVisit = new ArrayList<>(leftToVisit);
                Integer nextLocation = nextPath.getLast();
                newLeftToVisit.remove(nextLocation);

                IPath<T> path = calculateShortestPath(problem, nextPath, newLeftToVisit);

                if (path != null && (path.isBetter(bestPath))) {
                    bestPath = path;
                }
            }
        }

        return bestPath;
    }
}
