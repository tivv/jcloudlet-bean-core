/**
 * Module  : Criterias.java
 * Abstract:
 *
 * Created Aug 22, 2010
 */
package org.jcloudlet.bean.impl;

import java.lang.annotation.Annotation;
import java.util.*;

import org.jcloudlet.bean.Property;
import org.jcloudlet.bean.criteria.PropertySelector;

/**
 * @author Sergiy Yevtushenko
 */
public class PropertySelectorImpl extends AbstractSelector<Property> implements PropertySelector {
    private boolean checkGetter;
    private boolean checkSetter;
    private Class<? extends Annotation> ignore;
    private Class<?> type;

    public PropertySelectorImpl(Class<?> clazz) {
        super(clazz);
    }
    
    @Override
    public PropertySelectorImpl with(Class<? extends Annotation>... annotations) {
        return (PropertySelectorImpl) super.with(annotations);
    }
    
    @Override
    public Map<String, Property> asMap() {
        return buildMap(false);
    }

    @Override
    public Map<String, Property> asAliasMap() {
        return buildMap(true);
    }

    @Override
    public PropertySelectorImpl declared() {
        return (PropertySelectorImpl) super.declared();
    }
    
    @Override
    public PropertySelector ignore(Class<? extends Annotation> ignore) {
        this.ignore = ignore;
        return this;
    }
    
    @Override
    public PropertySelector ofType(Class<?> type) {
        this.type = type; 
        return this;
    }
    
    @Override
    public Collection<Property> select() {
        List<Property> res = new ArrayList<Property>();
        
        for (Property property : details().properties()) {
            if (matches(property)) {
                res.add(property);
            }
        }
        return res;
    }
    
    @Override
    public PropertySelector withGetter() {
        checkGetter = true;
        return this;
    }

    @Override
    public PropertySelector withSetter() {
        checkSetter = true;
        return this;
    }

    @Override
    public PropertySelector forBean() {
        return withGetter().withSetter();
    }
    
    private Map<String, Property> buildMap(boolean asAlias) {
        Map<String, Property> map = new LinkedHashMap<String, Property>();

        for (Property property : details().properties()) {
            if (matches(property)) {
                map.put(asAlias ? property.alias() : property.name(), property);
            }
        }
        
        return map;
    }
    
    private boolean checkAccessors(Property property) {
        if (checkGetter && property.getter() == null) {
            return false;
        }
        
        if (checkSetter && property.setter() == null) {
            return false;
        }
        
        return checkTypesAndAnnotations(property);
    }

    private boolean checkAnnotations(Property property) {
        return !annotations().isEmpty() ? searchAnnotation(property) : true;
    }

    private boolean checkDeclared(Property property) {
        if (selectDeclared() && property.inherited()) {
            return false;
        }

        return checkAccessors(property);
    }

    private boolean checkTypesAndAnnotations(Property property) {
        if (ignore != null && property.has(ignore)) {
            return false;
        }
        
        if (type != null && !type.isAssignableFrom(property.type())) {
            return false;
        }
        
        return true;
    }

    private boolean matches(Property property) {
        if (!checkDeclared(property)) {
            return false;
        }

        return checkAnnotations(property);
    }

    private boolean searchAnnotation(Property property) {
        for (Class<? extends Annotation> ann : annotations()) {
            if (property.has(ann)) {
                return true;
            }
        }
        return false;
    }
}
