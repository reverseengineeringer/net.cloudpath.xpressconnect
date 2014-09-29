package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.DHParameter;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x9.DHDomainParameters;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPublicKeyParameters;

public class JCEDHPublicKey
  implements DHPublicKey
{
  static final long serialVersionUID = -216691575254424324L;
  private DHParameterSpec dhSpec;
  private SubjectPublicKeyInfo info;
  private BigInteger y;

  JCEDHPublicKey(BigInteger paramBigInteger, DHParameterSpec paramDHParameterSpec)
  {
    this.y = paramBigInteger;
    this.dhSpec = paramDHParameterSpec;
  }

  JCEDHPublicKey(DHPublicKey paramDHPublicKey)
  {
    this.y = paramDHPublicKey.getY();
    this.dhSpec = paramDHPublicKey.getParams();
  }

  JCEDHPublicKey(DHPublicKeySpec paramDHPublicKeySpec)
  {
    this.y = paramDHPublicKeySpec.getY();
    this.dhSpec = new DHParameterSpec(paramDHPublicKeySpec.getP(), paramDHPublicKeySpec.getG());
  }

  JCEDHPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    this.info = paramSubjectPublicKeyInfo;
    ASN1Sequence localASN1Sequence;
    DERObjectIdentifier localDERObjectIdentifier;
    DHParameter localDHParameter;
    try
    {
      DERInteger localDERInteger = (DERInteger)paramSubjectPublicKeyInfo.getPublicKey();
      this.y = localDERInteger.getValue();
      localASN1Sequence = ASN1Sequence.getInstance(paramSubjectPublicKeyInfo.getAlgorithmId().getParameters());
      localDERObjectIdentifier = paramSubjectPublicKeyInfo.getAlgorithmId().getObjectId();
      if ((!localDERObjectIdentifier.equals(PKCSObjectIdentifiers.dhKeyAgreement)) && (!isPKCSParam(localASN1Sequence)))
        break label148;
      localDHParameter = new DHParameter(localASN1Sequence);
      if (localDHParameter.getL() != null)
      {
        this.dhSpec = new DHParameterSpec(localDHParameter.getP(), localDHParameter.getG(), localDHParameter.getL().intValue());
        return;
      }
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException("invalid info structure in DH public key");
    }
    this.dhSpec = new DHParameterSpec(localDHParameter.getP(), localDHParameter.getG());
    return;
    label148: if (localDERObjectIdentifier.equals(X9ObjectIdentifiers.dhpublicnumber))
    {
      DHDomainParameters localDHDomainParameters = DHDomainParameters.getInstance(localASN1Sequence);
      this.dhSpec = new DHParameterSpec(localDHDomainParameters.getP().getValue(), localDHDomainParameters.getG().getValue());
      return;
    }
    throw new IllegalArgumentException("unknown algorithm type: " + localDERObjectIdentifier);
  }

  JCEDHPublicKey(DHPublicKeyParameters paramDHPublicKeyParameters)
  {
    this.y = paramDHPublicKeyParameters.getY();
    this.dhSpec = new DHParameterSpec(paramDHPublicKeyParameters.getParameters().getP(), paramDHPublicKeyParameters.getParameters().getG(), paramDHPublicKeyParameters.getParameters().getL());
  }

  private boolean isPKCSParam(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() == 2);
    DERInteger localDERInteger1;
    DERInteger localDERInteger2;
    do
    {
      return true;
      if (paramASN1Sequence.size() > 3)
        return false;
      localDERInteger1 = DERInteger.getInstance(paramASN1Sequence.getObjectAt(2));
      localDERInteger2 = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    }
    while (localDERInteger1.getValue().compareTo(BigInteger.valueOf(localDERInteger2.getValue().bitLength())) <= 0);
    return false;
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.y = ((BigInteger)paramObjectInputStream.readObject());
    this.dhSpec = new DHParameterSpec((BigInteger)paramObjectInputStream.readObject(), (BigInteger)paramObjectInputStream.readObject(), paramObjectInputStream.readInt());
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(getY());
    paramObjectOutputStream.writeObject(this.dhSpec.getP());
    paramObjectOutputStream.writeObject(this.dhSpec.getG());
    paramObjectOutputStream.writeInt(this.dhSpec.getL());
  }

  public String getAlgorithm()
  {
    return "DH";
  }

  public byte[] getEncoded()
  {
    if (this.info != null)
      return this.info.getDEREncoded();
    return new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement, new DHParameter(this.dhSpec.getP(), this.dhSpec.getG(), this.dhSpec.getL()).getDERObject()), new DERInteger(this.y)).getDEREncoded();
  }

  public String getFormat()
  {
    return "X.509";
  }

  public DHParameterSpec getParams()
  {
    return this.dhSpec;
  }

  public BigInteger getY()
  {
    return this.y;
  }
}