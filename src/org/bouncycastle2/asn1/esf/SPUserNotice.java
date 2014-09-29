package org.bouncycastle2.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.DisplayText;
import org.bouncycastle2.asn1.x509.NoticeReference;

public class SPUserNotice
{
  private DisplayText explicitText;
  private NoticeReference noticeRef;

  public SPUserNotice(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      DEREncodable localDEREncodable = (DEREncodable)localEnumeration.nextElement();
      if ((localDEREncodable instanceof NoticeReference))
      {
        this.noticeRef = NoticeReference.getInstance(localDEREncodable);
      }
      else
      {
        if (!(localDEREncodable instanceof DisplayText))
          break;
        this.explicitText = DisplayText.getInstance(localDEREncodable);
      }
    }
    throw new IllegalArgumentException("Invalid element in 'SPUserNotice'.");
  }

  public SPUserNotice(NoticeReference paramNoticeReference, DisplayText paramDisplayText)
  {
    this.noticeRef = paramNoticeReference;
    this.explicitText = paramDisplayText;
  }

  public static SPUserNotice getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SPUserNotice)))
      return (SPUserNotice)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SPUserNotice((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in 'SPUserNotice' factory : " + paramObject.getClass().getName() + ".");
  }

  public DisplayText getExplicitText()
  {
    return this.explicitText;
  }

  public NoticeReference getNoticeRef()
  {
    return this.noticeRef;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.noticeRef != null)
      localASN1EncodableVector.add(this.noticeRef);
    if (this.explicitText != null)
      localASN1EncodableVector.add(this.explicitText);
    return new DERSequence(localASN1EncodableVector);
  }
}