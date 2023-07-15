package com.gitee.swsk33.gitdocument.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 用于Git裸仓库操作对象的注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GitRepository {

	@AliasFor(annotation = Component.class)
	String value() default "";

}
