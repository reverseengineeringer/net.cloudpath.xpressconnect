package org.xbill.DNS;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class ReverseMap
{
  private static Name inaddr4 = Name.fromConstantString("in-addr.arpa.");
  private static Name inaddr6 = Name.fromConstantString("ip6.arpa.");

  public static Name fromAddress(String paramString)
    throws UnknownHostException
  {
    byte[] arrayOfByte = Address.toByteArray(paramString, 1);
    if (arrayOfByte == null)
      arrayOfByte = Address.toByteArray(paramString, 2);
    if (arrayOfByte == null)
      throw new UnknownHostException("Invalid IP address");
    return fromAddress(arrayOfByte);
  }

  public static Name fromAddress(String paramString, int paramInt)
    throws UnknownHostException
  {
    byte[] arrayOfByte = Address.toByteArray(paramString, paramInt);
    if (arrayOfByte == null)
      throw new UnknownHostException("Invalid IP address");
    return fromAddress(arrayOfByte);
  }

  public static Name fromAddress(InetAddress paramInetAddress)
  {
    return fromAddress(paramInetAddress.getAddress());
  }

  public static Name fromAddress(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte.length != 4) && (paramArrayOfByte.length != 16))
      throw new IllegalArgumentException("array must contain 4 or 16 elements");
    StringBuffer localStringBuffer = new StringBuffer();
    if (paramArrayOfByte.length == 4)
      for (int k = -1 + paramArrayOfByte.length; k >= 0; k--)
      {
        localStringBuffer.append(0xFF & paramArrayOfByte[k]);
        if (k > 0)
          localStringBuffer.append(".");
      }
    int[] arrayOfInt = new int[2];
    for (int i = -1 + paramArrayOfByte.length; i >= 0; i--)
    {
      arrayOfInt[0] = ((0xFF & paramArrayOfByte[i]) >> 4);
      arrayOfInt[1] = (0xF & (0xFF & paramArrayOfByte[i]));
      for (int j = -1 + arrayOfInt.length; j >= 0; j--)
      {
        localStringBuffer.append(Integer.toHexString(arrayOfInt[j]));
        if ((i > 0) || (j > 0))
          localStringBuffer.append(".");
      }
    }
    try
    {
      if (paramArrayOfByte.length == 4)
        return Name.fromString(localStringBuffer.toString(), inaddr4);
      Name localName = Name.fromString(localStringBuffer.toString(), inaddr6);
      return localName;
    }
    catch (TextParseException localTextParseException)
    {
    }
    throw new IllegalStateException("name cannot be invalid");
  }

  public static Name fromAddress(int[] paramArrayOfInt)
  {
    byte[] arrayOfByte = new byte[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      if ((paramArrayOfInt[i] < 0) || (paramArrayOfInt[i] > 255))
        throw new IllegalArgumentException("array must contain values between 0 and 255");
      arrayOfByte[i] = ((byte)paramArrayOfInt[i]);
    }
    return fromAddress(arrayOfByte);
  }
}