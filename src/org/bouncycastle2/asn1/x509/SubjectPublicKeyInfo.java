package org.bouncycastle2.asn1.x509;

import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class SubjectPublicKeyInfo extends ASN1Encodable
{
  private AlgorithmIdentifier algId;
  private DERBitString keyData;

  public SubjectPublicKeyInfo(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.algId = AlgorithmIdentifier.getInstance(localEnumeration.nextElement());
    this.keyData = DERBitString.getInstance(localEnumeration.nextElement());
  }

  public SubjectPublicKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, DEREncodable paramDEREncodable)
  {
    this.keyData = new DERBitString(paramDEREncodable);
    this.algId = paramAlgorithmIdentifier;
  }

  public SubjectPublicKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this.keyData = new DERBitString(paramArrayOfByte);
    this.algId = paramAlgorithmIdentifier;
  }

  public static SubjectPublicKeyInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof SubjectPublicKeyInfo))
      return (SubjectPublicKeyInfo)paramObject;
    if (paramObject != null)
      return new SubjectPublicKeyInfo(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public static SubjectPublicKeyInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AlgorithmIdentifier getAlgorithmId()
  {
    return this.algId;
  }

  public DERObject getPublicKey()
    throws IOException
  {
    return new ASN1InputStream(this.keyData.getBytes()).readObject();
  }

  public DERBitString getPublicKeyData()
  {
    return this.keyData;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.algId);
    localASN1EncodableVector.add(this.keyData);
    return new DERSequence(localASN1EncodableVector);
  }
}