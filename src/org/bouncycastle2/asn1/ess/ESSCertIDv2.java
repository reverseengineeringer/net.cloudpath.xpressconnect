package org.bouncycastle2.asn1.ess;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.IssuerSerial;

public class ESSCertIDv2 extends ASN1Encodable
{
  private static final AlgorithmIdentifier DEFAULT_ALG_ID = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256);
  private byte[] certHash;
  private AlgorithmIdentifier hashAlgorithm;
  private IssuerSerial issuerSerial;

  public ESSCertIDv2(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() > 3)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    int i = 0;
    if ((paramASN1Sequence.getObjectAt(0) instanceof ASN1OctetString))
      this.hashAlgorithm = DEFAULT_ALG_ID;
    while (true)
    {
      int k = i + 1;
      this.certHash = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(i).getDERObject()).getOctets();
      if (paramASN1Sequence.size() > k)
        this.issuerSerial = new IssuerSerial(ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(k).getDERObject()));
      return;
      int j = 0 + 1;
      this.hashAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(0).getDERObject());
      i = j;
    }
  }

  public ESSCertIDv2(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this(paramAlgorithmIdentifier, paramArrayOfByte, null);
  }

  public ESSCertIDv2(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte, IssuerSerial paramIssuerSerial)
  {
    if (paramAlgorithmIdentifier == null);
    for (this.hashAlgorithm = DEFAULT_ALG_ID; ; this.hashAlgorithm = paramAlgorithmIdentifier)
    {
      this.certHash = paramArrayOfByte;
      this.issuerSerial = paramIssuerSerial;
      return;
    }
  }

  public static ESSCertIDv2 getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ESSCertIDv2)))
      return (ESSCertIDv2)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ESSCertIDv2((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in 'ESSCertIDv2' factory : " + paramObject.getClass().getName() + ".");
  }

  public byte[] getCertHash()
  {
    return this.certHash;
  }

  public AlgorithmIdentifier getHashAlgorithm()
  {
    return this.hashAlgorithm;
  }

  public IssuerSerial getIssuerSerial()
  {
    return this.issuerSerial;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (!this.hashAlgorithm.equals(DEFAULT_ALG_ID))
      localASN1EncodableVector.add(this.hashAlgorithm);
    localASN1EncodableVector.add(new DEROctetString(this.certHash).toASN1Object());
    if (this.issuerSerial != null)
      localASN1EncodableVector.add(this.issuerSerial);
    return new DERSequence(localASN1EncodableVector);
  }
}