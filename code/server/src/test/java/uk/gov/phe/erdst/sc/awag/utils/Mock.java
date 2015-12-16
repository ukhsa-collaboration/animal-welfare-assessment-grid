package uk.gov.phe.erdst.sc.awag.utils;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Stereotype;

@Stereotype
@Retention(RUNTIME)
@Target(TYPE)
// here define all annotations you want to replace by this one.
// this stereotype define an alternative
@Alternative
public @interface Mock
{

}
