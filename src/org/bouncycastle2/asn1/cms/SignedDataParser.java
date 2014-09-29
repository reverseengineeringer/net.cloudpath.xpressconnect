package org.bouncycastle2.asn1.cms;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1SequenceParser;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.ASN1SetParser;
import org.bouncycastle2.asn1.ASN1TaggedObjectParser;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;

public class SignedDataParser
{
  private boolean _certsCalled;
  private boolean _crlsCalled;
  private Object _nextObject;
  private ASN1SequenceParser _seq;
  private DERInteger _version;

  private SignedDataParser(ASN1SequenceParser paramASN1SequenceParser)
    throws IOException
  {
    this._seq = paramASN1SequenceParser;
    this._version = ((DERInteger)paramASN1SequenceParser.readObject());
  }

  public static SignedDataParser getInstance(Object paramObject)
    throws IOException
  {
    if ((paramObject instanceof ASN1Sequence))
      return new SignedDataParser(((ASN1Sequence)paramObject).parser());
    if ((paramObject instanceof ASN1SequenceParser))
      return new SignedDataParser((ASN1SequenceParser)paramObject);
    throw new IOException("unknown object encountered: " + paramObject.getClass().getName());
  }

  public ASN1SetParser getCertificates()
    throws IOException
  {
    this._certsCalled = true;
    this._nextObject = this._seq.readObject();
    if (((this._nextObject instanceof ASN1TaggedObjectParser)) && (((ASN1TaggedObjectParser)this._nextObject).getTagNo() == 0))
    {
      ASN1SetParser localASN1SetParser = (ASN1SetParser)((ASN1TaggedObjectParser)this._nextObject).getObjectParser(17, false);
      this._nextObject = null;
      return localASN1SetParser;
    }
    return null;
  }

  public ASN1SetParser getCrls()
    throws IOException
  {
    if (!this._certsCalled)
      throw new IOException("getCerts() has not been called.");
    this._crlsCalled = true;
    if (this._nextObject == null)
      this._nextObject = this._seq.readObject();
    if (((this._nextObject instanceof ASN1TaggedObjectParser)) && (((ASN1TaggedObjectParser)this._nextObject).getTagNo() == 1))
    {
      ASN1SetParser localASN1SetParser = (ASN1SetParser)((ASN1TaggedObjectParser)this._nextObject).getObjectParser(17, false);
      this._nextObject = null;
      return localASN1SetParser;
    }
    return null;
  }

  public ASN1SetParser getDigestAlgorithms()
    throws IOException
  {
    DEREncodable localDEREncodable = this._seq.readObject();
    if ((localDEREncodable instanceof ASN1Set))
      return ((ASN1Set)localDEREncodable).parser();
    return (ASN1SetParser)localDEREncodable;
  }

  public ContentInfoParser getEncapContentInfo()
    throws IOException
  {
    return new ContentInfoParser((ASN1SequenceParser)this._seq.readObject());
  }

  public ASN1SetParser getSignerInfos()
    throws IOException
  {
    if ((!this._certsCalled) || (!this._crlsCalled))
      throw new IOException("getCerts() and/or getCrls() has not been called.");
    if (this._nextObject == null)
      this._nextObject = this._seq.readObject();
    return (ASN1SetParser)this._nextObject;
  }

  public DERInteger getVersion()
  {
    return this._version;
  }
}