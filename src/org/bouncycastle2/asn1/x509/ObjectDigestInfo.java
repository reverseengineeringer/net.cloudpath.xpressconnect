package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREnumerated;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class ObjectDigestInfo extends ASN1Encodable
{
  public static final int otherObjectDigest = 2;
  public static final int publicKey = 0;
  public static final int publicKeyCert = 1;
  AlgorithmIdentifier digestAlgorithm;
  DEREnumerated digestedObjectType;
  DERBitString objectDigest;
  DERObjectIdentifier otherObjectTypeID;

  public ObjectDigestInfo(int paramInt, String paramString, AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this.digestedObjectType = new DEREnumerated(paramInt);
    if (paramInt == 2)
      this.otherObjectTypeID = new DERObjectIdentifier(paramString);
    this.digestAlgorithm = paramAlgorithmIdentifier;
    this.objectDigest = new DERBitString(paramArrayOfByte);
  }

  private ObjectDigestInfo(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() > 4) || (paramASN1Sequence.size() < 3))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.digestedObjectType = DEREnumerated.getInstance(paramASN1Sequence.getObjectAt(0));
    int i = paramASN1Sequence.size();
    int j = 0;
    if (i == 4)
    {
      this.otherObjectTypeID = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
      j = 0 + 1;
    }
    this.digestAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(j + 1));
    this.objectDigest = DERBitString.getInstance(paramASN1Sequence.getObjectAt(j + 2));
  }

  public static ObjectDigestInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ObjectDigestInfo)))
      return (ObjectDigestInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ObjectDigestInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static ObjectDigestInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AlgorithmIdentifier getDigestAlgorithm()
  {
    return this.digestAlgorithm;
  }

  public DEREnumerated getDigestedObjectType()
  {
    return this.digestedObjectType;
  }

  public DERBitString getObjectDigest()
  {
    return this.objectDigest;
  }

  public DERObjectIdentifier getOtherObjectTypeID()
  {
    return this.otherObjectTypeID;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.digestedObjectType);
    if (this.otherObjectTypeID != null)
      localASN1EncodableVector.add(this.otherObjectTypeID);
    localASN1EncodableVector.add(this.digestAlgorithm);
    localASN1EncodableVector.add(this.objectDigest);
    return new DERSequence(localASN1EncodableVector);
  }
}