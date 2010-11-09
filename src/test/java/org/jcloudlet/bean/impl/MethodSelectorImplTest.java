package org.jcloudlet.bean.impl;

import java.util.Collections;
import java.util.Set;

import org.jcloudlet.bean.criteria.Visibility;
import org.junit.Assert;
import org.junit.Test;

public class MethodSelectorImplTest
{
    @Test
    public void testWithVisibility() {
        abstract class TestedClass {
            abstract void defaultVisibility();
            @SuppressWarnings("unused")
            private void privateVisibility() {};
            protected abstract void protectedVisibility();
            public abstract void publicVisibility();
        }
        for (Visibility v : Visibility.values()) {
            Set<String> methods = new MethodSelectorImpl(TestedClass.class)
                .with(v).declared().asMap().keySet();
            Assert.assertEquals(Collections.singleton(v.name().toLowerCase() + "Visibility"), 
                methods);
        }
    }
}
