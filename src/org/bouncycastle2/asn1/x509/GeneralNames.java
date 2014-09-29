package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class GeneralNames extends ASN1Encodable
{
  private final GeneralName[] names;

  public GeneralNames(ASN1Sequence paramASN1Sequence)
  {
    this.names = new GeneralName[paramASN1Sequence.size()];
    for (int i = 0; ; i++)
    {
      if (i == paramASN1Sequence.size())
        return;
      this.names[i] = GeneralName.getInstance(paramASN1Sequence.getObjectAt(i));
    }
  }

  public GeneralNames(GeneralName paramGeneralName)
  {
    this.names = new GeneralName[] { paramGeneralName };
  }

  public static GeneralNames getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof GeneralNames)))
      return (GeneralNames)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new GeneralNames((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static GeneralNames getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public GeneralName[] getNames()
  {
    GeneralName[] arrayOfGeneralName = new GeneralName[this.names.length];
    System.arraycopy(this.names, 0, arrayOfGeneralName, 0, this.names.length);
    return arrayOfGeneralName;
  }

  public DERObject toASN1Object()
  {
    return new DERSequence(this.names);
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("GeneralNames:");
    localStringBuffer.append(str);
    for (int i = 0; ; i++)
    {
      if (i == this.names.length)
        return localStringBuffer.toString();
      localStringBuffer.append("    ");
      localStringBuffer.append(this.names[i]);
      localStringBuffer.append(str);
    }
  }
}