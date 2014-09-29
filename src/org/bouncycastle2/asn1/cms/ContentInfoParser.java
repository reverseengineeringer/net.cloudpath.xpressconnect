package org.bouncycastle2.asn1.cms;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1SequenceParser;
import org.bouncycastle2.asn1.ASN1TaggedObjectParser;
import org.bouncycastle2.asn1.DEREncodable;

public class ContentInfoParser
{
  private ASN1TaggedObjectParser content;
  private ASN1ObjectIdentifier contentType;

  public ContentInfoParser(ASN1SequenceParser paramASN1SequenceParser)
    throws IOException
  {
    this.contentType = ((ASN1ObjectIdentifier)paramASN1SequenceParser.readObject());
    this.content = ((ASN1TaggedObjectParser)paramASN1SequenceParser.readObject());
  }

  public DEREncodable getContent(int paramInt)
    throws IOException
  {
    if (this.content != null)
      return this.content.getObjectParser(paramInt, true);
    return null;
  }

  public ASN1ObjectIdentifier getContentType()
  {
    return this.contentType;
  }
}