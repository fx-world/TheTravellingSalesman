package de.fxworld.thetravelingsalesman.impl;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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

    public double calculateLength(int[] path) {
        double result = 0;

        for (int i = 1; i < path.length; i++) {
            result += getDistance(path[i - 1], path[i]);
        }

        return result;
    }

    @Override
    public void calculateLengths(List<IPath> paths) {

        for (IPath path : paths) {
            int result = 0;
            int[] locations = path.getLocations();
            for (int i = 1; i < locations.length; i++) {
            	int from = locations[i - 1];
            	int to = locations[i];
                result += distances[from][to];
            }
            ((IntegerPath) path).setLength(result);
        }
    }
    
	@Override
	public IPath createPath(int[] locations) {
		return new IntegerPath(this, locations);
	}
}