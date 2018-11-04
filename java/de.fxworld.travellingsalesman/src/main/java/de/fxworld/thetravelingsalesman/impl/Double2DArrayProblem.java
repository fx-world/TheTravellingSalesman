package de.fxworld.thetravelingsalesman.impl;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.IProblem;

public class Double2DArrayProblem<T> extends AbstractProblem<T> implements IProblem<T> {

    private double[][] distances;

    public Double2DArrayProblem(List<T> locations, double[][] distances) {
        super(locations);
        this.distances = distances;
    }

    public Double2DArrayProblem(String path) throws IOException {
        super();
        this.distances = readGraph(path);

//        List<T> locations = new ArrayList<>();
//        for (int i = 0; i < distances.length; i++) {
//            locations.add("L" + i);
//        }
//        setLocations(locations);
    }

    public double getDistance(int from, int to) {
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
            double result = 0;
            int[] locations = path.getLocations();
            for (int i = 1; i < locations.length; i++) {
            	int from = locations[i - 1];
            	int to = locations[i];
                result += distances[from][to];
            }
            ((DoublePath) path).setLength(result);
        }
    }

    // Read in graph from a file.
    // Allocates all memory.
    // Adds 1 to edge lengths to ensure no zero length edges.
    public double[][] readGraph(String path) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader buf = new BufferedReader(fr);
        String line;
        int i = 0;

        double[][] graph = null;

        while ((line = buf.readLine()) != null) {
            String splitA[] = line.split(" ");
            LinkedList<String> split = new LinkedList<>();
            for (String s : splitA) {
                if (!s.isEmpty()) {
                    split.add(s);
                }
            }

            if (graph == null) {
                graph = new double[split.size()][split.size()];
            }
            int j = 0;

            for (String s : split) {
                if (!s.isEmpty()) {
                    graph[i][j++] = Double.parseDouble(s) + 1;
                }
            }

            i++;
        }

        return graph;
    }

	@Override
	public IPath createPath(int[] locations) {
		return new DoublePath(this, locations);
	}
}