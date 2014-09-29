package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1String;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBMPString;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERUTF8String;
import org.bouncycastle2.asn1.DERVisibleString;

public class DisplayText extends ASN1Encodable
  implements ASN1Choice
{
  public static final int CONTENT_TYPE_BMPSTRING = 1;
  public static final int CONTENT_TYPE_IA5STRING = 0;
  public static final int CONTENT_TYPE_UTF8STRING = 2;
  public static final int CONTENT_TYPE_VISIBLESTRING = 3;
  public static final int DISPLAY_TEXT_MAXIMUM_SIZE = 200;
  int contentType;
  ASN1String contents;

  public DisplayText(int paramInt, String paramString)
  {
    if (paramString.length() > 200)
      paramString = paramString.substring(0, 200);
    this.contentType = paramInt;
    switch (paramInt)
    {
    default:
      this.contents = new DERUTF8String(paramString);
      return;
    case 0:
      this.contents = new DERIA5String(paramString);
      return;
    case 2:
      this.contents = new DERUTF8String(paramString);
      return;
    case 3:
      this.contents = new DERVisibleString(paramString);
      return;
    case 1:
    }
    this.contents = new DERBMPString(paramString);
  }

  public DisplayText(String paramString)
  {
    if (paramString.length() > 200)
      paramString = paramString.substring(0, 200);
    this.contentType = 2;
    this.contents = new DERUTF8String(paramString);
  }

  private DisplayText(ASN1String paramASN1String)
  {
    this.contents = paramASN1String;
  }

  public static DisplayText getInstance(Object paramObject)
  {
    if ((paramObject instanceof ASN1String))
      return new DisplayText((ASN1String)paramObject);
    if ((paramObject instanceof DisplayText))
      return (DisplayText)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DisplayText getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public String getString()
  {
    return this.contents.getString();
  }

  public DERObject toASN1Object()
  {
    return (DERObject)this.contents;
  }
}