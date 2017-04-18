package com.cluster.math;

import com.cluster.math.model.Conformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by envoy on 16.04.2017.
 */
public class MinsRepository {
    private int size;
    private int count;
    private ArrayList<Conformation> mins;
    private Map<String, Conformation> cache;


    public MinsRepository(int size) {
        this.mins = new ArrayList<>();
        this.count = 0;
        this.size = size;
        cache = new HashMap<>();
    }

    public boolean tryAddConf(Conformation conformation) {
        //TODO
        return false;
    }

    public Map<String, Conformation> getCache() {
        return cache;
    }
}
