package org.bouncycastle2.asn1.util;

import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;

public class DERDump extends ASN1Dump
{
  public static String dumpAsString(DEREncodable paramDEREncodable)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    _dumpAsString("", false, paramDEREncodable.getDERObject(), localStringBuffer);
    return localStringBuffer.toString();
  }

  public static String dumpAsString(DERObject paramDERObject)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    _dumpAsString("", false, paramDERObject, localStringBuffer);
    return localStringBuffer.toString();
  }
}