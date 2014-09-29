package org.bouncycastle2.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle2.math.ec.ECCurve;
import org.bouncycastle2.math.ec.ECFieldElement;

public class X9IntegerConverter
{
  public int getByteLength(ECCurve paramECCurve)
  {
    return (7 + paramECCurve.getFieldSize()) / 8;
  }

  public int getByteLength(ECFieldElement paramECFieldElement)
  {
    return (7 + paramECFieldElement.getFieldSize()) / 8;
  }

  public byte[] integerToBytes(BigInteger paramBigInteger, int paramInt)
  {
    byte[] arrayOfByte1 = paramBigInteger.toByteArray();
    if (paramInt < arrayOfByte1.length)
    {
      byte[] arrayOfByte3 = new byte[paramInt];
      System.arraycopy(arrayOfByte1, arrayOfByte1.length - arrayOfByte3.length, arrayOfByte3, 0, arrayOfByte3.length);
      return arrayOfByte3;
    }
    if (paramInt > arrayOfByte1.length)
    {
      byte[] arrayOfByte2 = new byte[paramInt];
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, arrayOfByte2.length - arrayOfByte1.length, arrayOfByte1.length);
      return arrayOfByte2;
    }
    return arrayOfByte1;
  }
}