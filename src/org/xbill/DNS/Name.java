package org.xbill.DNS;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.DecimalFormat;

public class Name
  implements Comparable, Serializable
{
  private static final int LABEL_COMPRESSION = 192;
  private static final int LABEL_MASK = 192;
  private static final int LABEL_NORMAL = 0;
  private static final int MAXLABEL = 63;
  private static final int MAXLABELS = 128;
  private static final int MAXNAME = 255;
  private static final int MAXOFFSETS = 7;
  private static final DecimalFormat byteFormat;
  public static final Name empty;
  private static final byte[] emptyLabel = { 0 };
  private static final byte[] lowercase;
  public static final Name root;
  private static final long serialVersionUID = -7257019940971525644L;
  private static final Name wild;
  private static final byte[] wildLabel = { 1, 42 };
  private int hashcode;
  private byte[] name;
  private long offsets;

  static
  {
    byteFormat = new DecimalFormat();
    lowercase = new byte[256];
    byteFormat.setMinimumIntegerDigits(3);
    int i = 0;
    if (i < lowercase.length)
    {
      if ((i < 65) || (i > 90))
        lowercase[i] = ((byte)i);
      while (true)
      {
        i++;
        break;
        lowercase[i] = ((byte)(97 + (i - 65)));
      }
    }
    root = new Name();
    root.appendSafe(emptyLabel, 0, 1);
    empty = new Name();
    empty.name = new byte[0];
    wild = new Name();
    wild.appendSafe(wildLabel, 0, 1);
  }

  private Name()
  {
  }

  public Name(String paramString)
    throws TextParseException
  {
    this(paramString, null);
  }

  public Name(String paramString, Name paramName)
    throws TextParseException
  {
    if (paramString.equals(""))
      throw parseException(paramString, "empty name");
    if (paramString.equals("@"))
      if (paramName == null)
        copy(empty, this);
    while (true)
    {
      return;
      copy(paramName, this);
      return;
      if (paramString.equals("."))
      {
        copy(root, this);
        return;
      }
      int i = -1;
      int j = 1;
      byte[] arrayOfByte = new byte[64];
      int k = 0;
      int m = 0;
      int n = 0;
      int i1 = 0;
      if (i1 < paramString.length())
      {
        int i3 = (byte)paramString.charAt(i1);
        if (k != 0)
          if ((i3 >= 48) && (i3 <= 57) && (m < 3))
          {
            m++;
            n = n * 10 + (i3 - 48);
            if (n > 255)
              throw parseException(paramString, "bad escape");
            if (m >= 3);
          }
        while (true)
        {
          i1++;
          break;
          i3 = (byte)n;
          while (j > 63)
          {
            throw parseException(paramString, "label too long");
            if ((m > 0) && (m < 3))
              throw parseException(paramString, "bad escape");
          }
          i = j;
          int i5 = j + 1;
          arrayOfByte[j] = i3;
          j = i5;
          k = 0;
          continue;
          if (i3 == 92)
          {
            k = 1;
            m = 0;
            n = 0;
          }
          else if (i3 == 46)
          {
            if (i == -1)
              throw parseException(paramString, "invalid empty label");
            arrayOfByte[0] = ((byte)(j - 1));
            appendFromString(paramString, arrayOfByte, 0, 1);
            i = -1;
            j = 1;
          }
          else
          {
            if (i == -1)
              i = i1;
            if (j > 63)
              throw parseException(paramString, "label too long");
            int i4 = j + 1;
            arrayOfByte[j] = i3;
            j = i4;
          }
        }
      }
      if ((m > 0) && (m < 3))
        throw parseException(paramString, "bad escape");
      if (k != 0)
        throw parseException(paramString, "bad escape");
      if (i == -1)
        appendFromString(paramString, emptyLabel, 0, 1);
      for (int i2 = 1; (paramName != null) && (i2 == 0); i2 = 0)
      {
        appendFromString(paramString, paramName.name, 0, paramName.getlabels());
        return;
        arrayOfByte[0] = ((byte)(j - 1));
        appendFromString(paramString, arrayOfByte, 0, 1);
      }
    }
  }

  public Name(DNSInput paramDNSInput)
    throws WireParseException
  {
    int i = 0;
    byte[] arrayOfByte = new byte[64];
    int j = 0;
    while (i == 0)
    {
      int k = paramDNSInput.readU8();
      switch (k & 0xC0)
      {
      default:
        throw new WireParseException("bad label type");
      case 0:
        if (getlabels() >= 128)
          throw new WireParseException("too many labels");
        if (k == 0)
        {
          append(emptyLabel, 0, 1);
          i = 1;
        }
        else
        {
          arrayOfByte[0] = ((byte)k);
          paramDNSInput.readByteArray(arrayOfByte, 1, k);
          append(arrayOfByte, 0, 1);
        }
        break;
      case 192:
        int m = paramDNSInput.readU8() + ((k & 0xFFFFFF3F) << 8);
        if (Options.check("verbosecompression"))
          System.err.println("currently " + paramDNSInput.current() + ", pointer to " + m);
        if (m >= -2 + paramDNSInput.current())
          throw new WireParseException("bad compression");
        if (j == 0)
        {
          paramDNSInput.save();
          j = 1;
        }
        paramDNSInput.jump(m);
        if (Options.check("verbosecompression"))
          System.err.println("current name '" + this + "', seeking to " + m);
        break;
      }
    }
    if (j != 0)
      paramDNSInput.restore();
  }

  public Name(Name paramName, int paramInt)
  {
    int i = paramName.labels();
    if (paramInt > i)
      throw new IllegalArgumentException("attempted to remove too many labels");
    this.name = paramName.name;
    setlabels(i - paramInt);
    for (int j = 0; (j < 7) && (j < i - paramInt); j++)
      setoffset(j, paramName.offset(j + paramInt));
  }

  public Name(byte[] paramArrayOfByte)
    throws IOException
  {
    this(new DNSInput(paramArrayOfByte));
  }

  private final void append(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws NameTooLongException
  {
    int i;
    int j;
    int k;
    int m;
    if (this.name == null)
    {
      i = 0;
      j = 0;
      k = 0;
      m = paramInt1;
    }
    while (true)
    {
      if (k >= paramInt2)
        break label90;
      int i5 = paramArrayOfByte[m];
      if (i5 > 63)
      {
        throw new IllegalStateException("invalid label");
        i = this.name.length - offset(0);
        break;
      }
      int i6 = i5 + 1;
      m += i6;
      j += i6;
      k++;
    }
    label90: int n = i + j;
    if (n > 255)
      throw new NameTooLongException();
    int i1 = getlabels();
    int i2 = i1 + paramInt2;
    if (i2 > 128)
      throw new IllegalStateException("too many labels");
    byte[] arrayOfByte = new byte[n];
    if (i != 0)
      System.arraycopy(this.name, offset(0), arrayOfByte, 0, i);
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, i, j);
    this.name = arrayOfByte;
    int i3 = 0;
    int i4 = i;
    while (i3 < paramInt2)
    {
      setoffset(i1 + i3, i4);
      i4 += 1 + arrayOfByte[i4];
      i3++;
    }
    setlabels(i2);
  }

  private final void appendFromString(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws TextParseException
  {
    try
    {
      append(paramArrayOfByte, paramInt1, paramInt2);
      return;
    }
    catch (NameTooLongException localNameTooLongException)
    {
    }
    throw parseException(paramString, "Name too long");
  }

  private final void appendSafe(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    try
    {
      append(paramArrayOfByte, paramInt1, paramInt2);
      return;
    }
    catch (NameTooLongException localNameTooLongException)
    {
    }
  }

  private String byteString(byte[] paramArrayOfByte, int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramInt + 1;
    int j = paramArrayOfByte[paramInt];
    int k = i;
    if (k < i + j)
    {
      int m = 0xFF & paramArrayOfByte[k];
      if ((m <= 32) || (m >= 127))
      {
        localStringBuffer.append('\\');
        localStringBuffer.append(byteFormat.format(m));
      }
      while (true)
      {
        k++;
        break;
        if ((m == 34) || (m == 40) || (m == 41) || (m == 46) || (m == 59) || (m == 92) || (m == 64) || (m == 36))
        {
          localStringBuffer.append('\\');
          localStringBuffer.append((char)m);
        }
        else
        {
          localStringBuffer.append((char)m);
        }
      }
    }
    return localStringBuffer.toString();
  }

  public static Name concatenate(Name paramName1, Name paramName2)
    throws NameTooLongException
  {
    if (paramName1.isAbsolute())
      return paramName1;
    Name localName = new Name();
    copy(paramName1, localName);
    localName.append(paramName2.name, paramName2.offset(0), paramName2.getlabels());
    return localName;
  }

  private static final void copy(Name paramName1, Name paramName2)
  {
    if (paramName1.offset(0) == 0)
    {
      paramName2.name = paramName1.name;
      paramName2.offsets = paramName1.offsets;
      return;
    }
    int i = paramName1.offset(0);
    int j = paramName1.name.length - i;
    int k = paramName1.labels();
    paramName2.name = new byte[j];
    System.arraycopy(paramName1.name, i, paramName2.name, 0, j);
    for (int m = 0; (m < k) && (m < 7); m++)
      paramName2.setoffset(m, paramName1.offset(m) - i);
    paramName2.setlabels(k);
  }

  private final boolean equals(byte[] paramArrayOfByte, int paramInt)
  {
    int i = labels();
    int j = 0;
    int k = offset(0);
    while (j < i)
    {
      if (this.name[k] != paramArrayOfByte[paramInt])
        return false;
      byte[] arrayOfByte1 = this.name;
      int m = k + 1;
      int n = arrayOfByte1[k];
      int i1 = paramInt + 1;
      if (n > 63)
        throw new IllegalStateException("invalid label");
      int i2 = 0;
      k = m;
      int i6;
      for (int i3 = i1; ; i3 = i6)
      {
        if (i2 >= n)
          break label170;
        byte[] arrayOfByte2 = lowercase;
        byte[] arrayOfByte3 = this.name;
        int i4 = k + 1;
        int i5 = arrayOfByte2[(0xFF & arrayOfByte3[k])];
        byte[] arrayOfByte4 = lowercase;
        i6 = i3 + 1;
        if (i5 != arrayOfByte4[(0xFF & paramArrayOfByte[i3])])
          break;
        i2++;
        k = i4;
      }
      label170: j++;
      paramInt = i3;
    }
    return true;
  }

  public static Name fromConstantString(String paramString)
  {
    try
    {
      Name localName = fromString(paramString, null);
      return localName;
    }
    catch (TextParseException localTextParseException)
    {
    }
    throw new IllegalArgumentException("Invalid name '" + paramString + "'");
  }

  public static Name fromString(String paramString)
    throws TextParseException
  {
    return fromString(paramString, null);
  }

  public static Name fromString(String paramString, Name paramName)
    throws TextParseException
  {
    if ((paramString.equals("@")) && (paramName != null))
      return paramName;
    if (paramString.equals("."))
      return root;
    return new Name(paramString, paramName);
  }

  private final int getlabels()
  {
    return (int)(0xFF & this.offsets);
  }

  private final int offset(int paramInt)
  {
    int i;
    if ((paramInt == 0) && (getlabels() == 0))
      i = 0;
    while (true)
    {
      return i;
      if ((paramInt < 0) || (paramInt >= getlabels()))
        throw new IllegalArgumentException("label out of range");
      if (paramInt < 7)
      {
        int k = 8 * (7 - paramInt);
        return 0xFF & (int)(this.offsets >>> k);
      }
      i = offset(6);
      for (int j = 6; j < paramInt; j++)
        i += 1 + this.name[i];
    }
  }

  private static TextParseException parseException(String paramString1, String paramString2)
  {
    return new TextParseException("'" + paramString1 + "': " + paramString2);
  }

  private final void setlabels(int paramInt)
  {
    this.offsets = (0xFFFFFF00 & this.offsets);
    this.offsets |= paramInt;
  }

  private final void setoffset(int paramInt1, int paramInt2)
  {
    if (paramInt1 >= 7)
      return;
    int i = 8 * (7 - paramInt1);
    this.offsets &= (0xFFFFFFFF ^ 255L << i);
    this.offsets |= paramInt2 << i;
  }

  public int compareTo(Object paramObject)
  {
    Name localName = (Name)paramObject;
    int i5;
    if (this == localName)
    {
      i5 = 0;
      return i5;
    }
    int i = labels();
    int j = localName.labels();
    int k;
    if (i > j)
      k = j;
    label37: for (int m = 1; ; m++)
    {
      if (m > k)
        break label182;
      int n = offset(i - m);
      int i1 = localName.offset(j - m);
      int i2 = this.name[n];
      int i3 = localName.name[i1];
      int i4 = 0;
      while (true)
        if ((i4 < i2) && (i4 < i3))
        {
          i5 = lowercase[(0xFF & this.name[(1 + (i4 + n))])] - lowercase[(0xFF & localName.name[(1 + (i4 + i1))])];
          if (i5 != 0)
            break;
          i4++;
          continue;
          k = i;
          break label37;
        }
      if (i2 != i3)
        return i2 - i3;
    }
    label182: return i - j;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool1;
    if (paramObject == this)
      bool1 = true;
    Name localName;
    int k;
    int m;
    do
    {
      int i;
      int j;
      do
      {
        boolean bool2;
        do
        {
          do
          {
            return bool1;
            bool1 = false;
          }
          while (paramObject == null);
          bool2 = paramObject instanceof Name;
          bool1 = false;
        }
        while (!bool2);
        localName = (Name)paramObject;
        if (localName.hashcode == 0)
          localName.hashCode();
        if (this.hashcode == 0)
          hashCode();
        i = localName.hashcode;
        j = this.hashcode;
        bool1 = false;
      }
      while (i != j);
      k = localName.labels();
      m = labels();
      bool1 = false;
    }
    while (k != m);
    return equals(localName.name, localName.offset(0));
  }

  public Name fromDNAME(DNAMERecord paramDNAMERecord)
    throws NameTooLongException
  {
    Name localName1 = paramDNAMERecord.getName();
    Name localName2 = paramDNAMERecord.getTarget();
    Name localName3;
    if (!subdomain(localName1))
      localName3 = null;
    while (true)
    {
      return localName3;
      int i = labels() - localName1.labels();
      int j = length() - localName1.length();
      int k = offset(0);
      int m = localName2.labels();
      int n = localName2.length();
      if (j + n > 255)
        throw new NameTooLongException();
      localName3 = new Name();
      localName3.setlabels(i + m);
      localName3.name = new byte[j + n];
      System.arraycopy(this.name, k, localName3.name, 0, j);
      System.arraycopy(localName2.name, 0, localName3.name, j, n);
      int i1 = 0;
      int i2 = 0;
      while ((i1 < 7) && (i1 < i + m))
      {
        localName3.setoffset(i1, i2);
        i2 += 1 + localName3.name[i2];
        i1++;
      }
    }
  }

  public byte[] getLabel(int paramInt)
  {
    int i = offset(paramInt);
    int j = (byte)(1 + this.name[i]);
    byte[] arrayOfByte = new byte[j];
    System.arraycopy(this.name, i, arrayOfByte, 0, j);
    return arrayOfByte;
  }

  public String getLabelString(int paramInt)
  {
    int i = offset(paramInt);
    return byteString(this.name, i);
  }

  public int hashCode()
  {
    if (this.hashcode != 0)
      return this.hashcode;
    int i = 0;
    for (int j = offset(0); j < this.name.length; j++)
      i += (i << 3) + lowercase[(0xFF & this.name[j])];
    this.hashcode = i;
    return this.hashcode;
  }

  public boolean isAbsolute()
  {
    if (labels() == 0);
    while (this.name[(-1 + this.name.length)] != 0)
      return false;
    return true;
  }

  public boolean isWild()
  {
    int i = 1;
    if (labels() == 0)
      return false;
    if ((this.name[0] == i) && (this.name[i] == 42));
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public int labels()
  {
    return getlabels();
  }

  public short length()
  {
    if (getlabels() == 0)
      return 0;
    return (short)(this.name.length - offset(0));
  }

  public Name relativize(Name paramName)
  {
    if ((paramName == null) || (!subdomain(paramName)))
      return this;
    Name localName = new Name();
    copy(this, localName);
    int i = length() - paramName.length();
    localName.setlabels(localName.labels() - paramName.labels());
    localName.name = new byte[i];
    System.arraycopy(this.name, offset(0), localName.name, 0, i);
    return localName;
  }

  public boolean subdomain(Name paramName)
  {
    int i = labels();
    int j = paramName.labels();
    if (j > i)
      return false;
    if (j == i)
      return equals(paramName);
    return paramName.equals(this.name, offset(i - j));
  }

  public String toString()
  {
    int i = labels();
    if (i == 0)
      return "@";
    if ((i == 1) && (this.name[offset(0)] == 0))
      return ".";
    StringBuffer localStringBuffer = new StringBuffer();
    int j = 0;
    int k = offset(0);
    while (true)
    {
      int m;
      if (j < i)
      {
        m = this.name[k];
        if (m > 63)
          throw new IllegalStateException("invalid label");
        if (m != 0);
      }
      else
      {
        if (!isAbsolute())
          localStringBuffer.deleteCharAt(-1 + localStringBuffer.length());
        return localStringBuffer.toString();
      }
      localStringBuffer.append(byteString(this.name, k));
      localStringBuffer.append('.');
      k += m + 1;
      j++;
    }
  }

  public void toWire(DNSOutput paramDNSOutput, Compression paramCompression)
  {
    if (!isAbsolute())
      throw new IllegalArgumentException("toWire() called on non-absolute name");
    int i = labels();
    for (int j = 0; j < i - 1; j++)
    {
      if (j == 0);
      for (Name localName = this; ; localName = new Name(this, j))
      {
        int k = -1;
        if (paramCompression != null)
          k = paramCompression.get(localName);
        if (k < 0)
          break;
        paramDNSOutput.writeU16(k | 0xC000);
        return;
      }
      if (paramCompression != null)
        paramCompression.add(paramDNSOutput.current(), localName);
      int m = offset(j);
      paramDNSOutput.writeByteArray(this.name, m, 1 + this.name[m]);
    }
    paramDNSOutput.writeU8(0);
  }

  public void toWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      toWireCanonical(paramDNSOutput);
      return;
    }
    toWire(paramDNSOutput, paramCompression);
  }

  public byte[] toWire()
  {
    DNSOutput localDNSOutput = new DNSOutput();
    toWire(localDNSOutput, null);
    return localDNSOutput.toByteArray();
  }

  public void toWireCanonical(DNSOutput paramDNSOutput)
  {
    paramDNSOutput.writeByteArray(toWireCanonical());
  }

  public byte[] toWireCanonical()
  {
    int i = labels();
    byte[] arrayOfByte1;
    if (i == 0)
      arrayOfByte1 = new byte[0];
    while (true)
    {
      return arrayOfByte1;
      arrayOfByte1 = new byte[this.name.length - offset(0)];
      int j = 0;
      int k = offset(0);
      int m = 0;
      int n = k;
      while (j < i)
      {
        int i1 = this.name[n];
        if (i1 > 63)
          throw new IllegalStateException("invalid label");
        int i2 = m + 1;
        byte[] arrayOfByte2 = this.name;
        int i3 = n + 1;
        arrayOfByte1[m] = arrayOfByte2[n];
        int i4 = 0;
        m = i2;
        int i6;
        for (n = i3; i4 < i1; n = i6)
        {
          int i5 = m + 1;
          byte[] arrayOfByte3 = lowercase;
          byte[] arrayOfByte4 = this.name;
          i6 = n + 1;
          arrayOfByte1[m] = arrayOfByte3[(0xFF & arrayOfByte4[n])];
          i4++;
          m = i5;
        }
        j++;
      }
    }
  }

  public Name wild(int paramInt)
  {
    if (paramInt < 1)
      throw new IllegalArgumentException("must replace 1 or more labels");
    try
    {
      Name localName = new Name();
      copy(wild, localName);
      localName.append(this.name, offset(paramInt), getlabels() - paramInt);
      return localName;
    }
    catch (NameTooLongException localNameTooLongException)
    {
    }
    throw new IllegalStateException("Name.wild: concatenate failed");
  }
}