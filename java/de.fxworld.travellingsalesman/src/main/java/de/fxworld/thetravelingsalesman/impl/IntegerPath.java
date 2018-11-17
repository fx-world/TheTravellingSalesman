package de.fxworld.thetravelingsalesman.impl;
import java.util.*;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public class IntegerPath<T> extends AbstractPath<T> implements IPath<T> {
    
    private int length = Integer.MIN_VALUE;

    protected IntegerPath(IProblem<T> problem, int[] locations) {
    	super(problem, locations);
    }

    protected IntegerPath(IProblem<T> problem) {
    	super(problem, new int[0]);
        this.length = 0;
    }

    protected IntegerPath(IProblem<T> problem, int start) {
    	super(problem, new int[] { start });
    }

    protected IntegerPath(IProblem<T> problem, int[] locations, int nextLocation) {
    	super(problem, Arrays.copyOf(locations, locations.length + 1));
    	this.locations[this.locations.length - 1] = nextLocation;
    }
    
    @Override
	public IntegerPath<T> to(int nextLocation) {
	    return new IntegerPath<T>(problem, locations, nextLocation);
	}

    public double getLength() {
        if (length == Integer.MIN_VALUE) {
            problem.calculateLengths(Collections.singletonList(this));
        }

        return length;
    }

    public void setLength(int length) {
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
        return Double.compare(getLength(), ((IntegerPath<T>) o).getLength());
    }

	@Override
	public boolean isBetter(IPath<T> globalBestPath) {
		boolean result = true;
		
		if (globalBestPath != null && ((IntegerPath<T>) globalBestPath).getLength() < getLength()) {
			result = false;
		}
		
		return result;
	}
}