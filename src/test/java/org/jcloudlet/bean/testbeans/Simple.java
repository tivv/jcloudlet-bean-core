/**
 * Module  : Simple.java
 * Abstract:
 *
 * Created Aug 24, 2010
 */
package org.jcloudlet.bean.testbeans;

import org.jcloudlet.bean.impl.Fragment;

/**
 * @author Sergiy Yevtushenko
 */
@Fragment
public class Simple {
    @SuppressWarnings("unused")
    private String field1;
    private Long field2;
    private boolean field3;
    private long field4;
    
    public Simple(String field1, Long field2, boolean field3, long field4) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
    }
    
    public String getField1() {
        return String.valueOf(field4);
    }
    public Long getField2() {
        return field2;
    }
    public Long getField4() {
        return Long.valueOf(field4);
    }
    public boolean isField3(String text) {
        return field3;
    }
    public void setField1(String field1) {
        this.field1 = field1;
    }
    public void setField2(Long field2) {
        //this.field2 = field2;
    }
    public void setField3(String text) {
        this.field3 = Boolean.parseBoolean(text);
    }
}
