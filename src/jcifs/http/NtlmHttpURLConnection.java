package jcifs.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import jcifs.Config;
import jcifs.ntlmssp.NtlmMessage;
import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import jcifs.util.Base64;

public class NtlmHttpURLConnection extends HttpURLConnection
{
  private static final String DEFAULT_DOMAIN = str;
  private static final int LM_COMPATIBILITY;
  private static final int MAX_REDIRECTS = Integer.parseInt(System.getProperty("http.maxRedirects", "20"));
  private String authMethod;
  private String authProperty;
  private ByteArrayOutputStream cachedOutput;
  private HttpURLConnection connection;
  private boolean handshakeComplete;
  private Map headerFields;
  private Map requestProperties;

  static
  {
    LM_COMPATIBILITY = Config.getInt("jcifs.smb.lmCompatibility", 0);
    String str = System.getProperty("http.auth.ntlm.domain");
    if (str == null)
      str = Type3Message.getDefaultDomain();
  }

  public NtlmHttpURLConnection(HttpURLConnection paramHttpURLConnection)
  {
    super(paramHttpURLConnection.getURL());
    this.connection = paramHttpURLConnection;
    this.requestProperties = new HashMap();
  }

  private NtlmMessage attemptNegotiation(int paramInt)
    throws IOException
  {
    this.authProperty = null;
    this.authMethod = null;
    InputStream localInputStream = this.connection.getErrorStream();
    if ((localInputStream != null) && (localInputStream.available() != 0))
    {
      byte[] arrayOfByte = new byte[1024];
      while (localInputStream.read(arrayOfByte, 0, 1024) != -1);
    }
    String str1;
    if (paramInt == 401)
      str1 = "WWW-Authenticate";
    List localList;
    Object localObject1;
    for (this.authProperty = "Authorization"; ; this.authProperty = "Proxy-Authorization")
    {
      localList = (List)getHeaderFields0().get(str1);
      if (localList != null)
        break;
      localObject1 = null;
      return localObject1;
      str1 = "Proxy-Authenticate";
    }
    Iterator localIterator = localList.iterator();
    boolean bool = localIterator.hasNext();
    String str2 = null;
    String str11;
    if (bool)
    {
      str11 = (String)localIterator.next();
      if (!str11.startsWith("NTLM"))
        break label207;
      if (str11.length() != 4)
        break label176;
      this.authMethod = "NTLM";
    }
    while (true)
    {
      if (this.authMethod != null)
        break label272;
      return null;
      label176: if (str11.indexOf(' ') != 4)
        break;
      this.authMethod = "NTLM";
      str2 = str11.substring(5).trim();
      continue;
      label207: if (!str11.startsWith("Negotiate"))
        break;
      if (str11.length() == 9)
      {
        this.authMethod = "Negotiate";
        str2 = null;
      }
      else
      {
        if (str11.indexOf(' ') != 9)
          break;
        this.authMethod = "Negotiate";
        str2 = str11.substring(10).trim();
      }
    }
    label272: if (str2 != null);
    for (Type2Message localType2Message = new Type2Message(Base64.decode(str2)); ; localType2Message = null)
    {
      reconnect();
      if (localType2Message != null)
        break label332;
      localObject1 = new Type1Message();
      if (LM_COMPATIBILITY <= 2)
        break;
      ((NtlmMessage)localObject1).setFlag(4, true);
      return localObject1;
    }
    label332: String str3 = DEFAULT_DOMAIN;
    Object localObject2 = Type3Message.getDefaultUser();
    String str4 = Type3Message.getDefaultPassword();
    String str5 = this.url.getUserInfo();
    String str6;
    if (str5 != null)
    {
      String str10 = URLDecoder.decode(str5);
      int j = str10.indexOf(':');
      if (j != -1);
      for (localObject2 = str10.substring(0, j); ; localObject2 = str10)
      {
        if (j != -1)
          str4 = str10.substring(j + 1);
        int k = ((String)localObject2).indexOf('\\');
        if (k == -1)
          k = ((String)localObject2).indexOf('/');
        if (k != -1)
          str3 = ((String)localObject2).substring(0, k);
        if (k != -1)
          localObject2 = ((String)localObject2).substring(k + 1);
        str6 = str4;
        if (localObject2 != null)
          break label612;
        if (this.allowUserInteraction)
          break;
        return null;
      }
    }
    while (true)
    {
      try
      {
        URL localURL = getURL();
        String str8 = localURL.getProtocol();
        i = localURL.getPort();
        if (i == -1)
        {
          if (!"https".equalsIgnoreCase(str8))
            break label626;
          i = 443;
        }
        String str9 = this.authMethod;
        PasswordAuthentication localPasswordAuthentication = Authenticator.requestPasswordAuthentication(null, i, str8, "", str9);
        if (localPasswordAuthentication == null)
          return null;
        localObject2 = localPasswordAuthentication.getUserName();
        str7 = new String(localPasswordAuthentication.getPassword());
        return new Type3Message((Type2Message)localType2Message, str7, str3, (String)localObject2, Type3Message.getDefaultWorkstation());
      }
      catch (Exception localException)
      {
        str7 = str6;
        continue;
      }
      label612: String str7 = str6;
      continue;
      str6 = str4;
      break;
      label626: int i = 80;
    }
  }

  private void doHandshake()
    throws IOException
  {
    connect();
    try
    {
      int i = parseResponseCode();
      if ((i != 401) && (i != 407))
        return;
      Type1Message localType1Message = (Type1Message)attemptNegotiation(i);
      if (localType1Message == null)
        return;
      int j = 0;
      while (j < MAX_REDIRECTS)
      {
        this.connection.setRequestProperty(this.authProperty, this.authMethod + ' ' + Base64.encode(localType1Message.toByteArray()));
        this.connection.connect();
        int k = parseResponseCode();
        if ((k != 401) && (k != 407))
          return;
        Type3Message localType3Message = (Type3Message)attemptNegotiation(k);
        if (localType3Message == null)
          return;
        this.connection.setRequestProperty(this.authProperty, this.authMethod + ' ' + Base64.encode(localType3Message.toByteArray()));
        this.connection.connect();
        if ((this.cachedOutput != null) && (this.doOutput))
        {
          OutputStream localOutputStream = this.connection.getOutputStream();
          this.cachedOutput.writeTo(localOutputStream);
          localOutputStream.flush();
        }
        int m = parseResponseCode();
        if ((m != 401) && (m != 407))
          return;
        j++;
        if ((!this.allowUserInteraction) || (j >= MAX_REDIRECTS))
          break;
        reconnect();
      }
    }
    finally
    {
      this.cachedOutput = null;
    }
    throw new IOException("Unable to negotiate NTLM authentication.");
  }

  private Map getHeaderFields0()
  {
    if (this.headerFields != null)
      return this.headerFields;
    HashMap localHashMap = new HashMap();
    String str1 = this.connection.getHeaderFieldKey(0);
    String str2 = this.connection.getHeaderField(0);
    for (int i = 1; (str1 != null) || (str2 != null); i++)
    {
      Object localObject = (List)localHashMap.get(str1);
      if (localObject == null)
      {
        localObject = new ArrayList();
        localHashMap.put(str1, localObject);
      }
      ((List)localObject).add(str2);
      str1 = this.connection.getHeaderFieldKey(i);
      str2 = this.connection.getHeaderField(i);
    }
    Iterator localIterator = localHashMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localEntry.setValue(Collections.unmodifiableList((List)localEntry.getValue()));
    }
    Map localMap = Collections.unmodifiableMap(localHashMap);
    this.headerFields = localMap;
    return localMap;
  }

  private void handshake()
    throws IOException
  {
    if (this.handshakeComplete)
      return;
    doHandshake();
    this.handshakeComplete = true;
  }

  private int parseResponseCode()
    throws IOException
  {
    try
    {
      String str = this.connection.getHeaderField(0);
      for (int i = str.indexOf(' '); str.charAt(i) == ' '; i++);
      int j = Integer.parseInt(str.substring(i, i + 3));
      return j;
    }
    catch (Exception localException)
    {
      throw new IOException(localException.getMessage());
    }
  }

  private void reconnect()
    throws IOException
  {
    this.connection = ((HttpURLConnection)this.connection.getURL().openConnection());
    this.connection.setRequestMethod(this.method);
    this.headerFields = null;
    Iterator localIterator1 = this.requestProperties.entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      String str = (String)localEntry.getKey();
      StringBuffer localStringBuffer = new StringBuffer();
      Iterator localIterator2 = ((List)localEntry.getValue()).iterator();
      while (localIterator2.hasNext())
      {
        localStringBuffer.append(localIterator2.next());
        if (localIterator2.hasNext())
          localStringBuffer.append(", ");
      }
      this.connection.setRequestProperty(str, localStringBuffer.toString());
    }
    this.connection.setAllowUserInteraction(this.allowUserInteraction);
    this.connection.setDoInput(this.doInput);
    this.connection.setDoOutput(this.doOutput);
    this.connection.setIfModifiedSince(this.ifModifiedSince);
    this.connection.setUseCaches(this.useCaches);
  }

  public void addRequestProperty(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      throw new NullPointerException();
    Iterator localIterator1 = this.requestProperties.entrySet().iterator();
    Map.Entry localEntry;
    do
    {
      boolean bool = localIterator1.hasNext();
      localObject = null;
      if (!bool)
        break;
      localEntry = (Map.Entry)localIterator1.next();
    }
    while (!paramString1.equalsIgnoreCase((String)localEntry.getKey()));
    Object localObject = (List)localEntry.getValue();
    ((List)localObject).add(paramString2);
    if (localObject == null)
    {
      localObject = new ArrayList();
      ((List)localObject).add(paramString2);
      this.requestProperties.put(paramString1, localObject);
    }
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator2 = ((List)localObject).iterator();
    while (localIterator2.hasNext())
    {
      localStringBuffer.append(localIterator2.next());
      if (localIterator2.hasNext())
        localStringBuffer.append(", ");
    }
    this.connection.setRequestProperty(paramString1, localStringBuffer.toString());
  }

  public void connect()
    throws IOException
  {
    if (this.connected)
      return;
    this.connection.connect();
    this.connected = true;
  }

  public void disconnect()
  {
    this.connection.disconnect();
    this.handshakeComplete = false;
    this.connected = false;
  }

  public boolean getAllowUserInteraction()
  {
    return this.connection.getAllowUserInteraction();
  }

  public Object getContent()
    throws IOException
  {
    try
    {
      handshake();
      label4: return this.connection.getContent();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public Object getContent(Class[] paramArrayOfClass)
    throws IOException
  {
    try
    {
      handshake();
      label4: return this.connection.getContent(paramArrayOfClass);
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public String getContentEncoding()
  {
    try
    {
      handshake();
      label4: return this.connection.getContentEncoding();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public int getContentLength()
  {
    try
    {
      handshake();
      label4: return this.connection.getContentLength();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public String getContentType()
  {
    try
    {
      handshake();
      label4: return this.connection.getContentType();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public long getDate()
  {
    try
    {
      handshake();
      label4: return this.connection.getDate();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public boolean getDefaultUseCaches()
  {
    return this.connection.getDefaultUseCaches();
  }

  public boolean getDoInput()
  {
    return this.connection.getDoInput();
  }

  public boolean getDoOutput()
  {
    return this.connection.getDoOutput();
  }

  public InputStream getErrorStream()
  {
    try
    {
      handshake();
      label4: return this.connection.getErrorStream();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public long getExpiration()
  {
    try
    {
      handshake();
      label4: return this.connection.getExpiration();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public String getHeaderField(int paramInt)
  {
    try
    {
      handshake();
      label4: return this.connection.getHeaderField(paramInt);
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public String getHeaderField(String paramString)
  {
    try
    {
      handshake();
      label4: return this.connection.getHeaderField(paramString);
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public long getHeaderFieldDate(String paramString, long paramLong)
  {
    try
    {
      handshake();
      label4: return this.connection.getHeaderFieldDate(paramString, paramLong);
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public int getHeaderFieldInt(String paramString, int paramInt)
  {
    try
    {
      handshake();
      label4: return this.connection.getHeaderFieldInt(paramString, paramInt);
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public String getHeaderFieldKey(int paramInt)
  {
    try
    {
      handshake();
      label4: return this.connection.getHeaderFieldKey(paramInt);
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public Map getHeaderFields()
  {
    if (this.headerFields != null)
      return this.headerFields;
    try
    {
      handshake();
      label16: return getHeaderFields0();
    }
    catch (IOException localIOException)
    {
      break label16;
    }
  }

  public long getIfModifiedSince()
  {
    return this.connection.getIfModifiedSince();
  }

  public InputStream getInputStream()
    throws IOException
  {
    try
    {
      handshake();
      label4: return this.connection.getInputStream();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public boolean getInstanceFollowRedirects()
  {
    return this.connection.getInstanceFollowRedirects();
  }

  public long getLastModified()
  {
    try
    {
      handshake();
      label4: return this.connection.getLastModified();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public OutputStream getOutputStream()
    throws IOException
  {
    try
    {
      connect();
      label4: OutputStream localOutputStream = this.connection.getOutputStream();
      this.cachedOutput = new ByteArrayOutputStream();
      return new CacheStream(localOutputStream, this.cachedOutput);
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public Permission getPermission()
    throws IOException
  {
    return this.connection.getPermission();
  }

  public String getRequestMethod()
  {
    return this.connection.getRequestMethod();
  }

  public Map getRequestProperties()
  {
    HashMap localHashMap = new HashMap();
    Iterator localIterator = this.requestProperties.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localHashMap.put(localEntry.getKey(), Collections.unmodifiableList((List)localEntry.getValue()));
    }
    return Collections.unmodifiableMap(localHashMap);
  }

  public String getRequestProperty(String paramString)
  {
    return this.connection.getRequestProperty(paramString);
  }

  public int getResponseCode()
    throws IOException
  {
    try
    {
      handshake();
      label4: return this.connection.getResponseCode();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public String getResponseMessage()
    throws IOException
  {
    try
    {
      handshake();
      label4: return this.connection.getResponseMessage();
    }
    catch (IOException localIOException)
    {
      break label4;
    }
  }

  public URL getURL()
  {
    return this.connection.getURL();
  }

  public boolean getUseCaches()
  {
    return this.connection.getUseCaches();
  }

  public void setAllowUserInteraction(boolean paramBoolean)
  {
    this.connection.setAllowUserInteraction(paramBoolean);
    this.allowUserInteraction = paramBoolean;
  }

  public void setDefaultUseCaches(boolean paramBoolean)
  {
    this.connection.setDefaultUseCaches(paramBoolean);
  }

  public void setDoInput(boolean paramBoolean)
  {
    this.connection.setDoInput(paramBoolean);
    this.doInput = paramBoolean;
  }

  public void setDoOutput(boolean paramBoolean)
  {
    this.connection.setDoOutput(paramBoolean);
    this.doOutput = paramBoolean;
  }

  public void setIfModifiedSince(long paramLong)
  {
    this.connection.setIfModifiedSince(paramLong);
    this.ifModifiedSince = paramLong;
  }

  public void setInstanceFollowRedirects(boolean paramBoolean)
  {
    this.connection.setInstanceFollowRedirects(paramBoolean);
  }

  public void setRequestMethod(String paramString)
    throws ProtocolException
  {
    this.connection.setRequestMethod(paramString);
    this.method = paramString;
  }

  public void setRequestProperty(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      throw new NullPointerException();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(paramString2);
    Iterator localIterator = this.requestProperties.entrySet().iterator();
    Map.Entry localEntry;
    do
    {
      boolean bool = localIterator.hasNext();
      i = 0;
      if (!bool)
        break;
      localEntry = (Map.Entry)localIterator.next();
    }
    while (!paramString1.equalsIgnoreCase((String)localEntry.getKey()));
    localEntry.setValue(localArrayList);
    int i = 1;
    if (i == 0)
      this.requestProperties.put(paramString1, localArrayList);
    this.connection.setRequestProperty(paramString1, paramString2);
  }

  public void setUseCaches(boolean paramBoolean)
  {
    this.connection.setUseCaches(paramBoolean);
    this.useCaches = paramBoolean;
  }

  public String toString()
  {
    return this.connection.toString();
  }

  public boolean usingProxy()
  {
    return this.connection.usingProxy();
  }

  private static class CacheStream extends OutputStream
  {
    private final OutputStream collector;
    private final OutputStream stream;

    public CacheStream(OutputStream paramOutputStream1, OutputStream paramOutputStream2)
    {
      this.stream = paramOutputStream1;
      this.collector = paramOutputStream2;
    }

    public void close()
      throws IOException
    {
      this.stream.close();
      this.collector.close();
    }

    public void flush()
      throws IOException
    {
      this.stream.flush();
      this.collector.flush();
    }

    public void write(int paramInt)
      throws IOException
    {
      this.stream.write(paramInt);
      this.collector.write(paramInt);
    }

    public void write(byte[] paramArrayOfByte)
      throws IOException
    {
      this.stream.write(paramArrayOfByte);
      this.collector.write(paramArrayOfByte);
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      this.stream.write(paramArrayOfByte, paramInt1, paramInt2);
      this.collector.write(paramArrayOfByte, paramInt1, paramInt2);
    }
  }
}