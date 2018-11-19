using System.Collections.Generic;
using DE.Fxworld.Thetravelingsalesman.Impl;
using Sharpen;
using Sharpen.Function;

namespace DE.Fxworld.Thetravelingsalesman
{
	public class ProblemBuilder<T>
	{
		private IList<T> locations__;

		private BiFunction<T, T, double> distanceCalc;

		private int intMultiplicator = 1;

		private int fixedFirstLocation = -1;

		private ProblemBuilder()
		{
		}

		public static ProblemBuilder<T> Create<T>()
		{
			return new ProblemBuilder<T>();
		}

		public virtual ProblemBuilder<T> Locations(IList<T> locations)
		{
			this.locations__ = locations;
			return this;
		}

		public virtual ProblemBuilder<T> Distances(BiFunction<T, T, double> distanceCalc)
		{
			this.distanceCalc = distanceCalc;
			return this;
		}

		public virtual IProblem<T> BuildDoubleArray()
		{
			return BuildDoubleProblem((IList<T> locations, double[][] dist) => new DoubleArrayProblem<T>(locations, dist));
		}

		public virtual IProblem<T> BuildDouble2DArray()
		{
			return BuildDoubleProblem((IList<T> locations, double[][] dist) => new Double2DArrayProblem<T>(locations, dist));
		}

		public virtual IProblem<T> BuildDoubleProblem(BiFunction<IList<T>, double[][], IProblem<T>> creator)
		{
			double[][] dist = new double[locations__.Count][];
			for (int i = 0; i < locations__.Count; i++)
			{
				dist[i] = new double[locations__.Count];
				for (int j = 0; j < locations__.Count; j++)
				{
					dist[i][j] = distanceCalc.Apply(locations__[i], locations__[j]);
				}
			}
			IProblem<T> result = creator.Apply(locations__, dist);
			result.SetFixedFirstLocation(fixedFirstLocation);
			return result;
		}

		public virtual IProblem<T> BuildIntegerArray()
		{
			return BuildIntegerArray((IList<T> locations, int[][] dist) => new IntegerArrayProblem<T>(locations, dist));
		}

		public virtual IProblem<T> BuildInteger2DArray()
		{
			return BuildIntegerArray((IList<T> locations, int[][] dist) => new Integer2DArrayProblem<T>(locations, dist));
		}

		public virtual IProblem<T> BuildIntegerArray(BiFunction<IList<T>, int[][], IProblem<T>> creator)
		{
			int[][] dist = new int[locations__.Count][];
			for (int i = 0; i < locations__.Count; i++)
			{
				dist[i] = new int[locations__.Count];
				for (int j = 0; j < locations__.Count; j++)
				{
					dist[i][j] = MapToInt(distanceCalc.Apply(locations__[i], locations__[j]));
				}
			}
			IProblem<T> result = creator.Apply(locations__, dist);
			result.SetFixedFirstLocation(fixedFirstLocation);
			return result;
		}

		protected internal virtual int MapToInt(double value)
		{
			return (int)(value * intMultiplicator);
		}
	}
}
