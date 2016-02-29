package com.jsoftgem.easycollection;

import com.jsoftgem.easycollection.util.DuplicateJavaBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * Created by m27 on 26-02-2016.
 */
@RunWith(JUnit4.class)
public class DuplicateJavaBeanTest {
    private DuplicateObjectTestClass sourceObjectTestClass;

    @Before
    public void setUp() {
        sourceObjectTestClass = createTestClass();
    }

    private DuplicateObjectTestClass createTestClass() {
        DuplicateObjectTestClass duplicateObjectTestClass = new DuplicateObjectTestClass();
        duplicateObjectTestClass.setCount(20);
        duplicateObjectTestClass.setName("Sample");
        return duplicateObjectTestClass;
    }

    @Test
    public void testDuplicateObject() {
        DuplicateJavaBean<DuplicateObjectTestClass> duplicateObject = new DuplicateJavaBean<DuplicateObjectTestClass>(sourceObjectTestClass, DuplicateObjectTestClass.class);
        DuplicateObjectTestClass duplicate = duplicateObject.execute();
        duplicate.setName("Hello");
        assertNotNull(duplicate);
        assertNotEquals(duplicate.getName(), sourceObjectTestClass.getName());
    }
}
