package org.synthful.gwt.mvp.client;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface PresentationEntity extends Serializable {
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
	public @interface Presentable {
		int sequence();
		String caption() default "";
		int fractionDigits() default -1;
		int length() default -1;
		int maxLen() default -1;
		int minLen() default -1;
		int totalDigits() default -1;
		float maxVal() default -1;
		float minVal() default -1;
		String pattern() default "";
		String whiteSpace() default "";
		boolean readOnly() default false;
		boolean multiValue() default false;
		boolean hidden() default false;
		boolean isTest() default true;
	}
}
