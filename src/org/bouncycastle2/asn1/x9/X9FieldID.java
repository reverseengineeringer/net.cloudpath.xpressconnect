package org.bouncycastle2.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class X9FieldID extends ASN1Encodable
  implements X9ObjectIdentifiers
{
  private DERObjectIdentifier id;
  private DERObject parameters;

  public X9FieldID(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.id = characteristic_two_field;
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    localASN1EncodableVector1.add(new DERInteger(paramInt1));
    if (paramInt3 == 0)
    {
      localASN1EncodableVector1.add(tpBasis);
      localASN1EncodableVector1.add(new DERInteger(paramInt2));
    }
    while (true)
    {
      this.parameters = new DERSequence(localASN1EncodableVector1);
      return;
      localASN1EncodableVector1.add(ppBasis);
      ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
      localASN1EncodableVector2.add(new DERInteger(paramInt2));
      localASN1EncodableVector2.add(new DERInteger(paramInt3));
      localASN1EncodableVector2.add(new DERInteger(paramInt4));
      localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
    }
  }

  public X9FieldID(BigInteger paramBigInteger)
  {
    this.id = prime_field;
    this.parameters = new DERInteger(paramBigInteger);
  }

  public X9FieldID(ASN1Sequence paramASN1Sequence)
  {
    this.id = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    this.parameters = ((DERObject)paramASN1Sequence.getObjectAt(1));
  }

  public DERObjectIdentifier getIdentifier()
  {
    return this.id;
  }

  public DERObject getParameters()
  {
    return this.parameters;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.id);
    localASN1EncodableVector.add(this.parameters);
    return new DERSequence(localASN1EncodableVector);
  }
}