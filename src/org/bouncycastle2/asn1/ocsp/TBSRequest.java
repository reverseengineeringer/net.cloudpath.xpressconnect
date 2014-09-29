package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class TBSRequest extends ASN1Encodable
{
  private static final DERInteger V1 = new DERInteger(0);
  X509Extensions requestExtensions;
  ASN1Sequence requestList;
  GeneralName requestorName;
  DERInteger version;
  boolean versionSet;

  public TBSRequest(ASN1Sequence paramASN1Sequence)
  {
    int i;
    if ((paramASN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject))
      if (((ASN1TaggedObject)paramASN1Sequence.getObjectAt(0)).getTagNo() == 0)
      {
        this.versionSet = true;
        this.version = DERInteger.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(0), true);
        i = 0 + 1;
      }
    while (true)
    {
      if ((paramASN1Sequence.getObjectAt(i) instanceof ASN1TaggedObject))
      {
        int k = i + 1;
        this.requestorName = GeneralName.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(i), true);
        i = k;
      }
      int j = i + 1;
      this.requestList = ((ASN1Sequence)paramASN1Sequence.getObjectAt(i));
      if (paramASN1Sequence.size() == j + 1)
        this.requestExtensions = X509Extensions.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(j), true);
      return;
      this.version = V1;
      i = 0;
      continue;
      this.version = V1;
      i = 0;
    }
  }

  public TBSRequest(GeneralName paramGeneralName, ASN1Sequence paramASN1Sequence, X509Extensions paramX509Extensions)
  {
    this.version = V1;
    this.requestorName = paramGeneralName;
    this.requestList = paramASN1Sequence;
    this.requestExtensions = paramX509Extensions;
  }

  public static TBSRequest getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof TBSRequest)))
      return (TBSRequest)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new TBSRequest((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static TBSRequest getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public X509Extensions getRequestExtensions()
  {
    return this.requestExtensions;
  }

  public ASN1Sequence getRequestList()
  {
    return this.requestList;
  }

  public GeneralName getRequestorName()
  {
    return this.requestorName;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if ((!this.version.equals(V1)) || (this.versionSet))
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.version));
    if (this.requestorName != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.requestorName));
    localASN1EncodableVector.add(this.requestList);
    if (this.requestExtensions != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 2, this.requestExtensions));
    return new DERSequence(localASN1EncodableVector);
  }
}