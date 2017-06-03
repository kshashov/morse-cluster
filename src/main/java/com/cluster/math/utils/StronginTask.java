package com.cluster.math.utils;

import com.cluster.math.Strongin;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by envoy on 02.05.2017.
 */
public class StronginTask extends FutureTask<Strongin> {
    private ProgressCallBack progressCallBack;

    public StronginTask(Strongin strongin, ProgressCallBack progressCallBack) {
        super(new StronginCallable(strongin, progressCallBack));
        this.progressCallBack = progressCallBack;
    }

    public ProgressCallBack getProgressCallBack() {
        return progressCallBack;
    }

    public static class StronginCallable implements Callable<Strongin> {
        private final ProgressCallBack progressCallBack;
        private final Strongin strongin;

        public StronginCallable(Strongin strongin, ProgressCallBack progressCallBack) {
            this.strongin = strongin;
            this.progressCallBack = progressCallBack;
        }

        @Override
        public Strongin call() throws Exception {
            progressCallBack.onProgress(0);
            strongin.solve(progressCallBack);
            return strongin;
        }
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

        public abstract void onFinish(int percent);

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
