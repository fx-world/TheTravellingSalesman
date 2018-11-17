package de.fxworld.thetravelingsalesman.impl;
import java.util.List;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public class Integer2DArrayProblem<T> extends AbstractProblem<T> implements IProblem<T> {

    private int[][] distances;

    public Integer2DArrayProblem(List<T> locations, int[][] distances) {
        super(locations);
        this.distances = distances;
    }

    public int getDistance(int from, int to) {
        return distances[from][to];
    }

    @Override
    public void calculateLengths(List<IPath<T>> paths) {

        for (IPath<T> path : paths) {
            int result = 0;
            int[] locations = path.getLocations();
            
            if (fixedFirstLocation >= 0 && locations.length > 0 && locations[0] != fixedFirstLocation) {
            	((IntegerPath<T>) path).setLength(Integer.MAX_VALUE);
            	continue;
            }         
        
            for (int i = 1; i < locations.length; i++) {
            	int from = locations[i - 1];
            	int to = locations[i];
                result += distances[from][to];
            }
        
            ((IntegerPath<T>) path).setLength(result);
        }
    }
    
	@Override
	public IPath<T> createPath(int[] locations) {
		return new IntegerPath<T>(this, locations);
	}
}