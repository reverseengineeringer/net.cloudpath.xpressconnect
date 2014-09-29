package org.xbill.DNS;

import java.io.IOException;
import java.util.Date;
import org.xbill.DNS.utils.base64;

public class TKEYRecord extends Record
{
  public static final int DELETE = 5;
  public static final int DIFFIEHELLMAN = 2;
  public static final int GSSAPI = 3;
  public static final int RESOLVERASSIGNED = 4;
  public static final int SERVERASSIGNED = 1;
  private static final long serialVersionUID = 8828458121926391756L;
  private Name alg;
  private int error;
  private byte[] key;
  private int mode;
  private byte[] other;
  private Date timeExpire;
  private Date timeInception;

  TKEYRecord()
  {
  }

  public TKEYRecord(Name paramName1, int paramInt1, long paramLong, Name paramName2, Date paramDate1, Date paramDate2, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    super(paramName1, 249, paramInt1, paramLong);
    this.alg = checkName("alg", paramName2);
    this.timeInception = paramDate1;
    this.timeExpire = paramDate2;
    this.mode = checkU16("mode", paramInt2);
    this.error = checkU16("error", paramInt3);
    this.key = paramArrayOfByte1;
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

  public byte[] getKey()
  {
    return this.key;
  }

  public int getMode()
  {
    return this.mode;
  }

  Record getObject()
  {
    return new TKEYRecord();
  }

  public byte[] getOther()
  {
    return this.other;
  }

  public Date getTimeExpire()
  {
    return this.timeExpire;
  }

  public Date getTimeInception()
  {
    return this.timeInception;
  }

  protected String modeString()
  {
    switch (this.mode)
    {
    default:
      return Integer.toString(this.mode);
    case 1:
      return "SERVERASSIGNED";
    case 2:
      return "DIFFIEHELLMAN";
    case 3:
      return "GSSAPI";
    case 4:
      return "RESOLVERASSIGNED";
    case 5:
    }
    return "DELETE";
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    throw paramTokenizer.exception("no text format defined for TKEY");
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.alg = new Name(paramDNSInput);
    this.timeInception = new Date(1000L * paramDNSInput.readU32());
    this.timeExpire = new Date(1000L * paramDNSInput.readU32());
    this.mode = paramDNSInput.readU16();
    this.error = paramDNSInput.readU16();
    int i = paramDNSInput.readU16();
    if (i > 0);
    for (this.key = paramDNSInput.readByteArray(i); ; this.key = null)
    {
      int j = paramDNSInput.readU16();
      if (j <= 0)
        break;
      this.other = paramDNSInput.readByteArray(j);
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
    localStringBuffer.append(FormattedTime.format(this.timeInception));
    localStringBuffer.append(" ");
    localStringBuffer.append(FormattedTime.format(this.timeExpire));
    localStringBuffer.append(" ");
    localStringBuffer.append(modeString());
    localStringBuffer.append(" ");
    localStringBuffer.append(Rcode.TSIGstring(this.error));
    if (Options.check("multiline"))
    {
      localStringBuffer.append("\n");
      if (this.key != null)
      {
        localStringBuffer.append(base64.formatString(this.key, 64, "\t", false));
        localStringBuffer.append("\n");
      }
      if (this.other != null)
        localStringBuffer.append(base64.formatString(this.other, 64, "\t", false));
      localStringBuffer.append(" )");
    }
    while (true)
    {
      return localStringBuffer.toString();
      localStringBuffer.append(" ");
      if (this.key != null)
      {
        localStringBuffer.append(base64.toString(this.key));
        localStringBuffer.append(" ");
      }
      if (this.other != null)
        localStringBuffer.append(base64.toString(this.other));
    }
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    this.alg.toWire(paramDNSOutput, null, paramBoolean);
    paramDNSOutput.writeU32(this.timeInception.getTime() / 1000L);
    paramDNSOutput.writeU32(this.timeExpire.getTime() / 1000L);
    paramDNSOutput.writeU16(this.mode);
    paramDNSOutput.writeU16(this.error);
    if (this.key != null)
    {
      paramDNSOutput.writeU16(this.key.length);
      paramDNSOutput.writeByteArray(this.key);
    }
    while (this.other != null)
    {
      paramDNSOutput.writeU16(this.other.length);
      paramDNSOutput.writeByteArray(this.other);
      return;
      paramDNSOutput.writeU16(0);
    }
    paramDNSOutput.writeU16(0);
  }
}