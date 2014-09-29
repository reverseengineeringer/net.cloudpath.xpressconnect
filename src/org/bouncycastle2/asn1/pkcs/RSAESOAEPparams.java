package org.bouncycastle2.asn1.pkcs;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class RSAESOAEPparams extends ASN1Encodable
{
  public static final AlgorithmIdentifier DEFAULT_HASH_ALGORITHM = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, new DERNull());
  public static final AlgorithmIdentifier DEFAULT_MASK_GEN_FUNCTION = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, DEFAULT_HASH_ALGORITHM);
  public static final AlgorithmIdentifier DEFAULT_P_SOURCE_ALGORITHM = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_pSpecified, new DEROctetString(new byte[0]));
  private AlgorithmIdentifier hashAlgorithm;
  private AlgorithmIdentifier maskGenAlgorithm;
  private AlgorithmIdentifier pSourceAlgorithm;

  public RSAESOAEPparams()
  {
    this.hashAlgorithm = DEFAULT_HASH_ALGORITHM;
    this.maskGenAlgorithm = DEFAULT_MASK_GEN_FUNCTION;
    this.pSourceAlgorithm = DEFAULT_P_SOURCE_ALGORITHM;
  }

  public RSAESOAEPparams(ASN1Sequence paramASN1Sequence)
  {
    this.hashAlgorithm = DEFAULT_HASH_ALGORITHM;
    this.maskGenAlgorithm = DEFAULT_MASK_GEN_FUNCTION;
    this.pSourceAlgorithm = DEFAULT_P_SOURCE_ALGORITHM;
    int i = 0;
    if (i == paramASN1Sequence.size())
      return;
    ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)paramASN1Sequence.getObjectAt(i);
    switch (localASN1TaggedObject.getTagNo())
    {
    default:
      throw new IllegalArgumentException("unknown tag");
    case 0:
      this.hashAlgorithm = AlgorithmIdentifier.getInstance(localASN1TaggedObject, true);
    case 1:
    case 2:
    }
    while (true)
    {
      i++;
      break;
      this.maskGenAlgorithm = AlgorithmIdentifier.getInstance(localASN1TaggedObject, true);
      continue;
      this.pSourceAlgorithm = AlgorithmIdentifier.getInstance(localASN1TaggedObject, true);
    }
  }

  public RSAESOAEPparams(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2, AlgorithmIdentifier paramAlgorithmIdentifier3)
  {
    this.hashAlgorithm = paramAlgorithmIdentifier1;
    this.maskGenAlgorithm = paramAlgorithmIdentifier2;
    this.pSourceAlgorithm = paramAlgorithmIdentifier3;
  }

  public static RSAESOAEPparams getInstance(Object paramObject)
  {
    if ((paramObject instanceof RSAESOAEPparams))
      return (RSAESOAEPparams)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RSAESOAEPparams((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public AlgorithmIdentifier getHashAlgorithm()
  {
    return this.hashAlgorithm;
  }

  public AlgorithmIdentifier getMaskGenAlgorithm()
  {
    return this.maskGenAlgorithm;
  }

  public AlgorithmIdentifier getPSourceAlgorithm()
  {
    return this.pSourceAlgorithm;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (!this.hashAlgorithm.equals(DEFAULT_HASH_ALGORITHM))
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.hashAlgorithm));
    if (!this.maskGenAlgorithm.equals(DEFAULT_MASK_GEN_FUNCTION))
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.maskGenAlgorithm));
    if (!this.pSourceAlgorithm.equals(DEFAULT_P_SOURCE_ALGORITHM))
      localASN1EncodableVector.add(new DERTaggedObject(true, 2, this.pSourceAlgorithm));
    return new DERSequence(localASN1EncodableVector);
  }
}