package org.bouncycastle2.asn1.ess;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.IssuerSerial;

public class ESSCertID extends ASN1Encodable
{
  private ASN1OctetString certHash;
  private IssuerSerial issuerSerial;

  public ESSCertID(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.certHash = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.issuerSerial = IssuerSerial.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public ESSCertID(byte[] paramArrayOfByte)
  {
    this.certHash = new DEROctetString(paramArrayOfByte);
  }

  public ESSCertID(byte[] paramArrayOfByte, IssuerSerial paramIssuerSerial)
  {
    this.certHash = new DEROctetString(paramArrayOfByte);
    this.issuerSerial = paramIssuerSerial;
  }

  public static ESSCertID getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ESSCertID)))
      return (ESSCertID)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ESSCertID((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in 'ESSCertID' factory : " + paramObject.getClass().getName() + ".");
  }

  public byte[] getCertHash()
  {
    return this.certHash.getOctets();
  }

  public IssuerSerial getIssuerSerial()
  {
    return this.issuerSerial;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.certHash);
    if (this.issuerSerial != null)
      localASN1EncodableVector.add(this.issuerSerial);
    return new DERSequence(localASN1EncodableVector);
  }
}