package org.bouncycastle2.math.ec;

import java.math.BigInteger;

class SimpleBigDecimal
{
  private static final long serialVersionUID = 1L;
  private final BigInteger bigInt;
  private final int scale;

  public SimpleBigDecimal(BigInteger paramBigInteger, int paramInt)
  {
    if (paramInt < 0)
      throw new IllegalArgumentException("scale may not be negative");
    this.bigInt = paramBigInteger;
    this.scale = paramInt;
  }

  private SimpleBigDecimal(SimpleBigDecimal paramSimpleBigDecimal)
  {
    this.bigInt = paramSimpleBigDecimal.bigInt;
    this.scale = paramSimpleBigDecimal.scale;
  }

  private void checkScale(SimpleBigDecimal paramSimpleBigDecimal)
  {
    if (this.scale != paramSimpleBigDecimal.scale)
      throw new IllegalArgumentException("Only SimpleBigDecimal of same scale allowed in arithmetic operations");
  }

  public static SimpleBigDecimal getInstance(BigInteger paramBigInteger, int paramInt)
  {
    return new SimpleBigDecimal(paramBigInteger.shiftLeft(paramInt), paramInt);
  }

  public SimpleBigDecimal add(BigInteger paramBigInteger)
  {
    return new SimpleBigDecimal(this.bigInt.add(paramBigInteger.shiftLeft(this.scale)), this.scale);
  }

  public SimpleBigDecimal add(SimpleBigDecimal paramSimpleBigDecimal)
  {
    checkScale(paramSimpleBigDecimal);
    return new SimpleBigDecimal(this.bigInt.add(paramSimpleBigDecimal.bigInt), this.scale);
  }

  public SimpleBigDecimal adjustScale(int paramInt)
  {
    if (paramInt < 0)
      throw new IllegalArgumentException("scale may not be negative");
    if (paramInt == this.scale)
      return new SimpleBigDecimal(this);
    return new SimpleBigDecimal(this.bigInt.shiftLeft(paramInt - this.scale), paramInt);
  }

  public int compareTo(BigInteger paramBigInteger)
  {
    return this.bigInt.compareTo(paramBigInteger.shiftLeft(this.scale));
  }

  public int compareTo(SimpleBigDecimal paramSimpleBigDecimal)
  {
    checkScale(paramSimpleBigDecimal);
    return this.bigInt.compareTo(paramSimpleBigDecimal.bigInt);
  }

  public SimpleBigDecimal divide(BigInteger paramBigInteger)
  {
    return new SimpleBigDecimal(this.bigInt.divide(paramBigInteger), this.scale);
  }

  public SimpleBigDecimal divide(SimpleBigDecimal paramSimpleBigDecimal)
  {
    checkScale(paramSimpleBigDecimal);
    return new SimpleBigDecimal(this.bigInt.shiftLeft(this.scale).divide(paramSimpleBigDecimal.bigInt), this.scale);
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject);
    SimpleBigDecimal localSimpleBigDecimal;
    do
    {
      return true;
      if (!(paramObject instanceof SimpleBigDecimal))
        return false;
      localSimpleBigDecimal = (SimpleBigDecimal)paramObject;
    }
    while ((this.bigInt.equals(localSimpleBigDecimal.bigInt)) && (this.scale == localSimpleBigDecimal.scale));
    return false;
  }

  public BigInteger floor()
  {
    return this.bigInt.shiftRight(this.scale);
  }

  public int getScale()
  {
    return this.scale;
  }

  public int hashCode()
  {
    return this.bigInt.hashCode() ^ this.scale;
  }

  public int intValue()
  {
    return floor().intValue();
  }

  public long longValue()
  {
    return floor().longValue();
  }

  public SimpleBigDecimal multiply(BigInteger paramBigInteger)
  {
    return new SimpleBigDecimal(this.bigInt.multiply(paramBigInteger), this.scale);
  }

  public SimpleBigDecimal multiply(SimpleBigDecimal paramSimpleBigDecimal)
  {
    checkScale(paramSimpleBigDecimal);
    return new SimpleBigDecimal(this.bigInt.multiply(paramSimpleBigDecimal.bigInt), this.scale + this.scale);
  }

  public SimpleBigDecimal negate()
  {
    return new SimpleBigDecimal(this.bigInt.negate(), this.scale);
  }

  public BigInteger round()
  {
    return add(new SimpleBigDecimal(ECConstants.ONE, 1).adjustScale(this.scale)).floor();
  }

  public SimpleBigDecimal shiftLeft(int paramInt)
  {
    return new SimpleBigDecimal(this.bigInt.shiftLeft(paramInt), this.scale);
  }

  public SimpleBigDecimal subtract(BigInteger paramBigInteger)
  {
    return new SimpleBigDecimal(this.bigInt.subtract(paramBigInteger.shiftLeft(this.scale)), this.scale);
  }

  public SimpleBigDecimal subtract(SimpleBigDecimal paramSimpleBigDecimal)
  {
    return add(paramSimpleBigDecimal.negate());
  }

  public String toString()
  {
    if (this.scale == 0)
      return this.bigInt.toString();
    BigInteger localBigInteger1 = floor();
    BigInteger localBigInteger2 = this.bigInt.subtract(localBigInteger1.shiftLeft(this.scale));
    if (this.bigInt.signum() == -1)
      localBigInteger2 = ECConstants.ONE.shiftLeft(this.scale).subtract(localBigInteger2);
    if ((localBigInteger1.signum() == -1) && (!localBigInteger2.equals(ECConstants.ZERO)))
      localBigInteger1 = localBigInteger1.add(ECConstants.ONE);
    String str1 = localBigInteger1.toString();
    char[] arrayOfChar = new char[this.scale];
    String str2 = localBigInteger2.toString(2);
    int i = str2.length();
    int j = this.scale - i;
    int k = 0;
    if (k >= j);
    for (int m = 0; ; m++)
    {
      if (m >= i)
      {
        String str3 = new String(arrayOfChar);
        StringBuffer localStringBuffer = new StringBuffer(str1);
        localStringBuffer.append(".");
        localStringBuffer.append(str3);
        return localStringBuffer.toString();
        arrayOfChar[k] = '0';
        k++;
        break;
      }
      arrayOfChar[(j + m)] = str2.charAt(m);
    }
  }
}