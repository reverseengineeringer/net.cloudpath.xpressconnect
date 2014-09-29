package org.bouncycastle2.jce.interfaces;

public abstract interface ConfigurableProvider
{
  public static final String EC_IMPLICITLY_CA = "ecImplicitlyCa";
  public static final String THREAD_LOCAL_EC_IMPLICITLY_CA = "threadLocalEcImplicitlyCa";

  public abstract void setParameter(String paramString, Object paramObject);
}