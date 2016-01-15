package edu.cedarville.adld.common.dagger.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Top-level application wide scope of Injection graph
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AppScope {
}
