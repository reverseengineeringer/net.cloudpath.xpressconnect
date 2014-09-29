package jcifs.ntlmssp;

import java.io.IOException;
import java.net.UnknownHostException;
import jcifs.Config;
import jcifs.netbios.NbtAddress;

public class Type2Message extends NtlmMessage
{
  private static final String DEFAULT_DOMAIN;
  private static final int DEFAULT_FLAGS;
  private static final byte[] DEFAULT_TARGET_INFORMATION;
  private byte[] challenge;
  private byte[] context;
  private String target;
  private byte[] targetInformation;

  static
  {
    int i;
    if (Config.getBoolean("jcifs.smb.client.useUnicode", true))
      i = 1;
    while (true)
    {
      DEFAULT_FLAGS = i | 0x200;
      DEFAULT_DOMAIN = Config.getProperty("jcifs.smb.client.domain", null);
      Object localObject1 = new byte[0];
      if (DEFAULT_DOMAIN != null);
      try
      {
        byte[] arrayOfByte3 = DEFAULT_DOMAIN.getBytes("UnicodeLittleUnmarked");
        localObject1 = arrayOfByte3;
        label51: int j = localObject1.length;
        Object localObject2 = new byte[0];
        try
        {
          str = NbtAddress.getLocalHost().getHostName();
          if (str == null);
        }
        catch (UnknownHostException localUnknownHostException)
        {
          try
          {
            String str;
            byte[] arrayOfByte2 = str.getBytes("UnicodeLittleUnmarked");
            localObject2 = arrayOfByte2;
            while (true)
            {
              label83: int k = localObject2.length;
              int m;
              if (j > 0)
              {
                m = j + 4;
                label96: if (k <= 0)
                  break label220;
              }
              label220: for (int n = k + 4; ; n = 0)
              {
                byte[] arrayOfByte1 = new byte[4 + (n + m)];
                int i1 = 0;
                if (j > 0)
                {
                  writeUShort(arrayOfByte1, 0, 2);
                  int i3 = 0 + 2;
                  writeUShort(arrayOfByte1, i3, j);
                  System.arraycopy(localObject1, 0, arrayOfByte1, i3 + 2, j);
                  i1 = j + 4;
                }
                if (k > 0)
                {
                  writeUShort(arrayOfByte1, i1, 1);
                  int i2 = i1 + 2;
                  writeUShort(arrayOfByte1, i2, k);
                  System.arraycopy(localObject2, 0, arrayOfByte1, i2 + 2, k);
                }
                DEFAULT_TARGET_INFORMATION = arrayOfByte1;
                return;
                i = 2;
                break;
                m = 0;
                break label96;
              }
              localUnknownHostException = localUnknownHostException;
            }
          }
          catch (IOException localIOException1)
          {
            break label83;
          }
        }
      }
      catch (IOException localIOException2)
      {
        break label51;
      }
    }
  }

  public Type2Message()
  {
    this(getDefaultFlags(), null, null);
  }

  public Type2Message(int paramInt, byte[] paramArrayOfByte, String paramString)
  {
    setFlags(paramInt);
    setChallenge(paramArrayOfByte);
    setTarget(paramString);
    if (paramString != null)
      setTargetInformation(getDefaultTargetInformation());
  }

  public Type2Message(Type1Message paramType1Message)
  {
    this(paramType1Message, null, null);
  }

  public Type2Message(Type1Message paramType1Message, byte[] paramArrayOfByte, String paramString)
  {
    this(i, paramArrayOfByte, paramString);
  }

  public Type2Message(byte[] paramArrayOfByte)
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

  public static int getDefaultFlags(Type1Message paramType1Message)
  {
    int k;
    if (paramType1Message == null)
    {
      k = DEFAULT_FLAGS;
      return k;
    }
    int i = paramType1Message.getFlags();
    if ((i & 0x1) != 0);
    for (int j = 1; ; j = 2)
    {
      k = 0x200 | j;
      if (((i & 0x4) == 0) || (getDefaultDomain() == null))
        break;
      return k | 0x10004;
    }
  }

  public static byte[] getDefaultTargetInformation()
  {
    return DEFAULT_TARGET_INFORMATION;
  }

  private void parse(byte[] paramArrayOfByte)
    throws IOException
  {
    for (int i = 0; i < 8; i++)
      if (paramArrayOfByte[i] != NTLMSSP_SIGNATURE[i])
        throw new IOException("Not an NTLMSSP message.");
    if (readULong(paramArrayOfByte, 8) != 2)
      throw new IOException("Not a Type 2 message.");
    int j = readULong(paramArrayOfByte, 20);
    setFlags(j);
    byte[] arrayOfByte1 = readSecurityBuffer(paramArrayOfByte, 12);
    int k = arrayOfByte1.length;
    String str1 = null;
    String str2;
    if (k != 0)
    {
      if ((j & 0x1) != 0)
      {
        str2 = "UnicodeLittleUnmarked";
        str1 = new String(arrayOfByte1, str2);
      }
    }
    else
      setTarget(str1);
    int n;
    for (int m = 24; ; m++)
      if (m < 32)
      {
        if (paramArrayOfByte[m] != 0)
        {
          byte[] arrayOfByte4 = new byte[8];
          System.arraycopy(paramArrayOfByte, 24, arrayOfByte4, 0, 8);
          setChallenge(arrayOfByte4);
        }
      }
      else
      {
        n = readULong(paramArrayOfByte, 16);
        if ((n != 32) && (paramArrayOfByte.length != 32))
          break label195;
        return;
        str2 = getOEMEncoding();
        break;
      }
    label195: for (int i1 = 32; ; i1++)
      if (i1 < 40)
      {
        if (paramArrayOfByte[i1] != 0)
        {
          byte[] arrayOfByte3 = new byte[8];
          System.arraycopy(paramArrayOfByte, 32, arrayOfByte3, 0, 8);
          setContext(arrayOfByte3);
        }
      }
      else
      {
        if ((n == 40) || (paramArrayOfByte.length == 40))
          break;
        byte[] arrayOfByte2 = readSecurityBuffer(paramArrayOfByte, 40);
        if (arrayOfByte2.length == 0)
          break;
        setTargetInformation(arrayOfByte2);
        return;
      }
  }

  public byte[] getChallenge()
  {
    return this.challenge;
  }

  public byte[] getContext()
  {
    return this.context;
  }

  public String getTarget()
  {
    return this.target;
  }

  public byte[] getTargetInformation()
  {
    return this.targetInformation;
  }

  public void setChallenge(byte[] paramArrayOfByte)
  {
    this.challenge = paramArrayOfByte;
  }

  public void setContext(byte[] paramArrayOfByte)
  {
    this.context = paramArrayOfByte;
  }

  public void setTarget(String paramString)
  {
    this.target = paramString;
  }

  public void setTargetInformation(byte[] paramArrayOfByte)
  {
    this.targetInformation = paramArrayOfByte;
  }

  public byte[] toByteArray()
  {
    while (true)
    {
      byte[] arrayOfByte2;
      byte[] arrayOfByte3;
      int i;
      byte[] arrayOfByte5;
      try
      {
        String str = getTarget();
        byte[] arrayOfByte1 = getChallenge();
        arrayOfByte2 = getContext();
        arrayOfByte3 = getTargetInformation();
        i = getFlags();
        byte[] arrayOfByte4 = new byte[0];
        if ((0x70000 & i) != 0)
        {
          if ((str == null) || (str.length() == 0))
            break label280;
          if ((i & 0x1) != 0)
            arrayOfByte4 = str.getBytes("UnicodeLittleUnmarked");
        }
        else
        {
          if (arrayOfByte3 == null)
            break label254;
          i ^= 8388608;
          if (arrayOfByte2 != null)
            break label254;
          arrayOfByte2 = new byte[8];
          break label254;
          int k = j + arrayOfByte4.length;
          int m = 0;
          if (arrayOfByte3 != null)
            m = arrayOfByte3.length;
          arrayOfByte5 = new byte[m + k];
          System.arraycopy(NTLMSSP_SIGNATURE, 0, arrayOfByte5, 0, 8);
          writeULong(arrayOfByte5, 8, 2);
          writeSecurityBuffer(arrayOfByte5, 12, j, arrayOfByte4);
          writeULong(arrayOfByte5, 20, i);
          if (arrayOfByte1 == null)
            continue;
          System.arraycopy(arrayOfByte1, 0, arrayOfByte5, 24, 8);
          if (arrayOfByte2 != null)
            System.arraycopy(arrayOfByte2, 0, arrayOfByte5, 32, 8);
          if (arrayOfByte3 == null)
            break label277;
          writeSecurityBuffer(arrayOfByte5, 40, j + arrayOfByte4.length, arrayOfByte3);
          return arrayOfByte5;
        }
        arrayOfByte4 = str.toUpperCase().getBytes(getOEMEncoding());
        continue;
        arrayOfByte1 = new byte[8];
        continue;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException.getMessage());
      }
      label254: int j = 32;
      if (arrayOfByte2 != null)
        j += 8;
      if (arrayOfByte3 != null)
      {
        j += 8;
        continue;
        label277: return arrayOfByte5;
        label280: i &= -458753;
      }
    }
  }

  public String toString()
  {
    String str = getTarget();
    byte[] arrayOfByte1 = getChallenge();
    byte[] arrayOfByte2 = getContext();
    byte[] arrayOfByte3 = getTargetInformation();
    int i = getFlags();
    StringBuffer localStringBuffer = new StringBuffer();
    if (str != null)
      localStringBuffer.append("target: ").append(str);
    if (arrayOfByte1 != null)
    {
      if (localStringBuffer.length() > 0)
        localStringBuffer.append("; ");
      localStringBuffer.append("challenge: ");
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
      localStringBuffer.append("context: ");
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
      localStringBuffer.append("targetInformation: ");
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