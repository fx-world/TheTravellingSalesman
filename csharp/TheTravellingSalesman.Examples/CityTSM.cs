using System;
using System.Collections.Generic;
using TheTravelingSalesman;
using TheTravelingSalesman.Solvers;

namespace TheTravellingSalesman.Examples
{
	public class CityTSM
	{
		private IProblem<City> problem;

        public class City
        {
            internal string name;

            internal double lat;

            internal double lon;

            public City(string name, double lat, double lon)
            {
                this.name = name;
                this.lat = lat;
                this.lon = lon;
            }

            public override string ToString() => name;
        }

		public CityTSM()
		{
			IList<City> cities = new List<City>();
			cities.Add(new City("Dresden", 51.050407, 13.737262));
			cities.Add(new City("Freiburg", 47.997791, 7.842609));
			cities.Add(new City("Grimma", 51.236443, 12.720231));		
			cities.Add(new City("Gera", 50.884842, 12.079811));
			cities.Add(new City("Bremen", 53.073635, 8.806422));
			cities.Add(new City("Darmstadt", 49.878708, 8.646927));
			cities.Add(new City("Berlin", 52.520008, 13.404954));
			cities.Add(new City("Munich", 48.137154, 11.576124));
			
			problem = new ProblemBuilder<City>(typeof(City))
                .Locations(cities)
                .Distances((City c1, City c2) => DistFrom(c1.lat, c1.lon, c2.lat, c2.lon))
                .BuildIntegerArray();

            //ISolver<City> solver = new NearestNeighborSolver<City>(problem)
            ISolver<City> solver = new ParallelBranchBoundSolver<City>(problem);

            IPath<City> path = solver.Solve();

            System.Console.Out.WriteLine(solver.ToString());
            System.Console.Out.WriteLine("\tbest=" + path);
        }

		public static double DistFrom(double lat1, double lng1, double lat2, double lng2)
		{
			double earthRadius = 6371000;
			//meters
			double dLat = ToRadians(lat2 - lat1);
			double dLng = ToRadians(lng2 - lng1);
			double a = System.Math.Sin(dLat / 2) * System.Math.Sin(dLat / 2) + System.Math.Cos(ToRadians(lat1)) * System.Math.Cos(ToRadians(lat2)) * System.Math.Sin(dLng / 2) * System.Math.Sin(dLng / 2);
			double c = 2 * System.Math.Atan2(System.Math.Sqrt(a), System.Math.Sqrt(1 - a));
			double dist = (float)(earthRadius * c);
			return dist;
		}

        public static double ToRadians(double angle)
        {
            return (Math.PI / 180) * angle;
        }
    }
}
