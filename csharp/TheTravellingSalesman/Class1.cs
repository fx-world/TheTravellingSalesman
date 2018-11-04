using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;

namespace TheTravellingSalesman
{
    public class Location
    {
        public string name;
        public int index;

        public Location(int index, string name)
        {
            this.index = index;
            this.name = name;
        }

        override public string ToString()
        {
            return name;
        }
    }

    public interface IProblem
    {
        double getDistance(Location from, Location to);

        double calculateLength(List<Location> path);

        List<Location> getLocations();

        Path getBestPath();

        void setBestPath(Path bestPath);

        void resetBestPath();

        void calculateLengths(List<Path> paths);

        int getLocationsCount();
    }

    public class Path
    {
        private IProblem problem;
        private List<Location> locations;
        private double length = Double.NaN;

        public Path(IProblem problem, List<Location> locations)
        {
            this.problem = problem;
            this.locations = new List<Location>(locations);
        }

        public Path(IProblem problem)
        {
            this.problem = problem;
            this.locations = new List<Location>();
            this.length = 0;
        }

        public Path(IProblem problem, Location start)
        {
            this.problem = problem;
            this.locations = new List<Location>();
            this.locations.Add(start);
        }

        public Path(IProblem problem, List<Location> locations, Location nextLocation)
        {
            List<Location> temp = new List<Location>(locations);
            temp.Add(nextLocation);

            this.problem = problem;
            this.locations = temp;
        }

        public double getLength()
        {
            if (Double.IsNaN(length))
            {
                length = problem.calculateLength(locations);
            }

            return length;
        }

        public void setLength(double length)
        {
            this.length = length;
        }

        public Path to(Location nextLocation)
        {
            return new Path(problem, locations, nextLocation);
        }

        public IProblem getProblem()
        {
            return problem;
        }

        public List<Location> getLocations()
        {
            return locations;
        }

        override public String ToString()
        {
            return "Path [locations=" + locations + ", length=" + length + "]";
        }

        public Location getLast()
        {
            if (!(locations.Count > 0))
            {
                return locations[locations.Count - 1];
            }
            else
            {
                return null;
            }
        }

        public int getLocationsCount()
        {
            return locations.Count;
        }

        public int compareTo(Path o)
        {
            return getLength().CompareTo(o.getLength());
        }
    }

    public abstract class AbstractProblem
    {
        private List<Location> locations;
        private Path bestPath;
        private int locationsCount;

        public AbstractProblem() { }

        public AbstractProblem(List<Location> locations)
        {
            setLocations(locations);
        }

        protected void setLocations(List<Location> locations)
        {
            if (this.locations == null)
            {
                this.locations = locations;
                this.locationsCount = locations.Count;
            }
            else
            {
                throw new Exception("Locations already set");
            }
        }

        public List<Location> getLocations()
        {
            return locations;
        }

        public Path getBestPath()
        {
            return bestPath;
        }
        
        public void setBestPath(Path bestPath)
        {
            if (this.bestPath == null || (bestPath != null && bestPath.getLength() < this.bestPath.getLength()))
            {
                this.bestPath = bestPath;
            }
        }
    
        public void resetBestPath()
        {
            this.bestPath = null;
        }
        
        public int getLocationsCount()
        {
            return locationsCount;
        }
    }

    public class Problem2D : AbstractProblem, IProblem
    {
        private double[,] distances;

        public Problem2D(List<Location> locations, double[,] distances) : base(locations)
        {
            this.distances = distances;
        }

        public Problem2D(String path) : base()
        {
            this.distances = readGraph(path);

            List<Location> locations = new List<Location>();
            for (int i = 0; i<distances.Length; i++) {
                locations.Add(new Location(i, "L" + i));
            }
            setLocations(locations);
        }

    public double getDistance(Location from, Location to)
    {
        //int fromIndex = locations.indexOf(from);
        //int toIndex = locations.indexOf(to);
        //return distances[fromIndex][toIndex];
        return distances[from.index,to.index];
    }

    public double calculateLength(List<Location> path)
    {
        double result = 0;

        for (int i = 1; i < path.Count; i++)
        {
            result += getDistance(path[i - 1], path[i]);
        }

        return result;
    }

    public void calculateLengths(List<Path> paths)
    {
        foreach (Path path in paths)
        {
            double result = 0;
            List<Location> locations = path.getLocations();
            for (int i = 1; i < locations.Count; i++)
            {
                Location from = locations[i - 1];
                Location to = locations[i];
                result += distances[from.index, to.index];
            }
            path.setLength(result);
        }
    }

    // Read in graph from a file.
    // Allocates all memory.
    // Adds 1 to edge lengths to ensure no zero length edges.
    public double[,] readGraph(String path) 
    {
            double[,] graph = null;

            using (StreamReader buf = new StreamReader(path))
            {
                String line;
                int i = 0;

                while ((line = buf.ReadLine()) != null)
                {
                    String[] splitA = line.Split(" ");
                    List<String> split = new List<String>();
                    foreach (String s in splitA)
                    {
                        if (s.Length > 0)
                        {
                            split.Add(s);
                        }
                    }

                    if (graph == null)
                    {
                        graph = new double[split.Count, split.Count];
                    }
                    int j = 0;

                    foreach (String s in split)
                    {
                        if (s.Length > 0)
                        {
                            graph[i, j++] = Double.Parse(s) + 1;
                        }
                    }

                    i++;
                }
            }
        return graph;
        }
    }

    public interface ISolver
    {

        Path solve();
        IProblem getProblem();
    }

    public class BranchBoundSolver : ISolver
    {
        IProblem problem;

        public BranchBoundSolver(IProblem problem)
        {
            this.problem = problem;
        }

        public Path solve()
        {
            return calculateShortestPath(problem);
        }
        
        public IProblem getProblem()
        {
            return problem;
        }

        protected Path calculateShortestPath(IProblem problem)
        {
            return calculateShortestPath(problem, new Path(problem), problem.getLocations());
        }

        protected Path calculateShortestPath(IProblem problem, Path startPath, List<Location> leftToVisit)
        {
            Path bestPath = null;

            if (leftToVisit.Count == 1)
            {
                bestPath = startPath.to(leftToVisit[0]);

                problem.setBestPath(bestPath);

            }
            else
            {
                List<Path> nextPaths = new List<Path>();
                Path globalBestPath = problem.getBestPath();

                foreach (Location nextLocation in leftToVisit)
                {
                    Path nextPath = startPath.to(nextLocation);

                    if (globalBestPath == null || nextPath.getLength() < globalBestPath.getLength())
                    {
                        nextPaths.Add(nextPath);
                    }
                }

                nextPaths.Sort();

                foreach (Path nextPath in nextPaths)
                {
                    List<Location> newLeftToVisit = new List<Location>(leftToVisit);
                    Location nextLocation = nextPath.getLast();
                    newLeftToVisit.Remove(nextLocation);

                    Path path = calculateShortestPath(problem, nextPath, newLeftToVisit);

                    if (path != null && (bestPath == null || path.getLength() < bestPath.getLength()))
                    {
                        bestPath = path;
                    }
                }
            }

            return bestPath;
        }
    }

    public class PathFinder
    {

        public static void main(String[] args)
        {
            new PathFinder().run();
        }

        protected void run()
        {

    //        IProblem problem1 = new Problem2D("tspadata1.txt");
    //        testSolvers(problem1);
    //
    //        IProblem problem2 = new Problem2D("tspadata2.txt");
    //        testSolvers(problem2);
    //
    //        IProblem problem3 = new Problem2D("tspadata3.txt");
    //        testSolvers(problem3);

            for (int i = 5; i <= 20; i++) {
                Console.WriteLine("Problem with size " + i);
                IProblem problem = createProblem(i);

                testSolvers(problem);
            }
        }

        protected void testSolvers(IProblem problem)
        {
            List<ISolver> solvers = new List<ISolver>();
            //solvers.Add(new DummySolver(problem));
            //solvers.Add(new AntSolver(problem));
            //solvers.add(new ParallelBranchBoundSolver(problem));
            solvers.Add(new BranchBoundSolver(problem));

            foreach (ISolver solver in solvers)
            {
                //problem.resetBestPath();

                Stopwatch stopWatch = new Stopwatch();
                stopWatch.Start();
                Path path = solver.solve();
                stopWatch.Stop();

                Console.WriteLine(solver.ToString() + " best=" + path.getLength() + " time=" + stopWatch.Elapsed);
            }

            Console.WriteLine("");
        }

        protected IProblem createProblem(int count)
        {
            IProblem result = null;
            double[,] distances = createDistances(count);
            List<Location> locations = new List<Location>();

            for (int i = 0; i < count; i++)
            {
                locations.Add(new Location(i, "L" + i));
            }

            //result = new Problem2D(locations, distances);
            result = new Problem2D(locations, distances);

            return result;
        }

        protected double[,] createDistances(int count)
        {
            double[,] result = new double[count, count];
            Random rand = new Random(42);

            for (int i = 0; i < count; i++)
            {
                for (int j = 0; j < count; j++)
                {
                    result[i,j] = rand.Next(5000);
                }
            }

            return result;
        }

    }
    }
