package net.cloudpath.xpressconnect.nativeproxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LinkPropertiesProxy
{
  private Class<?> mLinkClass = null;
  private Object mLinkTarget = null;

  public LinkPropertiesProxy(Object paramObject)
    throws ClassNotFoundException
  {
    this.mLinkTarget = paramObject;
    this.mLinkClass = Class.forName("android.net.LinkProperties");
  }

  public ProxyPropertiesProxy getHttpProxy()
    throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
  {
    return new ProxyPropertiesProxy(this.mLinkClass.getMethod("getHttpProxy", (Class[])null).invoke(this.mLinkTarget, (Object[])null));
  }

  public void setHttpProxy(Object paramObject)
    throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException
  {
    Class localClass = this.mLinkClass;
    Class[] arrayOfClass = new Class[1];
    arrayOfClass[0] = Class.forName("android.net.ProxyProperties");
    localClass.getMethod("setHttpProxy", arrayOfClass).invoke(this.mLinkTarget, new Object[] { paramObject });
  }
}