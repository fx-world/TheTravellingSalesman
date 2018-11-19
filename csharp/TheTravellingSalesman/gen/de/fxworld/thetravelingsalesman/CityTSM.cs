using System;
using System.Collections.Generic;
using DE.Fxworld.Thetravelingsalesman.Solvers;
using Sharpen;

namespace DE.Fxworld.Thetravelingsalesman
{
	public class CityTSM
	{
		private IProblem<CityTSM.City> problem;

		public class City
		{
			internal string name;

			internal double lat;

			internal double lon;

			public City(CityTSM _enclosing, string name, double lat, double lon)
			{
				this._enclosing = _enclosing;
				this.name = name;
				this.lat = lat;
				this.lon = lon;
			}

			private readonly CityTSM _enclosing;
		}

		public static void Main(string[] args)
		{
			new CityTSM().Run();
		}

		public CityTSM()
		{
			IList<CityTSM.City> cities = new List<CityTSM.City>();
			//		cities.add(new City("Dresden", 51.050407, 13.737262));
			//		cities.add(new City("Freiburg", 47.997791, 7.842609));
			//		cities.add(new City("Grimma", 51.236443, 12.720231));		
			//		cities.add(new City("Gera", 50.884842, 12.079811));
			//		cities.add(new City("Bremen", 53.073635, 8.806422));
			//		cities.add(new City("Darmstadt", 49.878708, 8.646927));
			//		cities.add(new City("Berlin", 52.520008, 13.404954));
			//		cities.add(new City("Munich", 48.137154, 11.576124));
			Random rand = new Random();
			for (int i = 0; i < 20; i++)
			{
				cities.Add(new CityTSM.City(this, "City " + i, rand.NextDouble() * 0, rand.NextDouble() * 14));
			}
			problem = ProblemBuilder.Create<CityTSM.City>().Locations(cities).Distances((CityTSM.City c1, CityTSM.City c2) => DistFrom(c1.lat, c1.lon, c2.lat, c2.lon)).BuildIntegerArray();
		}

		public virtual void Run()
		{
			problem.ResetBestPath();
			System.Console.Out.WriteLine(problem);
			TestSolvers(new DummySolver<CityTSM.City>(problem));
			TestSolvers(new NearestNeighborSolver<CityTSM.City>(problem));
			TestSolvers(new EvolutionSolver<CityTSM.City>(problem));
			TestSolvers(new ParallelBranchBoundSolver<CityTSM.City>(problem));
			TestSolvers(new BranchBoundSolver<CityTSM.City>(problem));
		}

		protected internal virtual void TestSolvers(ISolver<CityTSM.City> solver)
		{
			long from = Runtime.CurrentTimeMillis();
			IPath path = solver.Solve();
			long to = Runtime.CurrentTimeMillis();
			System.Console.Out.WriteLine(solver.ToString());
			System.Console.Out.WriteLine("\ttime=" + (to - from));
			System.Console.Out.WriteLine("\tbest=" + path);
		}

		public static double DistFrom(double lat1, double lng1, double lat2, double lng2)
		{
			double earthRadius = 6371000;
			//meters
			double dLat = Math.ToRadians(lat2 - lat1);
			double dLng = Math.ToRadians(lng2 - lng1);
			double a = System.Math.Sin(dLat / 2) * System.Math.Sin(dLat / 2) + System.Math.Cos(Math.ToRadians(lat1)) * System.Math.Cos(Math.ToRadians(lat2)) * System.Math.Sin(dLng / 2) * System.Math.Sin(dLng / 2);
			double c = 2 * System.Math.Atan2(System.Math.Sqrt(a), System.Math.Sqrt(1 - a));
			double dist = (float)(earthRadius * c);
			return dist;
		}
	}
}
