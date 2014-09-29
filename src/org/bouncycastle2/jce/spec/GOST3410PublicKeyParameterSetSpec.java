package org.bouncycastle2.jce.spec;

import java.math.BigInteger;

public class GOST3410PublicKeyParameterSetSpec
{
  private BigInteger a;
  private BigInteger p;
  private BigInteger q;

  public GOST3410PublicKeyParameterSetSpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    this.p = paramBigInteger1;
    this.q = paramBigInteger2;
    this.a = paramBigInteger3;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof GOST3410PublicKeyParameterSetSpec;
    boolean bool2 = false;
    if (bool1)
    {
      GOST3410PublicKeyParameterSetSpec localGOST3410PublicKeyParameterSetSpec = (GOST3410PublicKeyParameterSetSpec)paramObject;
      boolean bool3 = this.a.equals(localGOST3410PublicKeyParameterSetSpec.a);
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = this.p.equals(localGOST3410PublicKeyParameterSetSpec.p);
        bool2 = false;
        if (bool4)
        {
          boolean bool5 = this.q.equals(localGOST3410PublicKeyParameterSetSpec.q);
          bool2 = false;
          if (bool5)
            bool2 = true;
        }
      }
    }
    return bool2;
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

  public int hashCode()
  {
    return this.a.hashCode() ^ this.p.hashCode() ^ this.q.hashCode();
  }
}