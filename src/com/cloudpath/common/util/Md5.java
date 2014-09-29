package com.cloudpath.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5
{
  private static final char[] HEX_CHARS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
  private static MessageDigest MESSAGE_DIGEST = null;

  private static byte[] doCalculateHash(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = paramArrayOfByte;
    MessageDigest localMessageDigest = getInstance();
    int i = 0;
    while (true)
    {
      if (i < paramInt);
      try
      {
        localMessageDigest.update(arrayOfByte, 0, arrayOfByte.length);
        arrayOfByte = localMessageDigest.digest();
        i++;
        continue;
        return arrayOfByte;
      }
      finally
      {
      }
    }
  }

  private static String getHexStringFromBytes(byte[] paramArrayOfByte)
  {
    String str = "";
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      int j = (0xFF & paramArrayOfByte[i]) / 16;
      int k = (0xFF & paramArrayOfByte[i]) % 16;
      str = str + HEX_CHARS[j] + "" + HEX_CHARS[k];
    }
    return str;
  }

  private static MessageDigest getInstance()
  {
    if (MESSAGE_DIGEST == null);
    try
    {
      MESSAGE_DIGEST = MessageDigest.getInstance("MD5");
      return MESSAGE_DIGEST;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new RuntimeException(localNoSuchAlgorithmException);
    }
  }

  public static String md5(String paramString)
  {
    if (paramString == null)
      paramString = "";
    return getHexStringFromBytes(doCalculateHash(paramString.getBytes(), 1));
  }
}