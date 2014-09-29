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

public class PBEParameter extends ASN1Encodable
{
  DERInteger iterations;
  ASN1OctetString salt;

  public PBEParameter(ASN1Sequence paramASN1Sequence)
  {
    this.salt = ((ASN1OctetString)paramASN1Sequence.getObjectAt(0));
    this.iterations = ((DERInteger)paramASN1Sequence.getObjectAt(1));
  }

  public PBEParameter(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramArrayOfByte.length != 8)
      throw new IllegalArgumentException("salt length must be 8");
    this.salt = new DEROctetString(paramArrayOfByte);
    this.iterations = new DERInteger(paramInt);
  }

  public static PBEParameter getInstance(Object paramObject)
  {
    if ((paramObject instanceof PBEParameter))
      return (PBEParameter)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PBEParameter((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public BigInteger getIterationCount()
  {
    return this.iterations.getValue();
  }

  public byte[] getSalt()
  {
    return this.salt.getOctets();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.salt);
    localASN1EncodableVector.add(this.iterations);
    return new DERSequence(localASN1EncodableVector);
  }
}