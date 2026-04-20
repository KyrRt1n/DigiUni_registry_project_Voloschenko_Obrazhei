package ua.sopsany.reflection;

import ua.sopsany.auth.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MenuItem {

    String label();

    Role role() default Role.USER;

    int order() default 100;
}