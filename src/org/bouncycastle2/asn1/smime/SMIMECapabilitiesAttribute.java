package org.bouncycastle2.asn1.smime;

import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERSet;
import org.bouncycastle2.asn1.cms.Attribute;

public class SMIMECapabilitiesAttribute extends Attribute
{
  public SMIMECapabilitiesAttribute(SMIMECapabilityVector paramSMIMECapabilityVector)
  {
    super(SMIMEAttributes.smimeCapabilities, new DERSet(new DERSequence(paramSMIMECapabilityVector.toASN1EncodableVector())));
  }
}