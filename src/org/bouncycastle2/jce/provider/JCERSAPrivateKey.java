package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Enumeration;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.crypto.params.RSAKeyParameters;
import org.bouncycastle2.jce.interfaces.PKCS12BagAttributeCarrier;

public class JCERSAPrivateKey
  implements RSAPrivateKey, PKCS12BagAttributeCarrier
{
  private static BigInteger ZERO = BigInteger.valueOf(0L);
  static final long serialVersionUID = 5110188922551353628L;
  private PKCS12BagAttributeCarrierImpl attrCarrier = new PKCS12BagAttributeCarrierImpl();
  protected BigInteger modulus;
  protected BigInteger privateExponent;

  protected JCERSAPrivateKey()
  {
  }

  JCERSAPrivateKey(RSAPrivateKey paramRSAPrivateKey)
  {
    this.modulus = paramRSAPrivateKey.getModulus();
    this.privateExponent = paramRSAPrivateKey.getPrivateExponent();
  }

  JCERSAPrivateKey(RSAPrivateKeySpec paramRSAPrivateKeySpec)
  {
    this.modulus = paramRSAPrivateKeySpec.getModulus();
    this.privateExponent = paramRSAPrivateKeySpec.getPrivateExponent();
  }

  JCERSAPrivateKey(RSAKeyParameters paramRSAKeyParameters)
  {
    this.modulus = paramRSAKeyParameters.getModulus();
    this.privateExponent = paramRSAKeyParameters.getExponent();
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.modulus = ((BigInteger)paramObjectInputStream.readObject());
    this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    this.attrCarrier.readObject(paramObjectInputStream);
    this.privateExponent = ((BigInteger)paramObjectInputStream.readObject());
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(this.modulus);
    this.attrCarrier.writeObject(paramObjectOutputStream);
    paramObjectOutputStream.writeObject(this.privateExponent);
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof PKCS12BagAttributeCarrier));
    RSAPrivateKey localRSAPrivateKey;
    do
    {
      return false;
      if (paramObject == this)
        return true;
      localRSAPrivateKey = (PKCS12BagAttributeCarrier)paramObject;
    }
    while ((!getModulus().equals(localRSAPrivateKey.getModulus())) || (!getPrivateExponent().equals(localRSAPrivateKey.getPrivateExponent())));
    return true;
  }

  public String getAlgorithm()
  {
    return "RSA";
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
    return new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, new DERNull()), new RSAPrivateKeyStructure(getModulus(), ZERO, getPrivateExponent(), ZERO, ZERO, ZERO, ZERO, ZERO).getDERObject()).getDEREncoded();
  }

  public String getFormat()
  {
    return "PKCS#8";
  }

  public BigInteger getModulus()
  {
    return this.modulus;
  }

  public BigInteger getPrivateExponent()
  {
    return this.privateExponent;
  }

  public int hashCode()
  {
    return getModulus().hashCode() ^ getPrivateExponent().hashCode();
  }

  public void setBagAttribute(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.attrCarrier.setBagAttribute(paramDERObjectIdentifier, paramDEREncodable);
  }
}