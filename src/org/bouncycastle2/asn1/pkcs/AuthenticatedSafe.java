package org.bouncycastle2.asn1.pkcs;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.DERObject;

public class AuthenticatedSafe extends ASN1Encodable
{
  ContentInfo[] info;

  public AuthenticatedSafe(ASN1Sequence paramASN1Sequence)
  {
    this.info = new ContentInfo[paramASN1Sequence.size()];
    for (int i = 0; ; i++)
    {
      if (i == this.info.length)
        return;
      this.info[i] = ContentInfo.getInstance(paramASN1Sequence.getObjectAt(i));
    }
  }

  public AuthenticatedSafe(ContentInfo[] paramArrayOfContentInfo)
  {
    this.info = paramArrayOfContentInfo;
  }

  public ContentInfo[] getContentInfo()
  {
    return this.info;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i == this.info.length)
        return new BERSequence(localASN1EncodableVector);
      localASN1EncodableVector.add(this.info[i]);
    }
  }
}