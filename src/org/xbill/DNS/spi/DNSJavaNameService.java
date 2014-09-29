package org.xbill.DNS.spi;

import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import org.xbill.DNS.AAAARecord;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.PTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.TextParseException;

public class DNSJavaNameService
  implements InvocationHandler
{
  static Class array$$B;
  static Class array$Ljava$net$InetAddress;
  private static final String domainProperty = "sun.net.spi.nameservice.domain";
  private static final String nsProperty = "sun.net.spi.nameservice.nameservers";
  private static final String v6Property = "java.net.preferIPv6Addresses";
  private boolean preferV6 = false;

  protected DNSJavaNameService()
  {
    String str1 = System.getProperty("sun.net.spi.nameservice.nameservers");
    String str2 = System.getProperty("sun.net.spi.nameservice.domain");
    String str3 = System.getProperty("java.net.preferIPv6Addresses");
    String[] arrayOfString;
    if (str1 != null)
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(str1, ",");
      arrayOfString = new String[localStringTokenizer.countTokens()];
      int j;
      for (int i = 0; localStringTokenizer.hasMoreTokens(); i = j)
      {
        j = i + 1;
        arrayOfString[i] = localStringTokenizer.nextToken();
      }
    }
    try
    {
      Lookup.setDefaultResolver(new ExtendedResolver(arrayOfString));
      if (str2 == null);
    }
    catch (UnknownHostException localUnknownHostException)
    {
      try
      {
        Lookup.setDefaultSearchPath(new String[] { str2 });
        if ((str3 != null) && (str3.equalsIgnoreCase("true")))
          this.preferV6 = true;
        return;
        localUnknownHostException = localUnknownHostException;
        System.err.println("DNSJavaNameService: invalid sun.net.spi.nameservice.nameservers");
      }
      catch (TextParseException localTextParseException)
      {
        while (true)
          System.err.println("DNSJavaNameService: invalid sun.net.spi.nameservice.domain");
      }
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

  public String getHostByAddr(byte[] paramArrayOfByte)
    throws UnknownHostException
  {
    Record[] arrayOfRecord = new Lookup(ReverseMap.fromAddress(InetAddress.getByAddress(paramArrayOfByte)), 12).run();
    if (arrayOfRecord == null)
      throw new UnknownHostException();
    return ((PTRRecord)arrayOfRecord[0]).getTarget().toString();
  }

  public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    throws Throwable
  {
    InetAddress[] arrayOfInetAddress;
    try
    {
      if (paramMethod.getName().equals("getHostByAddr"))
        return getHostByAddr((byte[])paramArrayOfObject[0]);
      if (paramMethod.getName().equals("lookupAllHostAddr"))
      {
        arrayOfInetAddress = lookupAllHostAddr((String)paramArrayOfObject[0]);
        Class localClass1 = paramMethod.getReturnType();
        Class localClass2;
        Class localClass3;
        if (array$Ljava$net$InetAddress == null)
        {
          localClass2 = class$("[Ljava.net.InetAddress;");
          array$Ljava$net$InetAddress = localClass2;
          if (localClass1.equals(localClass2))
            break label200;
          if (array$$B != null)
            break label161;
          localClass3 = class$("[[B");
          array$$B = localClass3;
        }
        label161: 
        while (true)
          label102: if (localClass1.equals(localClass3))
          {
            int i = arrayOfInetAddress.length;
            byte[][] arrayOfByte = new byte[i][];
            int j = 0;
            while (true)
              if (j < i)
              {
                arrayOfByte[j] = arrayOfInetAddress[j].getAddress();
                j++;
                continue;
                localClass2 = array$Ljava$net$InetAddress;
                break;
                localClass3 = array$$B;
                break label102;
              }
            return arrayOfByte;
          }
      }
    }
    catch (Throwable localThrowable)
    {
      System.err.println("DNSJavaNameService: Unexpected error.");
      localThrowable.printStackTrace();
      throw localThrowable;
    }
    throw new IllegalArgumentException("Unknown function name or arguments.");
    label200: return arrayOfInetAddress;
  }

  public InetAddress[] lookupAllHostAddr(String paramString)
    throws UnknownHostException
  {
    Record[] arrayOfRecord;
    try
    {
      Name localName = new Name(paramString);
      boolean bool = this.preferV6;
      arrayOfRecord = null;
      if (bool)
        arrayOfRecord = new Lookup(localName, 28).run();
      if (arrayOfRecord == null)
        arrayOfRecord = new Lookup(localName, 1).run();
      if ((arrayOfRecord == null) && (!this.preferV6))
        arrayOfRecord = new Lookup(localName, 28).run();
      if (arrayOfRecord == null)
        throw new UnknownHostException(paramString);
    }
    catch (TextParseException localTextParseException)
    {
      throw new UnknownHostException(paramString);
    }
    InetAddress[] arrayOfInetAddress = new InetAddress[arrayOfRecord.length];
    int i = 0;
    if (i < arrayOfRecord.length)
    {
      arrayOfRecord[i];
      if ((arrayOfRecord[i] instanceof ARecord))
        arrayOfInetAddress[i] = ((ARecord)arrayOfRecord[i]).getAddress();
      while (true)
      {
        i++;
        break;
        arrayOfInetAddress[i] = ((AAAARecord)arrayOfRecord[i]).getAddress();
      }
    }
    return arrayOfInetAddress;
  }
}