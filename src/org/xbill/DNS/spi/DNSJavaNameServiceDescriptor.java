package org.xbill.DNS.spi;

import java.lang.reflect.Proxy;
import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

public class DNSJavaNameServiceDescriptor
  implements NameServiceDescriptor
{
  static Class class$sun$net$spi$nameservice$NameService;
  private static NameService nameService;

  static
  {
    Class localClass1;
    ClassLoader localClassLoader;
    Class[] arrayOfClass;
    Class localClass2;
    if (class$sun$net$spi$nameservice$NameService == null)
    {
      localClass1 = class$("sun.net.spi.nameservice.NameService");
      class$sun$net$spi$nameservice$NameService = localClass1;
      localClassLoader = localClass1.getClassLoader();
      arrayOfClass = new Class[1];
      if (class$sun$net$spi$nameservice$NameService != null)
        break label72;
      localClass2 = class$("sun.net.spi.nameservice.NameService");
      class$sun$net$spi$nameservice$NameService = localClass2;
    }
    while (true)
    {
      arrayOfClass[0] = localClass2;
      nameService = (NameService)Proxy.newProxyInstance(localClassLoader, arrayOfClass, new DNSJavaNameService());
      return;
      localClass1 = class$sun$net$spi$nameservice$NameService;
      break;
      label72: localClass2 = class$sun$net$spi$nameservice$NameService;
    }
  }

  static Class class$(String paramString)
  {
    try
    {
      Class localClass = Class.forName(paramString);
      return localClass;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      throw new NoClassDefFoundError().initCause(localClassNotFoundException);
    }
  }

  public NameService createNameService()
  {
    return nameService;
  }

  public String getProviderName()
  {
    return "dnsjava";
  }

  public String getType()
  {
    return "dns";
  }
}