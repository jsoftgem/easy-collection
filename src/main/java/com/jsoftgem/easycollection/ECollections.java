package com.jsoftgem.easycollection;

import com.jsoftgem.easycollection.util.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by m27 on 26-02-2016.
 */
public class ECollections {

    private ECollections() {
    }

    public static <T> T getItem(List<T> data, Class<T> type, CollectionCriteria<T> collectionCriteria) {
        ECollectionProcessor<T> processor = new ECollectionProcessor<T>(type);
        processor.setCollectionCriteria(collectionCriteria);
        return processor.getResult(data);
    }

    public static <T> List<T> getItems(List<T> data, Class<T> type, CollectionCriteria<T> collectionCriteria) {
        ECollectionProcessor<T> processor = new ECollectionProcessor<T>(type);
        processor.setCollectionCriteria(collectionCriteria);
        return processor.getResults(data);
    }

    public static <T> void modifyList(List<T> data, Class<T> type, CollectionCriteria<T> collectionCriteria, CollectionModifier<T> collectionModifier) {
        ECollectionProcessor<T> processor = new ECollectionProcessor<T>(type);
        processor.setCollectionCriteria(collectionCriteria);
        processor.setCollectionModifier(collectionModifier);
        processor.modifyList(data);
    }

    public static <T> void modifyList(List<T> data, Class<T> type, CollectionCriteria<T> collectionCriteria, CollectionModifier<T> collectionModifier, CollectionProgress collectionProgress) {
        ECollectionProcessor<T> processor = new ECollectionProcessor<T>(type);
        processor.setCollectionCriteria(collectionCriteria);
        processor.setCollectionModifier(collectionModifier);
        processor.setCollectionProgress(collectionProgress);
        processor.modifyList(data);
    }

    public static <T, C> List<C> copy(List<T> data, Class<T> type, CollectionCopy<T, C> collectionCopy) {
        ECollectionProcessor<T> processor = new ECollectionProcessor<T>(type);
        return processor.copy(data, collectionCopy);
    }

    public static <T, C> List<C> copy(List<T> data, Class<T> type, CollectionCopy<T, C> collectionCopy, CollectionCriteria<T> collectionCriteria) {
        ECollectionProcessor<T> processor = new ECollectionProcessor<T>(type);
        processor.setCollectionCriteria(collectionCriteria);
        return processor.copy(data, collectionCopy);
    }

    public static <T, C> List<C> copy(List<T> data, Class<T> type, CollectionCopy<T, C> collectionCopy, CollectionCriteria<T> collectionCriteria, CollectionProgress collectionProgress) {
        ECollectionProcessor<T> processor = new ECollectionProcessor<T>(type);
        processor.setCollectionCriteria(collectionCriteria);
        processor.setCollectionProgress(collectionProgress);
        return processor.copy(data, collectionCopy);
    }

    public static <T> void delete(List<T> data, Class<T> type, CollectionCriteria<T> collectionCriteria) {
        ECollectionProcessor<T> processor = new ECollectionProcessor<T>(type);
        processor.setCollectionCriteria(collectionCriteria);
        processor.delete(data);
    }

}
