package de.fxworld.thetravelingsalesman.solvers;

import de.fxworld.thetravelingsalesman.*;

public class DummySolver<T> implements ISolver<T> {

    private IProblem<T> problem;

    public DummySolver(IProblem<T> problem) {
        this.problem = problem;
    }

    @Override
    public IPath<T> solve() {
        int[] locations = new int[problem.getLocationsCount()];
        for (int i = 0; i < locations.length; i++) {
        	locations[i] = i;
        }
        	
		IPath<T> result = problem.createPath(locations);
        problem.setBestPath(result);
        return result;
    }

    @Override
    public IProblem<T> getProblem() {
        return problem;
    }

    @Override
    public String toString() {
        return "DummySolver []";
    }
}
