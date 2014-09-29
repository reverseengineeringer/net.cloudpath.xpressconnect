package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Enumeration;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPrivateKeySpec;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.DHParameter;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x9.DHDomainParameters;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle2.jce.interfaces.PKCS12BagAttributeCarrier;

public class JCEDHPrivateKey
  implements DHPrivateKey, PKCS12BagAttributeCarrier
{
  static final long serialVersionUID = 311058815616901812L;
  private PKCS12BagAttributeCarrier attrCarrier = new PKCS12BagAttributeCarrierImpl();
  private DHParameterSpec dhSpec;
  private PrivateKeyInfo info;
  BigInteger x;

  protected JCEDHPrivateKey()
  {
  }

  JCEDHPrivateKey(DHPrivateKey paramDHPrivateKey)
  {
    this.x = paramDHPrivateKey.getX();
    this.dhSpec = paramDHPrivateKey.getParams();
  }

  JCEDHPrivateKey(DHPrivateKeySpec paramDHPrivateKeySpec)
  {
    this.x = paramDHPrivateKeySpec.getX();
    this.dhSpec = new DHParameterSpec(paramDHPrivateKeySpec.getP(), paramDHPrivateKeySpec.getG());
  }

  JCEDHPrivateKey(PrivateKeyInfo paramPrivateKeyInfo)
  {
    ASN1Sequence localASN1Sequence = ASN1Sequence.getInstance(paramPrivateKeyInfo.getAlgorithmId().getParameters());
    DERInteger localDERInteger = (DERInteger)paramPrivateKeyInfo.getPrivateKey();
    DERObjectIdentifier localDERObjectIdentifier = paramPrivateKeyInfo.getAlgorithmId().getObjectId();
    this.info = paramPrivateKeyInfo;
    this.x = localDERInteger.getValue();
    if (localDERObjectIdentifier.equals(PKCSObjectIdentifiers.dhKeyAgreement))
    {
      DHParameter localDHParameter = new DHParameter(localASN1Sequence);
      if (localDHParameter.getL() != null)
      {
        this.dhSpec = new DHParameterSpec(localDHParameter.getP(), localDHParameter.getG(), localDHParameter.getL().intValue());
        return;
      }
      this.dhSpec = new DHParameterSpec(localDHParameter.getP(), localDHParameter.getG());
      return;
    }
    if (localDERObjectIdentifier.equals(X9ObjectIdentifiers.dhpublicnumber))
    {
      DHDomainParameters localDHDomainParameters = DHDomainParameters.getInstance(localASN1Sequence);
      this.dhSpec = new DHParameterSpec(localDHDomainParameters.getP().getValue(), localDHDomainParameters.getG().getValue());
      return;
    }
    throw new IllegalArgumentException("unknown algorithm type: " + localDERObjectIdentifier);
  }

  JCEDHPrivateKey(DHPrivateKeyParameters paramDHPrivateKeyParameters)
  {
    this.x = paramDHPrivateKeyParameters.getX();
    this.dhSpec = new DHParameterSpec(paramDHPrivateKeyParameters.getParameters().getP(), paramDHPrivateKeyParameters.getParameters().getG(), paramDHPrivateKeyParameters.getParameters().getL());
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.x = ((BigInteger)paramObjectInputStream.readObject());
    this.dhSpec = new DHParameterSpec((BigInteger)paramObjectInputStream.readObject(), (BigInteger)paramObjectInputStream.readObject(), paramObjectInputStream.readInt());
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(getX());
    paramObjectOutputStream.writeObject(this.dhSpec.getP());
    paramObjectOutputStream.writeObject(this.dhSpec.getG());
    paramObjectOutputStream.writeInt(this.dhSpec.getL());
  }

  public String getAlgorithm()
  {
    return "DH";
  }

  public DEREncodable getBagAttribute(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return this.attrCarrier.getBagAttribute(paramDERObjectIdentifier);
  }

  public Enumeration getBagAttributeKeys()
  {
    return this.attrCarrier.getBagAttributeKeys();
  }

  public byte[] getEncoded()
  {
    if (this.info != null)
      return this.info.getDEREncoded();
    return new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement, new DHParameter(this.dhSpec.getP(), this.dhSpec.getG(), this.dhSpec.getL()).getDERObject()), new DERInteger(getX())).getDEREncoded();
  }

  public String getFormat()
  {
    return "PKCS#8";
  }

  public DHParameterSpec getParams()
  {
    return this.dhSpec;
  }

  public BigInteger getX()
  {
    return this.x;
  }

  public void setBagAttribute(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.attrCarrier.setBagAttribute(paramDERObjectIdentifier, paramDEREncodable);
  }
}