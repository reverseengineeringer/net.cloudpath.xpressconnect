package jcifs.util;

import java.io.IOException;
import java.io.InputStream;

public class MimeMap
{
  private static final int IN_SIZE = 7000;
  private static final int ST_COMM = 2;
  private static final int ST_EXT = 5;
  private static final int ST_GAP = 4;
  private static final int ST_START = 1;
  private static final int ST_TYPE = 3;
  private byte[] in = new byte[7000];
  private int inLen;

  public MimeMap()
    throws IOException
  {
    InputStream localInputStream = getClass().getClassLoader().getResourceAsStream("jcifs/util/mime.map");
    int i;
    for (this.inLen = 0; ; this.inLen = (i + this.inLen))
    {
      i = localInputStream.read(this.in, this.inLen, 7000 - this.inLen);
      if (i == -1)
        break;
    }
    if ((this.inLen < 100) || (this.inLen == 7000))
      throw new IOException("Error reading jcifs/util/mime.map resource");
    localInputStream.close();
  }

  public String getMimeType(String paramString)
    throws IOException
  {
    return getMimeType(paramString, "application/octet-stream");
  }

  public String getMimeType(String paramString1, String paramString2)
    throws IOException
  {
    byte[] arrayOfByte1 = new byte[''];
    byte[] arrayOfByte2 = new byte[16];
    byte[] arrayOfByte3 = paramString1.toLowerCase().getBytes("ASCII");
    int i = 1;
    int j = 0;
    int k = 0;
    int m = 0;
    int n;
    if (m < this.inLen)
    {
      n = this.in[m];
      switch (i)
      {
      default:
      case 1:
      case 3:
      case 2:
      case 4:
      case 5:
      }
      while (true)
      {
        m++;
        break;
        if ((n != 32) && (n != 9))
          if (n == 35)
          {
            i = 2;
          }
          else
          {
            i = 3;
            if ((n == 32) || (n == 9))
            {
              i = 4;
            }
            else
            {
              int i3 = k + 1;
              arrayOfByte1[k] = n;
              k = i3;
              continue;
              if (n == 10)
              {
                i = 1;
                k = 0;
                j = 0;
                continue;
                if ((n != 32) && (n != 9))
                {
                  i = 5;
                  switch (n)
                  {
                  default:
                    int i2 = j + 1;
                    arrayOfByte2[j] = n;
                    j = i2;
                  case 9:
                  case 10:
                  case 32:
                  case 35:
                  }
                }
              }
            }
          }
      }
      for (int i1 = 0; (i1 < j) && (j == arrayOfByte3.length) && (arrayOfByte2[i1] == arrayOfByte3[i1]); i1++);
      if (i1 == arrayOfByte3.length)
        paramString2 = new String(arrayOfByte1, 0, k, "ASCII");
    }
    else
    {
      return paramString2;
    }
    if (n == 35)
      i = 2;
    while (true)
    {
      j = 0;
      break;
      if (n == 10)
      {
        i = 1;
        k = 0;
      }
    }
  }
}