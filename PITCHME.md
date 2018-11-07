---
# The Travelling Salesman

---

### Problem

Given a list of locations, what is the shortest route that contains every location.

[Wikipedia](https://en.wikipedia.org/wiki/Travelling_salesman_problem)

---

### Brute Force

```java
for (int i = 0; i < factorial(locations.size()); i++) { 
	Path path = permutation(factorial(locations.size()), i);
	
	if (path.isBetter(bestPath)) {
    	bestPath = path;
    }
}
```
@[1]
@[2]
@[3-4]
@[5-7]
+++

### Branch & Bound

```
Path solve(Path pathUntilNow, List<Location> locationsLeftToVisit) {
	for (Location location in locationsLeftToVisit) {
	    Path newPath = pathUntilNow.to(location)
	    List<Location> newLocations = locationsLeftToVisit.remove(newPath)
	
	    if (newPath.isBetter(bestPath)) {
	        Path mayBeBestPath = solve(newPath, newLocations)
	
	        if (mayBeBestPath.isBetter(bestPath)) {
	            bestPath = mayBeBestPath;
	        }
	    }
	}
}
```
+++

### Others

* Evolution
* Ant
* Nearest Neighbor

---
<canvas data-chart="bar">
<!-- 
{
 "data": {
  "labels": ["NearestNeighborSolver","BranchBoundSolver","ParallelBranchBoundSolver","EvolutionSolver"],
  "datasets": [
   {
    "data":[1,435968,43902,22355],
    "label":"","backgroundColor":"rgba(20,220,220,.8)"
   }
  ]
 }, 
 "options": { "responsive": "true" }
}
-->
</canvas>