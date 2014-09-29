package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x500.X500Name;

public class ServiceLocator extends ASN1Encodable
{
  X500Name issuer;
  DERObject locator;

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.issuer);
    if (this.locator != null)
      localASN1EncodableVector.add(this.locator);
    return new DERSequence(localASN1EncodableVector);
  }
}