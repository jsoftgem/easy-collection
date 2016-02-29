package com.jsoftgem.easycollection.util;

import java.util.Collection;

/**
 * Created by rickzx98 on 28/02/2016.
 */
public abstract class CollectionCopy<T, C> implements ECollectionUsecase<C> {
    public abstract C copy(T t);
    @SuppressWarnings("unchecked")
    public C execute(Object... o) {
        if (o != null && o.length > 0) {
            return copy((T) o[0]);
        }
        return null;
    }
}
