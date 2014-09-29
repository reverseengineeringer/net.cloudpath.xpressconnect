package org.bouncycastle2.asn1;

import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle2.util.Arrays;

public class DERInteger extends ASN1Object
{
  byte[] bytes;

  public DERInteger(int paramInt)
  {
    this.bytes = BigInteger.valueOf(paramInt).toByteArray();
  }

  public DERInteger(BigInteger paramBigInteger)
  {
    this.bytes = paramBigInteger.toByteArray();
  }

  public DERInteger(byte[] paramArrayOfByte)
  {
    this.bytes = paramArrayOfByte;
  }

  public static DERInteger getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERInteger)))
      return (DERInteger)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERInteger getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if ((paramBoolean) || ((localDERObject instanceof DERInteger)))
      return getInstance(localDERObject);
    return new ASN1Integer(ASN1OctetString.getInstance(paramASN1TaggedObject.getObject()).getOctets());
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERInteger))
      return false;
    DERInteger localDERInteger = (DERInteger)paramDERObject;
    return Arrays.areEqual(this.bytes, localDERInteger.bytes);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(2, this.bytes);
  }

  public BigInteger getPositiveValue()
  {
    return new BigInteger(1, this.bytes);
  }

  public BigInteger getValue()
  {
    return new BigInteger(this.bytes);
  }

  public int hashCode()
  {
    int i = 0;
    for (int j = 0; ; j++)
    {
      if (j == this.bytes.length)
        return i;
      i ^= (0xFF & this.bytes[j]) << j % 4;
    }
  }

  public String toString()
  {
    return getValue().toString();
  }
}