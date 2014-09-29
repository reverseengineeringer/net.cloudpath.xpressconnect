package com.cloudpath.common.parse;

public abstract interface XmlContainer
{
  public abstract String getEndElement();

  public abstract String getStartElement();

  public abstract void setXml(String paramString);
}