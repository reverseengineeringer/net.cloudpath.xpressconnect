package org.bouncycastle2.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERUTF8String;

public class PKIFreeText extends ASN1Encodable
{
  ASN1Sequence strings;

  public PKIFreeText(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    do
      if (!localEnumeration.hasMoreElements())
      {
        this.strings = paramASN1Sequence;
        return;
      }
    while ((localEnumeration.nextElement() instanceof DERUTF8String));
    throw new IllegalArgumentException("attempt to insert non UTF8 STRING into PKIFreeText");
  }

  public PKIFreeText(DERUTF8String paramDERUTF8String)
  {
    this.strings = new DERSequence(paramDERUTF8String);
  }

  public PKIFreeText(String[] paramArrayOfString)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfString.length)
      {
        this.strings = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(new DERUTF8String(paramArrayOfString[i]));
    }
  }

  public PKIFreeText(DERUTF8String[] paramArrayOfDERUTF8String)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfDERUTF8String.length)
      {
        this.strings = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(paramArrayOfDERUTF8String[i]);
    }
  }

  public static PKIFreeText getInstance(Object paramObject)
  {
    if ((paramObject instanceof PKIFreeText))
      return (PKIFreeText)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PKIFreeText((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Unknown object in factory: " + paramObject.getClass().getName());
  }

  public static PKIFreeText getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERUTF8String getStringAt(int paramInt)
  {
    return (DERUTF8String)this.strings.getObjectAt(paramInt);
  }

  public int size()
  {
    return this.strings.size();
  }

  public DERObject toASN1Object()
  {
    return this.strings;
  }
}