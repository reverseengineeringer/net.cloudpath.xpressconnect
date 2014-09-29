package jcifs.util;

import java.io.IOException;
import java.util.Date;

public class Encdec
{
  public static final long MILLISECONDS_BETWEEN_1970_AND_1601 = 11644473600000L;
  public static final long SEC_BETWEEEN_1904_AND_1970 = 2082844800L;
  public static final int TIME_1601_NANOS_64BE = 6;
  public static final int TIME_1601_NANOS_64LE = 5;
  public static final int TIME_1904_SEC_32BE = 3;
  public static final int TIME_1904_SEC_32LE = 4;
  public static final int TIME_1970_MILLIS_64BE = 7;
  public static final int TIME_1970_MILLIS_64LE = 8;
  public static final int TIME_1970_SEC_32BE = 1;
  public static final int TIME_1970_SEC_32LE = 2;

  public static double dec_doublebe(byte[] paramArrayOfByte, int paramInt)
  {
    return Double.longBitsToDouble(dec_uint64be(paramArrayOfByte, paramInt));
  }

  public static double dec_doublele(byte[] paramArrayOfByte, int paramInt)
  {
    return Double.longBitsToDouble(dec_uint64le(paramArrayOfByte, paramInt));
  }

  public static float dec_floatbe(byte[] paramArrayOfByte, int paramInt)
  {
    return Float.intBitsToFloat(dec_uint32be(paramArrayOfByte, paramInt));
  }

  public static float dec_floatle(byte[] paramArrayOfByte, int paramInt)
  {
    return Float.intBitsToFloat(dec_uint32le(paramArrayOfByte, paramInt));
  }

  public static Date dec_time(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    switch (paramInt2)
    {
    default:
      throw new IllegalArgumentException("Unsupported time encoding");
    case 1:
      return new Date(1000L * dec_uint32be(paramArrayOfByte, paramInt1));
    case 2:
      return new Date(1000L * dec_uint32le(paramArrayOfByte, paramInt1));
    case 3:
      return new Date(1000L * ((0xFFFFFFFF & dec_uint32be(paramArrayOfByte, paramInt1)) - 2082844800L));
    case 4:
      return new Date(1000L * ((0xFFFFFFFF & dec_uint32le(paramArrayOfByte, paramInt1)) - 2082844800L));
    case 6:
      return new Date(dec_uint64be(paramArrayOfByte, paramInt1) / 10000L - 11644473600000L);
    case 5:
      return new Date(dec_uint64le(paramArrayOfByte, paramInt1) / 10000L - 11644473600000L);
    case 7:
      return new Date(dec_uint64be(paramArrayOfByte, paramInt1));
    case 8:
    }
    return new Date(dec_uint64le(paramArrayOfByte, paramInt1));
  }

  public static String dec_ucs2le(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar)
    throws IOException
  {
    int i = 0;
    while (true)
    {
      if (paramInt1 + 1 < paramInt2)
      {
        paramArrayOfChar[i] = ((char)dec_uint16le(paramArrayOfByte, paramInt1));
        if (paramArrayOfChar[i] != 0);
      }
      else
      {
        return new String(paramArrayOfChar, 0, i);
      }
      i++;
      paramInt1 += 2;
    }
  }

  public static short dec_uint16be(byte[] paramArrayOfByte, int paramInt)
  {
    return (short)((0xFF & paramArrayOfByte[paramInt]) << 8 | 0xFF & paramArrayOfByte[(paramInt + 1)]);
  }

  public static short dec_uint16le(byte[] paramArrayOfByte, int paramInt)
  {
    return (short)(0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8);
  }

  public static int dec_uint32be(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFF & paramArrayOfByte[paramInt]) << 24 | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 8 | 0xFF & paramArrayOfByte[(paramInt + 3)];
  }

  public static int dec_uint32le(byte[] paramArrayOfByte, int paramInt)
  {
    return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24;
  }

  public static long dec_uint64be(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFFFFFFFF & dec_uint32be(paramArrayOfByte, paramInt)) << 32 | 0xFFFFFFFF & dec_uint32be(paramArrayOfByte, paramInt + 4);
  }

  public static long dec_uint64le(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFFFFFFFF & dec_uint32le(paramArrayOfByte, paramInt + 4)) << 32 | 0xFFFFFFFF & dec_uint32le(paramArrayOfByte, paramInt);
  }

  public static String dec_utf8(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    char[] arrayOfChar = new char[paramInt2 - paramInt1];
    int i = 0;
    int j = paramInt1;
    int k;
    int m;
    while (true)
      if (j < paramInt2)
      {
        k = j + 1;
        m = 0xFF & paramArrayOfByte[j];
        if (m != 0)
        {
          if (m < 128)
          {
            arrayOfChar[i] = ((char)m);
            i++;
            j = k;
            continue;
          }
          if ((m & 0xE0) != 192)
            break label172;
          if (paramInt2 - k >= 2)
            break label96;
        }
      }
    while (true)
    {
      return new String(arrayOfChar, 0, i);
      label96: arrayOfChar[i] = ((char)((m & 0x1F) << 6));
      int i3 = k + 1;
      int i4 = 0xFF & paramArrayOfByte[k];
      arrayOfChar[i] = ((char)(arrayOfChar[i] | i4 & 0x3F));
      if (((i4 & 0xC0) != 128) || (arrayOfChar[i] < ''))
      {
        throw new IOException("Invalid UTF-8 sequence");
        label172: if ((m & 0xF0) == 224)
        {
          if (paramInt2 - k < 3)
            continue;
          arrayOfChar[i] = ((char)((m & 0xF) << 12));
          int n = k + 1;
          int i1 = 0xFF & paramArrayOfByte[k];
          if ((i1 & 0xC0) != 128)
            throw new IOException("Invalid UTF-8 sequence");
          arrayOfChar[i] = ((char)(arrayOfChar[i] | (i1 & 0x3F) << 6));
          k = n + 1;
          int i2 = 0xFF & paramArrayOfByte[n];
          arrayOfChar[i] = ((char)(arrayOfChar[i] | i2 & 0x3F));
          if (((i2 & 0xC0) == 128) && (arrayOfChar[i] >= 'ࠀ'))
            break;
          throw new IOException("Invalid UTF-8 sequence");
        }
        throw new IOException("Unsupported UTF-8 sequence");
      }
      k = i3;
      break;
    }
  }

  public static int enc_doublebe(double paramDouble, byte[] paramArrayOfByte, int paramInt)
  {
    return enc_uint64be(Double.doubleToLongBits(paramDouble), paramArrayOfByte, paramInt);
  }

  public static int enc_doublele(double paramDouble, byte[] paramArrayOfByte, int paramInt)
  {
    return enc_uint64le(Double.doubleToLongBits(paramDouble), paramArrayOfByte, paramInt);
  }

  public static int enc_floatbe(float paramFloat, byte[] paramArrayOfByte, int paramInt)
  {
    return enc_uint32be(Float.floatToIntBits(paramFloat), paramArrayOfByte, paramInt);
  }

  public static int enc_floatle(float paramFloat, byte[] paramArrayOfByte, int paramInt)
  {
    return enc_uint32le(Float.floatToIntBits(paramFloat), paramArrayOfByte, paramInt);
  }

  public static int enc_time(Date paramDate, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    switch (paramInt2)
    {
    default:
      throw new IllegalArgumentException("Unsupported time encoding");
    case 1:
      return enc_uint32be((int)(paramDate.getTime() / 1000L), paramArrayOfByte, paramInt1);
    case 2:
      return enc_uint32le((int)(paramDate.getTime() / 1000L), paramArrayOfByte, paramInt1);
    case 3:
      return enc_uint32be((int)(0xFFFFFFFF & 2082844800L + paramDate.getTime() / 1000L), paramArrayOfByte, paramInt1);
    case 4:
      return enc_uint32le((int)(0xFFFFFFFF & 2082844800L + paramDate.getTime() / 1000L), paramArrayOfByte, paramInt1);
    case 6:
      return enc_uint64be(10000L * (11644473600000L + paramDate.getTime()), paramArrayOfByte, paramInt1);
    case 5:
      return enc_uint64le(10000L * (11644473600000L + paramDate.getTime()), paramArrayOfByte, paramInt1);
    case 7:
      return enc_uint64be(paramDate.getTime(), paramArrayOfByte, paramInt1);
    case 8:
    }
    return enc_uint64le(paramDate.getTime(), paramArrayOfByte, paramInt1);
  }

  public static int enc_uint16be(short paramShort, byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = ((byte)(0xFF & paramShort >> 8));
    paramArrayOfByte[i] = ((byte)(paramShort & 0xFF));
    return 2;
  }

  public static int enc_uint16le(short paramShort, byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = ((byte)(paramShort & 0xFF));
    paramArrayOfByte[i] = ((byte)(0xFF & paramShort >> 8));
    return 2;
  }

  public static int enc_uint32be(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt2 + 1;
    paramArrayOfByte[paramInt2] = ((byte)(0xFF & paramInt1 >> 24));
    int j = i + 1;
    paramArrayOfByte[i] = ((byte)(0xFF & paramInt1 >> 16));
    int k = j + 1;
    paramArrayOfByte[j] = ((byte)(0xFF & paramInt1 >> 8));
    paramArrayOfByte[k] = ((byte)(paramInt1 & 0xFF));
    return 4;
  }

  public static int enc_uint32le(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt2 + 1;
    paramArrayOfByte[paramInt2] = ((byte)(paramInt1 & 0xFF));
    int j = i + 1;
    paramArrayOfByte[i] = ((byte)(0xFF & paramInt1 >> 8));
    int k = j + 1;
    paramArrayOfByte[j] = ((byte)(0xFF & paramInt1 >> 16));
    paramArrayOfByte[k] = ((byte)(0xFF & paramInt1 >> 24));
    return 4;
  }

  public static int enc_uint64be(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    enc_uint32be((int)(paramLong & 0xFFFFFFFF), paramArrayOfByte, paramInt + 4);
    enc_uint32be((int)(0xFFFFFFFF & paramLong >> 32), paramArrayOfByte, paramInt);
    return 8;
  }

  public static int enc_uint64le(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    enc_uint32le((int)(paramLong & 0xFFFFFFFF), paramArrayOfByte, paramInt);
    enc_uint32le((int)(0xFFFFFFFF & paramLong >> 32), paramArrayOfByte, paramInt + 4);
    return 8;
  }

  public static int enc_utf8(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = paramString.length();
    int j = 0;
    int k = paramInt1;
    int m;
    int i2;
    if ((k < paramInt2) && (j < i))
    {
      m = paramString.charAt(j);
      if ((m >= 1) && (m <= 127))
      {
        i2 = k + 1;
        paramArrayOfByte[k] = ((byte)m);
      }
    }
    while (true)
    {
      j++;
      k = i2;
      break;
      if (m > 2047)
        if (paramInt2 - k >= 3);
      while (paramInt2 - k < 2)
      {
        return k - paramInt1;
        int i3 = k + 1;
        paramArrayOfByte[k] = ((byte)(0xE0 | 0xF & m >> 12));
        int i4 = i3 + 1;
        paramArrayOfByte[i3] = ((byte)(0x80 | 0x3F & m >> 6));
        i2 = i4 + 1;
        paramArrayOfByte[i4] = ((byte)(0x80 | 0x3F & m >> 0));
        break;
      }
      int n = k + 1;
      paramArrayOfByte[k] = ((byte)(0xC0 | 0x1F & m >> 6));
      int i1 = n + 1;
      paramArrayOfByte[n] = ((byte)(0x80 | 0x3F & m >> 0));
      i2 = i1;
    }
  }
}