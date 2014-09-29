package org.bouncycastle2.asn1.pkcs;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;

public class PKCS12PBEParams extends ASN1Encodable
{
  DERInteger iterations;
  ASN1OctetString iv;

  public PKCS12PBEParams(ASN1Sequence paramASN1Sequence)
  {
    this.iv = ((ASN1OctetString)paramASN1Sequence.getObjectAt(0));
    this.iterations = ((DERInteger)paramASN1Sequence.getObjectAt(1));
  }

  public PKCS12PBEParams(byte[] paramArrayOfByte, int paramInt)
  {
    this.iv = new DEROctetString(paramArrayOfByte);
    this.iterations = new DERInteger(paramInt);
  }

  public static PKCS12PBEParams getInstance(Object paramObject)
  {
    if ((paramObject instanceof PKCS12PBEParams))
      return (PKCS12PBEParams)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PKCS12PBEParams((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public byte[] getIV()
  {
    return this.iv.getOctets();
  }

  public BigInteger getIterations()
  {
    return this.iterations.getValue();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.iv);
    localASN1EncodableVector.add(this.iterations);
    return new DERSequence(localASN1EncodableVector);
  }
}