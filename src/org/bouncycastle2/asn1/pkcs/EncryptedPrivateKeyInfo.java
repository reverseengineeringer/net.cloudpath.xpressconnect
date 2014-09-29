package org.bouncycastle2.asn1.pkcs;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class EncryptedPrivateKeyInfo extends ASN1Encodable
{
  private AlgorithmIdentifier algId;
  private ASN1OctetString data;

  public EncryptedPrivateKeyInfo(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.algId = AlgorithmIdentifier.getInstance(localEnumeration.nextElement());
    this.data = ((ASN1OctetString)localEnumeration.nextElement());
  }

  public EncryptedPrivateKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this.algId = paramAlgorithmIdentifier;
    this.data = new DEROctetString(paramArrayOfByte);
  }

  public static EncryptedPrivateKeyInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof EncryptedData))
      return (EncryptedPrivateKeyInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new EncryptedPrivateKeyInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public byte[] getEncryptedData()
  {
    return this.data.getOctets();
  }

  public AlgorithmIdentifier getEncryptionAlgorithm()
  {
    return this.algId;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.algId);
    localASN1EncodableVector.add(this.data);
    return new DERSequence(localASN1EncodableVector);
  }
}