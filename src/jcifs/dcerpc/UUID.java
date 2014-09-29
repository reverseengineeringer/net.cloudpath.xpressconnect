package jcifs.dcerpc;

public class UUID extends rpc.uuid_t
{
  static final char[] HEXCHARS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };

  public UUID(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    this.time_low = hex_to_bin(arrayOfChar, 0, 8);
    this.time_mid = S(hex_to_bin(arrayOfChar, 9, 4));
    this.time_hi_and_version = S(hex_to_bin(arrayOfChar, 14, 4));
    this.clock_seq_hi_and_reserved = B(hex_to_bin(arrayOfChar, 19, 2));
    this.clock_seq_low = B(hex_to_bin(arrayOfChar, 21, 2));
    this.node = new byte[6];
    this.node[0] = B(hex_to_bin(arrayOfChar, 24, 2));
    this.node[1] = B(hex_to_bin(arrayOfChar, 26, 2));
    this.node[2] = B(hex_to_bin(arrayOfChar, 28, 2));
    this.node[3] = B(hex_to_bin(arrayOfChar, 30, 2));
    this.node[4] = B(hex_to_bin(arrayOfChar, 32, 2));
    this.node[5] = B(hex_to_bin(arrayOfChar, 34, 2));
  }

  private static byte B(int paramInt)
  {
    return (byte)(paramInt & 0xFF);
  }

  private static short S(int paramInt)
  {
    return (short)(0xFFFF & paramInt);
  }

  public static String bin_to_hex(int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[paramInt2];
    int j;
    for (int i = arrayOfChar.length; ; i = j)
    {
      j = i - 1;
      if (i <= 0)
        break;
      arrayOfChar[j] = HEXCHARS[(paramInt1 & 0xF)];
      paramInt1 >>>= 4;
    }
    return new String(arrayOfChar);
  }

  public static int hex_to_bin(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = 0;
    int k = paramInt1;
    if ((k < paramArrayOfChar.length) && (j < paramInt2))
    {
      int m = i << 4;
      switch (paramArrayOfChar[k])
      {
      default:
        throw new IllegalArgumentException(new String(paramArrayOfChar, paramInt1, paramInt2));
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        i = m + ('\0*0' + paramArrayOfChar[k]);
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      }
      while (true)
      {
        j++;
        k++;
        break;
        i = m + (10 + ('\0'7' + paramArrayOfChar[k]));
        continue;
        i = m + (10 + ('\0#7' + paramArrayOfChar[k]));
      }
    }
    return i;
  }

  public String toString()
  {
    return bin_to_hex(this.time_low, 8) + '-' + bin_to_hex(this.time_mid, 4) + '-' + bin_to_hex(this.time_hi_and_version, 4) + '-' + bin_to_hex(this.clock_seq_hi_and_reserved, 2) + bin_to_hex(this.clock_seq_low, 2) + '-' + bin_to_hex(this.node[0], 2) + bin_to_hex(this.node[1], 2) + bin_to_hex(this.node[2], 2) + bin_to_hex(this.node[3], 2) + bin_to_hex(this.node[4], 2) + bin_to_hex(this.node[5], 2);
  }
}