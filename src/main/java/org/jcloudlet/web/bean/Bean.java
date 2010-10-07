/**
 * Module  : Bean.java
 * Abstract:
 *
 * Created Oct 7, 2010
 */
package org.jcloudlet.web.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;


/**
 * @author Sergiy Yevtushenko
 */
public interface Bean {

    Set<Annotation> annotations();

    Set<Annotation> declaredAnnotations();

    Method declaredMethod(String name);

    Collection<Method> declaredMethods();

    Method method(String name);

    Collection<Method> methods();

    Collection<Property> properties();

    Property property(String name);

}