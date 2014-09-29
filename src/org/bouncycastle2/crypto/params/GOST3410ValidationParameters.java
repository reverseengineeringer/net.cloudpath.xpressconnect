package org.bouncycastle2.crypto.params;

public class GOST3410ValidationParameters
{
  private int c;
  private long cL;
  private int x0;
  private long x0L;

  public GOST3410ValidationParameters(int paramInt1, int paramInt2)
  {
    this.x0 = paramInt1;
    this.c = paramInt2;
  }

  public GOST3410ValidationParameters(long paramLong1, long paramLong2)
  {
    this.x0L = paramLong1;
    this.cL = paramLong2;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof GOST3410ValidationParameters));
    GOST3410ValidationParameters localGOST3410ValidationParameters;
    do
    {
      return false;
      localGOST3410ValidationParameters = (GOST3410ValidationParameters)paramObject;
    }
    while ((localGOST3410ValidationParameters.c != this.c) || (localGOST3410ValidationParameters.x0 != this.x0) || (localGOST3410ValidationParameters.cL != this.cL) || (localGOST3410ValidationParameters.x0L != this.x0L));
    return true;
  }

  public int getC()
  {
    return this.c;
  }

  public long getCL()
  {
    return this.cL;
  }

  public int getX0()
  {
    return this.x0;
  }

  public long getX0L()
  {
    return this.x0L;
  }

  public int hashCode()
  {
    return this.x0 ^ this.c ^ (int)this.x0L ^ (int)(this.x0L >> 32) ^ (int)this.cL ^ (int)(this.cL >> 32);
  }
}