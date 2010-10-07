/**
 * Module  : MethodSelectorImpl.java
 * Abstract:
 *
 * Created Aug 22, 2010
 */
package org.jcloudlet.bean.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import org.jcloudlet.bean.criteria.MethodSelector;
import org.jcloudlet.bean.criteria.Visibility;

/**
 * @author Sergiy Yevtushenko
 */
public class MethodSelectorImpl extends AbstractSelector<Method> implements MethodSelector {
    private int parameters = -1;
    private Set<Visibility> visibility = new HashSet<Visibility>(); 
    
    public MethodSelectorImpl(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public MethodSelectorImpl with(Class<? extends Annotation>... annotations) {
        return (MethodSelectorImpl) super.with(annotations);
    }

    @Override
    public Map<String, Method> asMap() {
        return asMap(selectDeclared() ? details().declaredMethods() : details().methods());
    }

    @Override
    public MethodSelectorImpl declared() {
        return (MethodSelectorImpl) super.declared();
    }
    
    @Override
    public MethodSelector parametersCount(int n) {
        parameters = n;
        return this;
    }
    
    @Override
    public Collection<Method> select() {
        return select(selectDeclared() ? details().declaredMethods() : details().methods());
    }

    @Override
    public MethodSelector with(Visibility... vis) {
        visibility.addAll(Arrays.asList(vis));
        return this;
    }

    private Map<String, Method> asMap(Iterable<Method> source) {
        Map<String, Method> res = new LinkedHashMap<String, Method>();
        
        for (Method method : source) {
            if (matches(method)) {
                res.put(method.getName(), method);
            }
        }

        return res;
    }

    private boolean checkVisibility(Method method) {
        int mod = method.getModifiers();
        
        return matchesPublic(mod) || matchesProtected(mod) || matchesPrivate(mod) || visibility.contains(Visibility.DEFAULT);
    }

    private boolean checkVisibilityAndParameters(Method method) {
        if(!checkVisibility(method)) {
            return false;
        }
        
        return parameters < 0 || method.getParameterTypes().length == parameters;
    }

    private boolean matches(Method method) {
        if (!checkVisibilityAndParameters(method)) {
            return false;
        }
        
        return annotations().isEmpty() ? true : searchAnnotation(method);
    }

    private boolean matchesPrivate(int mod) {
        return Modifier.isPrivate(mod) && visibility.contains(Visibility.PRIVATE);
    }

    private boolean matchesProtected(int mod) {
        return Modifier.isProtected(mod) && visibility.contains(Visibility.PROTECTED);
    }

    private boolean matchesPublic(int mod) {
        return Modifier.isPublic(mod) && visibility.contains(Visibility.PUBLIC);
    }

    private boolean searchAnnotation(Method method) {
        for (Class<? extends Annotation> ann : annotations()) {
            if (method.isAnnotationPresent(ann)) {
                return true;
            }
        }
        return false;
    }

    private Collection<Method> select(Iterable<Method> source) {
        List<Method> res = new ArrayList<Method>();
        
        for (Method method : source) {
            if (matches(method)) {
                res.add(method);
            }
        }

        return res;
    }
}
