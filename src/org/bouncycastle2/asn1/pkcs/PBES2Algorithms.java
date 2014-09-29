package org.bouncycastle2.asn1.pkcs;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class PBES2Algorithms extends AlgorithmIdentifier
  implements PKCSObjectIdentifiers
{
  private KeyDerivationFunc func;
  private DERObjectIdentifier objectId;
  private EncryptionScheme scheme;

  public PBES2Algorithms(ASN1Sequence paramASN1Sequence)
  {
    super(paramASN1Sequence);
    Enumeration localEnumeration1 = paramASN1Sequence.getObjects();
    this.objectId = ((DERObjectIdentifier)localEnumeration1.nextElement());
    Enumeration localEnumeration2 = ((ASN1Sequence)localEnumeration1.nextElement()).getObjects();
    ASN1Sequence localASN1Sequence = (ASN1Sequence)localEnumeration2.nextElement();
    if (localASN1Sequence.getObjectAt(0).equals(id_PBKDF2));
    for (this.func = new KeyDerivationFunc(id_PBKDF2, PBKDF2Params.getInstance(localASN1Sequence.getObjectAt(1))); ; this.func = new KeyDerivationFunc(localASN1Sequence))
    {
      this.scheme = new EncryptionScheme((ASN1Sequence)localEnumeration2.nextElement());
      return;
    }
  }

  public DERObject getDERObject()
  {
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
    localASN1EncodableVector1.add(this.objectId);
    localASN1EncodableVector2.add(this.func);
    localASN1EncodableVector2.add(this.scheme);
    localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
    return new DERSequence(localASN1EncodableVector1);
  }

  public EncryptionScheme getEncryptionScheme()
  {
    return this.scheme;
  }

  public KeyDerivationFunc getKeyDerivationFunc()
  {
    return this.func;
  }

  public DERObjectIdentifier getObjectId()
  {
    return this.objectId;
  }
}