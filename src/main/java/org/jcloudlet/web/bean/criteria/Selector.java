/**
 * Module  : Selector.java
 * Abstract:
 *
 * Created Oct 7, 2010
 */
package org.jcloudlet.web.bean.criteria;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

/**
 * @author Sergiy Yevtushenko
 * 
 * @param <T>
 */
public interface Selector<T> {

    Selector<T> with(Class<? extends Annotation>... annotations);

    Map<String, T> asMap();

    Selector<T> declared();

    Collection<T> select();
}