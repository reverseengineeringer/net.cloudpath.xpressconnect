package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class UserNotice extends ASN1Encodable
{
  private DisplayText explicitText;
  private NoticeReference noticeRef;

  public UserNotice(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() == 2)
    {
      this.noticeRef = NoticeReference.getInstance(paramASN1Sequence.getObjectAt(0));
      this.explicitText = DisplayText.getInstance(paramASN1Sequence.getObjectAt(1));
      return;
    }
    if (paramASN1Sequence.size() == 1)
    {
      if ((paramASN1Sequence.getObjectAt(0).getDERObject() instanceof ASN1Sequence))
      {
        this.noticeRef = NoticeReference.getInstance(paramASN1Sequence.getObjectAt(0));
        return;
      }
      this.explicitText = DisplayText.getInstance(paramASN1Sequence.getObjectAt(0));
      return;
    }
    throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
  }

  public UserNotice(NoticeReference paramNoticeReference, String paramString)
  {
    this.noticeRef = paramNoticeReference;
    this.explicitText = new DisplayText(paramString);
  }

  public UserNotice(NoticeReference paramNoticeReference, DisplayText paramDisplayText)
  {
    this.noticeRef = paramNoticeReference;
    this.explicitText = paramDisplayText;
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