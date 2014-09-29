package org.bouncycastle2.asn1.pkcs;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.X509Name;

public class IssuerAndSerialNumber extends ASN1Encodable
{
  DERInteger certSerialNumber;
  X509Name name;

  public IssuerAndSerialNumber(ASN1Sequence paramASN1Sequence)
  {
    this.name = X509Name.getInstance(paramASN1Sequence.getObjectAt(0));
    this.certSerialNumber = ((DERInteger)paramASN1Sequence.getObjectAt(1));
  }

  public IssuerAndSerialNumber(X509Name paramX509Name, BigInteger paramBigInteger)
  {
    this.name = paramX509Name;
    this.certSerialNumber = new DERInteger(paramBigInteger);
  }

  public IssuerAndSerialNumber(X509Name paramX509Name, DERInteger paramDERInteger)
  {
    this.name = paramX509Name;
    this.certSerialNumber = paramDERInteger;
  }

  public static IssuerAndSerialNumber getInstance(Object paramObject)
  {
    if ((paramObject instanceof IssuerAndSerialNumber))
      return (IssuerAndSerialNumber)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new IssuerAndSerialNumber((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public DERInteger getCertificateSerialNumber()
  {
    return this.certSerialNumber;
  }

  public X509Name getName()
  {
    return this.name;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.name);
    localASN1EncodableVector.add(this.certSerialNumber);
    return new DERSequence(localASN1EncodableVector);
  }
}