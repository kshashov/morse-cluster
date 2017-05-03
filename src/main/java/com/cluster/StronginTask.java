package com.cluster;

import com.cluster.math.Strongin;

import java.util.concurrent.Callable;

/**
 * Created by envoy on 02.05.2017.
 */
public class StronginTask implements Callable<Strongin> {
    private final int index;
    private Strongin strongin;

    public StronginTask(Strongin strongin, int index) {
        this.strongin = strongin;
        this.index = index;
    }

    @Override
    public Strongin call() throws Exception {
        strongin.solve();
        return strongin;
    }
}
