package com.visitscotland.brxm.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks methods that cannot be tested because they use functionality that requires from the CMS to be running
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface NonTestable {
    enum Cause {
        UNKNOWN,

        /**
         * The class cannot be tested because its parent uses static methods that cannot be initialized.
         *
         * Tha means that either the constructor or static methods needs to be mocked. In practice, we try to go away
         * from this classes minimizing the amount of code on them.
         *
         * If a class is marked with this the containing code should be very limited leaving any logic to a different
         * component.
         */
        INHERITANCE,

        /**
         * The purpose of the method is to wrap other service so the code is not tightly couple to other services
         * (i.e HippoCms, DMS, Instagram)
         */
        BRIDGE;
    }

    Cause value() default Cause.UNKNOWN;
}
