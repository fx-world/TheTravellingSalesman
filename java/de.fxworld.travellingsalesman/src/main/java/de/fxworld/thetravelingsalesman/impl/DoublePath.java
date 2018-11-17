package de.fxworld.thetravelingsalesman.impl;
import java.util.*;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public class DoublePath<T> extends AbstractPath<T> implements IPath<T> {
    
	private double length = Double.NaN;

    protected DoublePath(IProblem<T> problem, int[] locations) {
    	super(problem, locations);
    }

    protected DoublePath(IProblem<T> problem) {
    	super(problem, new int[0]);
        this.length = 0;
    }

    protected DoublePath(IProblem<T> problem, int start) {
    	super(problem, new int[] { start });
    }

    protected DoublePath(IProblem<T> problem, int[] locations, int nextLocation) {
    	super(problem, Arrays.copyOf(locations, locations.length + 1));
    	this.locations[this.locations.length - 1] = nextLocation;
    }
    
    @Override
	public DoublePath<T> to(int nextLocation) {
	    return new DoublePath<T>(problem, locations, nextLocation);
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

    @Override
    public String toString() {
        return "Path [locations=" + Arrays.toString(locations) + ", length=" + length + "]";
    }

    /* (non-Javadoc)
	 * @see de.fxworld.thetravelingsalesman.impl.IPath#compareTo(de.fxworld.thetravelingsalesman.impl.Path)
	 */
    @Override
    public int compareTo(IPath<T> o) {
        return Double.compare(getLength(), ((DoublePath<T>) o).getLength());
    }

	@Override
	public boolean isBetter(IPath<T> globalBestPath) {
		boolean result = true;
		
		if (globalBestPath != null && ((DoublePath<T>) globalBestPath).getLength() < getLength()) {
			result = false;
		}
		
		return result;
	}
}