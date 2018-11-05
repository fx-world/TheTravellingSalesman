package de.fxworld.travellingsalesman.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import de.fxworld.thetravelingsalesman.IProblem;
import de.fxworld.thetravelingsalesman.ProblemBuilder;
import de.fxworld.thetravelingsalesman.solvers.BranchBoundSolver;
import de.fxworld.thetravelingsalesman.solvers.DummySolver;
import de.fxworld.thetravelingsalesman.solvers.EvolutionSolver;
import de.fxworld.thetravelingsalesman.solvers.NearestNeighborSolver;
import de.fxworld.thetravelingsalesman.solvers.ParallelBranchBoundSolver;

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Threads(1)
@State(Scope.Benchmark)
public class CityTSM {

	@Param({ "5", "10", "15", "18" })
	public int cityCount;
	
	@Param({ "Double2D", "Double", "Integer2D", "Integer"})
	public String arrayType;

	class City {
		String name;
		double lat;
		double lon;

		public City(String name, double lat, double lon) {
			this.name = name;
			this.lat = lat;
			this.lon = lon;
		}
	}

	protected IProblem<City> createProblem() {
		List<City> cities = new ArrayList<>();

		Random rand = new Random();
		for (int i = 0; i < cityCount; i++) {
			cities.add(new City("City " + i, rand.nextDouble() * 0, rand.nextDouble() * 14));
		}

		IProblem<City> problem = null;
		ProblemBuilder<City> builder = ProblemBuilder
				.create(City.class)
				.locations(cities)
				.distances((c1, c2) -> distFrom(c1.lat, c1.lon, c2.lat, c2.lon));

		if ("Double2D".equals(arrayType)) {
			problem = builder.buildDouble2DArray();
		
		} else if ("Double".equals(arrayType)) {
			problem = builder.buildDoubleArray();
			
		} else if ("Integer2D".equals(arrayType)) {
			problem = builder.buildInteger2DArray();
			
		} else if ("Integer".equals(arrayType)) {
			problem = builder.buildIntegerArray();
		}
		
		return problem;
	}

	@Benchmark
	public void testDummySolver() {
		IProblem<City> problem = createProblem();
		new DummySolver<City>(problem).solve();
	}

	@Benchmark
	public void testNearestNeighborSolver() {
		IProblem<City> problem = createProblem();
		new NearestNeighborSolver<City>(problem).solve();
	}
	
	@Benchmark
	public void testEvolutionSolver() {
		IProblem<City> problem = createProblem();
		new EvolutionSolver<City>(problem).solve();
	}
	
	@Benchmark
	public void testBranchBoundSolverSolver() {
		IProblem<City> problem = createProblem();
		new BranchBoundSolver<City>(problem).solve();
	}
	
	@Benchmark
	public void testParallelBranchBoundSolver() {
		IProblem<City> problem = createProblem();
		new ParallelBranchBoundSolver<City>(problem).solve();
	}
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = (float) (earthRadius * c);

		return dist;
	}

}
