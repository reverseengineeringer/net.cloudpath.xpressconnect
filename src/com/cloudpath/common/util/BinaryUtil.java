package com.cloudpath.common.util;

public class BinaryUtil
{
  public static String convertFromSpacelessHex(String paramString)
  {
    String str = "";
    int i = 0;
    if (i < paramString.length())
    {
      char c1 = paramString.charAt(i);
      if (paramString.length() > i + 1);
      for (char c2 = paramString.charAt(i + 1); ; c2 = '\000')
      {
        char c3 = (char)(int)fromHex("" + c1, "" + c2);
        str = str + c3;
        i += 2;
        break;
      }
    }
    return str;
  }

  public static final String doXor(String paramString1, String paramString2)
  {
    byte[] arrayOfByte1 = paramString1.getBytes();
    byte[] arrayOfByte2 = paramString2.getBytes();
    int i;
    byte[] arrayOfByte3;
    int j;
    if (arrayOfByte1.length > arrayOfByte2.length)
    {
      i = arrayOfByte1.length;
      arrayOfByte3 = new byte[i];
      j = 0;
      label30: if (j >= arrayOfByte3.length)
        break label110;
      if (j < arrayOfByte1.length)
        break label70;
      arrayOfByte3[j] = ((byte)(0x0 ^ arrayOfByte2[j]));
    }
    while (true)
    {
      j++;
      break label30;
      i = arrayOfByte2.length;
      break;
      label70: if (j >= arrayOfByte2.length)
        arrayOfByte3[j] = ((byte)(0x0 ^ arrayOfByte1[j]));
      else
        arrayOfByte3[j] = ((byte)(arrayOfByte1[j] ^ arrayOfByte2[j]));
    }
    label110: return new String(arrayOfByte3);
  }

  public static int fromHex(String paramString1, String paramString2)
  {
    try
    {
      int i = 16 * Integer.parseInt(paramString1, 16);
      int j = Integer.parseInt(paramString2, 16);
      return i + j;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return 0;
  }
}