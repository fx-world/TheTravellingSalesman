package de.fxworld.travellingsalesman.gpu;
import java.util.*;

import de.fxworld.thetravelingsalesman.*;
import de.fxworld.thetravelingsalesman.solvers.*;

public class GpuCityTSM {

	private IProblem<City> problem;

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
	
	public static void main(String[] args) {
		new GpuCityTSM().run();		
	}
	
	public GpuCityTSM() {
		List<City> cities = new ArrayList<>();
		
		Random rand = new Random();
		for (int i = 0; i < 20; i++) {
			cities.add(new City("City " + i, rand.nextDouble() * 0, rand.nextDouble() * 14));
		}
		
		problem = ProblemBuilder.create(City.class)
			.locations(cities)
			.distances((c1, c2) -> distFrom(c1.lat, c1.lon, c2.lat, c2.lon))
			.buildIntegerArray((locations, distances) -> new IntegerArrayGpuProblem<>(locations, distances));
			//.buildIntegerArray();
	}
	
	public void run() {
		problem.resetBestPath();
		
		System.out.println(problem);
		
		testSolvers(new DummySolver<City>(problem));
		testSolvers(new NearestNeighborSolver<City>(problem));
		testSolvers(new EvolutionSolver<City>(problem));
		testSolvers(new ParallelBranchBoundSolver<City>(problem));
	}
	
	protected void testSolvers(ISolver<City> solver) {
        long from = System.currentTimeMillis();
        IPath path = solver.solve();
        long to = System.currentTimeMillis();

        System.out.println(solver.toString());
        System.out.println("\ttime=" + (to - from));
        System.out.println("\tbest=" + path);
    }
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 6371000; //meters
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = (float) (earthRadius * c);

	    return dist;
	}

}
