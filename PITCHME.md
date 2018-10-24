---
# The Travelling Salesman

---

### Problem

Given a list of locations, what is the shortest route that contains every location.

[Wikipedia](https://en.wikipedia.org/wiki/Travelling_salesman_problem)

---

### Brute Force

```
Path solve(Path pathUntilNow, List<Location> locationsLeftToVisit) {
    Path bestPath

    for (Location location in locationsLeftToVisit) {
        Path newPath = pathUntilNow.to(location)
        List<Location> newLocations = locationsLeftToVisit.remove(newPath)

        Path mayBeBestPath = solve(newPath, newLocations)

        if (mayBeBestPath.isBetter(bestPath)) {
            bestPath = mayBeBestPath;
        }
    }

    return bestPath
}
```

+++

### Branch & Bound

```
Path solve(Path pathUntilNow, List<Location> locationsLeftToVisit) {
    Path bestPath

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

    return bestPath
}
```