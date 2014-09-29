package jcifs.ntlmssp;

import java.io.IOException;
import java.net.UnknownHostException;
import jcifs.Config;
import jcifs.netbios.NbtAddress;

public class Type1Message extends NtlmMessage
{
  private static final String DEFAULT_DOMAIN;
  private static final int DEFAULT_FLAGS;
  private static final String DEFAULT_WORKSTATION;
  private String suppliedDomain;
  private String suppliedWorkstation;

  static
  {
    int i = 1;
    if (Config.getBoolean("jcifs.smb.client.useUnicode", i));
    while (true)
    {
      DEFAULT_FLAGS = i | 0x200;
      DEFAULT_DOMAIN = Config.getProperty("jcifs.smb.client.domain", null);
      try
      {
        String str2 = NbtAddress.getLocalHost().getHostName();
        str1 = str2;
        DEFAULT_WORKSTATION = str1;
        return;
        int j = 2;
      }
      catch (UnknownHostException localUnknownHostException)
      {
        while (true)
          String str1 = null;
      }
    }
  }

  public Type1Message()
  {
    this(getDefaultFlags(), getDefaultDomain(), getDefaultWorkstation());
  }

  public Type1Message(int paramInt, String paramString1, String paramString2)
  {
    setFlags(paramInt);
    setSuppliedDomain(paramString1);
    setSuppliedWorkstation(paramString2);
  }

  public Type1Message(byte[] paramArrayOfByte)
    throws IOException
  {
    parse(paramArrayOfByte);
  }

  public static String getDefaultDomain()
  {
    return DEFAULT_DOMAIN;
  }

  public static int getDefaultFlags()
  {
    return DEFAULT_FLAGS;
  }

  public static String getDefaultWorkstation()
  {
    return DEFAULT_WORKSTATION;
  }

  private void parse(byte[] paramArrayOfByte)
    throws IOException
  {
    for (int i = 0; i < 8; i++)
      if (paramArrayOfByte[i] != NTLMSSP_SIGNATURE[i])
        throw new IOException("Not an NTLMSSP message.");
    if (readULong(paramArrayOfByte, 8) != 1)
      throw new IOException("Not a Type 1 message.");
    int j = readULong(paramArrayOfByte, 12);
    int k = j & 0x1000;
    String str1 = null;
    if (k != 0)
      str1 = new String(readSecurityBuffer(paramArrayOfByte, 16), getOEMEncoding());
    int m = j & 0x2000;
    String str2 = null;
    if (m != 0)
      str2 = new String(readSecurityBuffer(paramArrayOfByte, 24), getOEMEncoding());
    setFlags(j);
    setSuppliedDomain(str1);
    setSuppliedWorkstation(str2);
  }

  public String getSuppliedDomain()
  {
    return this.suppliedDomain;
  }

  public String getSuppliedWorkstation()
  {
    return this.suppliedWorkstation;
  }

  public void setSuppliedDomain(String paramString)
  {
    this.suppliedDomain = paramString;
  }

  public void setSuppliedWorkstation(String paramString)
  {
    this.suppliedWorkstation = paramString;
  }

  public byte[] toByteArray()
  {
    int i = 16;
    try
    {
      String str1 = getSuppliedDomain();
      String str2 = getSuppliedWorkstation();
      int j = getFlags();
      byte[] arrayOfByte1 = new byte[0];
      int k;
      int m;
      byte[] arrayOfByte2;
      int n;
      if ((str1 != null) && (str1.length() != 0))
      {
        k = 1;
        m = j | 0x1000;
        arrayOfByte1 = str1.toUpperCase().getBytes(getOEMEncoding());
        arrayOfByte2 = new byte[0];
        if ((str2 == null) || (str2.length() == 0))
          break label199;
        k = 1;
        n = m | 0x2000;
        arrayOfByte2 = str2.toUpperCase().getBytes(getOEMEncoding());
      }
      while (true)
      {
        if (k != 0)
          i = 32 + arrayOfByte1.length + arrayOfByte2.length;
        byte[] arrayOfByte3 = new byte[i];
        System.arraycopy(NTLMSSP_SIGNATURE, 0, arrayOfByte3, 0, 8);
        writeULong(arrayOfByte3, 8, 1);
        writeULong(arrayOfByte3, 12, n);
        if (k != 0)
        {
          writeSecurityBuffer(arrayOfByte3, 16, 32, arrayOfByte1);
          writeSecurityBuffer(arrayOfByte3, 24, 32 + arrayOfByte1.length, arrayOfByte2);
        }
        return arrayOfByte3;
        m = j & 0xFFFFEFFF;
        k = 0;
        break;
        label199: n = m & 0xFFFFDFFF;
      }
    }
    catch (IOException localIOException)
    {
      throw new IllegalStateException(localIOException.getMessage());
    }
  }

  public String toString()
  {
    String str1 = getSuppliedDomain();
    String str2 = getSuppliedWorkstation();
    int i = getFlags();
    StringBuffer localStringBuffer = new StringBuffer();
    if (str1 != null)
      localStringBuffer.append("suppliedDomain: ").append(str1);
    if (str2 != null)
    {
      if (localStringBuffer.length() > 0)
        localStringBuffer.append("; ");
      localStringBuffer.append("suppliedWorkstation: ").append(str2);
    }
    if (i != 0)
    {
      if (localStringBuffer.length() > 0)
        localStringBuffer.append("; ");
      localStringBuffer.append("flags: ");
      localStringBuffer.append("0x");
      localStringBuffer.append(Integer.toHexString(0xF & i >> 28));
      localStringBuffer.append(Integer.toHexString(0xF & i >> 24));
      localStringBuffer.append(Integer.toHexString(0xF & i >> 20));
      localStringBuffer.append(Integer.toHexString(0xF & i >> 16));
      localStringBuffer.append(Integer.toHexString(0xF & i >> 12));
      localStringBuffer.append(Integer.toHexString(0xF & i >> 8));
      localStringBuffer.append(Integer.toHexString(0xF & i >> 4));
      localStringBuffer.append(Integer.toHexString(i & 0xF));
    }
    return localStringBuffer.toString();
  }
}