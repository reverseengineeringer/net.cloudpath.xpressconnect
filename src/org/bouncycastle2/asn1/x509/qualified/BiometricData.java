package org.bouncycastle2.asn1.x509.qualified;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class BiometricData extends ASN1Encodable
{
  ASN1OctetString biometricDataHash;
  AlgorithmIdentifier hashAlgorithm;
  DERIA5String sourceDataUri;
  TypeOfBiometricData typeOfBiometricData;

  public BiometricData(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.typeOfBiometricData = TypeOfBiometricData.getInstance(localEnumeration.nextElement());
    this.hashAlgorithm = AlgorithmIdentifier.getInstance(localEnumeration.nextElement());
    this.biometricDataHash = ASN1OctetString.getInstance(localEnumeration.nextElement());
    if (localEnumeration.hasMoreElements())
      this.sourceDataUri = DERIA5String.getInstance(localEnumeration.nextElement());
  }

  public BiometricData(TypeOfBiometricData paramTypeOfBiometricData, AlgorithmIdentifier paramAlgorithmIdentifier, ASN1OctetString paramASN1OctetString)
  {
    this.typeOfBiometricData = paramTypeOfBiometricData;
    this.hashAlgorithm = paramAlgorithmIdentifier;
    this.biometricDataHash = paramASN1OctetString;
    this.sourceDataUri = null;
  }

  public BiometricData(TypeOfBiometricData paramTypeOfBiometricData, AlgorithmIdentifier paramAlgorithmIdentifier, ASN1OctetString paramASN1OctetString, DERIA5String paramDERIA5String)
  {
    this.typeOfBiometricData = paramTypeOfBiometricData;
    this.hashAlgorithm = paramAlgorithmIdentifier;
    this.biometricDataHash = paramASN1OctetString;
    this.sourceDataUri = paramDERIA5String;
  }

  public static BiometricData getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof BiometricData)))
      return (BiometricData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new BiometricData(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("unknown object in getInstance");
  }

  public ASN1OctetString getBiometricDataHash()
  {
    return this.biometricDataHash;
  }

  public AlgorithmIdentifier getHashAlgorithm()
  {
    return this.hashAlgorithm;
  }

  public DERIA5String getSourceDataUri()
  {
    return this.sourceDataUri;
  }

  public TypeOfBiometricData getTypeOfBiometricData()
  {
    return this.typeOfBiometricData;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.typeOfBiometricData);
    localASN1EncodableVector.add(this.hashAlgorithm);
    localASN1EncodableVector.add(this.biometricDataHash);
    if (this.sourceDataUri != null)
      localASN1EncodableVector.add(this.sourceDataUri);
    return new DERSequence(localASN1EncodableVector);
  }
}