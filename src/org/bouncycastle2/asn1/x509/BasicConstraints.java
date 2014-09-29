package org.bouncycastle2.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBoolean;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class BasicConstraints extends ASN1Encodable
{
  DERBoolean cA = new DERBoolean(false);
  DERInteger pathLenConstraint = null;

  public BasicConstraints(int paramInt)
  {
    this.cA = new DERBoolean(true);
    this.pathLenConstraint = new DERInteger(paramInt);
  }

  public BasicConstraints(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() == 0)
    {
      this.cA = null;
      this.pathLenConstraint = null;
    }
    while (true)
    {
      return;
      if ((paramASN1Sequence.getObjectAt(0) instanceof DERBoolean))
        this.cA = DERBoolean.getInstance(paramASN1Sequence.getObjectAt(0));
      while (paramASN1Sequence.size() > 1)
      {
        if (this.cA == null)
          break label110;
        this.pathLenConstraint = DERInteger.getInstance(paramASN1Sequence.getObjectAt(1));
        return;
        this.cA = null;
        this.pathLenConstraint = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
      }
    }
    label110: throw new IllegalArgumentException("wrong sequence in constructor");
  }

  public BasicConstraints(boolean paramBoolean)
  {
    if (paramBoolean);
    for (this.cA = new DERBoolean(true); ; this.cA = null)
    {
      this.pathLenConstraint = null;
      return;
    }
  }

  public BasicConstraints(boolean paramBoolean, int paramInt)
  {
    if (paramBoolean)
    {
      this.cA = new DERBoolean(paramBoolean);
      this.pathLenConstraint = new DERInteger(paramInt);
      return;
    }
    this.cA = null;
    this.pathLenConstraint = null;
  }

  public static BasicConstraints getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof BasicConstraints)))
      return (BasicConstraints)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new BasicConstraints((ASN1Sequence)paramObject);
    if ((paramObject instanceof X509Extension))
      return getInstance(X509Extension.convertValueToObject((X509Extension)paramObject));
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static BasicConstraints getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public BigInteger getPathLenConstraint()
  {
    if (this.pathLenConstraint != null)
      return this.pathLenConstraint.getValue();
    return null;
  }

  public boolean isCA()
  {
    return (this.cA != null) && (this.cA.isTrue());
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.cA != null)
      localASN1EncodableVector.add(this.cA);
    if (this.pathLenConstraint != null)
      localASN1EncodableVector.add(this.pathLenConstraint);
    return new DERSequence(localASN1EncodableVector);
  }

  public String toString()
  {
    if (this.pathLenConstraint == null)
    {
      if (this.cA == null)
        return "BasicConstraints: isCa(false)";
      return "BasicConstraints: isCa(" + isCA() + ")";
    }
    return "BasicConstraints: isCa(" + isCA() + "), pathLenConstraint = " + this.pathLenConstraint.getValue();
  }
}