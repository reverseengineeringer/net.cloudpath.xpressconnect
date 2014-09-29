package org.bouncycastle2.asn1.x500;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;

public abstract interface X500NameStyle
{
  public abstract boolean areEqual(X500Name paramX500Name1, X500Name paramX500Name2);

  public abstract ASN1ObjectIdentifier attrNameToOID(String paramString);

  public abstract int calculateHashCode(X500Name paramX500Name);

  public abstract RDN[] fromString(String paramString);

  public abstract ASN1Encodable stringToValue(ASN1ObjectIdentifier paramASN1ObjectIdentifier, String paramString);

  public abstract String toString(X500Name paramX500Name);
}