package org.bouncycastle2.asn1.esf;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERUTCTime;
import org.bouncycastle2.asn1.x500.X500Name;

public class CrlIdentifier extends ASN1Encodable
{
  private DERUTCTime crlIssuedTime;
  private X500Name crlIssuer;
  private DERInteger crlNumber;

  private CrlIdentifier(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 2) || (paramASN1Sequence.size() > 3))
      throw new IllegalArgumentException();
    this.crlIssuer = X500Name.getInstance(paramASN1Sequence.getObjectAt(0));
    this.crlIssuedTime = DERUTCTime.getInstance(paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() > 2)
      this.crlNumber = DERInteger.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public CrlIdentifier(X500Name paramX500Name, DERUTCTime paramDERUTCTime)
  {
    this(paramX500Name, paramDERUTCTime, null);
  }

  public CrlIdentifier(X500Name paramX500Name, DERUTCTime paramDERUTCTime, BigInteger paramBigInteger)
  {
    this.crlIssuer = paramX500Name;
    this.crlIssuedTime = paramDERUTCTime;
    if (paramBigInteger != null)
      this.crlNumber = new DERInteger(paramBigInteger);
  }

  public static CrlIdentifier getInstance(Object paramObject)
  {
    if ((paramObject instanceof CrlIdentifier))
      return (CrlIdentifier)paramObject;
    if (paramObject != null)
      return new CrlIdentifier(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance");
  }

  public DERUTCTime getCrlIssuedTime()
  {
    return this.crlIssuedTime;
  }

  public X500Name getCrlIssuer()
  {
    return this.crlIssuer;
  }

  public BigInteger getCrlNumber()
  {
    if (this.crlNumber == null)
      return null;
    return this.crlNumber.getValue();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.crlIssuer.toASN1Object());
    localASN1EncodableVector.add(this.crlIssuedTime);
    if (this.crlNumber != null)
      localASN1EncodableVector.add(this.crlNumber);
    return new DERSequence(localASN1EncodableVector);
  }
}