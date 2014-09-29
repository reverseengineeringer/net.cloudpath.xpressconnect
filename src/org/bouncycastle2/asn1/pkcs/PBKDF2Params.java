package org.bouncycastle2.asn1.pkcs;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;

public class PBKDF2Params extends ASN1Encodable
{
  DERInteger iterationCount;
  DERInteger keyLength;
  ASN1OctetString octStr;

  public PBKDF2Params(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.octStr = ((ASN1OctetString)localEnumeration.nextElement());
    this.iterationCount = ((DERInteger)localEnumeration.nextElement());
    if (localEnumeration.hasMoreElements())
    {
      this.keyLength = ((DERInteger)localEnumeration.nextElement());
      return;
    }
    this.keyLength = null;
  }

  public PBKDF2Params(byte[] paramArrayOfByte, int paramInt)
  {
    this.octStr = new DEROctetString(paramArrayOfByte);
    this.iterationCount = new DERInteger(paramInt);
  }

  public static PBKDF2Params getInstance(Object paramObject)
  {
    if ((paramObject instanceof PBKDF2Params))
      return (PBKDF2Params)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PBKDF2Params((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public BigInteger getIterationCount()
  {
    return this.iterationCount.getValue();
  }

  public BigInteger getKeyLength()
  {
    if (this.keyLength != null)
      return this.keyLength.getValue();
    return null;
  }

  public byte[] getSalt()
  {
    return this.octStr.getOctets();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.octStr);
    localASN1EncodableVector.add(this.iterationCount);
    if (this.keyLength != null)
      localASN1EncodableVector.add(this.keyLength);
    return new DERSequence(localASN1EncodableVector);
  }
}