package org.bouncycastle2.asn1;

import java.io.IOException;

public abstract interface ASN1SequenceParser extends DEREncodable, InMemoryRepresentable
{
  public abstract DEREncodable readObject()
    throws IOException;
}