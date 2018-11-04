package de.fxworld.thetravelingsalesman.impl;
import java.util.*;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public class DoublePath implements IPath {
    
	private IProblem<?> problem;
    private int[] locations;
    private double length = Double.NaN;

    protected DoublePath(IProblem<?> problem, int[] locations) {
        this.problem = problem;
        this.locations = locations;
    }

    protected DoublePath(IProblem<?> problem) {
        this.problem = problem;
        this.locations = new int[0];
        this.length = 0;
    }

    protected DoublePath(IProblem<?> problem, int start) {
        this.problem = problem;
        this.locations = new int[] { start };
    }

    protected DoublePath(IProblem<?> problem, int[] locations, int nextLocation) {
    	this.locations = Arrays.copyOf(locations, locations.length + 1);
    	this.locations[this.locations.length - 1] = nextLocation;
        this.problem = problem;
    }

    public double getLength() {
        if (Double.isNaN(length)) {
            problem.calculateLengths(Collections.singletonList(this));
        }

        return length;
    }

    protected void setLength(double length) {
        this.length = length;
    }

    /* (non-Javadoc)
	 * @see de.fxworld.thetravelingsalesman.impl.IPath#to(int)
	 */
    @Override
	public DoublePath to(int nextLocation) {
        return new DoublePath(problem, locations, nextLocation);
    }

    /* (non-Javadoc)
	 * @see de.fxworld.thetravelingsalesman.impl.IPath#getProblem()
	 */
    @Override
	public IProblem<?> getProblem() {
        return problem;
    }

    /* (non-Javadoc)
	 * @see de.fxworld.thetravelingsalesman.impl.IPath#getLocations()
	 */
    @Override
	public int[] getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        return "Path [locations=" + Arrays.toString(locations) + ", length=" + length + "]";
    }

    /* (non-Javadoc)
	 * @see de.fxworld.thetravelingsalesman.impl.IPath#getLast()
	 */
    @Override
	public int getLast() {
        if (locations.length > 0) {
            return locations[locations.length - 1];
        } else {
            return -1;
        }
    }

    /* (non-Javadoc)
	 * @see de.fxworld.thetravelingsalesman.impl.IPath#getLocationsCount()
	 */
    @Override
	public int getLocationsCount() {
        return locations.length;
    }

    /* (non-Javadoc)
	 * @see de.fxworld.thetravelingsalesman.impl.IPath#compareTo(de.fxworld.thetravelingsalesman.impl.Path)
	 */
    @Override
    public int compareTo(IPath o) {
        return Double.compare(getLength(), ((DoublePath) o).getLength());
    }

	/* (non-Javadoc)
	 * @see de.fxworld.thetravelingsalesman.impl.IPath#contains(int)
	 */
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

	@Override
	public boolean isBetter(IPath globalBestPath) {
		boolean result = true;
		
		if (globalBestPath != null && ((DoublePath) globalBestPath).getLength() < getLength()) {
			result = false;
		}
		
		return result;
	}
}