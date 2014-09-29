package org.bouncycastle2.asn1;

import java.io.IOException;

public abstract class ASN1Null extends ASN1Object
{
  boolean asn1Equals(DERObject paramDERObject)
  {
    return (paramDERObject instanceof ASN1Null);
  }

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;

  public int hashCode()
  {
    return -1;
  }

  public String toString()
  {
    return "NULL";
  }
}