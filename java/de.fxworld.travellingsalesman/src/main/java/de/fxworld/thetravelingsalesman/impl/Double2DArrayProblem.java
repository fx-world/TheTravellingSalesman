package de.fxworld.thetravelingsalesman.impl;
import java.util.List;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public class Double2DArrayProblem<T> extends AbstractProblem<T> implements IProblem<T> {

    private double[][] distances;

    public Double2DArrayProblem(List<T> locations, double[][] distances) {
        super(locations);
        this.distances = distances;
    }

    public double getDistance(int from, int to) {
        return distances[from][to];
    }

    @Override
    public void calculateLengths(List<IPath<T>> paths) {

        for (IPath<T> path : paths) {
            double result = 0;
            int[] locations = path.getLocations();
            
            if (fixedFirstLocation >= 0 && locations.length > 0 && locations[0] != fixedFirstLocation) {
            	((DoublePath<T>) path).setLength(Double.POSITIVE_INFINITY);
            	continue;
            }
            
            for (int i = 1; i < locations.length; i++) {
            	int from = locations[i - 1];
            	int to = locations[i];
                result += distances[from][to];
            }
            ((DoublePath<T>) path).setLength(result);
        }
    }

	@Override
	public IPath<T> createPath(int[] locations) {
		return new DoublePath<T>(this, locations);
	}
}