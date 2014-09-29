package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class NoticeReference extends ASN1Encodable
{
  private ASN1Sequence noticeNumbers;
  private DisplayText organization;

  public NoticeReference(int paramInt, String paramString, ASN1Sequence paramASN1Sequence)
  {
    this.organization = new DisplayText(paramInt, paramString);
    this.noticeNumbers = paramASN1Sequence;
  }

  public NoticeReference(String paramString, Vector paramVector)
  {
    this.organization = new DisplayText(paramString);
    Object localObject = paramVector.elementAt(0);
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    Enumeration localEnumeration;
    if ((localObject instanceof Integer))
      localEnumeration = paramVector.elements();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
      {
        this.noticeNumbers = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(new DERInteger(((Integer)localEnumeration.nextElement()).intValue()));
    }
  }

  public NoticeReference(String paramString, ASN1Sequence paramASN1Sequence)
  {
    this.organization = new DisplayText(paramString);
    this.noticeNumbers = paramASN1Sequence;
  }

  public NoticeReference(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.organization = DisplayText.getInstance(paramASN1Sequence.getObjectAt(0));
    this.noticeNumbers = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public static NoticeReference getInstance(Object paramObject)
  {
    if ((paramObject instanceof NoticeReference))
      return (NoticeReference)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new NoticeReference((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in getInstance.");
  }

  public ASN1Sequence getNoticeNumbers()
  {
    return this.noticeNumbers;
  }

  public DisplayText getOrganization()
  {
    return this.organization;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.organization);
    localASN1EncodableVector.add(this.noticeNumbers);
    return new DERSequence(localASN1EncodableVector);
  }
}