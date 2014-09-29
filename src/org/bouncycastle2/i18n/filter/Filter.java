package org.bouncycastle2.i18n.filter;

public abstract interface Filter
{
  public abstract String doFilter(String paramString);

  public abstract String doFilterUrl(String paramString);
}