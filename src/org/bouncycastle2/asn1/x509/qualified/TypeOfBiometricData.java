package org.bouncycastle2.asn1.x509.qualified;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;

public class TypeOfBiometricData extends ASN1Encodable
  implements ASN1Choice
{
  public static final int HANDWRITTEN_SIGNATURE = 1;
  public static final int PICTURE;
  DEREncodable obj;

  public TypeOfBiometricData(int paramInt)
  {
    if ((paramInt == 0) || (paramInt == 1))
    {
      this.obj = new DERInteger(paramInt);
      return;
    }
    throw new IllegalArgumentException("unknow PredefinedBiometricType : " + paramInt);
  }

  public TypeOfBiometricData(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.obj = paramDERObjectIdentifier;
  }

  public static TypeOfBiometricData getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof TypeOfBiometricData)))
      return (TypeOfBiometricData)paramObject;
    if ((paramObject instanceof DERInteger))
      return new TypeOfBiometricData(DERInteger.getInstance(paramObject).getValue().intValue());
    if ((paramObject instanceof DERObjectIdentifier))
      return new TypeOfBiometricData(DERObjectIdentifier.getInstance(paramObject));
    throw new IllegalArgumentException("unknown object in getInstance");
  }

  public DERObjectIdentifier getBiometricDataOid()
  {
    return (DERObjectIdentifier)this.obj;
  }

  public int getPredefinedBiometricType()
  {
    return ((DERInteger)this.obj).getValue().intValue();
  }

  public boolean isPredefined()
  {
    return this.obj instanceof DERInteger;
  }

  public DERObject toASN1Object()
  {
    return this.obj.getDERObject();
  }
}