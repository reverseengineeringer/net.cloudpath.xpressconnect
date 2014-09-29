package org.bouncycastle2.asn1;

import java.io.IOException;

public abstract interface ASN1TaggedObjectParser extends DEREncodable, InMemoryRepresentable
{
  public abstract DEREncodable getObjectParser(int paramInt, boolean paramBoolean)
    throws IOException;

  public abstract int getTagNo();
}