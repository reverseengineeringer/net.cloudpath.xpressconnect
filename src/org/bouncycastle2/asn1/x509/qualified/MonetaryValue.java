package org.bouncycastle2.asn1.x509.qualified;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class MonetaryValue extends ASN1Encodable
{
  DERInteger amount;
  Iso4217CurrencyCode currency;
  DERInteger exponent;

  public MonetaryValue(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.currency = Iso4217CurrencyCode.getInstance(localEnumeration.nextElement());
    this.amount = DERInteger.getInstance(localEnumeration.nextElement());
    this.exponent = DERInteger.getInstance(localEnumeration.nextElement());
  }

  public MonetaryValue(Iso4217CurrencyCode paramIso4217CurrencyCode, int paramInt1, int paramInt2)
  {
    this.currency = paramIso4217CurrencyCode;
    this.amount = new DERInteger(paramInt1);
    this.exponent = new DERInteger(paramInt2);
  }

  public static MonetaryValue getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof MonetaryValue)))
      return (MonetaryValue)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new MonetaryValue(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("unknown object in getInstance");
  }

  public BigInteger getAmount()
  {
    return this.amount.getValue();
  }

  public Iso4217CurrencyCode getCurrency()
  {
    return this.currency;
  }

  public BigInteger getExponent()
  {
    return this.exponent.getValue();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.currency);
    localASN1EncodableVector.add(this.amount);
    localASN1EncodableVector.add(this.exponent);
    return new DERSequence(localASN1EncodableVector);
  }
}