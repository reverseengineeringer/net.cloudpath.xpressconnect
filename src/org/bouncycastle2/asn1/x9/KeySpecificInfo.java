package org.bouncycastle2.asn1.x9;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class KeySpecificInfo extends ASN1Encodable
{
  private DERObjectIdentifier algorithm;
  private ASN1OctetString counter;

  public KeySpecificInfo(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.algorithm = ((DERObjectIdentifier)localEnumeration.nextElement());
    this.counter = ((ASN1OctetString)localEnumeration.nextElement());
  }

  public KeySpecificInfo(DERObjectIdentifier paramDERObjectIdentifier, ASN1OctetString paramASN1OctetString)
  {
    this.algorithm = paramDERObjectIdentifier;
    this.counter = paramASN1OctetString;
  }

  public DERObjectIdentifier getAlgorithm()
  {
    return this.algorithm;
  }

  public ASN1OctetString getCounter()
  {
    return this.counter;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.algorithm);
    localASN1EncodableVector.add(this.counter);
    return new DERSequence(localASN1EncodableVector);
  }
}