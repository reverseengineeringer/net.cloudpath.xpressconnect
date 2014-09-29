package org.bouncycastle2.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class ErrorMsgContent extends ASN1Encodable
{
  private DERInteger errorCode;
  private PKIFreeText errorDetails;
  private PKIStatusInfo pkiStatusInfo;

  private ErrorMsgContent(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.pkiStatusInfo = PKIStatusInfo.getInstance(localEnumeration.nextElement());
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      Object localObject = localEnumeration.nextElement();
      if ((localObject instanceof DERInteger))
        this.errorCode = DERInteger.getInstance(localObject);
      else
        this.errorDetails = PKIFreeText.getInstance(localObject);
    }
  }

  public ErrorMsgContent(PKIStatusInfo paramPKIStatusInfo)
  {
    this(paramPKIStatusInfo, null, null);
  }

  public ErrorMsgContent(PKIStatusInfo paramPKIStatusInfo, DERInteger paramDERInteger, PKIFreeText paramPKIFreeText)
  {
    if (paramPKIStatusInfo == null)
      throw new IllegalArgumentException("'pkiStatusInfo' cannot be null");
    this.pkiStatusInfo = paramPKIStatusInfo;
    this.errorCode = paramDERInteger;
    this.errorDetails = paramPKIFreeText;
  }

  private void addOptional(ASN1EncodableVector paramASN1EncodableVector, ASN1Encodable paramASN1Encodable)
  {
    if (paramASN1Encodable != null)
      paramASN1EncodableVector.add(paramASN1Encodable);
  }

  public static ErrorMsgContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof ErrorMsgContent))
      return (ErrorMsgContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ErrorMsgContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERInteger getErrorCode()
  {
    return this.errorCode;
  }

  public PKIFreeText getErrorDetails()
  {
    return this.errorDetails;
  }

  public PKIStatusInfo getPKIStatusInfo()
  {
    return this.pkiStatusInfo;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.pkiStatusInfo);
    addOptional(localASN1EncodableVector, this.errorCode);
    addOptional(localASN1EncodableVector, this.errorDetails);
    return new DERSequence(localASN1EncodableVector);
  }
}