package org.synthful.gwt.mvp.client;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Unused, retained as example on annotation.
 * Usage:
 * @Presentation({
 *	@Restrictions(restriction=Restriction.FractionDigits, value="1"),
 *	@Restrictions(restriction=Restriction.Length, value="10")
 *	})
 * 
 * @author icecream
 *
 */
public interface PresentableEntity
extends Serializable {
	
	enum Restriction {
	    None,
	    Enumeration,
	    FractionDigits,
	    Length,
	    MaxExclusive,
	    MaxInclusive,
	    MaxLength,
	    MinExclusive,
	    MinInclusive,
	    MinLength,
	    Pattern,
	    TotalDigits,
	    WhiteSpace;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Restrictions {
	    Restriction restriction() default Restriction.None;
	    String value() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
	@interface Presentation {
		Restrictions[] value();
	}
}
