package de.fxworld.thetravelingsalesman.impl;
import java.util.List;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public class DoubleArrayProblem<L> extends AbstractProblem<L>  implements IProblem<L> {

    private double[] distances;

    public DoubleArrayProblem(List<L> locations, double[][] distances) {
        super(locations);
        this.distances = convertDistances(distances, getLocationsCount());
    }

    protected double[] convertDistances(double[][] distances, int locationCount) {
        double[] result = new double[locationCount * locationCount];

        for (int i = 0; i < locationCount; i++) {
            for (int j = 0; j < locationCount; j++) {
                result[i * locationCount + j] = distances[i][j];
            }
        }

        return result;
    }

    public double getDistance(int from, int to) {
        return distances[from * getLocationsCount() + to];
    }

    @Override
    public void calculateLengths(List<IPath> paths) {

        for (IPath path : paths) {
            double result = 0;
            int[] locations = path.getLocations();
            
            if (fixedFirstLocation >= 0 && locations.length > 0 && locations[0] != fixedFirstLocation) {
            	((DoublePath) path).setLength(Double.POSITIVE_INFINITY);
            	continue;
            }  
            
            for (int i = 1; i < locations.length; i++) {
                int from = locations[i - 1];
                int to = locations[i];
                result += distances[from * getLocationsCount() + to];
            }
            ((DoublePath) path).setLength(result);
        }
    }

	@Override
	public IPath createPath(int[] locations) {
		return new DoublePath(this, locations);
	}
    
	@Override
	public String toString() {
		return "ProblemArray [LocationsCount=" + getLocationsCount() + "]";
	}
}