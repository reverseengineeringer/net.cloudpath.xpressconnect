package org.bouncycastle2.asn1.cms;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.asn1.x509.X509Name;

public class IssuerAndSerialNumber extends ASN1Encodable
{
  private X500Name name;
  private DERInteger serialNumber;

  public IssuerAndSerialNumber(ASN1Sequence paramASN1Sequence)
  {
    this.name = X500Name.getInstance(paramASN1Sequence.getObjectAt(0));
    this.serialNumber = ((DERInteger)paramASN1Sequence.getObjectAt(1));
  }

  public IssuerAndSerialNumber(X500Name paramX500Name, BigInteger paramBigInteger)
  {
    this.name = paramX500Name;
    this.serialNumber = new DERInteger(paramBigInteger);
  }

  public IssuerAndSerialNumber(X509Name paramX509Name, BigInteger paramBigInteger)
  {
    this.name = X500Name.getInstance(paramX509Name);
    this.serialNumber = new DERInteger(paramBigInteger);
  }

  public IssuerAndSerialNumber(X509Name paramX509Name, DERInteger paramDERInteger)
  {
    this.name = X500Name.getInstance(paramX509Name);
    this.serialNumber = paramDERInteger;
  }

  public static IssuerAndSerialNumber getInstance(Object paramObject)
  {
    if ((paramObject instanceof IssuerAndSerialNumber))
      return (IssuerAndSerialNumber)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new IssuerAndSerialNumber((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Illegal object in IssuerAndSerialNumber: " + paramObject.getClass().getName());
  }

  public X500Name getName()
  {
    return this.name;
  }

  public DERInteger getSerialNumber()
  {
    return this.serialNumber;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.name);
    localASN1EncodableVector.add(this.serialNumber);
    return new DERSequence(localASN1EncodableVector);
  }
}