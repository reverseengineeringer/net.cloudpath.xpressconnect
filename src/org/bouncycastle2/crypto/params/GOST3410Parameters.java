package org.bouncycastle2.crypto.params;

import java.math.BigInteger;
import org.bouncycastle2.crypto.CipherParameters;

public class GOST3410Parameters
  implements CipherParameters
{
  private BigInteger a;
  private BigInteger p;
  private BigInteger q;
  private GOST3410ValidationParameters validation;

  public GOST3410Parameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    this.p = paramBigInteger1;
    this.q = paramBigInteger2;
    this.a = paramBigInteger3;
  }

  public GOST3410Parameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, GOST3410ValidationParameters paramGOST3410ValidationParameters)
  {
    this.a = paramBigInteger3;
    this.p = paramBigInteger1;
    this.q = paramBigInteger2;
    this.validation = paramGOST3410ValidationParameters;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof GOST3410Parameters));
    GOST3410Parameters localGOST3410Parameters;
    do
    {
      return false;
      localGOST3410Parameters = (GOST3410Parameters)paramObject;
    }
    while ((!localGOST3410Parameters.getP().equals(this.p)) || (!localGOST3410Parameters.getQ().equals(this.q)) || (!localGOST3410Parameters.getA().equals(this.a)));
    return true;
  }

  public BigInteger getA()
  {
    return this.a;
  }

  public BigInteger getP()
  {
    return this.p;
  }

  public BigInteger getQ()
  {
    return this.q;
  }

  public GOST3410ValidationParameters getValidationParameters()
  {
    return this.validation;
  }

  public int hashCode()
  {
    return this.p.hashCode() ^ this.q.hashCode() ^ this.a.hashCode();
  }
}