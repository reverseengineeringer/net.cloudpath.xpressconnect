package org.bouncycastle2.asn1.sec;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.util.BigIntegers;

public class ECPrivateKeyStructure extends ASN1Encodable
{
  private ASN1Sequence seq;

  public ECPrivateKeyStructure(BigInteger paramBigInteger)
  {
    byte[] arrayOfByte = BigIntegers.asUnsignedByteArray(paramBigInteger);
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(new DERInteger(1));
    localASN1EncodableVector.add(new DEROctetString(arrayOfByte));
    this.seq = new DERSequence(localASN1EncodableVector);
  }

  public ECPrivateKeyStructure(BigInteger paramBigInteger, ASN1Encodable paramASN1Encodable)
  {
    this(paramBigInteger, null, paramASN1Encodable);
  }

  public ECPrivateKeyStructure(BigInteger paramBigInteger, DERBitString paramDERBitString, ASN1Encodable paramASN1Encodable)
  {
    byte[] arrayOfByte = BigIntegers.asUnsignedByteArray(paramBigInteger);
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(new DERInteger(1));
    localASN1EncodableVector.add(new DEROctetString(arrayOfByte));
    if (paramASN1Encodable != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, paramASN1Encodable));
    if (paramDERBitString != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, paramDERBitString));
    this.seq = new DERSequence(localASN1EncodableVector);
  }

  public ECPrivateKeyStructure(ASN1Sequence paramASN1Sequence)
  {
    this.seq = paramASN1Sequence;
  }

  private ASN1Object getObjectInTag(int paramInt)
  {
    Enumeration localEnumeration = this.seq.getObjects();
    ASN1TaggedObject localASN1TaggedObject;
    do
    {
      DEREncodable localDEREncodable;
      do
      {
        if (!localEnumeration.hasMoreElements())
          return null;
        localDEREncodable = (DEREncodable)localEnumeration.nextElement();
      }
      while (!(localDEREncodable instanceof ASN1TaggedObject));
      localASN1TaggedObject = (ASN1TaggedObject)localDEREncodable;
    }
    while (localASN1TaggedObject.getTagNo() != paramInt);
    return (ASN1Object)localASN1TaggedObject.getObject().getDERObject();
  }

  public BigInteger getKey()
  {
    return new BigInteger(1, ((ASN1OctetString)this.seq.getObjectAt(1)).getOctets());
  }

  public ASN1Object getParameters()
  {
    return getObjectInTag(0);
  }

  public DERBitString getPublicKey()
  {
    return (DERBitString)getObjectInTag(1);
  }

  public DERObject toASN1Object()
  {
    return this.seq;
  }
}