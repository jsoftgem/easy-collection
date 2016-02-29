package com.jsoftgem.easycollection;

import com.jsoftgem.easycollection.util.CollectionCopy;
import com.jsoftgem.easycollection.util.CollectionCriteria;

import static org.junit.Assert.*;

import com.jsoftgem.easycollection.util.CollectionModifier;
import com.jsoftgem.easycollection.util.CollectionProgress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

/**
 * Created by m27 on 26-02-2016.
 */
@RunWith(JUnit4.class)
public class ECollectionsTest {


    private List<String> mainList;

    @Before
    public void setUp() {
        mainList = new ECollectionTestData().getListData();
    }


    @Test
    public void testECollection() {
        String item = ECollections.getItem(mainList, String.class, new CollectionCriteria<String>() {
            @Override
            protected boolean where(String s) {
                return s.equals("Maria Mitchell");
            }
        });

        assertNotNull(item);
        assertEquals("Maria Mitchell", item);
    }

    @Test
    public void testECollectionResults() {
        List<String> items = ECollections.getItems(mainList, String.class, new CollectionCriteria<String>() {
            @Override
            protected boolean where(String s) {
                return s.equals("Maria Mitchell");
            }
        });
        assertNotNull(items);
        assertEquals(340, items.size());
    }

    @Test
    public void testECollectionPerformance() {
        String item = ECollections.getItem(mainList, String.class, new CollectionCriteria<String>() {
            @Override
            protected boolean where(String s) {
                return s.equals("Wolfgang Ernst Pauli");
            }
        });

        assertNotNull(item);
        assertEquals("Wolfgang Ernst Pauli", item);
    }

    @Test
    public void testECollectionModifier() {
        ECollections.modifyList(mainList, String.class,
                new CollectionCriteria<String>() {
                    @Override
                    protected boolean where(String s) {
                        return s.equals("Wolfgang Ernst Pauli");
                    }
                }, new CollectionModifier<String>() {
                    @Override
                    public String modify(String s) {
                        return "Boom";
                    }
                });

        assertTrue(mainList.contains("Boom"));
    }

    @Test
    public void testECollectionModifierWithProgress() {
        ECollections.modifyList(mainList, String.class,
                new CollectionCriteria<String>() {
                    @Override
                    protected boolean where(String s) {
                        return s.equals("Wolfgang Ernst Pauli");
                    }
                }, new CollectionModifier<String>() {
                    @Override
                    public String modify(String s) {
                        return "Boom";
                    }
                }, new CollectionProgress() {
                    private int total;
                    private double percent;

                    @Override
                    public void setPercentage(double percent) {
                        this.percent = percent;
                    }

                    @Override
                    public void setTotal(int total) {
                        this.total = total;
                    }

                    @Override
                    public void progress(int processed) {

                    }
                });

        assertTrue(mainList.contains("Boom"));
    }


    @Test
    public void testECollectionCopy() {
        List<String> copy = ECollections.copy(mainList, String.class, new CollectionCopy<String, String>() {
            @Override
            public String copy(String s) {
                return "copied - " + s;
            }
        });

        assertEquals(mainList.size(), copy.size());
    }

    @Test
    public void testECollectionCopyWithCriteria() {
        List<String> copy = ECollections.copy(mainList, String.class, new CollectionCopy<String, String>() {
            @Override
            public String copy(String s) {
                return "copied - " + s;
            }
        }, new CollectionCriteria<String>() {
            @Override
            protected boolean where(String s) {
                return s.equals("Wolfgang Ernst Pauli");
            }
        });
        assertEquals(317, copy.size());
    }

    @Test
    public void testECollectionCopyWithCriteriaAndProgress() {
        List<String> copy = ECollections.copy(mainList, String.class, new CollectionCopy<String, String>() {
            @Override
            public String copy(String s) {
                return "copied - " + s;
            }
        }, new CollectionCriteria<String>() {
            @Override
            protected boolean where(String s) {
                return s.equals("Wolfgang Ernst Pauli");
            }
        }, new CollectionProgress() {
            @Override
            public void progress(int processed) {

            }
        });
        assertEquals(317, copy.size());
    }

    @Test
    public void testECollectionDelete() {
        ECollections.delete(mainList, String.class, new CollectionCriteria<String>() {
            @Override
            protected boolean where(String s) {
                return s.equals("Wolfgang Ernst Pauli");
            }
        });
        assertNotNull(mainList);
    }
}
