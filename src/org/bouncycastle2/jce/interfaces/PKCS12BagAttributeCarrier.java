package org.bouncycastle2.jce.interfaces;

import java.util.Enumeration;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObjectIdentifier;

public abstract interface PKCS12BagAttributeCarrier
{
  public abstract DEREncodable getBagAttribute(DERObjectIdentifier paramDERObjectIdentifier);

  public abstract Enumeration getBagAttributeKeys();

  public abstract void setBagAttribute(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable);
}