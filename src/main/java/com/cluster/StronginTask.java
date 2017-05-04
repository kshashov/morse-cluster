package com.cluster;

import com.cluster.math.Strongin;

import java.util.concurrent.Callable;

/**
 * Created by envoy on 02.05.2017.
 */
public class StronginTask implements Callable<Strongin> {
    private final ProgressCallBack progressCallBack;
    private final Strongin strongin;

    public StronginTask(Strongin strongin, ProgressCallBack progressCallBack) {
        this.strongin = strongin;
        this.progressCallBack = progressCallBack;
    }

    @Override
    public Strongin call() throws Exception {
        strongin.solve(progressCallBack);
        return strongin;
    }

    public static abstract class ProgressCallBack implements Cloneable {
        private int id;

        public ProgressCallBack() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public abstract void onProgress(int percent);

        public ProgressCallBack clone() {
            try {
                return (ProgressCallBack) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
