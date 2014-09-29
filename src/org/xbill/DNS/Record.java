package org.xbill.DNS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import org.xbill.DNS.utils.base16;

public abstract class Record
  implements Cloneable, Comparable, Serializable
{
  private static final DecimalFormat byteFormat = new DecimalFormat();
  private static final long serialVersionUID = 2694906050116005466L;
  protected int dclass;
  protected Name name;
  protected long ttl;
  protected int type;

  static
  {
    byteFormat.setMinimumIntegerDigits(3);
  }

  protected Record()
  {
  }

  Record(Name paramName, int paramInt1, int paramInt2, long paramLong)
  {
    if (!paramName.isAbsolute())
      throw new RelativeNameException(paramName);
    Type.check(paramInt1);
    DClass.check(paramInt2);
    TTL.check(paramLong);
    this.name = paramName;
    this.type = paramInt1;
    this.dclass = paramInt2;
    this.ttl = paramLong;
  }

  protected static byte[] byteArrayFromString(String paramString)
    throws TextParseException
  {
    byte[] arrayOfByte = paramString.getBytes();
    int i = 0;
    for (int j = 0; ; j++)
    {
      int k = arrayOfByte.length;
      int m = 0;
      if (j < k)
      {
        if (arrayOfByte[j] == 92)
          m = 1;
      }
      else
      {
        if (m != 0)
          break label64;
        if (arrayOfByte.length <= 255)
          break;
        throw new TextParseException("text string too long");
      }
    }
    return arrayOfByte;
    label64: ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    if (i2 < arrayOfByte.length)
    {
      int i3 = arrayOfByte[i2];
      if (i != 0)
        if ((i3 >= 48) && (i3 <= 57) && (n < 3))
        {
          n++;
          i1 = i1 * 10 + (i3 - 48);
          if (i1 > 255)
            throw new TextParseException("bad escape");
          if (n >= 3);
        }
      while (true)
      {
        i2++;
        break;
        i3 = (byte)i1;
        do
        {
          localByteArrayOutputStream.write(i3);
          i = 0;
          break;
        }
        while ((n <= 0) || (n >= 3));
        throw new TextParseException("bad escape");
        if (arrayOfByte[i2] == 92)
        {
          i = 1;
          n = 0;
          i1 = 0;
        }
        else
        {
          localByteArrayOutputStream.write(arrayOfByte[i2]);
        }
      }
    }
    if ((n > 0) && (n < 3))
      throw new TextParseException("bad escape");
    if (localByteArrayOutputStream.toByteArray().length > 255)
      throw new TextParseException("text string too long");
    return localByteArrayOutputStream.toByteArray();
  }

  protected static String byteArrayToString(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (paramBoolean)
      localStringBuffer.append('"');
    int i = 0;
    if (i < paramArrayOfByte.length)
    {
      int j = 0xFF & paramArrayOfByte[i];
      if ((j < 32) || (j >= 127))
      {
        localStringBuffer.append('\\');
        localStringBuffer.append(byteFormat.format(j));
      }
      while (true)
      {
        i++;
        break;
        if ((j == 34) || (j == 92))
        {
          localStringBuffer.append('\\');
          localStringBuffer.append((char)j);
        }
        else
        {
          localStringBuffer.append((char)j);
        }
      }
    }
    if (paramBoolean)
      localStringBuffer.append('"');
    return localStringBuffer.toString();
  }

  static byte[] checkByteArrayLength(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    if (paramArrayOfByte.length > 65535)
      throw new IllegalArgumentException("\"" + paramString + "\" array " + "must have no more than " + paramInt + " elements");
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
    return arrayOfByte;
  }

  static Name checkName(String paramString, Name paramName)
  {
    if (!paramName.isAbsolute())
      throw new RelativeNameException(paramName);
    return paramName;
  }

  static int checkU16(String paramString, int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 65535))
      throw new IllegalArgumentException("\"" + paramString + "\" " + paramInt + " must be an unsigned 16 " + "bit value");
    return paramInt;
  }

  static long checkU32(String paramString, long paramLong)
  {
    if ((paramLong < 0L) || (paramLong > 4294967295L))
      throw new IllegalArgumentException("\"" + paramString + "\" " + paramLong + " must be an unsigned 32 " + "bit value");
    return paramLong;
  }

  static int checkU8(String paramString, int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 255))
      throw new IllegalArgumentException("\"" + paramString + "\" " + paramInt + " must be an unsigned 8 " + "bit value");
    return paramInt;
  }

  public static Record fromString(Name paramName1, int paramInt1, int paramInt2, long paramLong, String paramString, Name paramName2)
    throws IOException
  {
    return fromString(paramName1, paramInt1, paramInt2, paramLong, new Tokenizer(paramString), paramName2);
  }

  public static Record fromString(Name paramName1, int paramInt1, int paramInt2, long paramLong, Tokenizer paramTokenizer, Name paramName2)
    throws IOException
  {
    if (!paramName1.isAbsolute())
      throw new RelativeNameException(paramName1);
    Type.check(paramInt1);
    DClass.check(paramInt2);
    TTL.check(paramLong);
    Tokenizer.Token localToken1 = paramTokenizer.get();
    Record localRecord;
    if ((localToken1.type == 3) && (localToken1.value.equals("\\#")))
    {
      int i = paramTokenizer.getUInt16();
      byte[] arrayOfByte = paramTokenizer.getHex();
      if (arrayOfByte == null)
        arrayOfByte = new byte[0];
      if (i != arrayOfByte.length)
        throw paramTokenizer.exception("invalid unknown RR encoding: length mismatch");
      localRecord = newRecord(paramName1, paramInt1, paramInt2, paramLong, i, new DNSInput(arrayOfByte));
    }
    Tokenizer.Token localToken2;
    do
    {
      return localRecord;
      paramTokenizer.unget();
      localRecord = getEmptyRecord(paramName1, paramInt1, paramInt2, paramLong, true);
      localRecord.rdataFromString(paramTokenizer, paramName2);
      localToken2 = paramTokenizer.get();
    }
    while ((localToken2.type == 1) || (localToken2.type == 0));
    throw paramTokenizer.exception("unexpected tokens at end of record");
  }

  static Record fromWire(DNSInput paramDNSInput, int paramInt)
    throws IOException
  {
    return fromWire(paramDNSInput, paramInt, false);
  }

  static Record fromWire(DNSInput paramDNSInput, int paramInt, boolean paramBoolean)
    throws IOException
  {
    Name localName = new Name(paramDNSInput);
    int i = paramDNSInput.readU16();
    int j = paramDNSInput.readU16();
    if (paramInt == 0)
      return newRecord(localName, i, j);
    long l = paramDNSInput.readU32();
    int k = paramDNSInput.readU16();
    if ((k == 0) && (paramBoolean) && ((paramInt == 1) || (paramInt == 2)))
      return newRecord(localName, i, j, l);
    return newRecord(localName, i, j, l, k, paramDNSInput);
  }

  public static Record fromWire(byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    return fromWire(new DNSInput(paramArrayOfByte), paramInt, false);
  }

  private static final Record getEmptyRecord(Name paramName, int paramInt1, int paramInt2, long paramLong, boolean paramBoolean)
  {
    Object localObject;
    if (paramBoolean)
    {
      Record localRecord = Type.getProto(paramInt1);
      if (localRecord != null)
        localObject = localRecord.getObject();
    }
    while (true)
    {
      ((Record)localObject).name = paramName;
      ((Record)localObject).type = paramInt1;
      ((Record)localObject).dclass = paramInt2;
      ((Record)localObject).ttl = paramLong;
      return localObject;
      localObject = new UNKRecord();
      continue;
      localObject = new EmptyRecord();
    }
  }

  public static Record newRecord(Name paramName, int paramInt1, int paramInt2)
  {
    return newRecord(paramName, paramInt1, paramInt2, 0L);
  }

  public static Record newRecord(Name paramName, int paramInt1, int paramInt2, long paramLong)
  {
    if (!paramName.isAbsolute())
      throw new RelativeNameException(paramName);
    Type.check(paramInt1);
    DClass.check(paramInt2);
    TTL.check(paramLong);
    return getEmptyRecord(paramName, paramInt1, paramInt2, paramLong, false);
  }

  private static Record newRecord(Name paramName, int paramInt1, int paramInt2, long paramLong, int paramInt3, DNSInput paramDNSInput)
    throws IOException
  {
    if (paramDNSInput != null);
    Record localRecord;
    for (boolean bool = true; ; bool = false)
    {
      localRecord = getEmptyRecord(paramName, paramInt1, paramInt2, paramLong, bool);
      if (paramDNSInput == null)
        break label89;
      if (paramDNSInput.remaining() >= paramInt3)
        break;
      throw new WireParseException("truncated record");
    }
    paramDNSInput.setActive(paramInt3);
    localRecord.rrFromWire(paramDNSInput);
    if (paramDNSInput.remaining() > 0)
      throw new WireParseException("invalid record length");
    paramDNSInput.clearActive();
    label89: return localRecord;
  }

  public static Record newRecord(Name paramName, int paramInt1, int paramInt2, long paramLong, int paramInt3, byte[] paramArrayOfByte)
  {
    if (!paramName.isAbsolute())
      throw new RelativeNameException(paramName);
    Type.check(paramInt1);
    DClass.check(paramInt2);
    TTL.check(paramLong);
    DNSInput localDNSInput;
    if (paramArrayOfByte != null)
      localDNSInput = new DNSInput(paramArrayOfByte);
    try
    {
      while (true)
      {
        Record localRecord = newRecord(paramName, paramInt1, paramInt2, paramLong, paramInt3, localDNSInput);
        return localRecord;
        localDNSInput = null;
      }
    }
    catch (IOException localIOException)
    {
    }
    return null;
  }

  public static Record newRecord(Name paramName, int paramInt1, int paramInt2, long paramLong, byte[] paramArrayOfByte)
  {
    return newRecord(paramName, paramInt1, paramInt2, paramLong, paramArrayOfByte.length, paramArrayOfByte);
  }

  private void toWireCanonical(DNSOutput paramDNSOutput, boolean paramBoolean)
  {
    this.name.toWireCanonical(paramDNSOutput);
    paramDNSOutput.writeU16(this.type);
    paramDNSOutput.writeU16(this.dclass);
    if (paramBoolean)
      paramDNSOutput.writeU32(0L);
    while (true)
    {
      int i = paramDNSOutput.current();
      paramDNSOutput.writeU16(0);
      rrToWire(paramDNSOutput, null, true);
      paramDNSOutput.writeU16At(-2 + (paramDNSOutput.current() - i), i);
      return;
      paramDNSOutput.writeU32(this.ttl);
    }
  }

  private byte[] toWireCanonical(boolean paramBoolean)
  {
    DNSOutput localDNSOutput = new DNSOutput();
    toWireCanonical(localDNSOutput, paramBoolean);
    return localDNSOutput.toByteArray();
  }

  protected static String unknownToString(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("\\# ");
    localStringBuffer.append(paramArrayOfByte.length);
    localStringBuffer.append(" ");
    localStringBuffer.append(base16.toString(paramArrayOfByte));
    return localStringBuffer.toString();
  }

  Record cloneRecord()
  {
    try
    {
      Record localRecord = (Record)clone();
      return localRecord;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
    }
    throw new IllegalStateException();
  }

  public int compareTo(Object paramObject)
  {
    Record localRecord = (Record)paramObject;
    int i;
    if (this == localRecord)
      i = 0;
    do
    {
      do
      {
        do
        {
          return i;
          i = this.name.compareTo(localRecord.name);
        }
        while (i != 0);
        i = this.dclass - localRecord.dclass;
      }
      while (i != 0);
      i = this.type - localRecord.type;
    }
    while (i != 0);
    byte[] arrayOfByte1 = rdataToWireCanonical();
    byte[] arrayOfByte2 = localRecord.rdataToWireCanonical();
    for (int j = 0; ; j++)
    {
      if ((j >= arrayOfByte1.length) || (j >= arrayOfByte2.length))
        break label119;
      i = (0xFF & arrayOfByte1[j]) - (0xFF & arrayOfByte2[j]);
      if (i != 0)
        break;
    }
    label119: return arrayOfByte1.length - arrayOfByte2.length;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof Record)));
    Record localRecord;
    do
    {
      return false;
      localRecord = (Record)paramObject;
    }
    while ((this.type != localRecord.type) || (this.dclass != localRecord.dclass) || (!this.name.equals(localRecord.name)));
    return Arrays.equals(rdataToWireCanonical(), localRecord.rdataToWireCanonical());
  }

  public Name getAdditionalName()
  {
    return null;
  }

  public int getDClass()
  {
    return this.dclass;
  }

  public Name getName()
  {
    return this.name;
  }

  abstract Record getObject();

  public int getRRsetType()
  {
    if (this.type == 46)
      return ((RRSIGRecord)this).getTypeCovered();
    return this.type;
  }

  public long getTTL()
  {
    return this.ttl;
  }

  public int getType()
  {
    return this.type;
  }

  public int hashCode()
  {
    byte[] arrayOfByte = toWireCanonical(true);
    int i = 0;
    for (int j = 0; j < arrayOfByte.length; j++)
      i += (i << 3) + (0xFF & arrayOfByte[j]);
    return i;
  }

  abstract void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException;

  public String rdataToString()
  {
    return rrToString();
  }

  public byte[] rdataToWireCanonical()
  {
    DNSOutput localDNSOutput = new DNSOutput();
    rrToWire(localDNSOutput, null, true);
    return localDNSOutput.toByteArray();
  }

  abstract void rrFromWire(DNSInput paramDNSInput)
    throws IOException;

  abstract String rrToString();

  abstract void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean);

  public boolean sameRRset(Record paramRecord)
  {
    return (getRRsetType() == paramRecord.getRRsetType()) && (this.dclass == paramRecord.dclass) && (this.name.equals(paramRecord.name));
  }

  void setTTL(long paramLong)
  {
    this.ttl = paramLong;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.name);
    if (localStringBuffer.length() < 8)
      localStringBuffer.append("\t");
    if (localStringBuffer.length() < 16)
      localStringBuffer.append("\t");
    localStringBuffer.append("\t");
    if (Options.check("BINDTTL"))
      localStringBuffer.append(TTL.format(this.ttl));
    while (true)
    {
      localStringBuffer.append("\t");
      if ((this.dclass != 1) || (!Options.check("noPrintIN")))
      {
        localStringBuffer.append(DClass.string(this.dclass));
        localStringBuffer.append("\t");
      }
      localStringBuffer.append(Type.string(this.type));
      String str = rrToString();
      if (!str.equals(""))
      {
        localStringBuffer.append("\t");
        localStringBuffer.append(str);
      }
      return localStringBuffer.toString();
      localStringBuffer.append(this.ttl);
    }
  }

  void toWire(DNSOutput paramDNSOutput, int paramInt, Compression paramCompression)
  {
    this.name.toWire(paramDNSOutput, paramCompression);
    paramDNSOutput.writeU16(this.type);
    paramDNSOutput.writeU16(this.dclass);
    if (paramInt == 0)
      return;
    paramDNSOutput.writeU32(this.ttl);
    int i = paramDNSOutput.current();
    paramDNSOutput.writeU16(0);
    rrToWire(paramDNSOutput, paramCompression, false);
    paramDNSOutput.writeU16At(-2 + (paramDNSOutput.current() - i), i);
  }

  public byte[] toWire(int paramInt)
  {
    DNSOutput localDNSOutput = new DNSOutput();
    toWire(localDNSOutput, paramInt, null);
    return localDNSOutput.toByteArray();
  }

  public byte[] toWireCanonical()
  {
    return toWireCanonical(false);
  }

  Record withDClass(int paramInt, long paramLong)
  {
    Record localRecord = cloneRecord();
    localRecord.dclass = paramInt;
    localRecord.ttl = paramLong;
    return localRecord;
  }

  public Record withName(Name paramName)
  {
    if (!paramName.isAbsolute())
      throw new RelativeNameException(paramName);
    Record localRecord = cloneRecord();
    localRecord.name = paramName;
    return localRecord;
  }
}