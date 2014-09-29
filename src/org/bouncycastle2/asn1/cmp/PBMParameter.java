package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class PBMParameter extends ASN1Encodable
{
  private DERInteger iterationCount;
  private AlgorithmIdentifier mac;
  private AlgorithmIdentifier owf;
  private ASN1OctetString salt;

  public PBMParameter(ASN1OctetString paramASN1OctetString, AlgorithmIdentifier paramAlgorithmIdentifier1, DERInteger paramDERInteger, AlgorithmIdentifier paramAlgorithmIdentifier2)
  {
    this.salt = paramASN1OctetString;
    this.owf = paramAlgorithmIdentifier1;
    this.iterationCount = paramDERInteger;
    this.mac = paramAlgorithmIdentifier2;
  }

  private PBMParameter(ASN1Sequence paramASN1Sequence)
  {
    this.salt = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(0));
    this.owf = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
    this.iterationCount = DERInteger.getInstance(paramASN1Sequence.getObjectAt(2));
    this.mac = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(3));
  }

  public PBMParameter(byte[] paramArrayOfByte, AlgorithmIdentifier paramAlgorithmIdentifier1, int paramInt, AlgorithmIdentifier paramAlgorithmIdentifier2)
  {
    this(new DEROctetString(paramArrayOfByte), paramAlgorithmIdentifier1, new DERInteger(paramInt), paramAlgorithmIdentifier2);
  }

  public static PBMParameter getInstance(Object paramObject)
  {
    if ((paramObject instanceof PBMParameter))
      return (PBMParameter)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PBMParameter((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERInteger getIterationCount()
  {
    return this.iterationCount;
  }

  public AlgorithmIdentifier getMac()
  {
    return this.mac;
  }

  public AlgorithmIdentifier getOwf()
  {
    return this.owf;
  }

  public ASN1OctetString getSalt()
  {
    return this.salt;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.salt);
    localASN1EncodableVector.add(this.owf);
    localASN1EncodableVector.add(this.iterationCount);
    localASN1EncodableVector.add(this.mac);
    return new DERSequence(localASN1EncodableVector);
  }
}