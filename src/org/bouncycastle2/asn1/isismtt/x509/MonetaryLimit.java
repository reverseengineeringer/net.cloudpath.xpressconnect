package org.bouncycastle2.asn1.isismtt.x509;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.asn1.DERSequence;

public class MonetaryLimit extends ASN1Encodable
{
  DERInteger amount;
  DERPrintableString currency;
  DERInteger exponent;

  public MonetaryLimit(String paramString, int paramInt1, int paramInt2)
  {
    this.currency = new DERPrintableString(paramString, true);
    this.amount = new DERInteger(paramInt1);
    this.exponent = new DERInteger(paramInt2);
  }

  private MonetaryLimit(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 3)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.currency = DERPrintableString.getInstance(localEnumeration.nextElement());
    this.amount = DERInteger.getInstance(localEnumeration.nextElement());
    this.exponent = DERInteger.getInstance(localEnumeration.nextElement());
  }

  public static MonetaryLimit getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof MonetaryLimit)))
      return (MonetaryLimit)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new MonetaryLimit(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("unknown object in getInstance");
  }

  public BigInteger getAmount()
  {
    return this.amount.getValue();
  }

  public String getCurrency()
  {
    return this.currency.getString();
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