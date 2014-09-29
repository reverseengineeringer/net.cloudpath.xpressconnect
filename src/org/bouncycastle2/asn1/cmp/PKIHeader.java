package org.bouncycastle2.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.GeneralName;

public class PKIHeader extends ASN1Encodable
{
  public static final int CMP_1999 = 1;
  public static final int CMP_2000 = 2;
  public static final GeneralName NULL_NAME = new GeneralName(X500Name.getInstance(new DERSequence()));
  private PKIFreeText freeText;
  private ASN1Sequence generalInfo;
  private DERGeneralizedTime messageTime;
  private AlgorithmIdentifier protectionAlg;
  private DERInteger pvno;
  private ASN1OctetString recipKID;
  private ASN1OctetString recipNonce;
  private GeneralName recipient;
  private GeneralName sender;
  private ASN1OctetString senderKID;
  private ASN1OctetString senderNonce;
  private ASN1OctetString transactionID;

  public PKIHeader(int paramInt, GeneralName paramGeneralName1, GeneralName paramGeneralName2)
  {
    this(new DERInteger(paramInt), paramGeneralName1, paramGeneralName2);
  }

  private PKIHeader(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.pvno = DERInteger.getInstance(localEnumeration.nextElement());
    this.sender = GeneralName.getInstance(localEnumeration.nextElement());
    this.recipient = GeneralName.getInstance(localEnumeration.nextElement());
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)localEnumeration.nextElement();
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        throw new IllegalArgumentException("unknown tag number: " + localASN1TaggedObject.getTagNo());
      case 0:
        this.messageTime = DERGeneralizedTime.getInstance(localASN1TaggedObject, true);
        break;
      case 1:
        this.protectionAlg = AlgorithmIdentifier.getInstance(localASN1TaggedObject, true);
        break;
      case 2:
        this.senderKID = ASN1OctetString.getInstance(localASN1TaggedObject, true);
        break;
      case 3:
        this.recipKID = ASN1OctetString.getInstance(localASN1TaggedObject, true);
        break;
      case 4:
        this.transactionID = ASN1OctetString.getInstance(localASN1TaggedObject, true);
        break;
      case 5:
        this.senderNonce = ASN1OctetString.getInstance(localASN1TaggedObject, true);
        break;
      case 6:
        this.recipNonce = ASN1OctetString.getInstance(localASN1TaggedObject, true);
        break;
      case 7:
        this.freeText = PKIFreeText.getInstance(localASN1TaggedObject, true);
        break;
      case 8:
        this.generalInfo = ASN1Sequence.getInstance(localASN1TaggedObject, true);
      }
    }
  }

  private PKIHeader(DERInteger paramDERInteger, GeneralName paramGeneralName1, GeneralName paramGeneralName2)
  {
    this.pvno = paramDERInteger;
    this.sender = paramGeneralName1;
    this.recipient = paramGeneralName2;
  }

  private void addOptional(ASN1EncodableVector paramASN1EncodableVector, int paramInt, ASN1Encodable paramASN1Encodable)
  {
    if (paramASN1Encodable != null)
      paramASN1EncodableVector.add(new DERTaggedObject(true, paramInt, paramASN1Encodable));
  }

  public static PKIHeader getInstance(Object paramObject)
  {
    if ((paramObject instanceof PKIHeader))
      return (PKIHeader)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PKIHeader((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public PKIFreeText getFreeText()
  {
    return this.freeText;
  }

  public InfoTypeAndValue[] getGeneralInfo()
  {
    InfoTypeAndValue[] arrayOfInfoTypeAndValue;
    if (this.generalInfo == null)
      arrayOfInfoTypeAndValue = null;
    while (true)
    {
      return arrayOfInfoTypeAndValue;
      arrayOfInfoTypeAndValue = new InfoTypeAndValue[this.generalInfo.size()];
      for (int i = 0; i < arrayOfInfoTypeAndValue.length; i++)
        arrayOfInfoTypeAndValue[i] = InfoTypeAndValue.getInstance(this.generalInfo.getObjectAt(i));
    }
  }

  public DERGeneralizedTime getMessageTime()
  {
    return this.messageTime;
  }

  public AlgorithmIdentifier getProtectionAlg()
  {
    return this.protectionAlg;
  }

  public DERInteger getPvno()
  {
    return this.pvno;
  }

  public ASN1OctetString getRecipKID()
  {
    return this.recipKID;
  }

  public ASN1OctetString getRecipNonce()
  {
    return this.recipNonce;
  }

  public GeneralName getRecipient()
  {
    return this.recipient;
  }

  public GeneralName getSender()
  {
    return this.sender;
  }

  public ASN1OctetString getSenderKID()
  {
    return this.senderKID;
  }

  public ASN1OctetString getSenderNonce()
  {
    return this.senderNonce;
  }

  public ASN1OctetString getTransactionID()
  {
    return this.transactionID;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.pvno);
    localASN1EncodableVector.add(this.sender);
    localASN1EncodableVector.add(this.recipient);
    addOptional(localASN1EncodableVector, 0, this.messageTime);
    addOptional(localASN1EncodableVector, 1, this.protectionAlg);
    addOptional(localASN1EncodableVector, 2, this.senderKID);
    addOptional(localASN1EncodableVector, 3, this.recipKID);
    addOptional(localASN1EncodableVector, 4, this.transactionID);
    addOptional(localASN1EncodableVector, 5, this.senderNonce);
    addOptional(localASN1EncodableVector, 6, this.recipNonce);
    addOptional(localASN1EncodableVector, 7, this.freeText);
    addOptional(localASN1EncodableVector, 8, this.generalInfo);
    return new DERSequence(localASN1EncodableVector);
  }
}