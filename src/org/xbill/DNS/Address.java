package org.xbill.DNS;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Address
{
  public static final int IPv4 = 1;
  public static final int IPv6 = 2;

  private static InetAddress addrFromRecord(String paramString, Record paramRecord)
    throws UnknownHostException
  {
    return InetAddress.getByAddress(paramString, ((ARecord)paramRecord).getAddress().getAddress());
  }

  public static int addressLength(int paramInt)
  {
    if (paramInt == 1)
      return 4;
    if (paramInt == 2)
      return 16;
    throw new IllegalArgumentException("unknown address family");
  }

  public static int familyOf(InetAddress paramInetAddress)
  {
    if ((paramInetAddress instanceof Inet4Address))
      return 1;
    if ((paramInetAddress instanceof Inet6Address))
      return 2;
    throw new IllegalArgumentException("unknown address family");
  }

  public static InetAddress[] getAllByName(String paramString)
    throws UnknownHostException
  {
    InetAddress[] arrayOfInetAddress;
    Record[] arrayOfRecord;
    int i;
    try
    {
      InetAddress localInetAddress = getByAddress(paramString);
      arrayOfInetAddress = new InetAddress[1];
      arrayOfInetAddress[0] = localInetAddress;
      return arrayOfInetAddress;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      arrayOfRecord = lookupHostName(paramString);
      arrayOfInetAddress = new InetAddress[arrayOfRecord.length];
      i = 0;
    }
    while (i < arrayOfRecord.length)
    {
      arrayOfInetAddress[i] = addrFromRecord(paramString, arrayOfRecord[i]);
      i++;
    }
  }

  public static InetAddress getByAddress(String paramString)
    throws UnknownHostException
  {
    byte[] arrayOfByte1 = toByteArray(paramString, 1);
    if (arrayOfByte1 != null)
      return InetAddress.getByAddress(arrayOfByte1);
    byte[] arrayOfByte2 = toByteArray(paramString, 2);
    if (arrayOfByte2 != null)
      return InetAddress.getByAddress(arrayOfByte2);
    throw new UnknownHostException("Invalid address: " + paramString);
  }

  public static InetAddress getByAddress(String paramString, int paramInt)
    throws UnknownHostException
  {
    if ((paramInt != 1) && (paramInt != 2))
      throw new IllegalArgumentException("unknown address family");
    byte[] arrayOfByte = toByteArray(paramString, paramInt);
    if (arrayOfByte != null)
      return InetAddress.getByAddress(arrayOfByte);
    throw new UnknownHostException("Invalid address: " + paramString);
  }

  public static InetAddress getByName(String paramString)
    throws UnknownHostException
  {
    try
    {
      InetAddress localInetAddress = getByAddress(paramString);
      return localInetAddress;
    }
    catch (UnknownHostException localUnknownHostException)
    {
    }
    return addrFromRecord(paramString, lookupHostName(paramString)[0]);
  }

  public static String getHostName(InetAddress paramInetAddress)
    throws UnknownHostException
  {
    Record[] arrayOfRecord = new Lookup(ReverseMap.fromAddress(paramInetAddress), 12).run();
    if (arrayOfRecord == null)
      throw new UnknownHostException("unknown address");
    return ((PTRRecord)arrayOfRecord[0]).getTarget().toString();
  }

  public static boolean isDottedQuad(String paramString)
  {
    return toByteArray(paramString, 1) != null;
  }

  private static Record[] lookupHostName(String paramString)
    throws UnknownHostException
  {
    Record[] arrayOfRecord;
    try
    {
      arrayOfRecord = new Lookup(paramString).run();
      if (arrayOfRecord == null)
        throw new UnknownHostException("unknown host");
    }
    catch (TextParseException localTextParseException)
    {
      throw new UnknownHostException("invalid name");
    }
    return arrayOfRecord;
  }

  private static byte[] parseV4(String paramString)
  {
    byte[] arrayOfByte = new byte[4];
    int i = paramString.length();
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i2;
    if (m < i)
    {
      int i1 = paramString.charAt(m);
      if ((i1 >= 48) && (i1 <= 57))
      {
        if (k == 3)
          return null;
        if ((k > 0) && (j == 0))
          return null;
        k++;
        j = j * 10 + (i1 - 48);
        if (j <= 255)
          break label164;
        return null;
      }
      if (i1 == 46)
      {
        if (n == 3)
          return null;
        if (k == 0)
          return null;
        i2 = n + 1;
        arrayOfByte[n] = ((byte)j);
        j = 0;
        k = 0;
      }
    }
    while (true)
    {
      m++;
      n = i2;
      break;
      return null;
      if (n != 3)
        return null;
      if (k == 0)
        return null;
      arrayOfByte[n] = ((byte)j);
      return arrayOfByte;
      label164: i2 = n;
    }
  }

  // ERROR //
  private static byte[] parseV6(String paramString)
  {
    // Byte code:
    //   0: iconst_m1
    //   1: istore_1
    //   2: bipush 16
    //   4: newarray byte
    //   6: astore_2
    //   7: aload_0
    //   8: ldc 135
    //   10: iconst_m1
    //   11: invokevirtual 139	java/lang/String:split	(Ljava/lang/String;I)[Ljava/lang/String;
    //   14: astore_3
    //   15: iconst_m1
    //   16: aload_3
    //   17: arraylength
    //   18: iadd
    //   19: istore 4
    //   21: aload_3
    //   22: iconst_0
    //   23: aaload
    //   24: invokevirtual 126	java/lang/String:length	()I
    //   27: istore 5
    //   29: iconst_0
    //   30: istore 6
    //   32: iload 5
    //   34: ifne +24 -> 58
    //   37: iload 4
    //   39: iconst_0
    //   40: isub
    //   41: ifle +66 -> 107
    //   44: aload_3
    //   45: iconst_1
    //   46: aaload
    //   47: invokevirtual 126	java/lang/String:length	()I
    //   50: ifne +57 -> 107
    //   53: iconst_0
    //   54: iconst_1
    //   55: iadd
    //   56: istore 6
    //   58: aload_3
    //   59: iload 4
    //   61: aaload
    //   62: invokevirtual 126	java/lang/String:length	()I
    //   65: ifne +26 -> 91
    //   68: iload 4
    //   70: iload 6
    //   72: isub
    //   73: ifle +36 -> 109
    //   76: aload_3
    //   77: iload 4
    //   79: iconst_1
    //   80: isub
    //   81: aaload
    //   82: invokevirtual 126	java/lang/String:length	()I
    //   85: ifne +24 -> 109
    //   88: iinc 4 255
    //   91: iconst_1
    //   92: iload 4
    //   94: iload 6
    //   96: isub
    //   97: iadd
    //   98: bipush 8
    //   100: if_icmple +11 -> 111
    //   103: aconst_null
    //   104: astore_2
    //   105: aload_2
    //   106: areturn
    //   107: aconst_null
    //   108: areturn
    //   109: aconst_null
    //   110: areturn
    //   111: iload 6
    //   113: istore 7
    //   115: iconst_0
    //   116: istore 8
    //   118: iload 7
    //   120: iload 4
    //   122: if_icmpgt +301 -> 423
    //   125: aload_3
    //   126: iload 7
    //   128: aaload
    //   129: invokevirtual 126	java/lang/String:length	()I
    //   132: ifne +26 -> 158
    //   135: iload_1
    //   136: iflt +5 -> 141
    //   139: aconst_null
    //   140: areturn
    //   141: iload 8
    //   143: istore_1
    //   144: iload 8
    //   146: istore 20
    //   148: iinc 7 1
    //   151: iload 20
    //   153: istore 8
    //   155: goto -37 -> 118
    //   158: aload_3
    //   159: iload 7
    //   161: aaload
    //   162: bipush 46
    //   164: invokevirtual 142	java/lang/String:indexOf	(I)I
    //   167: iflt +89 -> 256
    //   170: iload 7
    //   172: iload 4
    //   174: if_icmpge +5 -> 179
    //   177: aconst_null
    //   178: areturn
    //   179: iload 7
    //   181: bipush 6
    //   183: if_icmple +5 -> 188
    //   186: aconst_null
    //   187: areturn
    //   188: aload_3
    //   189: iload 7
    //   191: aaload
    //   192: iconst_1
    //   193: invokestatic 62	org/xbill/DNS/Address:toByteArray	(Ljava/lang/String;I)[B
    //   196: astore 21
    //   198: aload 21
    //   200: ifnonnull +5 -> 205
    //   203: aconst_null
    //   204: areturn
    //   205: iconst_0
    //   206: istore 22
    //   208: iload 22
    //   210: iconst_4
    //   211: if_icmpge +28 -> 239
    //   214: iload 8
    //   216: iconst_1
    //   217: iadd
    //   218: istore 23
    //   220: aload_2
    //   221: iload 8
    //   223: aload 21
    //   225: iload 22
    //   227: baload
    //   228: bastore
    //   229: iinc 22 1
    //   232: iload 23
    //   234: istore 8
    //   236: goto -28 -> 208
    //   239: iload 8
    //   241: istore 9
    //   243: iload 9
    //   245: bipush 16
    //   247: if_icmpge +123 -> 370
    //   250: iload_1
    //   251: ifge +119 -> 370
    //   254: aconst_null
    //   255: areturn
    //   256: iconst_0
    //   257: istore 12
    //   259: iload 12
    //   261: aload_3
    //   262: iload 7
    //   264: aaload
    //   265: invokevirtual 126	java/lang/String:length	()I
    //   268: if_icmpge +22 -> 290
    //   271: aload_3
    //   272: iload 7
    //   274: aaload
    //   275: iload 12
    //   277: invokevirtual 130	java/lang/String:charAt	(I)C
    //   280: bipush 16
    //   282: invokestatic 148	java/lang/Character:digit	(CI)I
    //   285: ifge +145 -> 430
    //   288: aconst_null
    //   289: areturn
    //   290: aload_3
    //   291: iload 7
    //   293: aaload
    //   294: bipush 16
    //   296: invokestatic 154	java/lang/Integer:parseInt	(Ljava/lang/String;I)I
    //   299: istore 15
    //   301: iload 15
    //   303: ldc 155
    //   305: if_icmpgt +8 -> 313
    //   308: iload 15
    //   310: ifge +5 -> 315
    //   313: aconst_null
    //   314: areturn
    //   315: iload 8
    //   317: iconst_1
    //   318: iadd
    //   319: istore 16
    //   321: iload 15
    //   323: bipush 8
    //   325: iushr
    //   326: i2b
    //   327: istore 17
    //   329: aload_2
    //   330: iload 8
    //   332: iload 17
    //   334: bastore
    //   335: iload 16
    //   337: iconst_1
    //   338: iadd
    //   339: istore 8
    //   341: iload 15
    //   343: sipush 255
    //   346: iand
    //   347: i2b
    //   348: istore 19
    //   350: aload_2
    //   351: iload 16
    //   353: iload 19
    //   355: bastore
    //   356: iload 8
    //   358: istore 20
    //   360: goto -212 -> 148
    //   363: astore 13
    //   365: iload 8
    //   367: pop
    //   368: aconst_null
    //   369: areturn
    //   370: iload_1
    //   371: iflt -266 -> 105
    //   374: bipush 16
    //   376: iload 9
    //   378: isub
    //   379: istore 10
    //   381: aload_2
    //   382: iload_1
    //   383: aload_2
    //   384: iload_1
    //   385: iload 10
    //   387: iadd
    //   388: iload 9
    //   390: iload_1
    //   391: isub
    //   392: invokestatic 161	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   395: iload_1
    //   396: istore 11
    //   398: iload 11
    //   400: iload_1
    //   401: iload 10
    //   403: iadd
    //   404: if_icmpge -299 -> 105
    //   407: aload_2
    //   408: iload 11
    //   410: iconst_0
    //   411: bastore
    //   412: iinc 11 1
    //   415: goto -17 -> 398
    //   418: astore 18
    //   420: goto -52 -> 368
    //   423: iload 8
    //   425: istore 9
    //   427: goto -184 -> 243
    //   430: iinc 12 1
    //   433: goto -174 -> 259
    //
    // Exception table:
    //   from	to	target	type
    //   259	288	363	java/lang/NumberFormatException
    //   290	301	363	java/lang/NumberFormatException
    //   350	356	363	java/lang/NumberFormatException
    //   329	335	418	java/lang/NumberFormatException
  }

  public static int[] toArray(String paramString)
  {
    return toArray(paramString, 1);
  }

  public static int[] toArray(String paramString, int paramInt)
  {
    byte[] arrayOfByte = toByteArray(paramString, paramInt);
    int[] arrayOfInt;
    if (arrayOfByte == null)
      arrayOfInt = null;
    while (true)
    {
      return arrayOfInt;
      arrayOfInt = new int[arrayOfByte.length];
      for (int i = 0; i < arrayOfByte.length; i++)
        arrayOfInt[i] = (0xFF & arrayOfByte[i]);
    }
  }

  public static byte[] toByteArray(String paramString, int paramInt)
  {
    if (paramInt == 1)
      return parseV4(paramString);
    if (paramInt == 2)
      return parseV6(paramString);
    throw new IllegalArgumentException("unknown address family");
  }

  public static String toDottedQuad(byte[] paramArrayOfByte)
  {
    return (0xFF & paramArrayOfByte[0]) + "." + (0xFF & paramArrayOfByte[1]) + "." + (0xFF & paramArrayOfByte[2]) + "." + (0xFF & paramArrayOfByte[3]);
  }

  public static String toDottedQuad(int[] paramArrayOfInt)
  {
    return paramArrayOfInt[0] + "." + paramArrayOfInt[1] + "." + paramArrayOfInt[2] + "." + paramArrayOfInt[3];
  }

  public static InetAddress truncate(InetAddress paramInetAddress, int paramInt)
  {
    int i = 8 * addressLength(familyOf(paramInetAddress));
    if ((paramInt < 0) || (paramInt > i))
      throw new IllegalArgumentException("invalid mask length");
    if (paramInt == i)
      return paramInetAddress;
    byte[] arrayOfByte = paramInetAddress.getAddress();
    for (int j = 1 + paramInt / 8; j < arrayOfByte.length; j++)
      arrayOfByte[j] = 0;
    int k = paramInt % 8;
    int m = 0;
    for (int n = 0; n < k; n++)
      m |= 1 << 7 - n;
    int i1 = paramInt / 8;
    arrayOfByte[i1] = ((byte)(m & arrayOfByte[i1]));
    try
    {
      InetAddress localInetAddress = InetAddress.getByAddress(arrayOfByte);
      return localInetAddress;
    }
    catch (UnknownHostException localUnknownHostException)
    {
    }
    throw new IllegalArgumentException("invalid address");
  }
}