package de.fxworld.thetravelingsalesman.impl;
import java.util.*;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public class IntegerPath extends AbstractPath implements IPath {
    
    private int length = Integer.MIN_VALUE;

    protected IntegerPath(IProblem<?> problem, int[] locations) {
    	super(problem, locations);
    }

    protected IntegerPath(IProblem<?> problem) {
    	super(problem, new int[0]);
        this.length = 0;
    }

    protected IntegerPath(IProblem<?> problem, int start) {
    	super(problem, new int[] { start });
    }

    protected IntegerPath(IProblem<?> problem, int[] locations, int nextLocation) {
    	super(problem, Arrays.copyOf(locations, locations.length + 1));
    	this.locations[this.locations.length - 1] = nextLocation;
    }
    
    @Override
	public IntegerPath to(int nextLocation) {
	    return new IntegerPath(problem, locations, nextLocation);
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
    public int compareTo(IPath o) {
        return Double.compare(getLength(), ((IntegerPath) o).getLength());
    }

	@Override
	public boolean isBetter(IPath globalBestPath) {
		boolean result = true;
		
		if (globalBestPath != null && ((IntegerPath) globalBestPath).getLength() < getLength()) {
			result = false;
		}
		
		return result;
	}
}