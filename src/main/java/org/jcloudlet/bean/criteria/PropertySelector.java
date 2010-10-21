/**
 * Module  : PropertySelector.java
 * Abstract:
 *
 * Created Oct 7, 2010
 */
package org.jcloudlet.bean.criteria;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

import org.jcloudlet.bean.Property;

/**
 * @author Sergiy Yevtushenko
 * 
 * @param <T>
 */
public interface PropertySelector extends Selector<Property> {

    PropertySelector with(Class<? extends Annotation>... annotations);

    Map<String, Property> asMap();

    Map<String, Property> asAliasMap();

    PropertySelector declared();

    PropertySelector ignore(Class<? extends Annotation>... ignore);

    PropertySelector ofType(Class<?> type);

    Collection<Property> select();

    PropertySelector withGetter();

    PropertySelector withSetter();

    PropertySelector forBean();
}