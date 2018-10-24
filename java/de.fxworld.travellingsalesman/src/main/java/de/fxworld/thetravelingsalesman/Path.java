package de.fxworld.thetravelingsalesman;
import java.util.*;

public class Path implements Comparable<Path> {
    private IProblem<?> problem;
    private int[] locations;
    private double length = Double.NaN;

    public Path(IProblem<?> problem, int[] locations) {
        this.problem = problem;
        this.locations = locations;
    }

    public Path(IProblem<?> problem) {
        this.problem = problem;
        this.locations = new int[0];
        this.length = 0;
    }

    public Path(IProblem<?> problem, int start) {
        this.problem = problem;
        this.locations = new int[] { start };
    }

    public Path(IProblem<?> problem, int[] locations, int nextLocation) {
    	this.locations = Arrays.copyOf(locations, locations.length + 1);
    	this.locations[this.locations.length - 1] = nextLocation;
        this.problem = problem;
    }

    public double getLength() {
        if (Double.isNaN(length)) {
            length = problem.calculateLength(locations);
        }

        return length;
    }

    protected void setLength(double length) {
        this.length = length;
    }

    public Path to(int nextLocation) {
        return new Path(problem, locations, nextLocation);
    }

    public IProblem<?> getProblem() {
        return problem;
    }

    public int[] getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        return "Path [locations=" + Arrays.toString(locations) + ", length=" + length + "]";
    }

    public int getLast() {
        if (locations.length > 0) {
            return locations[locations.length - 1];
        } else {
            return -1;
        }
    }

    public int getLocationsCount() {
        return locations.length;
    }

    @Override
    public int compareTo(Path o) {
        return Double.compare(getLength(), o.getLength());
    }

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