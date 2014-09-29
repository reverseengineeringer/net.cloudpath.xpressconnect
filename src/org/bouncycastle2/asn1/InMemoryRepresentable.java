package org.bouncycastle2.asn1;

import java.io.IOException;

public abstract interface InMemoryRepresentable
{
  public abstract DERObject getLoadedObject()
    throws IOException;
}