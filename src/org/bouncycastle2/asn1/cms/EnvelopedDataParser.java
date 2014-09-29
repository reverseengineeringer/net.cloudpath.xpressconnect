package org.bouncycastle2.asn1.cms;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1SequenceParser;
import org.bouncycastle2.asn1.ASN1SetParser;
import org.bouncycastle2.asn1.ASN1TaggedObjectParser;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;

public class EnvelopedDataParser
{
  private DEREncodable _nextObject;
  private boolean _originatorInfoCalled;
  private ASN1SequenceParser _seq;
  private DERInteger _version;

  public EnvelopedDataParser(ASN1SequenceParser paramASN1SequenceParser)
    throws IOException
  {
    this._seq = paramASN1SequenceParser;
    this._version = ((DERInteger)paramASN1SequenceParser.readObject());
  }

  public EncryptedContentInfoParser getEncryptedContentInfo()
    throws IOException
  {
    if (this._nextObject == null)
      this._nextObject = this._seq.readObject();
    DEREncodable localDEREncodable = this._nextObject;
    EncryptedContentInfoParser localEncryptedContentInfoParser = null;
    if (localDEREncodable != null)
    {
      ASN1SequenceParser localASN1SequenceParser = (ASN1SequenceParser)this._nextObject;
      this._nextObject = null;
      localEncryptedContentInfoParser = new EncryptedContentInfoParser(localASN1SequenceParser);
    }
    return localEncryptedContentInfoParser;
  }

  public OriginatorInfo getOriginatorInfo()
    throws IOException
  {
    this._originatorInfoCalled = true;
    if (this._nextObject == null)
      this._nextObject = this._seq.readObject();
    if (((this._nextObject instanceof ASN1TaggedObjectParser)) && (((ASN1TaggedObjectParser)this._nextObject).getTagNo() == 0))
    {
      ASN1SequenceParser localASN1SequenceParser = (ASN1SequenceParser)((ASN1TaggedObjectParser)this._nextObject).getObjectParser(16, false);
      this._nextObject = null;
      return OriginatorInfo.getInstance(localASN1SequenceParser.getDERObject());
    }
    return null;
  }

  public ASN1SetParser getRecipientInfos()
    throws IOException
  {
    if (!this._originatorInfoCalled)
      getOriginatorInfo();
    if (this._nextObject == null)
      this._nextObject = this._seq.readObject();
    ASN1SetParser localASN1SetParser = (ASN1SetParser)this._nextObject;
    this._nextObject = null;
    return localASN1SetParser;
  }

  public ASN1SetParser getUnprotectedAttrs()
    throws IOException
  {
    if (this._nextObject == null)
      this._nextObject = this._seq.readObject();
    DEREncodable localDEREncodable1 = this._nextObject;
    ASN1SetParser localASN1SetParser = null;
    if (localDEREncodable1 != null)
    {
      DEREncodable localDEREncodable2 = this._nextObject;
      this._nextObject = null;
      localASN1SetParser = (ASN1SetParser)((ASN1TaggedObjectParser)localDEREncodable2).getObjectParser(17, false);
    }
    return localASN1SetParser;
  }

  public DERInteger getVersion()
  {
    return this._version;
  }
}