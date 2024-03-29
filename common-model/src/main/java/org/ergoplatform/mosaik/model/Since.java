package org.ergoplatform.mosaik.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Since {
    /**
     * the value indicating Mosaik version number since this member
     * or type has been present.
     */
    int value();
}
