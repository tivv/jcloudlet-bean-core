/**
 * Module  : AbstractSelector.java
 * Abstract:
 *
 * Created Aug 22, 2010
 */
package org.jcloudlet.bean.impl;

import java.lang.annotation.Annotation;
import java.util.*;

import org.jcloudlet.bean.Bean;
import org.jcloudlet.bean.criteria.Selector;

/**
 * @author Sergiy Yevtushenko
 */
public abstract class AbstractSelector<T> implements Selector<T> {
    private Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
    private boolean declaredOnly = false;
    private Bean details;

    AbstractSelector(Class<?> clazz) {
        this.details = BeanDetails.forClass(clazz);
    }

    @Override
    public Selector<T> with(Class<? extends Annotation>... annotations) {
        this.annotations.addAll(Arrays.asList(annotations));
        return this;
    }

    @Override
    public abstract Map<String, T> asMap();
    
    @Override
    public Selector<T> declared() {
        declaredOnly = true;
        return this;
    }

    @Override
    public abstract Collection<T> select();
    
    protected Set<Class<? extends Annotation>> annotations() {
        return annotations;
    }
    
    protected Bean details() {
        return details;
    }
    
    protected boolean selectDeclared() {
        return declaredOnly;
    }
}