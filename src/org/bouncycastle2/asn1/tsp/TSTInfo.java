package org.bouncycastle2.asn1.tsp;

import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBoolean;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class TSTInfo extends ASN1Encodable
{
  Accuracy accuracy;
  X509Extensions extensions;
  DERGeneralizedTime genTime;
  MessageImprint messageImprint;
  DERInteger nonce;
  DERBoolean ordering;
  DERInteger serialNumber;
  GeneralName tsa;
  DERObjectIdentifier tsaPolicyId;
  DERInteger version;

  public TSTInfo(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.version = DERInteger.getInstance(localEnumeration.nextElement());
    this.tsaPolicyId = DERObjectIdentifier.getInstance(localEnumeration.nextElement());
    this.messageImprint = MessageImprint.getInstance(localEnumeration.nextElement());
    this.serialNumber = DERInteger.getInstance(localEnumeration.nextElement());
    this.genTime = DERGeneralizedTime.getInstance(localEnumeration.nextElement());
    this.ordering = new DERBoolean(false);
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      DERObject localDERObject = (DERObject)localEnumeration.nextElement();
      if ((localDERObject instanceof ASN1TaggedObject))
      {
        DERTaggedObject localDERTaggedObject = (DERTaggedObject)localDERObject;
        switch (localDERTaggedObject.getTagNo())
        {
        default:
          throw new IllegalArgumentException("Unknown tag value " + localDERTaggedObject.getTagNo());
        case 0:
          this.tsa = GeneralName.getInstance(localDERTaggedObject, true);
          break;
        case 1:
          this.extensions = X509Extensions.getInstance(localDERTaggedObject, false);
          break;
        }
      }
      else if ((localDERObject instanceof DERSequence))
      {
        this.accuracy = Accuracy.getInstance(localDERObject);
      }
      else if ((localDERObject instanceof DERBoolean))
      {
        this.ordering = DERBoolean.getInstance(localDERObject);
      }
      else if ((localDERObject instanceof DERInteger))
      {
        this.nonce = DERInteger.getInstance(localDERObject);
      }
    }
  }

  public TSTInfo(DERObjectIdentifier paramDERObjectIdentifier, MessageImprint paramMessageImprint, DERInteger paramDERInteger1, DERGeneralizedTime paramDERGeneralizedTime, Accuracy paramAccuracy, DERBoolean paramDERBoolean, DERInteger paramDERInteger2, GeneralName paramGeneralName, X509Extensions paramX509Extensions)
  {
    this.version = new DERInteger(1);
    this.tsaPolicyId = paramDERObjectIdentifier;
    this.messageImprint = paramMessageImprint;
    this.serialNumber = paramDERInteger1;
    this.genTime = paramDERGeneralizedTime;
    this.accuracy = paramAccuracy;
    this.ordering = paramDERBoolean;
    this.nonce = paramDERInteger2;
    this.tsa = paramGeneralName;
    this.extensions = paramX509Extensions;
  }

  public static TSTInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof TSTInfo)))
      return (TSTInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new TSTInfo((ASN1Sequence)paramObject);
    if ((paramObject instanceof ASN1OctetString))
      try
      {
        TSTInfo localTSTInfo = getInstance(new ASN1InputStream(((ASN1OctetString)paramObject).getOctets()).readObject());
        return localTSTInfo;
      }
      catch (IOException localIOException)
      {
        throw new IllegalArgumentException("Bad object format in 'TSTInfo' factory.");
      }
    throw new IllegalArgumentException("Unknown object in 'TSTInfo' factory : " + paramObject.getClass().getName() + ".");
  }

  public Accuracy getAccuracy()
  {
    return this.accuracy;
  }

  public X509Extensions getExtensions()
  {
    return this.extensions;
  }

  public DERGeneralizedTime getGenTime()
  {
    return this.genTime;
  }

  public MessageImprint getMessageImprint()
  {
    return this.messageImprint;
  }

  public DERInteger getNonce()
  {
    return this.nonce;
  }

  public DERBoolean getOrdering()
  {
    return this.ordering;
  }

  public DERObjectIdentifier getPolicy()
  {
    return this.tsaPolicyId;
  }

  public DERInteger getSerialNumber()
  {
    return this.serialNumber;
  }

  public GeneralName getTsa()
  {
    return this.tsa;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.tsaPolicyId);
    localASN1EncodableVector.add(this.messageImprint);
    localASN1EncodableVector.add(this.serialNumber);
    localASN1EncodableVector.add(this.genTime);
    if (this.accuracy != null)
      localASN1EncodableVector.add(this.accuracy);
    if ((this.ordering != null) && (this.ordering.isTrue()))
      localASN1EncodableVector.add(this.ordering);
    if (this.nonce != null)
      localASN1EncodableVector.add(this.nonce);
    if (this.tsa != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.tsa));
    if (this.extensions != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.extensions));
    return new DERSequence(localASN1EncodableVector);
  }
}