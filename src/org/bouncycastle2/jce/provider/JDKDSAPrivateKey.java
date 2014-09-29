package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.spec.DSAParameterSpec;
import java.security.spec.DSAPrivateKeySpec;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.DSAParameter;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.crypto.params.DSAParameters;
import org.bouncycastle2.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle2.jce.interfaces.PKCS12BagAttributeCarrier;

public class JDKDSAPrivateKey
  implements DSAPrivateKey, PKCS12BagAttributeCarrier
{
  private static final long serialVersionUID = -4677259546958385734L;
  private PKCS12BagAttributeCarrierImpl attrCarrier = new PKCS12BagAttributeCarrierImpl();
  DSAParams dsaSpec;
  BigInteger x;

  protected JDKDSAPrivateKey()
  {
  }

  JDKDSAPrivateKey(DSAPrivateKey paramDSAPrivateKey)
  {
    this.x = paramDSAPrivateKey.getX();
    this.dsaSpec = paramDSAPrivateKey.getParams();
  }

  JDKDSAPrivateKey(DSAPrivateKeySpec paramDSAPrivateKeySpec)
  {
    this.x = paramDSAPrivateKeySpec.getX();
    this.dsaSpec = new DSAParameterSpec(paramDSAPrivateKeySpec.getP(), paramDSAPrivateKeySpec.getQ(), paramDSAPrivateKeySpec.getG());
  }

  JDKDSAPrivateKey(PrivateKeyInfo paramPrivateKeyInfo)
  {
    DSAParameter localDSAParameter = new DSAParameter((ASN1Sequence)paramPrivateKeyInfo.getAlgorithmId().getParameters());
    this.x = ((DERInteger)paramPrivateKeyInfo.getPrivateKey()).getValue();
    this.dsaSpec = new DSAParameterSpec(localDSAParameter.getP(), localDSAParameter.getQ(), localDSAParameter.getG());
  }

  JDKDSAPrivateKey(DSAPrivateKeyParameters paramDSAPrivateKeyParameters)
  {
    this.x = paramDSAPrivateKeyParameters.getX();
    this.dsaSpec = new DSAParameterSpec(paramDSAPrivateKeyParameters.getParameters().getP(), paramDSAPrivateKeyParameters.getParameters().getQ(), paramDSAPrivateKeyParameters.getParameters().getG());
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.x = ((BigInteger)paramObjectInputStream.readObject());
    this.dsaSpec = new DSAParameterSpec((BigInteger)paramObjectInputStream.readObject(), (BigInteger)paramObjectInputStream.readObject(), (BigInteger)paramObjectInputStream.readObject());
    this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    this.attrCarrier.readObject(paramObjectInputStream);
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(this.x);
    paramObjectOutputStream.writeObject(this.dsaSpec.getP());
    paramObjectOutputStream.writeObject(this.dsaSpec.getQ());
    paramObjectOutputStream.writeObject(this.dsaSpec.getG());
    this.attrCarrier.writeObject(paramObjectOutputStream);
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof PKCS12BagAttributeCarrier));
    DSAPrivateKey localDSAPrivateKey;
    do
    {
      return false;
      localDSAPrivateKey = (PKCS12BagAttributeCarrier)paramObject;
    }
    while ((!getX().equals(localDSAPrivateKey.getX())) || (!getParams().getG().equals(localDSAPrivateKey.getParams().getG())) || (!getParams().getP().equals(localDSAPrivateKey.getParams().getP())) || (!getParams().getQ().equals(localDSAPrivateKey.getParams().getQ())));
    return true;
  }

  public String getAlgorithm()
  {
    return "DSA";
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
    return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(this.dsaSpec.getP(), this.dsaSpec.getQ(), this.dsaSpec.getG()).getDERObject()), new DERInteger(getX())).getDEREncoded();
  }

  public String getFormat()
  {
    return "PKCS#8";
  }

  public DSAParams getParams()
  {
    return this.dsaSpec;
  }

  public BigInteger getX()
  {
    return this.x;
  }

  public int hashCode()
  {
    return getX().hashCode() ^ getParams().getG().hashCode() ^ getParams().getP().hashCode() ^ getParams().getQ().hashCode();
  }

  public void setBagAttribute(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.attrCarrier.setBagAttribute(paramDERObjectIdentifier, paramDEREncodable);
  }
}