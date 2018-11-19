using System;
using System.Collections.Generic;
using DE.Fxworld.Thetravelingsalesman;
using Sharpen;

namespace DE.Fxworld.Thetravelingsalesman.Solvers
{
	public class ParallelBranchBoundSolver<T> : ISolver<T>
	{
		private IProblem<T> problem;

		private ThreadPoolExecutor pool;

		private BlockingQueue<Runnable> workQueue;

		private volatile IPath<T> bestPath;

		private int threadCount;

		public ParallelBranchBoundSolver(IProblem<T> problem)
			: this(problem, Runtime.GetRuntime().AvailableProcessors() + 1)
		{
		}

		public ParallelBranchBoundSolver(IProblem<T> problem, int threadCount)
		{
			this.problem = problem;
			this.threadCount = threadCount;
			this.workQueue = new LinkedBlockingQueue<Runnable>();
			this.pool = new ThreadPoolExecutor(threadCount, threadCount, 5000, TimeUnit.Milliseconds, workQueue);
		}

		public virtual IPath<T> Solve()
		{
			pool.Submit(() => CalculateShortestPath(problem));
			while (pool.GetTaskCount() != pool.GetCompletedTaskCount())
			{
				try
				{
					System.Threading.Thread.Sleep(200);
				}
				catch (Exception)
				{
				}
			}
			pool.Shutdown();
			try
			{
				pool.AwaitTermination(500, TimeUnit.Milliseconds);
			}
			catch (Exception)
			{
			}
			return bestPath;
		}

		public virtual IProblem<T> GetProblem()
		{
			return problem;
		}

		protected internal virtual void CalculateShortestPath(IProblem<T> problem)
		{
			IList<int> leftToVisit = new List<int>();
			for (int i = 0; i < problem.GetLocationsCount(); i++)
			{
				leftToVisit.Add(i);
			}
			CalculateShortestPath(problem, problem.CreatePath(), leftToVisit);
		}

		protected internal virtual void CalculateShortestPath(IProblem<T> problem, IPath<T> startPath, IList<int> leftToVisit)
		{
			if (leftToVisit.Count == 1)
			{
				IPath<T> path = startPath.To(leftToVisit[0]);
				SetBestPath(path);
			}
			else
			{
				IList<IPath<T>> nextPaths = new List<IPath<T>>();
				IPath<T> globalBestPath = problem.GetBestPath();
				foreach (int nextLocation in leftToVisit)
				{
					IPath<T> nextPath = startPath.To(nextLocation);
					//if (globalBestPath == null || nextPath.getLength() < globalBestPath.getLength()) {
					if (nextPath.IsBetter(globalBestPath))
					{
						nextPaths.Add(nextPath);
					}
				}
				problem.CalculateLengths(nextPaths);
				nextPaths.Sort();
				if (workQueue.Count < threadCount && problem.GetLocationsCount() > startPath.GetLocationsCount() * 2)
				{
					foreach (IPath<T> nextPath in nextPaths)
					{
						IList<int> newLeftToVisit = new List<int>(leftToVisit);
						int nextLocation = nextPath.GetLast();
						newLeftToVisit.Remove(nextLocation);
						pool.Submit(() => CalculateShortestPath(problem, nextPath, newLeftToVisit));
					}
				}
				else
				{
					foreach (IPath<T> nextPath in nextPaths)
					{
						IList<int> newLeftToVisit = new List<int>(leftToVisit);
						int nextLocation = nextPath.GetLast();
						newLeftToVisit.Remove(nextLocation);
						CalculateShortestPath(problem, nextPath, newLeftToVisit);
					}
				}
			}
		}

		protected internal virtual void SetBestPath(IPath<T> path)
		{
			lock (this)
			{
				if (path != null && path.IsBetter(bestPath))
				{
					bestPath = path;
					problem.SetBestPath(path);
				}
			}
		}

		public override string ToString()
		{
			return "ParallelBranchBoundSolver [threadCount=" + threadCount + "]";
		}
	}
}
