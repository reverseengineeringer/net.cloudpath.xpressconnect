package org.bouncycastle2.asn1.cms;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1SequenceParser;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class CompressedDataParser
{
  private AlgorithmIdentifier _compressionAlgorithm;
  private ContentInfoParser _encapContentInfo;
  private DERInteger _version;

  public CompressedDataParser(ASN1SequenceParser paramASN1SequenceParser)
    throws IOException
  {
    this._version = ((DERInteger)paramASN1SequenceParser.readObject());
    this._compressionAlgorithm = AlgorithmIdentifier.getInstance(paramASN1SequenceParser.readObject().getDERObject());
    this._encapContentInfo = new ContentInfoParser((ASN1SequenceParser)paramASN1SequenceParser.readObject());
  }

  public AlgorithmIdentifier getCompressionAlgorithmIdentifier()
  {
    return this._compressionAlgorithm;
  }

  public ContentInfoParser getEncapContentInfo()
  {
    return this._encapContentInfo;
  }

  public DERInteger getVersion()
  {
    return this._version;
  }
}