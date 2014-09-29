package org.xbill.DNS;

import java.io.IOException;

public class SRVRecord extends Record
{
  private static final long serialVersionUID = -3886460132387522052L;
  private int port;
  private int priority;
  private Name target;
  private int weight;

  SRVRecord()
  {
  }

  public SRVRecord(Name paramName1, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, Name paramName2)
  {
    super(paramName1, 33, paramInt1, paramLong);
    this.priority = checkU16("priority", paramInt2);
    this.weight = checkU16("weight", paramInt3);
    this.port = checkU16("port", paramInt4);
    this.target = checkName("target", paramName2);
  }

  public Name getAdditionalName()
  {
    return this.target;
  }

  Record getObject()
  {
    return new SRVRecord();
  }

  public int getPort()
  {
    return this.port;
  }

  public int getPriority()
  {
    return this.priority;
  }

  public Name getTarget()
  {
    return this.target;
  }

  public int getWeight()
  {
    return this.weight;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.priority = paramTokenizer.getUInt16();
    this.weight = paramTokenizer.getUInt16();
    this.port = paramTokenizer.getUInt16();
    this.target = paramTokenizer.getName(paramName);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.priority = paramDNSInput.readU16();
    this.weight = paramDNSInput.readU16();
    this.port = paramDNSInput.readU16();
    this.target = new Name(paramDNSInput);
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.priority + " ");
    localStringBuffer.append(this.weight + " ");
    localStringBuffer.append(this.port + " ");
    localStringBuffer.append(this.target);
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU16(this.priority);
    paramDNSOutput.writeU16(this.weight);
    paramDNSOutput.writeU16(this.port);
    this.target.toWire(paramDNSOutput, null, paramBoolean);
  }
}