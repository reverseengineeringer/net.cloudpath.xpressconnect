package org.bouncycastle2.asn1.isismtt.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.x500.DirectoryString;

public class Restriction extends ASN1Encodable
{
  private DirectoryString restriction;

  public Restriction(String paramString)
  {
    this.restriction = new DirectoryString(paramString);
  }

  private Restriction(DirectoryString paramDirectoryString)
  {
    this.restriction = paramDirectoryString;
  }

  public static Restriction getInstance(Object paramObject)
  {
    if ((paramObject instanceof Restriction))
      return (Restriction)paramObject;
    if ((paramObject instanceof ASN1String))
      return new Restriction(DirectoryString.getInstance(paramObject));
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public DirectoryString getRestriction()
  {
    return this.restriction;
  }

  public DERObject toASN1Object()
  {
    return this.restriction.toASN1Object();
  }
}