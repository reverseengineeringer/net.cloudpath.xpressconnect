package org.bouncycastle2.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class KeyRecRepContent extends ASN1Encodable
{
  private ASN1Sequence caCerts;
  private ASN1Sequence keyPairHist;
  private CMPCertificate newSigCert;
  private PKIStatusInfo status;

  private KeyRecRepContent(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.status = PKIStatusInfo.getInstance(localEnumeration.nextElement());
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = ASN1TaggedObject.getInstance(localEnumeration.nextElement());
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        throw new IllegalArgumentException("unknown tag number: " + localASN1TaggedObject.getTagNo());
      case 0:
        this.newSigCert = CMPCertificate.getInstance(localASN1TaggedObject.getObject());
        break;
      case 1:
        this.caCerts = ASN1Sequence.getInstance(localASN1TaggedObject.getObject());
        break;
      case 2:
        this.keyPairHist = ASN1Sequence.getInstance(localASN1TaggedObject.getObject());
      }
    }
  }

  private void addOptional(ASN1EncodableVector paramASN1EncodableVector, int paramInt, ASN1Encodable paramASN1Encodable)
  {
    if (paramASN1Encodable != null)
      paramASN1EncodableVector.add(new DERTaggedObject(true, paramInt, paramASN1Encodable));
  }

  public static KeyRecRepContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof KeyRecRepContent))
      return (KeyRecRepContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new KeyRecRepContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public CMPCertificate[] getCaCerts()
  {
    CMPCertificate[] arrayOfCMPCertificate;
    if (this.caCerts == null)
      arrayOfCMPCertificate = null;
    while (true)
    {
      return arrayOfCMPCertificate;
      arrayOfCMPCertificate = new CMPCertificate[this.caCerts.size()];
      for (int i = 0; i != arrayOfCMPCertificate.length; i++)
        arrayOfCMPCertificate[i] = CMPCertificate.getInstance(this.caCerts.getObjectAt(i));
    }
  }

  public CertifiedKeyPair[] getKeyPairHist()
  {
    CertifiedKeyPair[] arrayOfCertifiedKeyPair;
    if (this.keyPairHist == null)
      arrayOfCertifiedKeyPair = null;
    while (true)
    {
      return arrayOfCertifiedKeyPair;
      arrayOfCertifiedKeyPair = new CertifiedKeyPair[this.keyPairHist.size()];
      for (int i = 0; i != arrayOfCertifiedKeyPair.length; i++)
        arrayOfCertifiedKeyPair[i] = CertifiedKeyPair.getInstance(this.keyPairHist.getObjectAt(i));
    }
  }

  public CMPCertificate getNewSigCert()
  {
    return this.newSigCert;
  }

  public PKIStatusInfo getStatus()
  {
    return this.status;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.status);
    addOptional(localASN1EncodableVector, 0, this.newSigCert);
    addOptional(localASN1EncodableVector, 1, this.caCerts);
    addOptional(localASN1EncodableVector, 2, this.keyPairHist);
    return new DERSequence(localASN1EncodableVector);
  }
}