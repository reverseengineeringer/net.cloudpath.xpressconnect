package org.bouncycastle2.asn1.cms;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetStringParser;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1SequenceParser;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;

public class TimeStampedDataParser
{
  private ASN1OctetStringParser content;
  private DERIA5String dataUri;
  private MetaData metaData;
  private ASN1SequenceParser parser;
  private Evidence temporalEvidence;
  private DERInteger version;

  private TimeStampedDataParser(ASN1SequenceParser paramASN1SequenceParser)
    throws IOException
  {
    this.parser = paramASN1SequenceParser;
    this.version = DERInteger.getInstance(paramASN1SequenceParser.readObject());
    DEREncodable localDEREncodable = paramASN1SequenceParser.readObject();
    if ((localDEREncodable instanceof DERIA5String))
    {
      this.dataUri = DERIA5String.getInstance(localDEREncodable);
      localDEREncodable = paramASN1SequenceParser.readObject();
    }
    if (((localDEREncodable instanceof MetaData)) || ((localDEREncodable instanceof ASN1SequenceParser)))
    {
      this.metaData = MetaData.getInstance(localDEREncodable.getDERObject());
      localDEREncodable = paramASN1SequenceParser.readObject();
    }
    if ((localDEREncodable instanceof ASN1OctetStringParser))
      this.content = ((ASN1OctetStringParser)localDEREncodable);
  }

  public static TimeStampedDataParser getInstance(Object paramObject)
    throws IOException
  {
    if ((paramObject instanceof ASN1Sequence))
      return new TimeStampedDataParser(((ASN1Sequence)paramObject).parser());
    if ((paramObject instanceof ASN1SequenceParser))
      return new TimeStampedDataParser((ASN1SequenceParser)paramObject);
    return null;
  }

  public ASN1OctetStringParser getContent()
  {
    return this.content;
  }

  public DERIA5String getDataUri()
  {
    return this.dataUri;
  }

  public MetaData getMetaData()
  {
    return this.metaData;
  }

  public Evidence getTemporalEvidence()
    throws IOException
  {
    if (this.temporalEvidence == null)
      this.temporalEvidence = Evidence.getInstance(this.parser.readObject().getDERObject());
    return this.temporalEvidence;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    if (this.dataUri != null)
      localASN1EncodableVector.add(this.dataUri);
    if (this.metaData != null)
      localASN1EncodableVector.add(this.metaData);
    if (this.content != null)
      localASN1EncodableVector.add(this.content);
    localASN1EncodableVector.add(this.temporalEvidence);
    return new BERSequence(localASN1EncodableVector);
  }
}