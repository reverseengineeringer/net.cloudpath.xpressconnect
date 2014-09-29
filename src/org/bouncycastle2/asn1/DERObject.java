package org.bouncycastle2.asn1;

import java.io.IOException;

public abstract class DERObject extends ASN1Encodable
  implements DERTags
{
  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public DERObject toASN1Object()
  {
    return this;
  }
}