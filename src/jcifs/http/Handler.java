package jcifs.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Handler extends URLStreamHandler
{
  public static final int DEFAULT_HTTP_PORT = 80;
  private static final String HANDLER_PKGS_PROPERTY = "java.protocol.handler.pkgs";
  private static final String[] JVM_VENDOR_DEFAULT_PKGS = { "sun.net.www.protocol" };
  private static final Map PROTOCOL_HANDLERS = new HashMap();
  private static URLStreamHandlerFactory factory;

  private static URLStreamHandler getDefaultStreamHandler(String paramString)
    throws IOException
  {
    while (true)
    {
      URLStreamHandler localURLStreamHandler;
      String str3;
      synchronized (PROTOCOL_HANDLERS)
      {
        localURLStreamHandler = (URLStreamHandler)PROTOCOL_HANDLERS.get(paramString);
        if (localURLStreamHandler != null)
          return localURLStreamHandler;
        if (factory != null)
          localURLStreamHandler = factory.createURLStreamHandler(paramString);
        if (localURLStreamHandler == null)
        {
          StringTokenizer localStringTokenizer = new StringTokenizer(System.getProperty("java.protocol.handler.pkgs"), "|");
          if (localStringTokenizer.hasMoreTokens())
          {
            String str2 = localStringTokenizer.nextToken().trim();
            if (str2.equals("jcifs"))
              continue;
            str3 = str2 + "." + paramString + ".Handler";
          }
        }
      }
      try
      {
        Class localClass4 = Class.forName(str3);
        localClass3 = localClass4;
        if (localClass3 != null);
      }
      catch (Exception localException1)
      {
        try
        {
          while (true)
          {
            Class localClass3 = ClassLoader.getSystemClassLoader().loadClass(str3);
            localURLStreamHandler = (URLStreamHandler)localClass3.newInstance();
            int i;
            label164: String str1;
            if (localURLStreamHandler == null)
            {
              i = 0;
              if (i < JVM_VENDOR_DEFAULT_PKGS.length)
                str1 = JVM_VENDOR_DEFAULT_PKGS[i] + "." + paramString + ".Handler";
            }
            try
            {
              Class localClass2 = Class.forName(str1);
              localClass1 = localClass2;
              if (localClass1 != null);
            }
            catch (Exception localException1)
            {
              try
              {
                Class localClass1 = ClassLoader.getSystemClassLoader().loadClass(str1);
                localURLStreamHandler = (URLStreamHandler)localClass1.newInstance();
                label243: if (localURLStreamHandler != null)
                {
                  if (localURLStreamHandler == null)
                  {
                    throw new IOException("Unable to find default handler for protocol: " + paramString);
                    localObject = finally;
                    throw localObject;
                  }
                }
                else
                {
                  i++;
                  break label164;
                }
                PROTOCOL_HANDLERS.put(paramString, localURLStreamHandler);
                return localURLStreamHandler;
                localException3 = localException3;
                localClass3 = null;
                continue;
                localException1 = localException1;
                localClass1 = null;
              }
              catch (Exception localException2)
              {
                break label243;
              }
            }
          }
        }
        catch (Exception localException4)
        {
        }
      }
    }
  }

  public static void setURLStreamHandlerFactory(URLStreamHandlerFactory paramURLStreamHandlerFactory)
  {
    synchronized (PROTOCOL_HANDLERS)
    {
      if (factory != null)
        throw new IllegalStateException("URLStreamHandlerFactory already set.");
    }
    PROTOCOL_HANDLERS.clear();
    factory = paramURLStreamHandlerFactory;
  }

  protected int getDefaultPort()
  {
    return 80;
  }

  protected URLConnection openConnection(URL paramURL)
    throws IOException
  {
    return new NtlmHttpURLConnection((HttpURLConnection)new URL(paramURL, paramURL.toExternalForm(), getDefaultStreamHandler(paramURL.getProtocol())).openConnection());
  }
}