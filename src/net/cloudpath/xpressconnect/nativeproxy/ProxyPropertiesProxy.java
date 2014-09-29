package net.cloudpath.xpressconnect.nativeproxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyPropertiesProxy
{
  private Class[] intArgsClass;
  private Class<?> mProxyDefinition = null;
  private Object mProxyResult = null;

  public ProxyPropertiesProxy(Object paramObject)
    throws ClassNotFoundException
  {
    Class[] arrayOfClass = new Class[3];
    arrayOfClass[0] = String.class;
    arrayOfClass[1] = Integer.TYPE;
    arrayOfClass[2] = String.class;
    this.intArgsClass = arrayOfClass;
    this.mProxyDefinition = Class.forName("android.net.ProxyProperties");
    this.mProxyResult = paramObject;
  }

  public ProxyPropertiesProxy(String paramString1, int paramInt, String paramString2)
    throws ClassNotFoundException, NoSuchMethodException
  {
    Class[] arrayOfClass = new Class[3];
    arrayOfClass[0] = String.class;
    arrayOfClass[1] = Integer.TYPE;
    arrayOfClass[2] = String.class;
    this.intArgsClass = arrayOfClass;
    this.mProxyDefinition = Class.forName("android.net.ProxyProperties");
    this.mProxyResult = createObject(this.mProxyDefinition.getConstructor(this.intArgsClass), paramString1, paramInt, paramString2);
  }

  protected Object createObject(Constructor<?> paramConstructor, String paramString1, int paramInt, String paramString2)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = paramString1;
    arrayOfObject[1] = Integer.valueOf(paramInt);
    arrayOfObject[2] = paramString2;
    if (paramConstructor == null)
      return null;
    try
    {
      Object localObject = paramConstructor.newInstance(arrayOfObject);
      return localObject;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
      return null;
    }
    catch (InstantiationException localInstantiationException)
    {
      localInstantiationException.printStackTrace();
      return null;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
      return null;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      localInvocationTargetException.printStackTrace();
    }
    return null;
  }

  public String getExclusionList()
    throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    return (String)this.mProxyDefinition.getMethod("getExclusionList", (Class[])null).invoke(this.mProxyResult, (Object[])null);
  }

  public String getHost()
    throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    return (String)this.mProxyDefinition.getMethod("getHost", (Class[])null).invoke(this.mProxyResult, (Object[])null);
  }

  public Object getObject()
  {
    return this.mProxyResult;
  }

  public int getPort()
    throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    return ((Integer)this.mProxyDefinition.getMethod("getPort", (Class[])null).invoke(this.mProxyResult, (Object[])null)).intValue();
  }
}