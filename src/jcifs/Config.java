package jcifs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.StringTokenizer;
import jcifs.util.LogStream;

public class Config
{
  public static String DEFAULT_OEM_ENCODING;
  private static LogStream log;
  private static Properties prp;
  public static int socketCount = 0;

  static
  {
    prp = new Properties();
    DEFAULT_OEM_ENCODING = "Cp850";
    log = LogStream.getInstance();
    try
    {
      String str = System.getProperty("jcifs.properties");
      FileInputStream localFileInputStream = null;
      if (str != null)
      {
        int j = str.length();
        localFileInputStream = null;
        if (j > 1)
          localFileInputStream = new FileInputStream(str);
      }
      load(localFileInputStream);
      if (localFileInputStream != null)
        localFileInputStream.close();
      int i = getInt("jcifs.util.loglevel", -1);
      if (i != -1)
        LogStream.setLevel(i);
    }
    catch (IOException localUnsupportedEncodingException)
    {
      try
      {
        "".getBytes(DEFAULT_OEM_ENCODING);
        if (LogStream.level < 4);
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        try
        {
          while (true)
          {
            prp.store(log, "JCIFS PROPERTIES");
            return;
            localIOException1 = localIOException1;
            if (LogStream.level > 0)
              localIOException1.printStackTrace(log);
          }
          localUnsupportedEncodingException = localUnsupportedEncodingException;
          if (LogStream.level >= 2)
            log.println("WARNING: The default OEM encoding " + DEFAULT_OEM_ENCODING + " does not appear to be supported by this JRE. The default encoding will be US-ASCII.");
          DEFAULT_OEM_ENCODING = "US-ASCII";
        }
        catch (IOException localIOException2)
        {
        }
      }
    }
  }

  public static Object get(String paramString)
  {
    return prp.get(paramString);
  }

  public static boolean getBoolean(String paramString, boolean paramBoolean)
  {
    String str = getProperty(paramString);
    if (str != null)
      paramBoolean = str.toLowerCase().equals("true");
    return paramBoolean;
  }

  public static InetAddress getInetAddress(String paramString, InetAddress paramInetAddress)
  {
    String str = prp.getProperty(paramString);
    if (str != null);
    try
    {
      InetAddress localInetAddress = InetAddress.getByName(str);
      paramInetAddress = localInetAddress;
      return paramInetAddress;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      while (LogStream.level <= 0);
      log.println(str);
      localUnknownHostException.printStackTrace(log);
    }
    return paramInetAddress;
  }

  public static InetAddress[] getInetAddressArray(String paramString1, String paramString2, InetAddress[] paramArrayOfInetAddress)
  {
    String str1 = getProperty(paramString1);
    InetAddress[] arrayOfInetAddress;
    if (str1 != null)
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(str1, paramString2);
      int i = localStringTokenizer.countTokens();
      arrayOfInetAddress = new InetAddress[i];
      int j = 0;
      while (true)
      {
        if (j >= i)
          break label97;
        String str2 = localStringTokenizer.nextToken();
        try
        {
          arrayOfInetAddress[j] = InetAddress.getByName(str2);
          j++;
        }
        catch (UnknownHostException localUnknownHostException)
        {
          if (LogStream.level > 0)
          {
            log.println(str2);
            localUnknownHostException.printStackTrace(log);
          }
        }
      }
    }
    return paramArrayOfInetAddress;
    label97: return arrayOfInetAddress;
  }

  public static int getInt(String paramString)
  {
    String str = prp.getProperty(paramString);
    int i = -1;
    if (str != null);
    try
    {
      int j = Integer.parseInt(str);
      i = j;
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (LogStream.level <= 0);
      localNumberFormatException.printStackTrace(log);
    }
    return i;
  }

  public static int getInt(String paramString, int paramInt)
  {
    String str = prp.getProperty(paramString);
    if (str != null);
    try
    {
      int i = Integer.parseInt(str);
      paramInt = i;
      return paramInt;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (LogStream.level <= 0);
      localNumberFormatException.printStackTrace(log);
    }
    return paramInt;
  }

  public static InetAddress getLocalHost()
  {
    String str = prp.getProperty("jcifs.smb.client.laddr");
    if (str != null)
      try
      {
        InetAddress localInetAddress = InetAddress.getByName(str);
        return localInetAddress;
      }
      catch (UnknownHostException localUnknownHostException)
      {
        if (LogStream.level > 0)
        {
          log.println("Ignoring jcifs.smb.client.laddr address: " + str);
          localUnknownHostException.printStackTrace(log);
        }
      }
    return null;
  }

  public static long getLong(String paramString, long paramLong)
  {
    String str = prp.getProperty(paramString);
    if (str != null);
    try
    {
      long l = Long.parseLong(str);
      paramLong = l;
      return paramLong;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (LogStream.level <= 0);
      localNumberFormatException.printStackTrace(log);
    }
    return paramLong;
  }

  public static String getProperty(String paramString)
  {
    return prp.getProperty(paramString);
  }

  public static String getProperty(String paramString1, String paramString2)
  {
    return prp.getProperty(paramString1, paramString2);
  }

  public static void list(PrintStream paramPrintStream)
    throws IOException
  {
    prp.list(paramPrintStream);
  }

  public static void load(InputStream paramInputStream)
    throws IOException
  {
    if (paramInputStream != null)
      prp.load(paramInputStream);
    try
    {
      prp.putAll(System.getProperties());
      return;
    }
    catch (SecurityException localSecurityException)
    {
      while (LogStream.level <= 1);
      log.println("SecurityException: jcifs will ignore System properties");
    }
  }

  public static void registerSmbURLHandler()
  {
    String str1 = System.getProperty("java.version");
    if ((str1.startsWith("1.1.")) || (str1.startsWith("1.2.")))
      throw new RuntimeException("jcifs-0.7.0b4+ requires Java 1.3 or above. You are running " + str1);
    String str2 = System.getProperty("java.protocol.handler.pkgs");
    if (str2 == null)
      System.setProperty("java.protocol.handler.pkgs", "jcifs");
    while (str2.indexOf("jcifs") != -1)
      return;
    System.setProperty("java.protocol.handler.pkgs", str2 + "|jcifs");
  }

  public static void setProperties(Properties paramProperties)
  {
    prp = new Properties(paramProperties);
    try
    {
      prp.putAll(System.getProperties());
      return;
    }
    catch (SecurityException localSecurityException)
    {
      while (LogStream.level <= 1);
      log.println("SecurityException: jcifs will ignore System properties");
    }
  }

  public static Object setProperty(String paramString1, String paramString2)
  {
    return prp.setProperty(paramString1, paramString2);
  }

  public static void store(OutputStream paramOutputStream, String paramString)
    throws IOException
  {
    prp.store(paramOutputStream, paramString);
  }
}