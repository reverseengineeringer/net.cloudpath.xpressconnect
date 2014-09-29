package org.xbill.DNS;

import java.io.IOException;
import java.util.Date;
import org.xbill.DNS.utils.base64;

public class TSIGRecord extends Record
{
  private static final long serialVersionUID = -88820909016649306L;
  private Name alg;
  private int error;
  private int fudge;
  private int originalID;
  private byte[] other;
  private byte[] signature;
  private Date timeSigned;

  TSIGRecord()
  {
  }

  public TSIGRecord(Name paramName1, int paramInt1, long paramLong, Name paramName2, Date paramDate, int paramInt2, byte[] paramArrayOfByte1, int paramInt3, int paramInt4, byte[] paramArrayOfByte2)
  {
    super(paramName1, 250, paramInt1, paramLong);
    this.alg = checkName("alg", paramName2);
    this.timeSigned = paramDate;
    this.fudge = checkU16("fudge", paramInt2);
    this.signature = paramArrayOfByte1;
    this.originalID = checkU16("originalID", paramInt3);
    this.error = checkU16("error", paramInt4);
    this.other = paramArrayOfByte2;
  }

  public Name getAlgorithm()
  {
    return this.alg;
  }

  public int getError()
  {
    return this.error;
  }

  public int getFudge()
  {
    return this.fudge;
  }

  Record getObject()
  {
    return new TSIGRecord();
  }

  public int getOriginalID()
  {
    return this.originalID;
  }

  public byte[] getOther()
  {
    return this.other;
  }

  public byte[] getSignature()
  {
    return this.signature;
  }

  public Date getTimeSigned()
  {
    return this.timeSigned;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    throw paramTokenizer.exception("no text format defined for TSIG");
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.alg = new Name(paramDNSInput);
    long l = paramDNSInput.readU16();
    this.timeSigned = new Date(1000L * (paramDNSInput.readU32() + (l << 32)));
    this.fudge = paramDNSInput.readU16();
    this.signature = paramDNSInput.readByteArray(paramDNSInput.readU16());
    this.originalID = paramDNSInput.readU16();
    this.error = paramDNSInput.readU16();
    int i = paramDNSInput.readU16();
    if (i > 0)
    {
      this.other = paramDNSInput.readByteArray(i);
      return;
    }
    this.other = null;
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.alg);
    localStringBuffer.append(" ");
    if (Options.check("multiline"))
      localStringBuffer.append("(\n\t");
    localStringBuffer.append(this.timeSigned.getTime() / 1000L);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.fudge);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.signature.length);
    if (Options.check("multiline"))
    {
      localStringBuffer.append("\n");
      localStringBuffer.append(base64.formatString(this.signature, 64, "\t", false));
      localStringBuffer.append(" ");
      localStringBuffer.append(Rcode.TSIGstring(this.error));
      localStringBuffer.append(" ");
      if (this.other != null)
        break label201;
      localStringBuffer.append(0);
    }
    while (true)
    {
      if (Options.check("multiline"))
        localStringBuffer.append(" )");
      return localStringBuffer.toString();
      localStringBuffer.append(" ");
      localStringBuffer.append(base64.toString(this.signature));
      break;
      label201: localStringBuffer.append(this.other.length);
      if (Options.check("multiline"))
        localStringBuffer.append("\n\n\n\t");
      while (true)
        if (this.error == 18)
        {
          if (this.other.length != 6)
          {
            localStringBuffer.append("<invalid BADTIME other data>");
            break;
            localStringBuffer.append(" ");
            continue;
          }
          long l = ((0xFF & this.other[0]) << 40) + ((0xFF & this.other[1]) << 32) + ((0xFF & this.other[2]) << 24) + ((0xFF & this.other[3]) << 16) + ((0xFF & this.other[4]) << 8) + (0xFF & this.other[5]);
          localStringBuffer.append("<server time: ");
          localStringBuffer.append(new Date(l * 1000L));
          localStringBuffer.append(">");
          break;
        }
      localStringBuffer.append("<");
      localStringBuffer.append(base64.toString(this.other));
      localStringBuffer.append(">");
    }
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    this.alg.toWire(paramDNSOutput, null, paramBoolean);
    long l1 = this.timeSigned.getTime() / 1000L;
    int i = (int)(l1 >> 32);
    long l2 = l1 & 0xFFFFFFFF;
    paramDNSOutput.writeU16(i);
    paramDNSOutput.writeU32(l2);
    paramDNSOutput.writeU16(this.fudge);
    paramDNSOutput.writeU16(this.signature.length);
    paramDNSOutput.writeByteArray(this.signature);
    paramDNSOutput.writeU16(this.originalID);
    paramDNSOutput.writeU16(this.error);
    if (this.other != null)
    {
      paramDNSOutput.writeU16(this.other.length);
      paramDNSOutput.writeByteArray(this.other);
      return;
    }
    paramDNSOutput.writeU16(0);
  }
}