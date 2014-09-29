package org.bouncycastle2.asn1.pkcs;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class KeyDerivationFunc extends AlgorithmIdentifier
{
  KeyDerivationFunc(ASN1Sequence paramASN1Sequence)
  {
    super(paramASN1Sequence);
  }

  public KeyDerivationFunc(DERObjectIdentifier paramDERObjectIdentifier, ASN1Encodable paramASN1Encodable)
  {
    super(paramDERObjectIdentifier, paramASN1Encodable);
  }
}