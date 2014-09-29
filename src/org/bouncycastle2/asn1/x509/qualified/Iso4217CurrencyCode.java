package org.bouncycastle2.asn1.x509.qualified;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERPrintableString;

public class Iso4217CurrencyCode extends ASN1Encodable
  implements ASN1Choice
{
  final int ALPHABETIC_MAXSIZE = 3;
  final int NUMERIC_MAXSIZE = 999;
  final int NUMERIC_MINSIZE = 1;
  int numeric;
  DEREncodable obj;

  public Iso4217CurrencyCode(int paramInt)
  {
    if ((paramInt > 999) || (paramInt < 1))
      throw new IllegalArgumentException("wrong size in numeric code : not in (1..999)");
    this.obj = new DERInteger(paramInt);
  }

  public Iso4217CurrencyCode(String paramString)
  {
    if (paramString.length() > 3)
      throw new IllegalArgumentException("wrong size in alphabetic code : max size is 3");
    this.obj = new DERPrintableString(paramString);
  }

  public static Iso4217CurrencyCode getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof Iso4217CurrencyCode)))
      return (Iso4217CurrencyCode)paramObject;
    if ((paramObject instanceof DERInteger))
      return new Iso4217CurrencyCode(DERInteger.getInstance(paramObject).getValue().intValue());
    if ((paramObject instanceof DERPrintableString))
      return new Iso4217CurrencyCode(DERPrintableString.getInstance(paramObject).getString());
    throw new IllegalArgumentException("unknown object in getInstance");
  }

  public String getAlphabetic()
  {
    return ((DERPrintableString)this.obj).getString();
  }

  public int getNumeric()
  {
    return ((DERInteger)this.obj).getValue().intValue();
  }

  public boolean isAlphabetic()
  {
    return this.obj instanceof DERPrintableString;
  }

  public DERObject toASN1Object()
  {
    return this.obj.getDERObject();
  }
}