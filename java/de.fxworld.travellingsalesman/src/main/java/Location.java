/**
 * Copyright (c) Robotron Datenbank-Software GmbH and/or its affiliates. All rights reserved.
 */

public class Location {
    private String name;
    protected int index;

    public Location(int index, String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}