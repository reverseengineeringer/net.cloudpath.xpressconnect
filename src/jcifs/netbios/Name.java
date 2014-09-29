package jcifs.netbios;

import java.io.UnsupportedEncodingException;
import jcifs.Config;
import jcifs.util.Hexdump;

public class Name
{
  private static final String DEFAULT_SCOPE = Config.getProperty("jcifs.netbios.scope");
  static final String OEM_ENCODING = Config.getProperty("jcifs.encoding", System.getProperty("file.encoding"));
  private static final int SCOPE_OFFSET = 33;
  private static final int TYPE_OFFSET = 31;
  public int hexCode;
  public String name;
  public String scope;
  int srcHashCode;

  Name()
  {
  }

  public Name(String paramString1, int paramInt, String paramString2)
  {
    if (paramString1.length() > 15)
      paramString1 = paramString1.substring(0, 15);
    this.name = paramString1.toUpperCase();
    this.hexCode = paramInt;
    if ((paramString2 != null) && (paramString2.length() > 0));
    while (true)
    {
      this.scope = paramString2;
      this.srcHashCode = 0;
      return;
      paramString2 = DEFAULT_SCOPE;
    }
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (!(paramObject instanceof Name))
      bool = false;
    Name localName;
    do
    {
      do
      {
        return bool;
        localName = (Name)paramObject;
        if ((this.scope != null) || (localName.scope != null))
          break;
      }
      while ((this.name.equals(localName.name)) && (this.hexCode == localName.hexCode));
      return false;
    }
    while ((this.name.equals(localName.name)) && (this.hexCode == localName.hexCode) && (this.scope.equals(localName.scope)));
    return false;
  }

  public int hashCode()
  {
    int i = this.name.hashCode() + 65599 * this.hexCode + 65599 * this.srcHashCode;
    if ((this.scope != null) && (this.scope.length() != 0))
      i += this.scope.hashCode();
    return i;
  }

  int readScopeWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    int j = 0xFF & paramArrayOfByte[paramInt];
    if (j == 0)
    {
      this.scope = null;
      return 1;
    }
    try
    {
      localStringBuffer = new StringBuffer(new String(paramArrayOfByte, i, j, OEM_ENCODING));
      k = i + j;
      m = k + 1;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException2)
    {
      try
      {
        StringBuffer localStringBuffer;
        int m;
        while (true)
        {
          int n = 0xFF & paramArrayOfByte[k];
          if (n == 0)
            break;
          localStringBuffer.append('.').append(new String(paramArrayOfByte, m, n, OEM_ENCODING));
          int k = m + n;
        }
        this.scope = localStringBuffer.toString();
        while (true)
        {
          label120: return m - paramInt;
          localUnsupportedEncodingException2 = localUnsupportedEncodingException2;
          m = i;
        }
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException1)
      {
        break label120;
      }
    }
  }

  int readWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = new byte[33];
    int i = 15;
    for (int j = 0; j < 15; j++)
    {
      arrayOfByte[j] = ((byte)(-65 + (0xFF & paramArrayOfByte[(paramInt + (1 + j * 2))]) << 4));
      arrayOfByte[j] = ((byte)(arrayOfByte[j] | (byte)(0xF & -65 + (0xFF & paramArrayOfByte[(paramInt + (2 + j * 2))]))));
      if (arrayOfByte[j] != 32)
        i = j + 1;
    }
    try
    {
      this.name = new String(arrayOfByte, 0, i, OEM_ENCODING);
      label113: this.hexCode = (-65 + (0xFF & paramArrayOfByte[(paramInt + 31)]) << 4);
      this.hexCode |= 0xF & -65 + (0xFF & paramArrayOfByte[(1 + (paramInt + 31))]);
      return 33 + readScopeWireFormat(paramArrayOfByte, paramInt + 33);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      break label113;
    }
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = this.name;
    if (str == null)
      str = "null";
    while (true)
    {
      localStringBuffer.append(str).append("<").append(Hexdump.toHexString(this.hexCode, 2)).append(">");
      if (this.scope != null)
        localStringBuffer.append(".").append(this.scope);
      return localStringBuffer.toString();
      if (str.charAt(0) == '\001')
      {
        char[] arrayOfChar = str.toCharArray();
        arrayOfChar[0] = '.';
        arrayOfChar[1] = '.';
        arrayOfChar[14] = '.';
        str = new String(arrayOfChar);
      }
    }
  }

  int writeScopeWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    if (this.scope == null)
    {
      paramArrayOfByte[paramInt] = 0;
      return 1;
    }
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = 46;
    try
    {
      System.arraycopy(this.scope.getBytes(OEM_ENCODING), 0, paramArrayOfByte, i, this.scope.length());
      int j = i + this.scope.length();
      int k = j + 1;
      paramArrayOfByte[j] = 0;
      m = k - 2;
      int n = m - this.scope.length();
      int i1 = 0;
      if (paramArrayOfByte[m] == 46)
        paramArrayOfByte[m] = ((byte)i1);
      for (i1 = 0; ; i1++)
      {
        i2 = m - 1;
        if (m > n)
          break;
        return 2 + this.scope.length();
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      while (true)
      {
        int i2;
        continue;
        int m = i2;
      }
    }
  }

  int writeWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = 32;
    try
    {
      byte[] arrayOfByte = this.name.getBytes(OEM_ENCODING);
      for (int i = 0; i < arrayOfByte.length; i++)
      {
        paramArrayOfByte[(paramInt + (1 + i * 2))] = ((byte)(65 + ((0xF0 & arrayOfByte[i]) >> 4)));
        paramArrayOfByte[(paramInt + (2 + i * 2))] = ((byte)(65 + (0xF & arrayOfByte[i])));
      }
      while (i < 15)
      {
        paramArrayOfByte[(paramInt + (1 + i * 2))] = 67;
        paramArrayOfByte[(paramInt + (2 + i * 2))] = 65;
        i++;
      }
      paramArrayOfByte[(paramInt + 31)] = ((byte)(65 + ((0xF0 & this.hexCode) >> 4)));
      paramArrayOfByte[(1 + (paramInt + 31))] = ((byte)(65 + (0xF & this.hexCode)));
      label157: return 33 + writeScopeWireFormat(paramArrayOfByte, paramInt + 33);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      break label157;
    }
  }
}