package org.bouncycastle2.asn1.cms;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1SequenceParser;
import org.bouncycastle2.asn1.ASN1SetParser;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.ASN1TaggedObjectParser;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class AuthenticatedDataParser
{
  private DEREncodable nextObject;
  private boolean originatorInfoCalled;
  private ASN1SequenceParser seq;
  private DERInteger version;

  public AuthenticatedDataParser(ASN1SequenceParser paramASN1SequenceParser)
    throws IOException
  {
    this.seq = paramASN1SequenceParser;
    this.version = ((DERInteger)paramASN1SequenceParser.readObject());
  }

  public ASN1SetParser getAuthAttrs()
    throws IOException
  {
    if (this.nextObject == null)
      this.nextObject = this.seq.readObject();
    boolean bool = this.nextObject instanceof ASN1TaggedObjectParser;
    ASN1SetParser localASN1SetParser = null;
    if (bool)
    {
      DEREncodable localDEREncodable = this.nextObject;
      this.nextObject = null;
      localASN1SetParser = (ASN1SetParser)((ASN1TaggedObjectParser)localDEREncodable).getObjectParser(17, false);
    }
    return localASN1SetParser;
  }

  public AlgorithmIdentifier getDigestAlgorithm()
    throws IOException
  {
    if (this.nextObject == null)
      this.nextObject = this.seq.readObject();
    if ((this.nextObject instanceof ASN1TaggedObjectParser))
    {
      AlgorithmIdentifier localAlgorithmIdentifier = AlgorithmIdentifier.getInstance((ASN1TaggedObject)this.nextObject.getDERObject(), false);
      this.nextObject = null;
      return localAlgorithmIdentifier;
    }
    return null;
  }

  public ContentInfoParser getEnapsulatedContentInfo()
    throws IOException
  {
    if (this.nextObject == null)
      this.nextObject = this.seq.readObject();
    DEREncodable localDEREncodable = this.nextObject;
    ContentInfoParser localContentInfoParser = null;
    if (localDEREncodable != null)
    {
      ASN1SequenceParser localASN1SequenceParser = (ASN1SequenceParser)this.nextObject;
      this.nextObject = null;
      localContentInfoParser = new ContentInfoParser(localASN1SequenceParser);
    }
    return localContentInfoParser;
  }

  public ASN1OctetString getMac()
    throws IOException
  {
    if (this.nextObject == null)
      this.nextObject = this.seq.readObject();
    DEREncodable localDEREncodable = this.nextObject;
    this.nextObject = null;
    return ASN1OctetString.getInstance(localDEREncodable.getDERObject());
  }

  public AlgorithmIdentifier getMacAlgorithm()
    throws IOException
  {
    if (this.nextObject == null)
      this.nextObject = this.seq.readObject();
    DEREncodable localDEREncodable = this.nextObject;
    AlgorithmIdentifier localAlgorithmIdentifier = null;
    if (localDEREncodable != null)
    {
      ASN1SequenceParser localASN1SequenceParser = (ASN1SequenceParser)this.nextObject;
      this.nextObject = null;
      localAlgorithmIdentifier = AlgorithmIdentifier.getInstance(localASN1SequenceParser.getDERObject());
    }
    return localAlgorithmIdentifier;
  }

  public OriginatorInfo getOriginatorInfo()
    throws IOException
  {
    this.originatorInfoCalled = true;
    if (this.nextObject == null)
      this.nextObject = this.seq.readObject();
    if (((this.nextObject instanceof ASN1TaggedObjectParser)) && (((ASN1TaggedObjectParser)this.nextObject).getTagNo() == 0))
    {
      ASN1SequenceParser localASN1SequenceParser = (ASN1SequenceParser)((ASN1TaggedObjectParser)this.nextObject).getObjectParser(16, false);
      this.nextObject = null;
      return OriginatorInfo.getInstance(localASN1SequenceParser.getDERObject());
    }
    return null;
  }

  public ASN1SetParser getRecipientInfos()
    throws IOException
  {
    if (!this.originatorInfoCalled)
      getOriginatorInfo();
    if (this.nextObject == null)
      this.nextObject = this.seq.readObject();
    ASN1SetParser localASN1SetParser = (ASN1SetParser)this.nextObject;
    this.nextObject = null;
    return localASN1SetParser;
  }

  public ASN1SetParser getUnauthAttrs()
    throws IOException
  {
    if (this.nextObject == null)
      this.nextObject = this.seq.readObject();
    DEREncodable localDEREncodable1 = this.nextObject;
    ASN1SetParser localASN1SetParser = null;
    if (localDEREncodable1 != null)
    {
      DEREncodable localDEREncodable2 = this.nextObject;
      this.nextObject = null;
      localASN1SetParser = (ASN1SetParser)((ASN1TaggedObjectParser)localDEREncodable2).getObjectParser(17, false);
    }
    return localASN1SetParser;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }
}