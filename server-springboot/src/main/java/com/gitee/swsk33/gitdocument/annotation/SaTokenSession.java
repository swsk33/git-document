package com.gitee.swsk33.gitdocument.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 用于Sa-Token的用户Session操作对象的注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SaTokenSession {

	@AliasFor(annotation = Component.class)
	String value() default "";

}