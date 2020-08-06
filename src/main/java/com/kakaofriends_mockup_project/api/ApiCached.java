package com.kakaofriends_mockup_project.api;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiCached {

    String key() default "";

    int ttl() default 30;               // default 30분

}
