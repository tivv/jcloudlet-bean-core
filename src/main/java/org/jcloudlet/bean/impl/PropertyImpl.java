/**
 * Module  : Property.java
 * Abstract:
 *
 * Created Aug 21, 2010
 */
package org.jcloudlet.bean.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.*;

import org.jcloudlet.bean.Property;
import org.jcloudlet.bean.annotation.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sergiy Yevtushenko
 */
class PropertyImpl implements Property {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyImpl.class);

    private final Field field;
    private final Method getter;
    private final boolean inherited;
    private final Method setter;
    private final List<Class<?>> typeParameters;
    private final String alias;

    static final Set<Class<?>> NUMBERS = new HashSet<Class<?>>();
    static final Set<Class<?>> DATES = new HashSet<Class<?>>();
    static final Set<Class<?>> TIMES = new HashSet<Class<?>>();
    static final Set<Class<?>> OTHER_SIMPLE = new HashSet<Class<?>>();
    static final Set<Class<?>> SIMPLE_TYPES = new HashSet<Class<?>>();

    static {
        OTHER_SIMPLE.add(boolean.class);
        OTHER_SIMPLE.add(String.class);
        OTHER_SIMPLE.add(char.class);
        OTHER_SIMPLE.add(Character.class);

        NUMBERS.add(byte.class);
        NUMBERS.add(short.class);
        NUMBERS.add(int.class);
        NUMBERS.add(long.class);
        NUMBERS.add(float.class);
        NUMBERS.add(double.class);

        NUMBERS.add(Byte.class);
        NUMBERS.add(Short.class);
        NUMBERS.add(Integer.class);
        NUMBERS.add(Long.class);
        NUMBERS.add(Float.class);
        NUMBERS.add(Double.class);
        NUMBERS.add(BigDecimal.class);
        NUMBERS.add(BigInteger.class);

        DATES.add(Date.class);
        DATES.add(java.sql.Date.class);

        TIMES.add(java.sql.Time.class);
        TIMES.add(java.sql.Timestamp.class);

        SIMPLE_TYPES.addAll(NUMBERS);
        SIMPLE_TYPES.addAll(DATES);
        SIMPLE_TYPES.addAll(TIMES);
        SIMPLE_TYPES.addAll(OTHER_SIMPLE);
    }

    PropertyImpl(Field field, Method getter, Method setter, boolean inherited, List<Class<?>> typeParameters) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
        this.inherited = inherited;
        this.typeParameters = typeParameters;
        this.alias = aliasImpl();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T annotation(Class<? extends Annotation> type) {
        if (getter != null && getter.isAnnotationPresent(type)) {
            return (T) getter.getAnnotation(type);
        }

        if (setter != null && setter.isAnnotationPresent(type)) {
            return (T) setter.getAnnotation(type);
        }

        return (T) field.getAnnotation(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T anyAnnotation(Class<? extends Annotation> type) {
        Annotation t = annotation(type);

        return (T) (t == null ? typeAnnotation(type) : t);
    }

    @Override
    public Field field() {
        return field;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object base) {
        return (T) (getter != null ? getViaGetter(base) : getViaField(base));
    }

    @Override
    public Method getter() {
        return getter;
    }

    @Override
    public boolean has(Class<? extends Annotation> annotation) {
        return annotation(annotation) != null;
    }

    @Override
    public boolean inherited() {
        return inherited;
    }

    @Override
    public Class<?> itemType() {
        Class<?> itemType = typeParameter();

        if (itemType == null) {
            if (Collection.class.isAssignableFrom(type())) {
                itemType = Object.class;
            } else {
                itemType = type().getComponentType();
            }
        }

        return itemType;
    }

    @Override
    public boolean multivalue() {
        return Collection.class.isAssignableFrom(type()) || Array.class.isAssignableFrom(type());
    }

    @Override
    public String name() {
        return field.getName();
    }

    @Override
    public void set(Object base, Object input) {
        if (setter != null) {
            setViaSetter(base, input);
        } else {
            setViaField(base, input);
        }
    }

    @Override
    public Method setter() {
        return setter;
    }

    @Override
    public String toString() {
        return "property " + name() + "(" + alias() + ") " + type().getName();
    }

    @Override
    public Class<?> type() {
        return field.getType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T typeAnnotation(Class<? extends Annotation> type) {
        return (T) type().getAnnotation(type);
    }

    @Override
    public Class<?> typeParameter() {
        if (typeParameters.isEmpty()) {
            return null;
        }

        return typeParameters.get(0);
    }

    @Override
    public List<Class<?>> typeParameters() {
        return typeParameters;
    }

    @Override
    public String alias() {
        return alias;
    }

    @Override
    public boolean subclassOf(Class<?> clazz) {
        return clazz.isAssignableFrom(type());
    }

    @Override
    public boolean isCollection() {
        return Collection.class.isAssignableFrom(type());
    }

    @Override
    public boolean isEnum() {
        return type().isEnum();
    }

    @Override
    public boolean isArray() {
        return type().isArray();
    }

    @Override
    public boolean isBoolean() {
        return type().equals(boolean.class) || type().equals(Boolean.class);
    }

    @Override
    public boolean isSimple() {
        return SIMPLE_TYPES.contains(type());
    }

    @Override
    public boolean isNumber() {
        return NUMBERS.contains(type());
    }

    @Override
    public boolean isDate() {
        return DATES.contains(type());
    }

    @Override
    public boolean isTime() {
        return TIMES.contains(type());
    }

    private Object getViaField(Object base) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(base);
        } catch (Exception e) {
            reportError("get", e);
        }
        return null;
    }

    private Object getViaGetter(Object base) {
        try {
            return getter.invoke(base);
        } catch (Exception e) {
            reportError("get", e);
        }
        return null;
    }

    private void reportError(String string, Exception e) {
        LOG.error(MessageFormat.format("Unable to {0} property {1} for {2}", string, name(), type()), e);
    }

    private void setViaField(Object base, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(base, value);
        } catch (Exception e) {
            reportError("set", e);
        }
    }

    private void setViaSetter(Object base, Object value) {
        try {
            setter.invoke(base, value);
        } catch (Exception e) {
            reportError("set", e);
        }
    }

    private String aliasImpl() {
        Alias ann = annotation(Alias.class);

        if (ann != null && ann.value() != null && !ann.value().isEmpty()) {
            return ann.value();
        }

        return name();
    }
}
