/**
 * Module  : Alias.java
 * Abstract:
 *
 * Created Aug 9, 2010
 */
package org.jcloudlet.bean.annotation;

import java.lang.annotation.*;

/**
 * This annotation specifies property alias and is intended to simplify adjusting of property names
 * as it might be visible outside the application without need to rename property itself, since 
 * renaming may affect many other parts of the application.
 * 
 * @author Sergiy Yevtushenko
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {
    String value();
}
