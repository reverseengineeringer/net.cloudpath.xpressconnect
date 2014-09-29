package org.bouncycastle2.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class CrlListID extends ASN1Encodable
{
  private ASN1Sequence crls;

  private CrlListID(ASN1Sequence paramASN1Sequence)
  {
    this.crls = ((ASN1Sequence)paramASN1Sequence.getObjectAt(0));
    Enumeration localEnumeration = this.crls.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      CrlValidatedID.getInstance(localEnumeration.nextElement());
    }
  }

  public CrlListID(CrlValidatedID[] paramArrayOfCrlValidatedID)
  {
    this.crls = new DERSequence(paramArrayOfCrlValidatedID);
  }

  public static CrlListID getInstance(Object paramObject)
  {
    if ((paramObject instanceof CrlListID))
      return (CrlListID)paramObject;
    if (paramObject != null)
      return new CrlListID(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance");
  }

  public CrlValidatedID[] getCrls()
  {
    CrlValidatedID[] arrayOfCrlValidatedID = new CrlValidatedID[this.crls.size()];
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfCrlValidatedID.length)
        return arrayOfCrlValidatedID;
      arrayOfCrlValidatedID[i] = CrlValidatedID.getInstance(this.crls.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    return new DERSequence(this.crls);
  }
}