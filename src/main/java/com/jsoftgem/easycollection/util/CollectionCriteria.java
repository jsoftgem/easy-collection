package com.jsoftgem.easycollection.util;

/**
 * Created by m27 on 26-02-2016.
 */
public abstract class CollectionCriteria<T> implements ECollectionUsecase<Boolean> {

    protected abstract boolean where(T t);

    @SuppressWarnings("unchecked")
    public Boolean execute(Object... o) {
        if (o != null && o.length > 0) {
            return where((T) o[0]);
        }
        return false;
    }
}
