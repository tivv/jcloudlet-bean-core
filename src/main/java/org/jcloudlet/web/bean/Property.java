/**
 * Module  : Property1.java
 * Abstract:
 *
 * Created Sep 8, 2010
 */
package org.jcloudlet.web.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Sergiy Yevtushenko
 */
public interface Property {

    <T extends Annotation> T annotation(Class<? extends Annotation> type);

    <T extends Annotation> T anyAnnotation(Class<? extends Annotation> type);

    Field field();

    <T> T get(Object base);

    Method getter();

    boolean has(Class<? extends Annotation> annotation);

    boolean inherited();

    Class<?> itemType();

    boolean multivalue();

    String name();

    void set(Object base, Object input);

    Method setter();

    Class<?> type();

    <T extends Annotation> T typeAnnotation(Class<? extends Annotation> type);

    Class<?> typeParameter();

    List<Class<?>> typeParameters();

    String alias();

    boolean subclassOf(Class<?> clazz);

    boolean isCollection();

    boolean isEnum();

    boolean isArray();

    boolean isBoolean();

    boolean isSimple();
}