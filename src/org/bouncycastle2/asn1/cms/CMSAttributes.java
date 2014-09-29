package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;

public abstract interface CMSAttributes
{
  public static final ASN1ObjectIdentifier contentHint = PKCSObjectIdentifiers.id_aa_contentHint;
  public static final ASN1ObjectIdentifier contentType = PKCSObjectIdentifiers.pkcs_9_at_contentType;
  public static final ASN1ObjectIdentifier counterSignature;
  public static final ASN1ObjectIdentifier messageDigest = PKCSObjectIdentifiers.pkcs_9_at_messageDigest;
  public static final ASN1ObjectIdentifier signingTime = PKCSObjectIdentifiers.pkcs_9_at_signingTime;

  static
  {
    counterSignature = PKCSObjectIdentifiers.pkcs_9_at_counterSignature;
  }
}