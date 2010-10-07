/**
 * Module  : BeanDetailsTest.java
 * Abstract:
 *
 * Created Aug 24, 2010
 */

package org.jcloudlet.bean.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.jcloudlet.bean.Bean;
import org.jcloudlet.bean.Property;
import org.jcloudlet.bean.impl.BeanDetails;
import org.jcloudlet.bean.testbeans.DerivedSimple;
import org.jcloudlet.bean.testbeans.Simple;
import org.junit.Test;

/**
 * @author Sergiy Yevtushenko
 */
public class BeanDetailsTest {

    @Test
    public void allAnnotationsAreRetrieved() {
        Bean details = BeanDetails.forClass(Simple.class);
        
        assertEquals(1, details.annotations().size());
        assertTrue(details.annotations().iterator().next() instanceof Fragment);
    }
    
    @Test
    public void allDeclaredAnnotationsAreRetrieved() {
        Bean details = BeanDetails.forClass(DerivedSimple.class);
        
        assertEquals(2, details.declaredAnnotations().size());
        assertFalse(details.declaredAnnotations().iterator().next() instanceof Fragment);
        assertTrue(details.declaredAnnotations().iterator().next() instanceof Fragments);
    }

    @Test
    public void allPropertiesAreRetrieved() {
        List<String> names = Arrays.asList("field1", "field2", "field3", "field4");
        
        Bean details = BeanDetails.forClass(Simple.class);
        
        assertEquals(names.size(), details.properties().size());
        
        for (String name : names) {
            assertNotNull(details.property(name));
        }
    }

    @Test
    public void declaredMethodsAreRetrieved() {
        Bean details = BeanDetails.forClass(Simple.class);
        
        List<String> methodNames = Arrays.asList("getField1", "setField1", 
                                                 "getField2", "setField2", 
                                                 "setField3", "isField3",
                                                 "getField4");
 
        assertEquals(methodNames.size(), details.declaredMethods().size());
        
        for (String name : methodNames) {
            assertNotNull("Method " + name + " not found", details.declaredMethod(name));    
        }
    }
    
    @Test
    public void methodsAreRetrieved() {
        Bean details = BeanDetails.forClass(Simple.class);

        List<String> methodNames = Arrays.asList("getField1", "setField1", 
                                                 "getField2", "setField2", 
                                                 "setField3", "isField3",
                                                 "getField4",
                                                 "wait", "toString", "notify", "notifyAll",
                                                 "getClass", "equals", "hashCode");

        assertEquals(methodNames.size(), details.methods().size());
        
        for (String name : methodNames) {
            assertNotNull("Method " + name + " not found", details.method(name));    
        }
    }

    @Test
    public void simplePropertyTypeIsRetrieved() {
        Property prop = BeanDetails.forClass(Simple.class).property("field1");

        assertEquals(String.class, prop.type());
    }
}
