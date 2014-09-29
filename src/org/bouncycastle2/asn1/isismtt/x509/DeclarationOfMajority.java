package org.bouncycastle2.asn1.isismtt.x509;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBoolean;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class DeclarationOfMajority extends ASN1Encodable
  implements ASN1Choice
{
  public static final int dateOfBirth = 2;
  public static final int fullAgeAtCountry = 1;
  public static final int notYoungerThan;
  private ASN1TaggedObject declaration;

  public DeclarationOfMajority(int paramInt)
  {
    this.declaration = new DERTaggedObject(false, 0, new DERInteger(paramInt));
  }

  private DeclarationOfMajority(ASN1TaggedObject paramASN1TaggedObject)
  {
    if (paramASN1TaggedObject.getTagNo() > 2)
      throw new IllegalArgumentException("Bad tag number: " + paramASN1TaggedObject.getTagNo());
    this.declaration = paramASN1TaggedObject;
  }

  public DeclarationOfMajority(DERGeneralizedTime paramDERGeneralizedTime)
  {
    this.declaration = new DERTaggedObject(false, 2, paramDERGeneralizedTime);
  }

  public DeclarationOfMajority(boolean paramBoolean, String paramString)
  {
    if (paramString.length() > 2)
      throw new IllegalArgumentException("country can only be 2 characters");
    if (paramBoolean)
    {
      this.declaration = new DERTaggedObject(false, 1, new DERSequence(new DERPrintableString(paramString, true)));
      return;
    }
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(DERBoolean.FALSE);
    localASN1EncodableVector.add(new DERPrintableString(paramString, true));
    this.declaration = new DERTaggedObject(false, 1, new DERSequence(localASN1EncodableVector));
  }

  public static DeclarationOfMajority getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DeclarationOfMajority)))
      return (DeclarationOfMajority)paramObject;
    if ((paramObject instanceof ASN1TaggedObject))
      return new DeclarationOfMajority((ASN1TaggedObject)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public ASN1Sequence fullAgeAtCountry()
  {
    if (this.declaration.getTagNo() != 1)
      return null;
    return ASN1Sequence.getInstance(this.declaration, false);
  }

  public DERGeneralizedTime getDateOfBirth()
  {
    if (this.declaration.getTagNo() != 2)
      return null;
    return DERGeneralizedTime.getInstance(this.declaration, false);
  }

  public int getType()
  {
    return this.declaration.getTagNo();
  }

  public int notYoungerThan()
  {
    if (this.declaration.getTagNo() != 0)
      return -1;
    return DERInteger.getInstance(this.declaration, false).getValue().intValue();
  }

  public DERObject toASN1Object()
  {
    return this.declaration;
  }
}