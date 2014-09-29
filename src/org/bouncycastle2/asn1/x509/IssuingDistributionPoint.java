package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBoolean;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class IssuingDistributionPoint extends ASN1Encodable
{
  private DistributionPointName distributionPoint;
  private boolean indirectCRL;
  private boolean onlyContainsAttributeCerts;
  private boolean onlyContainsCACerts;
  private boolean onlyContainsUserCerts;
  private ReasonFlags onlySomeReasons;
  private ASN1Sequence seq;

  public IssuingDistributionPoint(ASN1Sequence paramASN1Sequence)
  {
    this.seq = paramASN1Sequence;
    int i = 0;
    if (i == paramASN1Sequence.size())
      return;
    ASN1TaggedObject localASN1TaggedObject = ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(i));
    switch (localASN1TaggedObject.getTagNo())
    {
    default:
      throw new IllegalArgumentException("unknown tag in IssuingDistributionPoint");
    case 0:
      this.distributionPoint = DistributionPointName.getInstance(localASN1TaggedObject, true);
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    }
    while (true)
    {
      i++;
      break;
      this.onlyContainsUserCerts = DERBoolean.getInstance(localASN1TaggedObject, false).isTrue();
      continue;
      this.onlyContainsCACerts = DERBoolean.getInstance(localASN1TaggedObject, false).isTrue();
      continue;
      this.onlySomeReasons = new ReasonFlags(ReasonFlags.getInstance(localASN1TaggedObject, false));
      continue;
      this.indirectCRL = DERBoolean.getInstance(localASN1TaggedObject, false).isTrue();
      continue;
      this.onlyContainsAttributeCerts = DERBoolean.getInstance(localASN1TaggedObject, false).isTrue();
    }
  }

  public IssuingDistributionPoint(DistributionPointName paramDistributionPointName, boolean paramBoolean1, boolean paramBoolean2, ReasonFlags paramReasonFlags, boolean paramBoolean3, boolean paramBoolean4)
  {
    this.distributionPoint = paramDistributionPointName;
    this.indirectCRL = paramBoolean3;
    this.onlyContainsAttributeCerts = paramBoolean4;
    this.onlyContainsCACerts = paramBoolean2;
    this.onlyContainsUserCerts = paramBoolean1;
    this.onlySomeReasons = paramReasonFlags;
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (paramDistributionPointName != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, paramDistributionPointName));
    if (paramBoolean1)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, new DERBoolean(true)));
    if (paramBoolean2)
      localASN1EncodableVector.add(new DERTaggedObject(false, 2, new DERBoolean(true)));
    if (paramReasonFlags != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 3, paramReasonFlags));
    if (paramBoolean3)
      localASN1EncodableVector.add(new DERTaggedObject(false, 4, new DERBoolean(true)));
    if (paramBoolean4)
      localASN1EncodableVector.add(new DERTaggedObject(false, 5, new DERBoolean(true)));
    this.seq = new DERSequence(localASN1EncodableVector);
  }

  private void appendObject(StringBuffer paramStringBuffer, String paramString1, String paramString2, String paramString3)
  {
    paramStringBuffer.append("    ");
    paramStringBuffer.append(paramString2);
    paramStringBuffer.append(":");
    paramStringBuffer.append(paramString1);
    paramStringBuffer.append("    ");
    paramStringBuffer.append("    ");
    paramStringBuffer.append(paramString3);
    paramStringBuffer.append(paramString1);
  }

  private String booleanToString(boolean paramBoolean)
  {
    if (paramBoolean)
      return "true";
    return "false";
  }

  public static IssuingDistributionPoint getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof IssuingDistributionPoint)))
      return (IssuingDistributionPoint)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new IssuingDistributionPoint((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static IssuingDistributionPoint getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DistributionPointName getDistributionPoint()
  {
    return this.distributionPoint;
  }

  public ReasonFlags getOnlySomeReasons()
  {
    return this.onlySomeReasons;
  }

  public boolean isIndirectCRL()
  {
    return this.indirectCRL;
  }

  public boolean onlyContainsAttributeCerts()
  {
    return this.onlyContainsAttributeCerts;
  }

  public boolean onlyContainsCACerts()
  {
    return this.onlyContainsCACerts;
  }

  public boolean onlyContainsUserCerts()
  {
    return this.onlyContainsUserCerts;
  }

  public DERObject toASN1Object()
  {
    return this.seq;
  }

  public String toString()
  {
    String str = System.getProperty("line.separator");
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("IssuingDistributionPoint: [");
    localStringBuffer.append(str);
    if (this.distributionPoint != null)
      appendObject(localStringBuffer, str, "distributionPoint", this.distributionPoint.toString());
    if (this.onlyContainsUserCerts)
      appendObject(localStringBuffer, str, "onlyContainsUserCerts", booleanToString(this.onlyContainsUserCerts));
    if (this.onlyContainsCACerts)
      appendObject(localStringBuffer, str, "onlyContainsCACerts", booleanToString(this.onlyContainsCACerts));
    if (this.onlySomeReasons != null)
      appendObject(localStringBuffer, str, "onlySomeReasons", this.onlySomeReasons.toString());
    if (this.onlyContainsAttributeCerts)
      appendObject(localStringBuffer, str, "onlyContainsAttributeCerts", booleanToString(this.onlyContainsAttributeCerts));
    if (this.indirectCRL)
      appendObject(localStringBuffer, str, "indirectCRL", booleanToString(this.indirectCRL));
    localStringBuffer.append("]");
    localStringBuffer.append(str);
    return localStringBuffer.toString();
  }
}