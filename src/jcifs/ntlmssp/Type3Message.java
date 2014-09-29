package jcifs.ntlmssp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import jcifs.Config;
import jcifs.netbios.NbtAddress;
import jcifs.smb.NtlmPasswordAuthentication;

public class Type3Message extends NtlmMessage
{
  private static final String DEFAULT_DOMAIN;
  private static final int DEFAULT_FLAGS;
  private static final String DEFAULT_PASSWORD;
  private static final String DEFAULT_USER;
  private static final String DEFAULT_WORKSTATION;
  private static final int LM_COMPATIBILITY;
  private static final SecureRandom RANDOM;
  private String domain;
  private byte[] lmResponse;
  private byte[] ntResponse;
  private byte[] sessionKey;
  private String user;
  private String workstation;

  static
  {
    int i = 1;
    RANDOM = new SecureRandom();
    if (Config.getBoolean("jcifs.smb.client.useUnicode", i));
    while (true)
    {
      DEFAULT_FLAGS = i | 0x200;
      DEFAULT_DOMAIN = Config.getProperty("jcifs.smb.client.domain", null);
      DEFAULT_USER = Config.getProperty("jcifs.smb.client.username", null);
      DEFAULT_PASSWORD = Config.getProperty("jcifs.smb.client.password", null);
      try
      {
        String str2 = NbtAddress.getLocalHost().getHostName();
        str1 = str2;
        DEFAULT_WORKSTATION = str1;
        LM_COMPATIBILITY = Config.getInt("jcifs.smb.lmCompatibility", 0);
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

  public Type3Message()
  {
    setFlags(getDefaultFlags());
    setDomain(getDefaultDomain());
    setUser(getDefaultUser());
    setWorkstation(getDefaultWorkstation());
  }

  public Type3Message(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString1, String paramString2, String paramString3)
  {
    setFlags(paramInt);
    setLMResponse(paramArrayOfByte1);
    setNTResponse(paramArrayOfByte2);
    setDomain(paramString1);
    setUser(paramString2);
    setWorkstation(paramString3);
  }

  public Type3Message(Type2Message paramType2Message)
  {
    setFlags(getDefaultFlags(paramType2Message));
    setWorkstation(getDefaultWorkstation());
    String str1 = getDefaultDomain();
    setDomain(str1);
    String str2 = getDefaultUser();
    setUser(str2);
    String str3 = getDefaultPassword();
    switch (LM_COMPATIBILITY)
    {
    default:
      setLMResponse(getLMResponse(paramType2Message, str3));
      setNTResponse(getNTResponse(paramType2Message, str3));
      return;
    case 0:
    case 1:
      setLMResponse(getLMResponse(paramType2Message, str3));
      setNTResponse(getNTResponse(paramType2Message, str3));
      return;
    case 2:
      byte[] arrayOfByte2 = getNTResponse(paramType2Message, str3);
      setLMResponse(arrayOfByte2);
      setNTResponse(arrayOfByte2);
      return;
    case 3:
    case 4:
    case 5:
    }
    byte[] arrayOfByte1 = new byte[8];
    RANDOM.nextBytes(arrayOfByte1);
    setLMResponse(getLMv2Response(paramType2Message, str1, str2, str3, arrayOfByte1));
  }

  public Type3Message(Type2Message paramType2Message, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    setFlags(getDefaultFlags(paramType2Message));
    setDomain(paramString2);
    setUser(paramString3);
    setWorkstation(paramString4);
    switch (LM_COMPATIBILITY)
    {
    default:
      setLMResponse(getLMResponse(paramType2Message, paramString1));
      setNTResponse(getNTResponse(paramType2Message, paramString1));
      return;
    case 0:
    case 1:
      setLMResponse(getLMResponse(paramType2Message, paramString1));
      setNTResponse(getNTResponse(paramType2Message, paramString1));
      return;
    case 2:
      byte[] arrayOfByte2 = getNTResponse(paramType2Message, paramString1);
      setLMResponse(arrayOfByte2);
      setNTResponse(arrayOfByte2);
      return;
    case 3:
    case 4:
    case 5:
    }
    byte[] arrayOfByte1 = new byte[8];
    RANDOM.nextBytes(arrayOfByte1);
    setLMResponse(getLMv2Response(paramType2Message, paramString2, paramString3, paramString1, arrayOfByte1));
  }

  public Type3Message(byte[] paramArrayOfByte)
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

  public static int getDefaultFlags(Type2Message paramType2Message)
  {
    if (paramType2Message == null)
      return DEFAULT_FLAGS;
    if ((0x1 & paramType2Message.getFlags()) != 0);
    for (int i = 1; ; i = 2)
      return 0x200 | i;
  }

  public static String getDefaultPassword()
  {
    return DEFAULT_PASSWORD;
  }

  public static String getDefaultUser()
  {
    return DEFAULT_USER;
  }

  public static String getDefaultWorkstation()
  {
    return DEFAULT_WORKSTATION;
  }

  public static byte[] getLMResponse(Type2Message paramType2Message, String paramString)
  {
    if ((paramType2Message == null) || (paramString == null))
      return null;
    return NtlmPasswordAuthentication.getPreNTLMResponse(paramString, paramType2Message.getChallenge());
  }

  public static byte[] getLMv2Response(Type2Message paramType2Message, String paramString1, String paramString2, String paramString3, byte[] paramArrayOfByte)
  {
    if ((paramType2Message == null) || (paramString1 == null) || (paramString2 == null) || (paramString3 == null) || (paramArrayOfByte == null))
      return null;
    return NtlmPasswordAuthentication.getLMv2Response(paramString1, paramString2, paramString3, paramType2Message.getChallenge(), paramArrayOfByte);
  }

  public static byte[] getNTResponse(Type2Message paramType2Message, String paramString)
  {
    if ((paramType2Message == null) || (paramString == null))
      return null;
    return NtlmPasswordAuthentication.getNTLMResponse(paramString, paramType2Message.getChallenge());
  }

  private void parse(byte[] paramArrayOfByte)
    throws IOException
  {
    for (int i = 0; i < 8; i++)
      if (paramArrayOfByte[i] != NTLMSSP_SIGNATURE[i])
        throw new IOException("Not an NTLMSSP message.");
    if (readULong(paramArrayOfByte, 8) != 3)
      throw new IOException("Not a Type 3 message.");
    byte[] arrayOfByte1 = readSecurityBuffer(paramArrayOfByte, 12);
    int j = readULong(paramArrayOfByte, 16);
    byte[] arrayOfByte2 = readSecurityBuffer(paramArrayOfByte, 20);
    int k = readULong(paramArrayOfByte, 24);
    byte[] arrayOfByte3 = readSecurityBuffer(paramArrayOfByte, 28);
    int m = readULong(paramArrayOfByte, 32);
    byte[] arrayOfByte4 = readSecurityBuffer(paramArrayOfByte, 36);
    int n = readULong(paramArrayOfByte, 40);
    byte[] arrayOfByte5 = readSecurityBuffer(paramArrayOfByte, 44);
    int i1 = readULong(paramArrayOfByte, 48);
    if ((j == 52) || (k == 52) || (m == 52) || (n == 52) || (i1 == 52))
    {
      i2 = 514;
      str = getOEMEncoding();
      setFlags(i2);
      setLMResponse(arrayOfByte1);
      if (arrayOfByte2.length == 24)
        setNTResponse(arrayOfByte2);
      setDomain(new String(arrayOfByte3, str));
      setUser(new String(arrayOfByte4, str));
      setWorkstation(new String(arrayOfByte5, str));
      return;
    }
    setSessionKey(readSecurityBuffer(paramArrayOfByte, 52));
    int i2 = readULong(paramArrayOfByte, 60);
    if ((i2 & 0x1) != 0);
    for (String str = "UnicodeLittleUnmarked"; ; str = getOEMEncoding())
      break;
  }

  public String getDomain()
  {
    return this.domain;
  }

  public byte[] getLMResponse()
  {
    return this.lmResponse;
  }

  public byte[] getNTResponse()
  {
    return this.ntResponse;
  }

  public byte[] getSessionKey()
  {
    return this.sessionKey;
  }

  public String getUser()
  {
    return this.user;
  }

  public String getWorkstation()
  {
    return this.workstation;
  }

  public void setDomain(String paramString)
  {
    this.domain = paramString;
  }

  public void setLMResponse(byte[] paramArrayOfByte)
  {
    this.lmResponse = paramArrayOfByte;
  }

  public void setNTResponse(byte[] paramArrayOfByte)
  {
    this.ntResponse = paramArrayOfByte;
  }

  public void setSessionKey(byte[] paramArrayOfByte)
  {
    this.sessionKey = paramArrayOfByte;
  }

  public void setUser(String paramString)
  {
    this.user = paramString;
  }

  public void setWorkstation(String paramString)
  {
    this.workstation = paramString;
  }

  public byte[] toByteArray()
  {
    while (true)
    {
      int j;
      label375: String str1;
      try
      {
        int i = getFlags();
        if ((i & 0x1) != 0)
        {
          j = 1;
          break label469;
          String str2 = getDomain();
          byte[] arrayOfByte1 = null;
          if (str2 != null)
          {
            int k = str2.length();
            arrayOfByte1 = null;
            if (k != 0)
            {
              if (j == 0)
                continue;
              arrayOfByte1 = str2.toUpperCase().getBytes("UnicodeLittleUnmarked");
            }
          }
          if (arrayOfByte1 == null)
            break label484;
          m = arrayOfByte1.length;
          String str3 = getUser();
          byte[] arrayOfByte2 = null;
          if (str3 != null)
          {
            int n = str3.length();
            arrayOfByte2 = null;
            if (n != 0)
            {
              if (j == 0)
                continue;
              arrayOfByte2 = str3.getBytes("UnicodeLittleUnmarked");
            }
          }
          if (arrayOfByte2 == null)
            break label490;
          i1 = arrayOfByte2.length;
          String str4 = getWorkstation();
          Object localObject = null;
          if (str4 != null)
          {
            int i2 = str4.length();
            localObject = null;
            if (i2 != 0)
            {
              if (j == 0)
                continue;
              localObject = str4.getBytes("UnicodeLittleUnmarked");
            }
          }
          if (localObject != null)
          {
            i3 = localObject.length;
            byte[] arrayOfByte3 = getLMResponse();
            if (arrayOfByte3 == null)
              continue;
            i4 = arrayOfByte3.length;
            byte[] arrayOfByte4 = getNTResponse();
            if (arrayOfByte4 == null)
              continue;
            i5 = arrayOfByte4.length;
            byte[] arrayOfByte5 = getSessionKey();
            if (arrayOfByte5 == null)
              continue;
            i6 = arrayOfByte5.length;
            byte[] arrayOfByte6 = new byte[i6 + (i5 + (i4 + (i3 + (i1 + (m + 64)))))];
            System.arraycopy(NTLMSSP_SIGNATURE, 0, arrayOfByte6, 0, 8);
            writeULong(arrayOfByte6, 8, 3);
            writeSecurityBuffer(arrayOfByte6, 12, 64, arrayOfByte3);
            int i7 = 64 + i4;
            writeSecurityBuffer(arrayOfByte6, 20, i7, arrayOfByte4);
            int i8 = i7 + i5;
            writeSecurityBuffer(arrayOfByte6, 28, i8, arrayOfByte1);
            int i9 = i8 + m;
            writeSecurityBuffer(arrayOfByte6, 36, i9, arrayOfByte2);
            int i10 = i9 + i1;
            writeSecurityBuffer(arrayOfByte6, 44, i10, (byte[])localObject);
            writeSecurityBuffer(arrayOfByte6, 52, i10 + i3, arrayOfByte5);
            writeULong(arrayOfByte6, 60, i);
            return arrayOfByte6;
            str1 = getOEMEncoding();
            continue;
            arrayOfByte1 = str2.toUpperCase().getBytes(str1);
            continue;
            arrayOfByte2 = str3.toUpperCase().getBytes(str1);
            continue;
            byte[] arrayOfByte7 = str4.toUpperCase().getBytes(str1);
            localObject = arrayOfByte7;
            continue;
          }
          int i3 = 0;
          continue;
          int i4 = 0;
          continue;
          int i5 = 0;
          continue;
          int i6 = 0;
          continue;
        }
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException.getMessage());
      }
      while (true)
      {
        label469: if (j == 0)
          break label375;
        str1 = null;
        break;
        j = 0;
      }
      label484: int m = 0;
      continue;
      label490: int i1 = 0;
    }
  }

  public String toString()
  {
    String str1 = getUser();
    String str2 = getDomain();
    String str3 = getWorkstation();
    byte[] arrayOfByte1 = getLMResponse();
    byte[] arrayOfByte2 = getNTResponse();
    byte[] arrayOfByte3 = getSessionKey();
    int i = getFlags();
    StringBuffer localStringBuffer = new StringBuffer();
    if (str2 != null)
      localStringBuffer.append("domain: ").append(str2);
    if (str1 != null)
    {
      if (localStringBuffer.length() > 0)
        localStringBuffer.append("; ");
      localStringBuffer.append("user: ").append(str1);
    }
    if (str3 != null)
    {
      if (localStringBuffer.length() > 0)
        localStringBuffer.append("; ");
      localStringBuffer.append("workstation: ").append(str3);
    }
    if (arrayOfByte1 != null)
    {
      if (localStringBuffer.length() > 0)
        localStringBuffer.append("; ");
      localStringBuffer.append("lmResponse: ");
      localStringBuffer.append("0x");
      for (int m = 0; m < arrayOfByte1.length; m++)
      {
        localStringBuffer.append(Integer.toHexString(0xF & arrayOfByte1[m] >> 4));
        localStringBuffer.append(Integer.toHexString(0xF & arrayOfByte1[m]));
      }
    }
    if (arrayOfByte2 != null)
    {
      if (localStringBuffer.length() > 0)
        localStringBuffer.append("; ");
      localStringBuffer.append("ntResponse: ");
      localStringBuffer.append("0x");
      for (int k = 0; k < arrayOfByte2.length; k++)
      {
        localStringBuffer.append(Integer.toHexString(0xF & arrayOfByte2[k] >> 4));
        localStringBuffer.append(Integer.toHexString(0xF & arrayOfByte2[k]));
      }
    }
    if (arrayOfByte3 != null)
    {
      if (localStringBuffer.length() > 0)
        localStringBuffer.append("; ");
      localStringBuffer.append("sessionKey: ");
      localStringBuffer.append("0x");
      for (int j = 0; j < arrayOfByte3.length; j++)
      {
        localStringBuffer.append(Integer.toHexString(0xF & arrayOfByte3[j] >> 4));
        localStringBuffer.append(Integer.toHexString(0xF & arrayOfByte3[j]));
      }
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