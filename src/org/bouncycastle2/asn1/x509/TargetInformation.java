package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class TargetInformation extends ASN1Encodable
{
  private ASN1Sequence targets;

  private TargetInformation(ASN1Sequence paramASN1Sequence)
  {
    this.targets = paramASN1Sequence;
  }

  public TargetInformation(Targets paramTargets)
  {
    this.targets = new DERSequence(paramTargets);
  }

  public TargetInformation(Target[] paramArrayOfTarget)
  {
    this(new Targets(paramArrayOfTarget));
  }

  public static TargetInformation getInstance(Object paramObject)
  {
    if ((paramObject instanceof TargetInformation))
      return (TargetInformation)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new TargetInformation((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass());
  }

  public Targets[] getTargetsObjects()
  {
    Targets[] arrayOfTargets = new Targets[this.targets.size()];
    int i = 0;
    Enumeration localEnumeration = this.targets.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return arrayOfTargets;
      int j = i + 1;
      arrayOfTargets[i] = Targets.getInstance(localEnumeration.nextElement());
      i = j;
    }
  }

  public DERObject toASN1Object()
  {
    return this.targets;
  }
}