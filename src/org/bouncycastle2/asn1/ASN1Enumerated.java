package org.bouncycastle2.asn1;

import java.math.BigInteger;

public class ASN1Enumerated extends DEREnumerated
{
  public ASN1Enumerated(int paramInt)
  {
    super(paramInt);
  }

  public ASN1Enumerated(BigInteger paramBigInteger)
  {
    super(paramBigInteger);
  }

  ASN1Enumerated(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }
}