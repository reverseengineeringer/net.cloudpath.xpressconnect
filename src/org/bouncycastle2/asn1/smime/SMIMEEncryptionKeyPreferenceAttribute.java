package org.bouncycastle2.asn1.smime;

import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.DERSet;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.cms.Attribute;
import org.bouncycastle2.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle2.asn1.cms.RecipientKeyIdentifier;

public class SMIMEEncryptionKeyPreferenceAttribute extends Attribute
{
  public SMIMEEncryptionKeyPreferenceAttribute(ASN1OctetString paramASN1OctetString)
  {
    super(SMIMEAttributes.encrypKeyPref, new DERSet(new DERTaggedObject(false, 2, paramASN1OctetString)));
  }

  public SMIMEEncryptionKeyPreferenceAttribute(IssuerAndSerialNumber paramIssuerAndSerialNumber)
  {
    super(SMIMEAttributes.encrypKeyPref, new DERSet(new DERTaggedObject(false, 0, paramIssuerAndSerialNumber)));
  }

  public SMIMEEncryptionKeyPreferenceAttribute(RecipientKeyIdentifier paramRecipientKeyIdentifier)
  {
    super(SMIMEAttributes.encrypKeyPref, new DERSet(new DERTaggedObject(false, 1, paramRecipientKeyIdentifier)));
  }
}