/**
 * Module  : DerivedSimple.java
 * Abstract:
 *
 * Created Aug 24, 2010
 */
package org.jcloudlet.web.bean.testbeans;

import org.jcloudlet.web.bean.impl.*;

/**
 * @author Sergiy Yevtushenko
 */
@Fragments("one")
@TemplateName("two")
public class DerivedSimple extends Simple {
    @Ignored
    private int field10 = -1;
    public DerivedSimple(String field1, Long field2, boolean field3, long field4) {
        super(field1, field2, field3, field4);
    }
    @ExternallyAccessible
    public int getField10() {
        return field10;
    }
    @Input
    public void setField10(int field10) {
        this.field10 = field10;
    }
}
