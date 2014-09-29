package org.bouncycastle2.asn1.x509.qualified;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.GeneralName;

public class SemanticsInformation extends ASN1Encodable
{
  GeneralName[] nameRegistrationAuthorities;
  DERObjectIdentifier semanticsIdentifier;

  public SemanticsInformation(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    if (paramASN1Sequence.size() < 1)
      throw new IllegalArgumentException("no objects in SemanticsInformation");
    Object localObject = localEnumeration.nextElement();
    ASN1Sequence localASN1Sequence;
    if ((localObject instanceof DERObjectIdentifier))
    {
      this.semanticsIdentifier = DERObjectIdentifier.getInstance(localObject);
      if (localEnumeration.hasMoreElements())
        localObject = localEnumeration.nextElement();
    }
    else if (localObject != null)
    {
      localASN1Sequence = ASN1Sequence.getInstance(localObject);
      this.nameRegistrationAuthorities = new GeneralName[localASN1Sequence.size()];
    }
    for (int i = 0; ; i++)
    {
      if (i >= localASN1Sequence.size())
      {
        return;
        localObject = null;
        break;
      }
      this.nameRegistrationAuthorities[i] = GeneralName.getInstance(localASN1Sequence.getObjectAt(i));
    }
  }

  public SemanticsInformation(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.semanticsIdentifier = paramDERObjectIdentifier;
    this.nameRegistrationAuthorities = null;
  }

  public SemanticsInformation(DERObjectIdentifier paramDERObjectIdentifier, GeneralName[] paramArrayOfGeneralName)
  {
    this.semanticsIdentifier = paramDERObjectIdentifier;
    this.nameRegistrationAuthorities = paramArrayOfGeneralName;
  }

  public SemanticsInformation(GeneralName[] paramArrayOfGeneralName)
  {
    this.semanticsIdentifier = null;
    this.nameRegistrationAuthorities = paramArrayOfGeneralName;
  }

  public static SemanticsInformation getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SemanticsInformation)))
      return (SemanticsInformation)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SemanticsInformation(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("unknown object in getInstance");
  }

  public GeneralName[] getNameRegistrationAuthorities()
  {
    return this.nameRegistrationAuthorities;
  }

  public DERObjectIdentifier getSemanticsIdentifier()
  {
    return this.semanticsIdentifier;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    if (this.semanticsIdentifier != null)
      localASN1EncodableVector1.add(this.semanticsIdentifier);
    ASN1EncodableVector localASN1EncodableVector2;
    if (this.nameRegistrationAuthorities != null)
      localASN1EncodableVector2 = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= this.nameRegistrationAuthorities.length)
      {
        localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
        return new DERSequence(localASN1EncodableVector1);
      }
      localASN1EncodableVector2.add(this.nameRegistrationAuthorities[i]);
    }
  }
}