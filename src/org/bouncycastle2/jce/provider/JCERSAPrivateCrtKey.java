package org.bouncycastle2.jce.provider;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.crypto.params.RSAPrivateCrtKeyParameters;

public class JCERSAPrivateCrtKey extends JCERSAPrivateKey
  implements RSAPrivateCrtKey
{
  static final long serialVersionUID = 7834723820638524718L;
  private BigInteger crtCoefficient;
  private BigInteger primeExponentP;
  private BigInteger primeExponentQ;
  private BigInteger primeP;
  private BigInteger primeQ;
  private BigInteger publicExponent;

  JCERSAPrivateCrtKey(RSAPrivateCrtKey paramRSAPrivateCrtKey)
  {
    this.modulus = paramRSAPrivateCrtKey.getModulus();
    this.publicExponent = paramRSAPrivateCrtKey.getPublicExponent();
    this.privateExponent = paramRSAPrivateCrtKey.getPrivateExponent();
    this.primeP = paramRSAPrivateCrtKey.getPrimeP();
    this.primeQ = paramRSAPrivateCrtKey.getPrimeQ();
    this.primeExponentP = paramRSAPrivateCrtKey.getPrimeExponentP();
    this.primeExponentQ = paramRSAPrivateCrtKey.getPrimeExponentQ();
    this.crtCoefficient = paramRSAPrivateCrtKey.getCrtCoefficient();
  }

  JCERSAPrivateCrtKey(RSAPrivateCrtKeySpec paramRSAPrivateCrtKeySpec)
  {
    this.modulus = paramRSAPrivateCrtKeySpec.getModulus();
    this.publicExponent = paramRSAPrivateCrtKeySpec.getPublicExponent();
    this.privateExponent = paramRSAPrivateCrtKeySpec.getPrivateExponent();
    this.primeP = paramRSAPrivateCrtKeySpec.getPrimeP();
    this.primeQ = paramRSAPrivateCrtKeySpec.getPrimeQ();
    this.primeExponentP = paramRSAPrivateCrtKeySpec.getPrimeExponentP();
    this.primeExponentQ = paramRSAPrivateCrtKeySpec.getPrimeExponentQ();
    this.crtCoefficient = paramRSAPrivateCrtKeySpec.getCrtCoefficient();
  }

  JCERSAPrivateCrtKey(PrivateKeyInfo paramPrivateKeyInfo)
  {
    this(new RSAPrivateKeyStructure((ASN1Sequence)paramPrivateKeyInfo.getPrivateKey()));
  }

  JCERSAPrivateCrtKey(RSAPrivateKeyStructure paramRSAPrivateKeyStructure)
  {
    this.modulus = paramRSAPrivateKeyStructure.getModulus();
    this.publicExponent = paramRSAPrivateKeyStructure.getPublicExponent();
    this.privateExponent = paramRSAPrivateKeyStructure.getPrivateExponent();
    this.primeP = paramRSAPrivateKeyStructure.getPrime1();
    this.primeQ = paramRSAPrivateKeyStructure.getPrime2();
    this.primeExponentP = paramRSAPrivateKeyStructure.getExponent1();
    this.primeExponentQ = paramRSAPrivateKeyStructure.getExponent2();
    this.crtCoefficient = paramRSAPrivateKeyStructure.getCoefficient();
  }

  JCERSAPrivateCrtKey(RSAPrivateCrtKeyParameters paramRSAPrivateCrtKeyParameters)
  {
    super(paramRSAPrivateCrtKeyParameters);
    this.publicExponent = paramRSAPrivateCrtKeyParameters.getPublicExponent();
    this.primeP = paramRSAPrivateCrtKeyParameters.getP();
    this.primeQ = paramRSAPrivateCrtKeyParameters.getQ();
    this.primeExponentP = paramRSAPrivateCrtKeyParameters.getDP();
    this.primeExponentQ = paramRSAPrivateCrtKeyParameters.getDQ();
    this.crtCoefficient = paramRSAPrivateCrtKeyParameters.getQInv();
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    RSAPrivateCrtKey localRSAPrivateCrtKey;
    do
    {
      return true;
      if (!(paramObject instanceof RSAPrivateCrtKey))
        return false;
      localRSAPrivateCrtKey = (RSAPrivateCrtKey)paramObject;
    }
    while ((getModulus().equals(localRSAPrivateCrtKey.getModulus())) && (getPublicExponent().equals(localRSAPrivateCrtKey.getPublicExponent())) && (getPrivateExponent().equals(localRSAPrivateCrtKey.getPrivateExponent())) && (getPrimeP().equals(localRSAPrivateCrtKey.getPrimeP())) && (getPrimeQ().equals(localRSAPrivateCrtKey.getPrimeQ())) && (getPrimeExponentP().equals(localRSAPrivateCrtKey.getPrimeExponentP())) && (getPrimeExponentQ().equals(localRSAPrivateCrtKey.getPrimeExponentQ())) && (getCrtCoefficient().equals(localRSAPrivateCrtKey.getCrtCoefficient())));
    return false;
  }

  public BigInteger getCrtCoefficient()
  {
    return this.crtCoefficient;
  }

  public byte[] getEncoded()
  {
    return new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, new DERNull()), new RSAPrivateKeyStructure(getModulus(), getPublicExponent(), getPrivateExponent(), getPrimeP(), getPrimeQ(), getPrimeExponentP(), getPrimeExponentQ(), getCrtCoefficient()).getDERObject()).getDEREncoded();
  }

  public String getFormat()
  {
    return "PKCS#8";
  }

  public BigInteger getPrimeExponentP()
  {
    return this.primeExponentP;
  }

  public BigInteger getPrimeExponentQ()
  {
    return this.primeExponentQ;
  }

  public BigInteger getPrimeP()
  {
    return this.primeP;
  }

  public BigInteger getPrimeQ()
  {
    return this.primeQ;
  }

  public BigInteger getPublicExponent()
  {
    return this.publicExponent;
  }

  public int hashCode()
  {
    return getModulus().hashCode() ^ getPublicExponent().hashCode() ^ getPrivateExponent().hashCode();
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("RSA Private CRT Key").append(str);
    localStringBuffer.append("            modulus: ").append(getModulus().toString(16)).append(str);
    localStringBuffer.append("    public exponent: ").append(getPublicExponent().toString(16)).append(str);
    localStringBuffer.append("   private exponent: ").append(getPrivateExponent().toString(16)).append(str);
    localStringBuffer.append("             primeP: ").append(getPrimeP().toString(16)).append(str);
    localStringBuffer.append("             primeQ: ").append(getPrimeQ().toString(16)).append(str);
    localStringBuffer.append("     primeExponentP: ").append(getPrimeExponentP().toString(16)).append(str);
    localStringBuffer.append("     primeExponentQ: ").append(getPrimeExponentQ().toString(16)).append(str);
    localStringBuffer.append("     crtCoefficient: ").append(getCrtCoefficient().toString(16)).append(str);
    return localStringBuffer.toString();
  }
}