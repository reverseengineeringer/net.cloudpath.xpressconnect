package org.bouncycastle2.asn1;

import java.io.InputStream;

public abstract interface ASN1OctetStringParser extends DEREncodable, InMemoryRepresentable
{
  public abstract InputStream getOctetStream();
}