package org.xbill.DNS;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import org.xbill.DNS.utils.base64;

public class IPSECKEYRecord extends Record
{
  private static final long serialVersionUID = 3050449702765909687L;
  private int algorithmType;
  private Object gateway;
  private int gatewayType;
  private byte[] key;
  private int precedence;

  IPSECKEYRecord()
  {
  }

  public IPSECKEYRecord(Name paramName, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, Object paramObject, byte[] paramArrayOfByte)
  {
    super(paramName, 45, paramInt1, paramLong);
    this.precedence = checkU8("precedence", paramInt2);
    this.gatewayType = checkU8("gatewayType", paramInt3);
    this.algorithmType = checkU8("algorithmType", paramInt4);
    switch (paramInt3)
    {
    default:
      throw new IllegalArgumentException("\"gatewayType\" must be between 0 and 3");
    case 0:
      this.gateway = null;
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      this.key = paramArrayOfByte;
      return;
      if (!(paramObject instanceof InetAddress))
        throw new IllegalArgumentException("\"gateway\" must be an IPv4 address");
      this.gateway = paramObject;
      continue;
      if (!(paramObject instanceof Inet6Address))
        throw new IllegalArgumentException("\"gateway\" must be an IPv6 address");
      this.gateway = paramObject;
      continue;
      if (!(paramObject instanceof Name))
        throw new IllegalArgumentException("\"gateway\" must be a DNS name");
      this.gateway = checkName("gateway", (Name)paramObject);
    }
  }

  public int getAlgorithmType()
  {
    return this.algorithmType;
  }

  public Object getGateway()
  {
    return this.gateway;
  }

  public int getGatewayType()
  {
    return this.gatewayType;
  }

  public byte[] getKey()
  {
    return this.key;
  }

  Record getObject()
  {
    return new IPSECKEYRecord();
  }

  public int getPrecedence()
  {
    return this.precedence;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.precedence = paramTokenizer.getUInt8();
    this.gatewayType = paramTokenizer.getUInt8();
    this.algorithmType = paramTokenizer.getUInt8();
    switch (this.gatewayType)
    {
    default:
      throw new WireParseException("invalid gateway type");
    case 0:
      if (!paramTokenizer.getString().equals("."))
        throw new TextParseException("invalid gateway format");
      this.gateway = null;
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      this.key = paramTokenizer.getBase64(false);
      return;
      this.gateway = paramTokenizer.getAddress(1);
      continue;
      this.gateway = paramTokenizer.getAddress(2);
      continue;
      this.gateway = paramTokenizer.getName(paramName);
    }
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.precedence = paramDNSInput.readU8();
    this.gatewayType = paramDNSInput.readU8();
    this.algorithmType = paramDNSInput.readU8();
    switch (this.gatewayType)
    {
    default:
      throw new WireParseException("invalid gateway type");
    case 0:
      this.gateway = null;
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      if (paramDNSInput.remaining() > 0)
        this.key = paramDNSInput.readByteArray();
      return;
      this.gateway = InetAddress.getByAddress(paramDNSInput.readByteArray(4));
      continue;
      this.gateway = InetAddress.getByAddress(paramDNSInput.readByteArray(16));
      continue;
      this.gateway = new Name(paramDNSInput);
    }
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.precedence);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.gatewayType);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.algorithmType);
    localStringBuffer.append(" ");
    switch (this.gatewayType)
    {
    default:
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      if (this.key != null)
      {
        localStringBuffer.append(" ");
        localStringBuffer.append(base64.toString(this.key));
      }
      return localStringBuffer.toString();
      localStringBuffer.append(".");
      continue;
      localStringBuffer.append(((InetAddress)this.gateway).getHostAddress());
      continue;
      localStringBuffer.append(this.gateway);
    }
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU8(this.precedence);
    paramDNSOutput.writeU8(this.gatewayType);
    paramDNSOutput.writeU8(this.algorithmType);
    switch (this.gatewayType)
    {
    case 0:
    default:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      if (this.key != null)
        paramDNSOutput.writeByteArray(this.key);
      return;
      paramDNSOutput.writeByteArray(((InetAddress)this.gateway).getAddress());
      continue;
      ((Name)this.gateway).toWire(paramDNSOutput, null, paramBoolean);
    }
  }

  public static class Algorithm
  {
    public static final int DSA = 1;
    public static final int RSA = 2;
  }

  public static class Gateway
  {
    public static final int IPv4 = 1;
    public static final int IPv6 = 2;
    public static final int Name = 3;
    public static final int None;
  }
}