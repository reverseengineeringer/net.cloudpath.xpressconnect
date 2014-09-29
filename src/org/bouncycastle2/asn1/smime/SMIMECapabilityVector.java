package org.bouncycastle2.asn1.smime;

import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class SMIMECapabilityVector
{
  private ASN1EncodableVector capabilities = new ASN1EncodableVector();

  public void addCapability(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.capabilities.add(new DERSequence(paramDERObjectIdentifier));
  }

  public void addCapability(DERObjectIdentifier paramDERObjectIdentifier, int paramInt)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(paramDERObjectIdentifier);
    localASN1EncodableVector.add(new DERInteger(paramInt));
    this.capabilities.add(new DERSequence(localASN1EncodableVector));
  }

  public void addCapability(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(paramDERObjectIdentifier);
    localASN1EncodableVector.add(paramDEREncodable);
    this.capabilities.add(new DERSequence(localASN1EncodableVector));
  }

  public ASN1EncodableVector toASN1EncodableVector()
  {
    return this.capabilities;
  }
}