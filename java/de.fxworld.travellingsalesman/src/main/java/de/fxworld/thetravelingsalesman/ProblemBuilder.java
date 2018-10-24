package de.fxworld.thetravelingsalesman;
import java.util.List;
import java.util.function.BiFunction;

public class ProblemBuilder<T> {

	private List<T> locations;
	private BiFunction<T, T, Double> distanceCalc;
	
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

	public IProblem<T> build() {
		double[][] dist = new double[locations.size()][];
		
		for (int i = 0; i < locations.size(); i++) {
			dist[i] = new double[locations.size()];
			for (int j = 0; j < locations.size(); j++) {
				dist[i][j] = distanceCalc.apply(locations.get(i), locations.get(j));
			}
		}
		
		return new ProblemArray<T>(locations, dist);
	}

}
