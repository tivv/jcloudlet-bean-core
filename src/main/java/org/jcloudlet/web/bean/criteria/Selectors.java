/**
 * Module  : Selectors.java
 * Abstract:
 *
 * Created Aug 22, 2010
 */
package org.jcloudlet.web.bean.criteria;

import org.jcloudlet.web.bean.impl.*;

/**
 * @author Sergiy Yevtushenko
 */
public final class Selectors {
    private Selectors() {
    }
    
    public static MethodSelector methods(Class<?> clazz) {
        return new MethodSelectorImpl(clazz);
    }
    
    public static PropertySelector properties(Class<?> clazz) {
        return new PropertySelectorImpl(clazz);
    }
}
