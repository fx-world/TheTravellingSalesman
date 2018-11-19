using Sharpen;

namespace DE.Fxworld.Thetravelingsalesman
{
	public interface ISolver<T>
	{
		IPath Solve();

		IProblem<T> GetProblem();
	}
}
