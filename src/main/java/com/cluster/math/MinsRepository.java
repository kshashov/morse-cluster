package com.cluster.math;

import com.cluster.math.model.Conformation;

import java.util.ArrayList;

/**
 * Created by envoy on 16.04.2017.
 */
public class MinsRepository {
    private int size;
    private int count;
    private ArrayList<Conformation> mins;

    public MinsRepository(int size) {
        this.mins = new ArrayList<>();
        this.count = 0;
        this.size = size;
    }

    public boolean  tryAddStructure(Structure structure) {
        //TODO
        return false;
    }
}
