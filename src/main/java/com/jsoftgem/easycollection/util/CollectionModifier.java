package com.jsoftgem.easycollection.util;

/**
 * Created by m27 on 26-02-2016.
 */
public abstract class CollectionModifier<T> implements ECollectionUsecase<T> {

    public abstract T modify(T t);

    public void setPreviousItem(T previousItem) {

    }

    public void setNextItem(T nextItem) {

    }

    public void setIndex(int index) {

    }

    @SuppressWarnings("unchecked")
    public T execute(Object... o) {
        if (o != null && o.length > 0) {
            return modify((T) o[0]);
        }
        return null;
    }
}
