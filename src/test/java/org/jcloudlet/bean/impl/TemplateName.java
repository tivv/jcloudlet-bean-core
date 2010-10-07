package org.jcloudlet.bean.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TemplateName {
    String value() default "";
}
