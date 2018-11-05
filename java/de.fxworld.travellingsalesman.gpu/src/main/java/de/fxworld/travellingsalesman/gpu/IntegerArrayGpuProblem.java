package de.fxworld.travellingsalesman.gpu;

import java.util.List;

import com.aparapi.Kernel;
import com.aparapi.Range;

import de.fxworld.thetravelingsalesman.IPath;
import de.fxworld.thetravelingsalesman.impl.IntegerArrayProblem;
import de.fxworld.thetravelingsalesman.impl.IntegerPath;

public class IntegerArrayGpuProblem<T> extends IntegerArrayProblem<T> {

	public IntegerArrayGpuProblem(List<T> locations, int[][] distances) {
		super(locations, distances);
	}

	@Override
	public void calculateLengths(final List<IPath> paths) {

		Kernel kernel = new Kernel() {
		    @Override
		    public void run() {
		        int id = getGlobalId();
		        IPath path = paths.get(id);
	            
		        int result = 0;
	            int[] locations = path.getLocations();
	            for (int i = 1; i < locations.length; i++) {
	                int from = locations[i - 1];
	                int to = locations[i];
	                result += distances[from * getLocationsCount() + to];
	            }
	            ((IntegerPath) path).setLength(result);
		    }
		};
		
		Range range = Range.create(paths.size());
		kernel.execute(range);
		System.out.println("Execution mode = "+kernel.getExecutionMode());
	}
}
