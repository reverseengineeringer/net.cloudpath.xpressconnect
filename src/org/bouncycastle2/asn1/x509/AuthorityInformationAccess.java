package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class AuthorityInformationAccess extends ASN1Encodable
{
  private AccessDescription[] descriptions;

  public AuthorityInformationAccess(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() < 1)
      throw new IllegalArgumentException("sequence may not be empty");
    this.descriptions = new AccessDescription[paramASN1Sequence.size()];
    for (int i = 0; ; i++)
    {
      if (i == paramASN1Sequence.size())
        return;
      this.descriptions[i] = AccessDescription.getInstance(paramASN1Sequence.getObjectAt(i));
    }
  }

  public AuthorityInformationAccess(DERObjectIdentifier paramDERObjectIdentifier, GeneralName paramGeneralName)
  {
    this.descriptions = new AccessDescription[1];
    this.descriptions[0] = new AccessDescription(paramDERObjectIdentifier, paramGeneralName);
  }

  public static AuthorityInformationAccess getInstance(Object paramObject)
  {
    if ((paramObject instanceof AuthorityInformationAccess))
      return (AuthorityInformationAccess)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new AuthorityInformationAccess((ASN1Sequence)paramObject);
    if ((paramObject instanceof X509Extension))
      return getInstance(X509Extension.convertValueToObject((X509Extension)paramObject));
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public AccessDescription[] getAccessDescriptions()
  {
    return this.descriptions;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i == this.descriptions.length)
        return new DERSequence(localASN1EncodableVector);
      localASN1EncodableVector.add(this.descriptions[i]);
    }
  }

  public String toString()
  {
    return "AuthorityInformationAccess: Oid(" + this.descriptions[0].getAccessMethod().getId() + ")";
  }
}