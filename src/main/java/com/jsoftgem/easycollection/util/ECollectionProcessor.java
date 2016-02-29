package com.jsoftgem.easycollection.util;

import javax.lang.model.type.PrimitiveType;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by rickzx98 on 28/02/2016.
 */
public class ECollectionProcessor<T> {

    private Class<T> type;
    private int processed;
    private int total;
    private Thread currentThread;
    private CollectionCriteria<T> collectionCriteria;
    private CollectionModifier<T> collectionModifier;
    private CollectionProgress collectionProgress;

    public void setCollectionCriteria(CollectionCriteria<T> collectionCriteria) {
        this.collectionCriteria = collectionCriteria;
    }

    public void setCollectionModifier(CollectionModifier<T> collectionModifier) {
        this.collectionModifier = collectionModifier;
    }

    public void setCollectionProgress(CollectionProgress collectionProgress) {
        this.collectionProgress = collectionProgress;
    }

    public ECollectionProcessor(Class<T> type) {
        this.type = type;
        this.processed = Integer.valueOf(0);
        this.currentThread = Thread.currentThread();

    }

    private T executeSingle(List<T> data) {
        T item = null;
        Iterator<T> dataIterator = getIterator(data);
        if (dataIterator != null) {
            item = findItem(dataIterator);
        }
        return item;
    }

    private List<T> executeMultiple(List<T> data) {
        List<T> items = null;
        Iterator<T> dataIterator = getIterator(data);
        if (dataIterator != null) {
            items = findItems(data);
        }
        return items;
    }

    private Iterator<T> getIterator(List<T> data) {
        Iterator<T> dataIterator = null;
        if (data != null) {
            dataIterator = data.iterator();
        }
        return dataIterator;
    }

    private T findItem(Iterator<T> dataIterator) {
        boolean found = Boolean.FALSE;
        T item = null;
        while (!found && dataIterator.hasNext()) {
            item = dataIterator.next();
            T exposed;
            if (item instanceof PrimitiveType || item instanceof String) {
                exposed = item;
            } else {
                exposed = new DuplicateJavaBean<T>(item, type).execute();
            }
            found = collectionCriteria != null && collectionCriteria.execute(exposed);
        }
        return item;
    }

    private List<T> findItems(List<T> data) {
        total = data.size();
        if (collectionProgress != null) collectionProgress.setTotal(total);
        List<T> foundItems = new ArrayList<T>();
        processed = 0;
        for (int count = 0; count < total; count++) {
            searchThread(count, data, foundItems).start();
        }
        waitForProcess();
        return foundItems;
    }

    private void setItems(List<T> data) {
        total = data.size();
        if (collectionProgress != null) collectionProgress.setTotal(total);
        processed = 0;
        for (int count = 0; count < total; count++) {
            searchThread(count, data).start();
        }
        waitForProcess();
    }

    private <C> List<C> copyItems(List<T> source, CollectionCopy<T, C> collectionCopy) {
        total = source.size();
        Object[] destination = new Object[total];
        List<C> destList = new ArrayList<C>();
        if (collectionProgress != null) collectionProgress.setTotal(total);
        processed = 0;
        for (int count = 0; count < total; count++) {
            if (collectionCriteria != null) {
                copyThread(count, source, destList, collectionCopy).start();
            } else {
                copyThread(count, source, destination, collectionCopy).start();
            }
        }
        waitForProcess();
        if (collectionCriteria == null)
            return (List<C>) Arrays.asList(destination);
        else
            return destList;
    }

    private void deleteItems(List<T> source) {
        total = source.size();
        List<T> removeIndexList = new ArrayList<T>();
        if (collectionProgress != null) collectionProgress.setTotal(total);
        processed = 0;
        for (int count = 0; count < total; count++) {
            deleteThread(count, source, removeIndexList).start();
        }
        waitForProcess();
        source.removeAll(removeIndexList);
    }

    private void waitForProcess() {
        while (processed < total && currentThread.isAlive()) {
        }
    }


    private double getPercentage(int processed, int dataSize) {
        return (processed * 100) / dataSize;
    }

    private <C> Thread copyThread(final int index, final List<T> data, final List<C> destination, final CollectionCopy<T, C> collectionCopy) {
        return new Thread(new Runnable() {
            public void run() {
                try {
                    T item = data.get(index);
                    T exposed;
                    try {
                        if (item instanceof PrimitiveType || item instanceof String) {
                            exposed = item;
                        } else {
                            exposed = new DuplicateJavaBean<T>(item, type).execute();
                        }
                        if (collectionCopy != null) {
                            synchronized (destination) {
                                if (collectionCriteria != null && collectionCriteria.execute(exposed)) {
                                    destination.add(collectionCopy.execute(exposed));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        item = null;
                        exposed = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    incrementProcess();
                }

            }
        });
    }

    private <C> Thread copyThread(final int index, final List<T> data, final Object[] destination, final CollectionCopy<T, C> collectionCopy) {
        return new Thread(new Runnable() {
            public void run() {
                try {
                    T item = data.get(index);
                    T exposed;
                    try {
                        if (item instanceof PrimitiveType || item instanceof String) {
                            exposed = item;
                        } else {
                            exposed = new DuplicateJavaBean<T>(item, type).execute();
                        }
                        if (collectionCopy != null) {
                            synchronized (destination) {
                                destination[index] = collectionCopy.execute(exposed);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        item = null;
                        exposed = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    incrementProcess();
                }

            }
        });
    }

    private Thread searchThread(final int index, final List<T> data) {
        return new Thread(new Runnable() {
            public void run() {
                try {
                    T item = data.get(index);
                    T exposed;
                    try {
                        if (item instanceof PrimitiveType || item instanceof String) {
                            exposed = item;
                        } else {
                            exposed = new DuplicateJavaBean<T>(item, type).execute();
                        }
                        if (collectionCriteria != null && collectionCriteria.execute(exposed)) {
                            if (collectionModifier != null) {
                                data.set(index, collectionModifier.execute(exposed));
                                collectionModifier.setIndex(index);
                                if (index > 0) {
                                    collectionModifier.setPreviousItem(data.get((index - 1)));
                                }
                                if (index < data.size() - 1) {
                                    collectionModifier.setNextItem(data.get((index + 1)));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        item = null;
                        exposed = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    incrementProcess();
                }

            }
        });
    }

    private Thread searchThread(final int index, final List<T> data, final List<T> foundItems) {
        return new Thread(new Runnable() {
            public void run() {
                try {
                    T item = data.get(index);
                    T exposed;
                    try {
                        if (item instanceof PrimitiveType || item instanceof String) {
                            exposed = item;
                        } else {
                            exposed = new DuplicateJavaBean<T>(item, type).execute();
                        }
                        if (collectionCriteria != null && collectionCriteria.execute(exposed)) {
                            synchronized (foundItems) {
                                foundItems.add(exposed);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        item = null;
                        exposed = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    incrementProcess();
                }
            }
        });
    }

    private Thread deleteThread(final int index, final List<T> data, final List<T> removeIndexList) {
        return new Thread(new Runnable() {
            public void run() {
                try {
                    T item = data.get(index);
                    T exposed;
                    try {
                        if (item instanceof PrimitiveType || item instanceof String) {
                            exposed = item;
                        } else {
                            exposed = new DuplicateJavaBean<T>(item, type).execute();
                        }
                        if (collectionCriteria != null && collectionCriteria.execute(exposed)) {
                            removeIndexList.add(exposed);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        item = null;
                        exposed = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    incrementProcess();
                }
            }
        });
    }

    public T getResult(List<T> data) {
        return executeSingle(data);
    }

    public List<T> getResults(List<T> data) {
        return executeMultiple(data);
    }

    public void modifyList(List<T> data) {
        setItems(data);
    }

    public <C> List<C> copy(List<T> data, CollectionCopy<T, C> collectionCopy) {
        return copyItems(data, collectionCopy);
    }

    public void delete(List<T> data) {
        deleteItems(data);
    }

    private synchronized void incrementProcess() {
        processed++;
        process();
    }

    private void process() {
        if (collectionProgress != null) {
            collectionProgress.setPercentage(getPercentage(processed, total));
            collectionProgress.execute(processed);
        }
    }
}
