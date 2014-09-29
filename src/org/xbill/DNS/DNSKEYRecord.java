package org.xbill.DNS;

import java.io.IOException;
import java.security.PublicKey;

public class DNSKEYRecord extends KEYBase
{
  private static final long serialVersionUID = -8679800040426675002L;

  DNSKEYRecord()
  {
  }

  public DNSKEYRecord(Name paramName, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, PublicKey paramPublicKey)
    throws DNSSEC.DNSSECException
  {
    super(paramName, 48, paramInt1, paramLong, paramInt2, paramInt3, paramInt4, DNSSEC.fromPublicKey(paramPublicKey, paramInt4));
    this.publicKey = paramPublicKey;
  }

  public DNSKEYRecord(Name paramName, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
  {
    super(paramName, 48, paramInt1, paramLong, paramInt2, paramInt3, paramInt4, paramArrayOfByte);
  }

  Record getObject()
  {
    return new DNSKEYRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.flags = paramTokenizer.getUInt16();
    this.proto = paramTokenizer.getUInt8();
    String str = paramTokenizer.getString();
    this.alg = DNSSEC.Algorithm.value(str);
    if (this.alg < 0)
      throw paramTokenizer.exception("Invalid algorithm: " + str);
    this.key = paramTokenizer.getBase64();
  }

  public static class Flags
  {
    public static final int REVOKE = 128;
    public static final int SEP_KEY = 1;
    public static final int ZONE_KEY = 256;
  }

  public static class Protocol
  {
    public static final int DNSSEC = 3;
  }
}