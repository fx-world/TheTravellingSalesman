using System.Collections.Generic;
using Sharpen;

namespace DE.Fxworld.Thetravelingsalesman
{
	public interface IProblem<L>
	{
		IList<L> GetLocations();

		IPath<L> GetBestPath();

		void SetBestPath(IPath<L> bestPath);

		void ResetBestPath();

		void CalculateLengths(IList<IPath<L>> paths);

		int GetLocationsCount();

		IPath<L> CreatePath(int[] result);

		IPath<L> CreatePath();

		void SetFixedFirstLocation(int fixedFirstLocation);
	}
}
