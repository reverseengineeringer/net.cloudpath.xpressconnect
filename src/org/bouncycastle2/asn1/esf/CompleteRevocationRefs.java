package org.bouncycastle2.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class CompleteRevocationRefs extends ASN1Encodable
{
  private ASN1Sequence crlOcspRefs;

  private CompleteRevocationRefs(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
      {
        this.crlOcspRefs = paramASN1Sequence;
        return;
      }
      CrlOcspRef.getInstance(localEnumeration.nextElement());
    }
  }

  public CompleteRevocationRefs(CrlOcspRef[] paramArrayOfCrlOcspRef)
  {
    this.crlOcspRefs = new DERSequence(paramArrayOfCrlOcspRef);
  }

  public static CompleteRevocationRefs getInstance(Object paramObject)
  {
    if ((paramObject instanceof CompleteRevocationRefs))
      return (CompleteRevocationRefs)paramObject;
    if (paramObject != null)
      return new CompleteRevocationRefs(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance");
  }

  public CrlOcspRef[] getCrlOcspRefs()
  {
    CrlOcspRef[] arrayOfCrlOcspRef = new CrlOcspRef[this.crlOcspRefs.size()];
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfCrlOcspRef.length)
        return arrayOfCrlOcspRef;
      arrayOfCrlOcspRef[i] = CrlOcspRef.getInstance(this.crlOcspRefs.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    return this.crlOcspRefs;
  }
}