package de.fxworld.thetravelingsalesman.solvers;

import java.util.*;

import de.fxworld.thetravelingsalesman.*;

public class NearestNeighborSolver<T> implements ISolver<T> {

    private IProblem<T> problem;

    public NearestNeighborSolver(IProblem<T> problem) {
        this.problem = problem;
    }

    @Override
    public IPath<T> solve() {
    	IPath<T> start = problem.createPath();
    	
    	for (int j = 0; j < problem.getLocationsCount(); j++) {
	    	List<IPath<T>> neighbors = new ArrayList<>();
	    	
	    	for (int i = 0; i < problem.getLocationsCount(); i++) {
	    		if (!start.contains(i)) {
	    			neighbors.add(start.to(i));    			
	    		}    		
	    	}
	    	
	    	problem.calculateLengths(neighbors);
	    	Collections.sort(neighbors);
	    	
	    	start = neighbors.get(0);
    	}
	    	
        return start;
    }

    @Override
    public IProblem<T> getProblem() {
        return problem;
    }

    @Override
    public String toString() {
        return "NearestNeighborSolver []";
    }
}
