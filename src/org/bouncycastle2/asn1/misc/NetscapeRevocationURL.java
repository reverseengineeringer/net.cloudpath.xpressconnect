package org.bouncycastle2.asn1.misc;

import org.bouncycastle2.asn1.DERIA5String;

public class NetscapeRevocationURL extends DERIA5String
{
  public NetscapeRevocationURL(DERIA5String paramDERIA5String)
  {
    super(paramDERIA5String.getString());
  }

  public String toString()
  {
    return "NetscapeRevocationURL: " + getString();
  }
}