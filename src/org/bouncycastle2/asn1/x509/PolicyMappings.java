package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import java.util.Hashtable;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class PolicyMappings extends ASN1Encodable
{
  ASN1Sequence seq = null;

  public PolicyMappings(Hashtable paramHashtable)
  {
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    Enumeration localEnumeration = paramHashtable.keys();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
      {
        this.seq = new DERSequence(localASN1EncodableVector1);
        return;
      }
      String str1 = (String)localEnumeration.nextElement();
      String str2 = (String)paramHashtable.get(str1);
      ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
      localASN1EncodableVector2.add(new DERObjectIdentifier(str1));
      localASN1EncodableVector2.add(new DERObjectIdentifier(str2));
      localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
    }
  }

  public PolicyMappings(ASN1Sequence paramASN1Sequence)
  {
    this.seq = paramASN1Sequence;
  }

  public DERObject toASN1Object()
  {
    return this.seq;
  }
}