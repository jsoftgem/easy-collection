package com.jsoftgem.easycollection.util;

import com.sun.deploy.util.StringUtils;

import java.beans.Statement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by m27 on 26-02-2016.
 */
public class DuplicateJavaBean<T> {
    private T duplicate;
    private T source;
    private Class<T> type;

    public DuplicateJavaBean(T source, Class<T> type) {
        this.source = source;
        this.type = type;
    }

    public T execute() {
        createDuplicateInstance();
        initCopy();
        return duplicate;
    }

    private void createDuplicateInstance() {
        try {
            duplicate = type.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void initCopy() {
        List<Field> sourceFields = getAllFields(type);
        for (Field field : sourceFields) {
            copyFieldValue(field);
        }
    }

    private void copyFieldValue(Field field) {
        try {
            String getterName = "get" + capitalized(field.getName());
            String setterName = "set" + capitalized(field.getName());
            Object value = type.getMethod(getterName).invoke(source, null);
            new Statement(duplicate, setterName, new Object[]{value}).execute();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String capitalized(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        setAllFields(fields, type);
        return fields;
    }

    private void setAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            setAllFields(fields, type.getSuperclass());
        }

    }


}
