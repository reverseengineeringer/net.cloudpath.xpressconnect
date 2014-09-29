package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;

public class POPOSigningKeyInput extends ASN1Encodable
{
  private SubjectPublicKeyInfo publicKey;
  private PKMACValue publicKeyMAC;
  private GeneralName sender;

  private POPOSigningKeyInput(ASN1Sequence paramASN1Sequence)
  {
    ASN1Encodable localASN1Encodable = (ASN1Encodable)paramASN1Sequence.getObjectAt(0);
    if ((localASN1Encodable instanceof ASN1TaggedObject))
    {
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)localASN1Encodable;
      if (localASN1TaggedObject.getTagNo() != 0)
        throw new IllegalArgumentException("Unknown authInfo tag: " + localASN1TaggedObject.getTagNo());
      this.sender = GeneralName.getInstance(localASN1TaggedObject.getObject());
    }
    while (true)
    {
      this.publicKey = SubjectPublicKeyInfo.getInstance(paramASN1Sequence.getObjectAt(1));
      return;
      this.publicKeyMAC = PKMACValue.getInstance(localASN1Encodable);
    }
  }

  public POPOSigningKeyInput(PKMACValue paramPKMACValue, SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    this.publicKeyMAC = paramPKMACValue;
    this.publicKey = paramSubjectPublicKeyInfo;
  }

  public POPOSigningKeyInput(GeneralName paramGeneralName, SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    this.sender = paramGeneralName;
    this.publicKey = paramSubjectPublicKeyInfo;
  }

  public static POPOSigningKeyInput getInstance(Object paramObject)
  {
    if ((paramObject instanceof POPOSigningKeyInput))
      return (POPOSigningKeyInput)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new POPOSigningKeyInput((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public SubjectPublicKeyInfo getPublicKey()
  {
    return this.publicKey;
  }

  public PKMACValue getPublicKeyMAC()
  {
    return this.publicKeyMAC;
  }

  public GeneralName getSender()
  {
    return this.sender;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.sender != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.sender));
    while (true)
    {
      localASN1EncodableVector.add(this.publicKey);
      return new DERSequence(localASN1EncodableVector);
      localASN1EncodableVector.add(this.publicKeyMAC);
    }
  }
}