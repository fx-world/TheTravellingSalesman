using DE.Fxworld.Thetravelingsalesman;
using Sharpen;

namespace DE.Fxworld.Thetravelingsalesman.Solvers
{
	public class DummySolver<T> : ISolver<T>
	{
		private IProblem<T> problem;

		public DummySolver(IProblem<T> problem)
		{
			this.problem = problem;
		}

		public virtual IPath<T> Solve()
		{
			int[] locations = new int[problem.GetLocationsCount()];
			for (int i = 0; i < locations.Length; i++)
			{
				locations[i] = i;
			}
			IPath<T> result = problem.CreatePath(locations);
			problem.SetBestPath(result);
			return result;
		}

		public virtual IProblem<T> GetProblem()
		{
			return problem;
		}

		public override string ToString()
		{
			return "DummySolver []";
		}
	}
}
