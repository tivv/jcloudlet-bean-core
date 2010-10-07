/**
 * Module  : MethodSelector.java
 * Abstract:
 *
 * Created Oct 7, 2010
 */
package org.jcloudlet.bean.criteria;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;


/**
 * @author Sergiy Yevtushenko
 */
public interface MethodSelector extends Selector<Method> {

    MethodSelector with(Class<? extends Annotation>... annotations);

    Map<String, Method> asMap();

    MethodSelector declared();

    MethodSelector parametersCount(int n);

    Collection<Method> select();

    MethodSelector with(Visibility... vis);
}