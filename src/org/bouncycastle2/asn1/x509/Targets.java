package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class Targets extends ASN1Encodable
{
  private ASN1Sequence targets;

  private Targets(ASN1Sequence paramASN1Sequence)
  {
    this.targets = paramASN1Sequence;
  }

  public Targets(Target[] paramArrayOfTarget)
  {
    this.targets = new DERSequence(paramArrayOfTarget);
  }

  public static Targets getInstance(Object paramObject)
  {
    if ((paramObject instanceof Targets))
      return (Targets)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new Targets((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass());
  }

  public Target[] getTargets()
  {
    Target[] arrayOfTarget = new Target[this.targets.size()];
    int i = 0;
    Enumeration localEnumeration = this.targets.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return arrayOfTarget;
      int j = i + 1;
      arrayOfTarget[i] = Target.getInstance(localEnumeration.nextElement());
      i = j;
    }
  }

  public DERObject toASN1Object()
  {
    return this.targets;
  }
}