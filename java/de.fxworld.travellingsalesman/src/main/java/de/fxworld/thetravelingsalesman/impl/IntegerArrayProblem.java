package de.fxworld.thetravelingsalesman.impl;
import java.util.List;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public class IntegerArrayProblem<L> extends AbstractProblem<L>  implements IProblem<L> {

    protected int[] distances;

    public IntegerArrayProblem(List<L> locations, int[][] distances) {
        super(locations);
        this.distances = convertDistances(distances, getLocationsCount());
    }

    protected int[] convertDistances(int[][] distances, int locationCount) {
    	int[] result = new int[locationCount * locationCount];

        for (int i = 0; i < locationCount; i++) {
            for (int j = 0; j < locationCount; j++) {
                result[i * locationCount + j] = distances[i][j];
            }
        }

        return result;
    }

    public int getDistance(int from, int to) {
        return distances[from * getLocationsCount() + to];
    }

    @Override
    public void calculateLengths(List<IPath> paths) {

        for (IPath path : paths) {
            int result = 0;
            int[] locations = path.getLocations();
            
            if (fixedFirstLocation >= 0 && locations.length > 0 && locations[0] != fixedFirstLocation) {
            	((IntegerPath) path).setLength(Integer.MAX_VALUE);
            	continue;
            }  
            
            for (int i = 1; i < locations.length; i++) {
                int from = locations[i - 1];
                int to = locations[i];
                result += distances[from * getLocationsCount() + to];
            }
            ((IntegerPath) path).setLength(result);
        }
    }

	@Override
	public IPath createPath(int[] locations) {
		return new IntegerPath(this, locations);
	}
    
	@Override
	public String toString() {
		return "ProblemArray [LocationsCount=" + getLocationsCount() + "]";
	}
}