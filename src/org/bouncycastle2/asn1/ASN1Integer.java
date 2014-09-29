package org.bouncycastle2.asn1;

import java.math.BigInteger;

public class ASN1Integer extends DERInteger
{
  public ASN1Integer(int paramInt)
  {
    super(paramInt);
  }

  public ASN1Integer(BigInteger paramBigInteger)
  {
    super(paramBigInteger);
  }

  ASN1Integer(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }
}