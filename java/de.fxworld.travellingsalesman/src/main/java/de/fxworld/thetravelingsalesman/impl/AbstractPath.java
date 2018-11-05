package de.fxworld.thetravelingsalesman.impl;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public abstract class AbstractPath implements IPath {

	protected IProblem<?> problem;
	protected int[] locations;

	public AbstractPath(IProblem<?> problem, int[] locations) {
		this.problem = problem;
		this.locations = locations;
	}

	@Override
	public IProblem<?> getProblem() {
	    return problem;
	}

	@Override
	public int[] getLocations() {
	    return locations;
	}

	@Override
	public int getLast() {
	    if (locations.length > 0) {
	        return locations[locations.length - 1];
	    } else {
	        return -1;
	    }
	}

	@Override
	public int getLocationsCount() {
	    return locations.length;
	}

	@Override
	public boolean contains(int location) {
		boolean result = false;
		
		for (int i = 0; i < locations.length; i++) {
			if (locations[i] == location) {
				result = true;
				break;
			}
		}
		
		return result;
	}
}
