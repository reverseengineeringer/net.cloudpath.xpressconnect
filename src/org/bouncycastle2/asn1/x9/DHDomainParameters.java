package org.bouncycastle2.asn1.x9;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class DHDomainParameters extends ASN1Encodable
{
  private DERInteger g;
  private DERInteger j;
  private DERInteger p;
  private DERInteger q;
  private DHValidationParms validationParms;

  private DHDomainParameters(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 3) || (paramASN1Sequence.size() > 5))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.p = DERInteger.getInstance(localEnumeration.nextElement());
    this.g = DERInteger.getInstance(localEnumeration.nextElement());
    this.q = DERInteger.getInstance(localEnumeration.nextElement());
    DEREncodable localDEREncodable = getNext(localEnumeration);
    if ((localDEREncodable != null) && ((localDEREncodable instanceof DERInteger)))
    {
      this.j = DERInteger.getInstance(localDEREncodable);
      localDEREncodable = getNext(localEnumeration);
    }
    if (localDEREncodable != null)
      this.validationParms = DHValidationParms.getInstance(localDEREncodable.getDERObject());
  }

  public DHDomainParameters(DERInteger paramDERInteger1, DERInteger paramDERInteger2, DERInteger paramDERInteger3, DERInteger paramDERInteger4, DHValidationParms paramDHValidationParms)
  {
    if (paramDERInteger1 == null)
      throw new IllegalArgumentException("'p' cannot be null");
    if (paramDERInteger2 == null)
      throw new IllegalArgumentException("'g' cannot be null");
    if (paramDERInteger3 == null)
      throw new IllegalArgumentException("'q' cannot be null");
    this.p = paramDERInteger1;
    this.g = paramDERInteger2;
    this.q = paramDERInteger3;
    this.j = paramDERInteger4;
    this.validationParms = paramDHValidationParms;
  }

  public static DHDomainParameters getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DHDomainParameters)))
      return (DHDomainParameters)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new DHDomainParameters((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid DHDomainParameters: " + paramObject.getClass().getName());
  }

  public static DHDomainParameters getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  private static DEREncodable getNext(Enumeration paramEnumeration)
  {
    if (paramEnumeration.hasMoreElements())
      return (DEREncodable)paramEnumeration.nextElement();
    return null;
  }

  public DERInteger getG()
  {
    return this.g;
  }

  public DERInteger getJ()
  {
    return this.j;
  }

  public DERInteger getP()
  {
    return this.p;
  }

  public DERInteger getQ()
  {
    return this.q;
  }

  public DHValidationParms getValidationParms()
  {
    return this.validationParms;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.p);
    localASN1EncodableVector.add(this.g);
    localASN1EncodableVector.add(this.q);
    if (this.j != null)
      localASN1EncodableVector.add(this.j);
    if (this.validationParms != null)
      localASN1EncodableVector.add(this.validationParms);
    return new DERSequence(localASN1EncodableVector);
  }
}