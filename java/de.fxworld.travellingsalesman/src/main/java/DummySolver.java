
public class DummySolver implements ISolver {

    private IProblem problem;

    public DummySolver(IProblem problem) {
        this.problem = problem;
    }

    @Override
    public Path solve() {
        Path result = new Path(problem, problem.getLocations());
        problem.setBestPath(result);
        return result;
    }

    @Override
    public IProblem getProblem() {
        return problem;
    }

    @Override
    public String toString() {
        return "DummySolver []";
    }
}
