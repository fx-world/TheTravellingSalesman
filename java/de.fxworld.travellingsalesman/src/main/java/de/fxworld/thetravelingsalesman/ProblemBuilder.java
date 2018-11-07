package de.fxworld.thetravelingsalesman;
import java.util.List;
import java.util.function.BiFunction;

import de.fxworld.thetravelingsalesman.impl.*;

public class ProblemBuilder<T> {

	private List<T> locations;
	private BiFunction<T, T, Double> distanceCalc;
	private int intMultiplicator = 1;
	private int fixedFirstLocation = -1;
	
	private ProblemBuilder() {		
	}
	
	public static <T> ProblemBuilder<T> create(Class<T> clazz) {
		return new ProblemBuilder<T>();
	}

	public ProblemBuilder<T> locations(List<T> locations) {
		this.locations = locations;
		return this;
	}

	public ProblemBuilder<T> distances(BiFunction<T, T, Double> distanceCalc) {
		this.distanceCalc = distanceCalc;
		return this;
	}

	public IProblem<T> buildDoubleArray() {
		return buildDoubleProblem((locations, dist) -> new DoubleArrayProblem<T>(locations, dist));		
	}
	
	public IProblem<T> buildDouble2DArray() {
		return buildDoubleProblem((locations, dist) -> new Double2DArrayProblem<T>(locations, dist));
	}
	
	public IProblem<T> buildDoubleProblem(BiFunction<List<T>, double[][], IProblem<T>> creator) {
		double[][] dist = new double[locations.size()][];
		
		for (int i = 0; i < locations.size(); i++) {
			dist[i] = new double[locations.size()];
			for (int j = 0; j < locations.size(); j++) {
				dist[i][j] = distanceCalc.apply(locations.get(i), locations.get(j));
			}
		}
		
		IProblem<T> result = creator.apply(locations, dist);
		
		result.setFixedFirstLocation(fixedFirstLocation);
		
		return result;
	}
	
	public IProblem<T> buildIntegerArray() {
		return buildIntegerArray((locations, dist) -> new IntegerArrayProblem<T>(locations, dist));
	}
	
	public IProblem<T> buildInteger2DArray() {
		return buildIntegerArray((locations, dist) -> new Integer2DArrayProblem<T>(locations, dist));
	}
	
	public IProblem<T> buildIntegerArray(BiFunction<List<T>, int[][], IProblem<T>> creator) {
		int[][] dist = new int[locations.size()][];
		
		for (int i = 0; i < locations.size(); i++) {
			dist[i] = new int[locations.size()];
			for (int j = 0; j < locations.size(); j++) {
				dist[i][j] = mapToInt(distanceCalc.apply(locations.get(i), locations.get(j)));
			}
		}
		
		IProblem<T> result = creator.apply(locations, dist);
		
		result.setFixedFirstLocation(fixedFirstLocation);
		
		return result;
	}

	protected int mapToInt(double value) {
		return (int) (value * intMultiplicator);
	}
}
