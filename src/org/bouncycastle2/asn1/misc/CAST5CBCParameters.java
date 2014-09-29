package org.bouncycastle2.asn1.misc;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;

public class CAST5CBCParameters extends ASN1Encodable
{
  ASN1OctetString iv;
  DERInteger keyLength;

  public CAST5CBCParameters(ASN1Sequence paramASN1Sequence)
  {
    this.iv = ((ASN1OctetString)paramASN1Sequence.getObjectAt(0));
    this.keyLength = ((DERInteger)paramASN1Sequence.getObjectAt(1));
  }

  public CAST5CBCParameters(byte[] paramArrayOfByte, int paramInt)
  {
    this.iv = new DEROctetString(paramArrayOfByte);
    this.keyLength = new DERInteger(paramInt);
  }

  public static CAST5CBCParameters getInstance(Object paramObject)
  {
    if ((paramObject instanceof CAST5CBCParameters))
      return (CAST5CBCParameters)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CAST5CBCParameters((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in CAST5CBCParameter factory");
  }

  public byte[] getIV()
  {
    return this.iv.getOctets();
  }

  public int getKeyLength()
  {
    return this.keyLength.getValue().intValue();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.iv);
    localASN1EncodableVector.add(this.keyLength);
    return new DERSequence(localASN1EncodableVector);
  }
}