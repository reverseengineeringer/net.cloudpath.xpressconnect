package org.bouncycastle2.asn1.pkcs;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.BERTaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class EncryptedData extends ASN1Encodable
{
  DERObjectIdentifier bagId;
  DERObject bagValue;
  ASN1Sequence data;

  public EncryptedData(ASN1Sequence paramASN1Sequence)
  {
    if (((DERInteger)paramASN1Sequence.getObjectAt(0)).getValue().intValue() != 0)
      throw new IllegalArgumentException("sequence not version 0");
    this.data = ((ASN1Sequence)paramASN1Sequence.getObjectAt(1));
  }

  public EncryptedData(DERObjectIdentifier paramDERObjectIdentifier, AlgorithmIdentifier paramAlgorithmIdentifier, DEREncodable paramDEREncodable)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(paramDERObjectIdentifier);
    localASN1EncodableVector.add(paramAlgorithmIdentifier.getDERObject());
    localASN1EncodableVector.add(new BERTaggedObject(false, 0, paramDEREncodable));
    this.data = new BERSequence(localASN1EncodableVector);
  }

  public static EncryptedData getInstance(Object paramObject)
  {
    if ((paramObject instanceof EncryptedData))
      return (EncryptedData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new EncryptedData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public ASN1OctetString getContent()
  {
    if (this.data.size() == 3)
      return ASN1OctetString.getInstance((DERTaggedObject)this.data.getObjectAt(2), false);
    return null;
  }

  public DERObjectIdentifier getContentType()
  {
    return (DERObjectIdentifier)this.data.getObjectAt(0);
  }

  public AlgorithmIdentifier getEncryptionAlgorithm()
  {
    return AlgorithmIdentifier.getInstance(this.data.getObjectAt(1));
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(new DERInteger(0));
    localASN1EncodableVector.add(this.data);
    return new BERSequence(localASN1EncodableVector);
  }
}