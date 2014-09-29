package org.bouncycastle2.asn1.misc;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;

public class IDEACBCPar extends ASN1Encodable
{
  ASN1OctetString iv;

  public IDEACBCPar(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() == 1)
    {
      this.iv = ((ASN1OctetString)paramASN1Sequence.getObjectAt(0));
      return;
    }
    this.iv = null;
  }

  public IDEACBCPar(byte[] paramArrayOfByte)
  {
    this.iv = new DEROctetString(paramArrayOfByte);
  }

  public static IDEACBCPar getInstance(Object paramObject)
  {
    if ((paramObject instanceof IDEACBCPar))
      return (IDEACBCPar)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new IDEACBCPar((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in IDEACBCPar factory");
  }

  public byte[] getIV()
  {
    if (this.iv != null)
      return this.iv.getOctets();
    return null;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.iv != null)
      localASN1EncodableVector.add(this.iv);
    return new DERSequence(localASN1EncodableVector);
  }
}