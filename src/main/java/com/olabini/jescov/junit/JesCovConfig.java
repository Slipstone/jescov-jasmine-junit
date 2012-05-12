package com.olabini.jescov.junit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * JesCov already excludes all the spec files and internal library files
 * 
 * @author kbranton
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JesCovConfig {
	/**
	 * A list of ant-style filters of sources to exclude
	 */
	String[] sourceExclude() default {};
}
