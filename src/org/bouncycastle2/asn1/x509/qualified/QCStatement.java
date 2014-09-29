package org.bouncycastle2.asn1.x509.qualified;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class QCStatement extends ASN1Encodable
  implements ETSIQCObjectIdentifiers, RFC3739QCObjectIdentifiers
{
  DERObjectIdentifier qcStatementId;
  ASN1Encodable qcStatementInfo;

  public QCStatement(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.qcStatementId = DERObjectIdentifier.getInstance(localEnumeration.nextElement());
    if (localEnumeration.hasMoreElements())
      this.qcStatementInfo = ((ASN1Encodable)localEnumeration.nextElement());
  }

  public QCStatement(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.qcStatementId = paramDERObjectIdentifier;
    this.qcStatementInfo = null;
  }

  public QCStatement(DERObjectIdentifier paramDERObjectIdentifier, ASN1Encodable paramASN1Encodable)
  {
    this.qcStatementId = paramDERObjectIdentifier;
    this.qcStatementInfo = paramASN1Encodable;
  }

  public static QCStatement getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof QCStatement)))
      return (QCStatement)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new QCStatement(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("unknown object in getInstance");
  }

  public DERObjectIdentifier getStatementId()
  {
    return this.qcStatementId;
  }

  public ASN1Encodable getStatementInfo()
  {
    return this.qcStatementInfo;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.qcStatementId);
    if (this.qcStatementInfo != null)
      localASN1EncodableVector.add(this.qcStatementInfo);
    return new DERSequence(localASN1EncodableVector);
  }
}