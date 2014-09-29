package org.bouncycastle2.asn1.pkcs;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class RSASSAPSSparams extends ASN1Encodable
{
  public static final AlgorithmIdentifier DEFAULT_HASH_ALGORITHM = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, new DERNull());
  public static final AlgorithmIdentifier DEFAULT_MASK_GEN_FUNCTION = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, DEFAULT_HASH_ALGORITHM);
  public static final DERInteger DEFAULT_SALT_LENGTH = new DERInteger(20);
  public static final DERInteger DEFAULT_TRAILER_FIELD = new DERInteger(1);
  private AlgorithmIdentifier hashAlgorithm;
  private AlgorithmIdentifier maskGenAlgorithm;
  private DERInteger saltLength;
  private DERInteger trailerField;

  public RSASSAPSSparams()
  {
    this.hashAlgorithm = DEFAULT_HASH_ALGORITHM;
    this.maskGenAlgorithm = DEFAULT_MASK_GEN_FUNCTION;
    this.saltLength = DEFAULT_SALT_LENGTH;
    this.trailerField = DEFAULT_TRAILER_FIELD;
  }

  public RSASSAPSSparams(ASN1Sequence paramASN1Sequence)
  {
    this.hashAlgorithm = DEFAULT_HASH_ALGORITHM;
    this.maskGenAlgorithm = DEFAULT_MASK_GEN_FUNCTION;
    this.saltLength = DEFAULT_SALT_LENGTH;
    this.trailerField = DEFAULT_TRAILER_FIELD;
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
    case 3:
    }
    while (true)
    {
      i++;
      break;
      this.maskGenAlgorithm = AlgorithmIdentifier.getInstance(localASN1TaggedObject, true);
      continue;
      this.saltLength = DERInteger.getInstance(localASN1TaggedObject, true);
      continue;
      this.trailerField = DERInteger.getInstance(localASN1TaggedObject, true);
    }
  }

  public RSASSAPSSparams(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2, DERInteger paramDERInteger1, DERInteger paramDERInteger2)
  {
    this.hashAlgorithm = paramAlgorithmIdentifier1;
    this.maskGenAlgorithm = paramAlgorithmIdentifier2;
    this.saltLength = paramDERInteger1;
    this.trailerField = paramDERInteger2;
  }

  public static RSASSAPSSparams getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof RSASSAPSSparams)))
      return (RSASSAPSSparams)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RSASSAPSSparams((ASN1Sequence)paramObject);
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

  public DERInteger getSaltLength()
  {
    return this.saltLength;
  }

  public DERInteger getTrailerField()
  {
    return this.trailerField;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (!this.hashAlgorithm.equals(DEFAULT_HASH_ALGORITHM))
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.hashAlgorithm));
    if (!this.maskGenAlgorithm.equals(DEFAULT_MASK_GEN_FUNCTION))
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.maskGenAlgorithm));
    if (!this.saltLength.equals(DEFAULT_SALT_LENGTH))
      localASN1EncodableVector.add(new DERTaggedObject(true, 2, this.saltLength));
    if (!this.trailerField.equals(DEFAULT_TRAILER_FIELD))
      localASN1EncodableVector.add(new DERTaggedObject(true, 3, this.trailerField));
    return new DERSequence(localASN1EncodableVector);
  }
}