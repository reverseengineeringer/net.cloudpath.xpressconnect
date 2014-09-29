package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;

public class TimeStampedData extends ASN1Encodable
{
  private ASN1OctetString content;
  private DERIA5String dataUri;
  private MetaData metaData;
  private Evidence temporalEvidence;
  private DERInteger version;

  private TimeStampedData(ASN1Sequence paramASN1Sequence)
  {
    this.version = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    int i = 1;
    if ((paramASN1Sequence.getObjectAt(i) instanceof DERIA5String))
    {
      int m = i + 1;
      this.dataUri = DERIA5String.getInstance(paramASN1Sequence.getObjectAt(i));
      i = m;
    }
    if (((paramASN1Sequence.getObjectAt(i) instanceof MetaData)) || ((paramASN1Sequence.getObjectAt(i) instanceof ASN1Sequence)))
    {
      int j = i + 1;
      this.metaData = MetaData.getInstance(paramASN1Sequence.getObjectAt(i));
      i = j;
    }
    if ((paramASN1Sequence.getObjectAt(i) instanceof ASN1OctetString))
    {
      int k = i + 1;
      this.content = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(i));
      i = k;
    }
    this.temporalEvidence = Evidence.getInstance(paramASN1Sequence.getObjectAt(i));
  }

  public TimeStampedData(DERIA5String paramDERIA5String, MetaData paramMetaData, ASN1OctetString paramASN1OctetString, Evidence paramEvidence)
  {
    this.version = new DERInteger(1);
    this.dataUri = paramDERIA5String;
    this.metaData = paramMetaData;
    this.content = paramASN1OctetString;
    this.temporalEvidence = paramEvidence;
  }

  public static TimeStampedData getInstance(Object paramObject)
  {
    if ((paramObject instanceof TimeStampedData))
      return (TimeStampedData)paramObject;
    if (paramObject != null)
      return new TimeStampedData(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public ASN1OctetString getContent()
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
  {
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