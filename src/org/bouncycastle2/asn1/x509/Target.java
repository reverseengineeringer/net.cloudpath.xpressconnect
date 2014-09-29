package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class Target extends ASN1Encodable
  implements ASN1Choice
{
  public static final int targetGroup = 1;
  public static final int targetName;
  private GeneralName targGroup;
  private GeneralName targName;

  public Target(int paramInt, GeneralName paramGeneralName)
  {
    this(new DERTaggedObject(paramInt, paramGeneralName));
  }

  private Target(ASN1TaggedObject paramASN1TaggedObject)
  {
    switch (paramASN1TaggedObject.getTagNo())
    {
    default:
      throw new IllegalArgumentException("unknown tag: " + paramASN1TaggedObject.getTagNo());
    case 0:
      this.targName = GeneralName.getInstance(paramASN1TaggedObject, true);
      return;
    case 1:
    }
    this.targGroup = GeneralName.getInstance(paramASN1TaggedObject, true);
  }

  public static Target getInstance(Object paramObject)
  {
    if ((paramObject instanceof Target))
      return (Target)paramObject;
    if ((paramObject instanceof ASN1TaggedObject))
      return new Target((ASN1TaggedObject)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass());
  }

  public GeneralName getTargetGroup()
  {
    return this.targGroup;
  }

  public GeneralName getTargetName()
  {
    return this.targName;
  }

  public DERObject toASN1Object()
  {
    if (this.targName != null)
      return new DERTaggedObject(true, 0, this.targName);
    return new DERTaggedObject(true, 1, this.targGroup);
  }
}