package org.bouncycastle2.asn1;

import java.util.Vector;

public class ASN1EncodableVector extends DEREncodableVector
{
  Vector v = new Vector();

  public void add(DEREncodable paramDEREncodable)
  {
    this.v.addElement(paramDEREncodable);
  }

  public DEREncodable get(int paramInt)
  {
    return (DEREncodable)this.v.elementAt(paramInt);
  }

  public int size()
  {
    return this.v.size();
  }
}