package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Generator
{
  private long current;
  public final int dclass;
  public long end;
  public final String namePattern;
  public final Name origin;
  public final String rdataPattern;
  public long start;
  public long step;
  public final long ttl;
  public final int type;

  public Generator(long paramLong1, long paramLong2, long paramLong3, String paramString1, int paramInt1, int paramInt2, long paramLong4, String paramString2, Name paramName)
  {
    if ((paramLong1 < 0L) || (paramLong2 < 0L) || (paramLong1 > paramLong2) || (paramLong3 <= 0L))
      throw new IllegalArgumentException("invalid range specification");
    if (!supportedType(paramInt1))
      throw new IllegalArgumentException("unsupported type");
    DClass.check(paramInt2);
    this.start = paramLong1;
    this.end = paramLong2;
    this.step = paramLong3;
    this.namePattern = paramString1;
    this.type = paramInt1;
    this.dclass = paramInt2;
    this.ttl = paramLong4;
    this.rdataPattern = paramString2;
    this.origin = paramName;
    this.current = paramLong1;
  }

  private String substitute(String paramString, long paramLong)
    throws IOException
  {
    int i = 0;
    byte[] arrayOfByte = paramString.getBytes();
    StringBuffer localStringBuffer = new StringBuffer();
    int j = 0;
    if (j < arrayOfByte.length)
    {
      char c = (char)(0xFF & arrayOfByte[j]);
      if (i != 0)
      {
        localStringBuffer.append(c);
        i = 0;
      }
      while (true)
      {
        j++;
        break;
        if (c == '\\')
        {
          if (j + 1 == arrayOfByte.length)
            throw new TextParseException("invalid escape character");
          i = 1;
        }
        else if (c == '$')
        {
          long l1 = 0L;
          long l2 = 0L;
          long l3 = 10L;
          if ((j + 1 < arrayOfByte.length) && (arrayOfByte[(j + 1)] == 36))
          {
            j++;
            localStringBuffer.append((char)(0xFF & arrayOfByte[j]));
          }
          else
          {
            int k = j + 1;
            int m = arrayOfByte.length;
            int n = 0;
            if (k < m)
            {
              int i3 = arrayOfByte[(j + 1)];
              n = 0;
              if (i3 == 123)
              {
                int i4 = j + 1;
                int i5 = i4 + 1;
                int i6 = arrayOfByte.length;
                int i7 = 0;
                if (i5 < i6)
                {
                  int i9 = arrayOfByte[(i4 + 1)];
                  i7 = 0;
                  if (i9 == 45)
                  {
                    i7 = 1;
                    i4++;
                  }
                }
                if (i4 + 1 < arrayOfByte.length)
                {
                  i4++;
                  c = (char)(0xFF & arrayOfByte[i4]);
                  if ((c != ',') && (c != '}'));
                }
                else
                {
                  if (i7 != 0)
                    l1 = -l1;
                  if (c != ',');
                }
                while (true)
                {
                  if (i4 + 1 < arrayOfByte.length)
                  {
                    i4++;
                    c = (char)(0xFF & arrayOfByte[i4]);
                    if ((c != ',') && (c != '}'));
                  }
                  else
                  {
                    n = 0;
                    if (c != ',')
                      break label496;
                    if (i4 + 1 != arrayOfByte.length)
                      break label469;
                    throw new TextParseException("invalid base");
                    if ((c < '0') || (c > '9'))
                      throw new TextParseException("invalid offset");
                    c = (char)(c - '0');
                    l1 = l1 * 10L + c;
                    break;
                  }
                  if ((c < '0') || (c > '9'))
                    throw new TextParseException("invalid width");
                  c = (char)(c - '0');
                  l2 = l2 * 10L + c;
                }
                label469: i4++;
                int i8 = (char)(0xFF & arrayOfByte[i4]);
                if (i8 == 111)
                  l3 = 8L;
                label496: 
                while ((i4 + 1 == arrayOfByte.length) || (arrayOfByte[(i4 + 1)] != 125))
                {
                  throw new TextParseException("invalid modifiers");
                  if (i8 == 120)
                  {
                    l3 = 16L;
                    n = 0;
                  }
                  else if (i8 == 88)
                  {
                    l3 = 16L;
                    n = 1;
                  }
                  else
                  {
                    n = 0;
                    if (i8 != 100)
                      throw new TextParseException("invalid base");
                  }
                }
                j = i4 + 1;
              }
            }
            long l4 = paramLong + l1;
            if (l4 < 0L)
              throw new TextParseException("invalid offset expansion");
            String str;
            if (l3 == 8L)
              str = Long.toOctalString(l4);
            while (true)
            {
              if (n != 0)
                str = str.toUpperCase();
              if ((l2 == 0L) || (l2 <= str.length()))
                break;
              int i2;
              for (int i1 = (int)l2 - str.length(); ; i1 = i2)
              {
                i2 = i1 - 1;
                if (i1 <= 0)
                  break;
                localStringBuffer.append('0');
              }
              if (l3 == 16L)
                str = Long.toHexString(l4);
              else
                str = Long.toString(l4);
            }
            localStringBuffer.append(str);
          }
        }
        else
        {
          localStringBuffer.append(c);
        }
      }
    }
    return localStringBuffer.toString();
  }

  public static boolean supportedType(int paramInt)
  {
    Type.check(paramInt);
    return (paramInt == 12) || (paramInt == 5) || (paramInt == 39) || (paramInt == 1) || (paramInt == 28) || (paramInt == 2);
  }

  public Record[] expand()
    throws IOException
  {
    ArrayList localArrayList = new ArrayList();
    for (long l = this.start; l < this.end; l += this.step)
    {
      Name localName = Name.fromString(substitute(this.namePattern, this.current), this.origin);
      String str = substitute(this.rdataPattern, this.current);
      localArrayList.add(Record.fromString(localName, this.type, this.dclass, this.ttl, str, this.origin));
    }
    return (Record[])localArrayList.toArray(new Record[localArrayList.size()]);
  }

  public Record nextRecord()
    throws IOException
  {
    if (this.current > this.end)
      return null;
    Name localName = Name.fromString(substitute(this.namePattern, this.current), this.origin);
    String str = substitute(this.rdataPattern, this.current);
    this.current += this.step;
    return Record.fromString(localName, this.type, this.dclass, this.ttl, str, this.origin);
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("$GENERATE ");
    localStringBuffer.append(this.start + "-" + this.end);
    if (this.step > 1L)
      localStringBuffer.append("/" + this.step);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.namePattern + " ");
    localStringBuffer.append(this.ttl + " ");
    if ((this.dclass != 1) || (!Options.check("noPrintIN")))
      localStringBuffer.append(DClass.string(this.dclass) + " ");
    localStringBuffer.append(Type.string(this.type) + " ");
    localStringBuffer.append(this.rdataPattern + " ");
    return localStringBuffer.toString();
  }
}