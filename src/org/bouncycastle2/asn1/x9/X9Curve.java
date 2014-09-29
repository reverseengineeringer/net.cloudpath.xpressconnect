package org.bouncycastle2.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.math.ec.ECCurve;
import org.bouncycastle2.math.ec.ECCurve.F2m;
import org.bouncycastle2.math.ec.ECCurve.Fp;
import org.bouncycastle2.math.ec.ECFieldElement;

public class X9Curve extends ASN1Encodable
  implements X9ObjectIdentifiers
{
  private ECCurve curve;
  private DERObjectIdentifier fieldIdentifier = null;
  private byte[] seed;

  public X9Curve(X9FieldID paramX9FieldID, ASN1Sequence paramASN1Sequence)
  {
    this.fieldIdentifier = paramX9FieldID.getIdentifier();
    if (this.fieldIdentifier.equals(prime_field))
    {
      localBigInteger3 = ((DERInteger)paramX9FieldID.getParameters()).getValue();
      localX9FieldElement3 = new X9FieldElement(localBigInteger3, (ASN1OctetString)paramASN1Sequence.getObjectAt(0));
      localX9FieldElement4 = new X9FieldElement(localBigInteger3, (ASN1OctetString)paramASN1Sequence.getObjectAt(1));
      this.curve = new ECCurve.Fp(localBigInteger3, localX9FieldElement3.getValue().toBigInteger(), localX9FieldElement4.getValue().toBigInteger());
    }
    while (!this.fieldIdentifier.equals(characteristic_two_field))
    {
      BigInteger localBigInteger3;
      X9FieldElement localX9FieldElement3;
      X9FieldElement localX9FieldElement4;
      if (paramASN1Sequence.size() == 3)
        this.seed = ((DERBitString)paramASN1Sequence.getObjectAt(2)).getBytes();
      return;
    }
    DERSequence localDERSequence1 = (DERSequence)paramX9FieldID.getParameters();
    int i = ((DERInteger)localDERSequence1.getObjectAt(0)).getValue().intValue();
    DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localDERSequence1.getObjectAt(1);
    int j = 0;
    int k = 0;
    int m;
    if (localDERObjectIdentifier.equals(tpBasis))
      m = ((DERInteger)localDERSequence1.getObjectAt(2)).getValue().intValue();
    while (true)
    {
      X9FieldElement localX9FieldElement1 = new X9FieldElement(i, m, j, k, (ASN1OctetString)paramASN1Sequence.getObjectAt(0));
      ASN1OctetString localASN1OctetString = (ASN1OctetString)paramASN1Sequence.getObjectAt(1);
      X9FieldElement localX9FieldElement2 = new X9FieldElement(i, m, j, k, localASN1OctetString);
      BigInteger localBigInteger1 = localX9FieldElement1.getValue().toBigInteger();
      BigInteger localBigInteger2 = localX9FieldElement2.getValue().toBigInteger();
      this.curve = new ECCurve.F2m(i, m, j, k, localBigInteger1, localBigInteger2);
      break;
      DERSequence localDERSequence2 = (DERSequence)localDERSequence1.getObjectAt(2);
      m = ((DERInteger)localDERSequence2.getObjectAt(0)).getValue().intValue();
      j = ((DERInteger)localDERSequence2.getObjectAt(1)).getValue().intValue();
      k = ((DERInteger)localDERSequence2.getObjectAt(2)).getValue().intValue();
    }
  }

  public X9Curve(ECCurve paramECCurve)
  {
    this.curve = paramECCurve;
    this.seed = null;
    setFieldIdentifier();
  }

  public X9Curve(ECCurve paramECCurve, byte[] paramArrayOfByte)
  {
    this.curve = paramECCurve;
    this.seed = paramArrayOfByte;
    setFieldIdentifier();
  }

  private void setFieldIdentifier()
  {
    if ((this.curve instanceof ECCurve.Fp))
    {
      this.fieldIdentifier = prime_field;
      return;
    }
    if ((this.curve instanceof ECCurve.F2m))
    {
      this.fieldIdentifier = characteristic_two_field;
      return;
    }
    throw new IllegalArgumentException("This type of ECCurve is not implemented");
  }

  public ECCurve getCurve()
  {
    return this.curve;
  }

  public byte[] getSeed()
  {
    return this.seed;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.fieldIdentifier.equals(prime_field))
    {
      localASN1EncodableVector.add(new X9FieldElement(this.curve.getA()).getDERObject());
      localASN1EncodableVector.add(new X9FieldElement(this.curve.getB()).getDERObject());
    }
    while (true)
    {
      if (this.seed != null)
        localASN1EncodableVector.add(new DERBitString(this.seed));
      return new DERSequence(localASN1EncodableVector);
      if (this.fieldIdentifier.equals(characteristic_two_field))
      {
        localASN1EncodableVector.add(new X9FieldElement(this.curve.getA()).getDERObject());
        localASN1EncodableVector.add(new X9FieldElement(this.curve.getB()).getDERObject());
      }
    }
  }
}