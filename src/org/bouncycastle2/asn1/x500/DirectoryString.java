package org.bouncycastle2.asn1.x500;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1String;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBMPString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.asn1.DERT61String;
import org.bouncycastle2.asn1.DERUTF8String;
import org.bouncycastle2.asn1.DERUniversalString;

public class DirectoryString extends ASN1Encodable
  implements ASN1Choice, ASN1String
{
  private ASN1String string;

  public DirectoryString(String paramString)
  {
    this.string = new DERUTF8String(paramString);
  }

  private DirectoryString(DERBMPString paramDERBMPString)
  {
    this.string = paramDERBMPString;
  }

  private DirectoryString(DERPrintableString paramDERPrintableString)
  {
    this.string = paramDERPrintableString;
  }

  private DirectoryString(DERT61String paramDERT61String)
  {
    this.string = paramDERT61String;
  }

  private DirectoryString(DERUTF8String paramDERUTF8String)
  {
    this.string = paramDERUTF8String;
  }

  private DirectoryString(DERUniversalString paramDERUniversalString)
  {
    this.string = paramDERUniversalString;
  }

  public static DirectoryString getInstance(Object paramObject)
  {
    if ((paramObject instanceof DirectoryString))
      return (DirectoryString)paramObject;
    if ((paramObject instanceof DERT61String))
      return new DirectoryString((DERT61String)paramObject);
    if ((paramObject instanceof DERPrintableString))
      return new DirectoryString((DERPrintableString)paramObject);
    if ((paramObject instanceof DERUniversalString))
      return new DirectoryString((DERUniversalString)paramObject);
    if ((paramObject instanceof DERUTF8String))
      return new DirectoryString((DERUTF8String)paramObject);
    if ((paramObject instanceof DERBMPString))
      return new DirectoryString((DERBMPString)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DirectoryString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    if (!paramBoolean)
      throw new IllegalArgumentException("choice item must be explicitly tagged");
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public String getString()
  {
    return this.string.getString();
  }

  public DERObject toASN1Object()
  {
    return ((DEREncodable)this.string).getDERObject();
  }

  public String toString()
  {
    return this.string.getString();
  }
}