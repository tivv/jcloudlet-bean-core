/**
 * Module  : PropertyTest.java
 * Abstract:
 *
 * Created Aug 24, 2010
 */
package org.jcloudlet.web.bean.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.jcloudlet.web.bean.Property;
import org.jcloudlet.web.bean.annotation.Alias;
import org.jcloudlet.web.bean.impl.BeanDetails;
import org.jcloudlet.web.bean.testbeans.*;
import org.junit.Test;

/**
 * @author Sergiy Yevtushenko
 */
public class PropertyTest {
    private Primitives testObj = new Primitives();

    @Test
    public void annotationsProperlyHandled() {
        Property prop1 = propertyFor(DerivedSimple.class, "field10");
        assertNotNull(prop1.annotation(Input.class));
        assertNotNull(prop1.annotation(ExternallyAccessible.class));
        assertNotNull(prop1.annotation(Ignored.class));
        assertTrue(prop1.has(Input.class));
        assertFalse(prop1.has(Alias.class));
    }
    
    @Test
    public void annotationsProperlyHandledForType() {
        Property prop1 = propertyFor(InnerStatic.class, "field");
        assertNotNull(prop1.typeAnnotation(TemplateName.class));
        assertNotNull(prop1.typeAnnotation(Fragments.class));
    }

    @Test
    public void getterUsedIfPresent() {
        Property prop = propertyFor(Simple.class, "field1");
        
        assertTrue("Getter not found!", prop.getter() != null);
        Simple expected = new Simple("Some text", 0L, false, 16L);
        // actual getter returns value of other field
        assertEquals("16", prop.get(expected));
    }

    @Test
    public void privateFieldIsAccessibleForGetWithoutGetter() {
        testObj.field14(10L);
        
        Property prop = propertyFor(Primitives.class, "field4");
        assertTrue(prop.getter() == null);
        assertEquals(testObj.field4(), prop.get(testObj));
    }
    
    @Test
    public void privateFieldIsAccessibleForSetWithoutSetter() {
        Property prop = propertyFor(Primitives.class, "field4");
        
        prop.set(testObj, 11);
        assertEquals(testObj.field4(), prop.get(testObj));
    }

    @Test
    public void propertyIsMarkedAsInheritedProperly() {
        Property prop1 = propertyFor(Simple.class, "field1");
        assertFalse(prop1.inherited());
        
        Property prop2 = propertyFor(DerivedSimple.class, "field1");
        assertTrue(prop2.inherited());
    }
        
    @Test
    public void setterUsedIfPresent() {
        Property prop = propertyFor(Simple.class, "field2");
        
        assertTrue("Setter not found!", prop.setter() != null);
        Simple expected = new Simple(null, 10L, false, 0);
        prop.set(expected, 43L);
        // setter does nothing, value remains the same
        assertEquals(10L, prop.get(expected));
    }
    
    @Test
    public void aliasNameIsCalculatedTakingIntoAccountAnnotation() {
        Property prop1 = propertyFor(InnerStatic.class, "comp");
        assertEquals("someOtherName", prop1.alias());
        
        Property prop2 = propertyFor(InnerStatic.class, "field");
        assertEquals("field", prop2.alias());
    }

    private Property propertyFor(Class<?> clazz, String name) {
        return BeanDetails.forClass(clazz).property(name);
    }
    
    public static class InnerStatic {
        @SuppressWarnings("unused")
        @Alias("someOtherName")
        private Component comp;
        @SuppressWarnings("unused")
        private DerivedSimple field;
        private int[] intArray;
        private Integer[] integerArray;
        private HashSet<Integer> integerHashSet;
        private List<Integer> integerList;
        private Queue<Integer> integerQueue;
        private Set<Integer> integerSet;
        
        public int[] array() {
            return intArray;
        }

        public Integer[] integerArray() {
            return integerArray;
        }

        public HashSet<Integer> integerHashSet() {
            return integerHashSet;
        }

        public List<Integer> integerList() {
            return integerList;
        }

        public Queue<Integer> integerQueue() {
            return integerQueue;
        }

        public Set<Integer> integerSet() {
            return integerSet;
        }
    }
}
