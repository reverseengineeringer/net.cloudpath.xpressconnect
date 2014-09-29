package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ARecord extends Record
{
  private static final long serialVersionUID = -2172609200849142323L;
  private int addr;

  ARecord()
  {
  }

  public ARecord(Name paramName, int paramInt, long paramLong, InetAddress paramInetAddress)
  {
    super(paramName, 1, paramInt, paramLong);
    if (Address.familyOf(paramInetAddress) != 1)
      throw new IllegalArgumentException("invalid IPv4 address");
    this.addr = fromArray(paramInetAddress.getAddress());
  }

  private static final int fromArray(byte[] paramArrayOfByte)
  {
    return (0xFF & paramArrayOfByte[0]) << 24 | (0xFF & paramArrayOfByte[1]) << 16 | (0xFF & paramArrayOfByte[2]) << 8 | 0xFF & paramArrayOfByte[3];
  }

  private static final byte[] toArray(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = ((byte)(0xFF & paramInt >>> 24));
    arrayOfByte[1] = ((byte)(0xFF & paramInt >>> 16));
    arrayOfByte[2] = ((byte)(0xFF & paramInt >>> 8));
    arrayOfByte[3] = ((byte)(paramInt & 0xFF));
    return arrayOfByte;
  }

  public InetAddress getAddress()
  {
    try
    {
      InetAddress localInetAddress = InetAddress.getByAddress(toArray(this.addr));
      return localInetAddress;
    }
    catch (UnknownHostException localUnknownHostException)
    {
    }
    return null;
  }

  Record getObject()
  {
    return new ARecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.addr = fromArray(paramTokenizer.getAddress(1).getAddress());
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.addr = fromArray(paramDNSInput.readByteArray(4));
  }

  String rrToString()
  {
    return Address.toDottedQuad(toArray(this.addr));
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU32(0xFFFFFFFF & this.addr);
  }
}