package de.fxworld.thetravelingsalesman;
import java.util.*;

public class CityTSM {

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
		new CityTSM().run();		
	}
	
	public CityTSM() {
		List<City> cities = new ArrayList<>();
		
//		cities.add(new City("Dresden", 51.050407, 13.737262));
//		cities.add(new City("Freiburg", 47.997791, 7.842609));
//		cities.add(new City("Grimma", 51.236443, 12.720231));		
//		cities.add(new City("Gera", 50.884842, 12.079811));
//		cities.add(new City("Bremen", 53.073635, 8.806422));
//		cities.add(new City("Darmstadt", 49.878708, 8.646927));
//		cities.add(new City("Berlin", 52.520008, 13.404954));
//		cities.add(new City("Munich", 48.137154, 11.576124));
		Random rand = new Random();
		for (int i = 0; i < 20; i++) {
			cities.add(new City("City " + i, rand.nextDouble() * 0, rand.nextDouble() * 14));
		}
		
		problem = ProblemBuilder.create(City.class)
			.locations(cities)
			.distances((c1, c2) -> distFrom(c1.lat, c1.lon, c2.lat, c2.lon))
			.build();
	}
	
	public void run() {
		problem.resetBestPath();
		
		System.out.println(problem);
		
		testSolvers(new DummySolver<City>(problem));
		testSolvers(new NearestNeighborSolver<City>(problem));
		testSolvers(new ParallelBranchBoundSolver<City>(problem));
		//testSolvers(new BranchBoundSolver<City>(problem));		
	}
	
	protected void testSolvers(ISolver<City> solver) {
        long from = System.currentTimeMillis();
        Path path = solver.solve();
        long to = System.currentTimeMillis();

        System.out.println(solver.toString() + " best=" + path.getLength() + " time=" + (to - from));
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
