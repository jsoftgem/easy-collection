package com.jsoftgem.easycollection.util;

/**
 * Created by rickzx98 on 27/02/2016.
 */
public abstract class CollectionProgress implements ECollectionUsecase<Void> {
    public abstract void progress(int processed);

    public void setTotal(int total) {
    }

    public void setPercentage(double percentage) {
    }

    @SuppressWarnings("unchecked")
    public Void execute(Object... o) {
        if (o != null && o.length > 0) {
            progress((Integer) o[0]);
        }
        return null;
    }
}
