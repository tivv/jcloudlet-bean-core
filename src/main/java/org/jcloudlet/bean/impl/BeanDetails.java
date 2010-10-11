/**
 * Module  : Bean.java
 * Abstract:
 *
 * Created Aug 20, 2010
 */
package org.jcloudlet.bean.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.jcloudlet.bean.Bean;
import org.jcloudlet.bean.Property;

/**
 * @author Sergiy Yevtushenko
 */
public final class BeanDetails implements Bean {
    private static Map<Class<?>, BeanDetails> cache = new ConcurrentHashMap<Class<?>, BeanDetails>();
    
    private final Class<?> clazz; 
    private final Set<Annotation> annotations         = new LinkedHashSet<Annotation>(); 
    private final Set<Annotation> declared            = new LinkedHashSet<Annotation>(); 
    private final Map<String, Field> fields           = new LinkedHashMap<String, Field>(); 
    private final Map<String, Field> fieldsDeclared   = new LinkedHashMap<String, Field>(); 
    private final Map<String, Method> methods         = new LinkedHashMap<String, Method>();
    private final Map<String, Method> methodsDeclared = new LinkedHashMap<String, Method>(); 
    private final Map<String, Property> properties    = new LinkedHashMap<String, Property>();
    
    private BeanDetails(Class<?> clazz) {
        this.clazz = clazz;
    }
    
    static Bean forClass(Class<?> clazz) {
        assert clazz != null;
        
        Bean details = cache.get(clazz);
        
        if (details != null) {
            return details;
        }
        
        return new BeanDetails(clazz).collect();
    }

    private static void addAll(Set<Annotation> set, Annotation[] arr) {
        for (Annotation ann : arr) {
            set.add(ann);
        }
    }

    private static void putAll(Map<String, Field> map, Field[] arr) {
        for (Field field : arr) {
            if (field.getName().indexOf('$') >= 0) {
                continue;
            }
            map.put(field.getName(), field);
        }
    }

    private static void putAll(Map<String, Method> map, Method[] arr) {
        for (Method method : arr) {
            if (method.getName().indexOf('$') >= 0) {
                continue;
            }
            map.put(method.getName(), method);
        }
    }

    @Override
    public Set<Annotation> annotations() {
        return annotations;
    }
    
    @Override
    public Set<Annotation> declaredAnnotations() {
        return declared;
    }

    @Override
    public Method declaredMethod(String name) {
        return methodsDeclared.get(name);
    }
    
    @Override
    public Collection<Method> declaredMethods() {
        return methodsDeclared.values();
    }

    @Override
    public Method method(String name) {
        return methods.get(name);
    }
    
    @Override
    public Collection<Method> methods() {
        return methods.values();
    }
    
    @Override
    public Collection<Property> properties() {
        return properties.values();
    }

    @Override
    public Property property(String name) {
        return properties.get(name);
    }

    private Bean collect() {
        putAll(fieldsDeclared, clazz.getDeclaredFields());
        collectFieldsRecursively(clazz);
        putAll(methodsDeclared, clazz.getDeclaredMethods());
        putAll(methods, clazz.getMethods());
        addAll(declared, clazz.getDeclaredAnnotations());
        addAll(annotations, clazz.getAnnotations());
        collectProperties(fields.values());
        
        cache.put(clazz, this);
        return this;
    }

    private void collectFieldsRecursively(Class<?> clazz) {
        if (clazz == Object.class || clazz.isInterface()) {
            return;
        }
        
        collectFieldsRecursively(clazz.getSuperclass());
        putAll(fields, clazz.getDeclaredFields());
    }

    private void collectProperties(Collection<Field> values) {
        for (Field field : values) {
            Property prop = processField(field);
            if (prop != null) {
                properties.put(prop.name(), prop);
            }
        }
    }

    private boolean filtered(Field field) {
        int modifiers = field.getModifiers();
        
        return field.getName().indexOf('$') >= 0 || 
                Modifier.isNative(modifiers) || 
                Modifier.isStatic(modifiers) ||
                Modifier.isFinal(modifiers);
    }

    private Method getter(Field field, String getterName) {
        Method getter = methods.get(getterName);

        if (getter != null) {
            if (!getter.getReturnType().isAssignableFrom(field.getType())) {
                getter = null;
            } else if (getter.getParameterTypes().length != 0) {
                getter = null;
            }
        }
        return getter;
    }

    private boolean isBoolean(Class<?> type) {
        return type == boolean.class;
    }

    private List<Class<?>> listTypes(ParameterizedType generic) {
        List<Class<?>> res = new ArrayList<Class<?>>();

        for (Type type : generic.getActualTypeArguments()) {
            if (type instanceof Class) {
                res.add((Class<?>) type);
            }
        }
        
        return res;
    }

    private String makeName(String prefix, String name) {
        int index = prefix.length();
        StringBuilder buf = new StringBuilder(prefix).append(name);
        buf.setCharAt(index, Character.toUpperCase(buf.charAt(index)));
        return buf.toString();
    }

    private Property processField(Field field) {
        if (filtered(field)) {
            return null;
        }
        
        String name = field.getName();
        Method getter = getter(field, makeName(isBoolean(field.getType()) ? "is" : "get", name));
        Method setter = setter(field, makeName("set", name));
        boolean inherited = !fieldsDeclared.containsKey(name);
        List<Class<?>> typeParameters = (field.getGenericType() instanceof ParameterizedType) ? 
                                                    listTypes((ParameterizedType) field.getGenericType()) :
                                                    Collections.<Class<?>>emptyList();
                                                    
        return new PropertyImpl(field, getter, setter, inherited, typeParameters);
    }

    private Method setter(Field field, String setterName) {
        Method setter = methods.get(setterName);
        
        if (setter != null) {
            Class<?>[] types = setter.getParameterTypes();
            if (types.length != 1 || !field.getType().isAssignableFrom(types[0])) {
                setter = null;
            }
        }
        return setter;
    }
}
