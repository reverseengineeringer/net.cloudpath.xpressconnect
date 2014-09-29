package org.bouncycastle2.asn1.cms;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1SequenceParser;
import org.bouncycastle2.asn1.ASN1TaggedObjectParser;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class EncryptedContentInfoParser
{
  private AlgorithmIdentifier _contentEncryptionAlgorithm;
  private DERObjectIdentifier _contentType;
  private ASN1TaggedObjectParser _encryptedContent;

  public EncryptedContentInfoParser(ASN1SequenceParser paramASN1SequenceParser)
    throws IOException
  {
    this._contentType = ((DERObjectIdentifier)paramASN1SequenceParser.readObject());
    this._contentEncryptionAlgorithm = AlgorithmIdentifier.getInstance(paramASN1SequenceParser.readObject().getDERObject());
    this._encryptedContent = ((ASN1TaggedObjectParser)paramASN1SequenceParser.readObject());
  }

  public AlgorithmIdentifier getContentEncryptionAlgorithm()
  {
    return this._contentEncryptionAlgorithm;
  }

  public DERObjectIdentifier getContentType()
  {
    return this._contentType;
  }

  public DEREncodable getEncryptedContent(int paramInt)
    throws IOException
  {
    return this._encryptedContent.getObjectParser(paramInt, false);
  }
}