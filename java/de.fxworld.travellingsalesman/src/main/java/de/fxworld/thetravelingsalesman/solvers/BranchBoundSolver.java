package de.fxworld.thetravelingsalesman.solvers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fxworld.thetravelingsalesman.*;
import de.fxworld.thetravelingsalesman.impl.DoublePath;

public class BranchBoundSolver<T> implements ISolver<T> {

    private IProblem<T> problem;

    public BranchBoundSolver(IProblem<T> problem) {
        this.problem = problem;
    }

    @Override
    public IPath solve() {
        return calculateShortestPath(problem);
    }

    @Override
    public IProblem<T> getProblem() {
        return problem;
    }

    protected IPath calculateShortestPath(IProblem<T> problem) {
    	List<Integer> leftToVisit = new ArrayList<Integer>();
    	
    	for (int i = 0; i < problem.getLocationsCount(); i++) {
    		leftToVisit.add(i);
    	}
    	
        return calculateShortestPath(problem, problem.createPath(), leftToVisit);
    }

    protected IPath calculateShortestPath(IProblem<T> problem, IPath startPath, List<Integer> leftToVisit) {
        IPath bestPath = null;

        if (leftToVisit.size() == 1) {
            bestPath = startPath.to(leftToVisit.get(0));

            problem.setBestPath(bestPath);

        } else {
            List<IPath> nextPaths = new ArrayList<>();
            IPath globalBestPath = problem.getBestPath();

            for (int nextLocation : leftToVisit) {
            	IPath nextPath = startPath.to(nextLocation);

                if (nextPath.isBetter(globalBestPath)) {
                    nextPaths.add(nextPath);
                }
            }

            Collections.sort(nextPaths);

            for (IPath nextPath : nextPaths) {
                List<Integer> newLeftToVisit = new ArrayList<>(leftToVisit);
                Integer nextLocation = nextPath.getLast();
                newLeftToVisit.remove(nextLocation);

                IPath path = calculateShortestPath(problem, nextPath, newLeftToVisit);

                if (path != null && (path.isBetter(bestPath))) {
                    bestPath = path;
                }
            }
        }

        return bestPath;
    }
}
