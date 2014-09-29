package org.bouncycastle2.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle2.asn1.DERInteger;

public class CRLNumber extends DERInteger
{
  public CRLNumber(BigInteger paramBigInteger)
  {
    super(paramBigInteger);
  }

  public BigInteger getCRLNumber()
  {
    return getPositiveValue();
  }

  public String toString()
  {
    return "CRLNumber: " + getCRLNumber();
  }
}