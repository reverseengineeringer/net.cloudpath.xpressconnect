package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.crypto.params.RSAKeyParameters;

public class JCERSAPublicKey
  implements RSAPublicKey
{
  static final long serialVersionUID = 2675817738516720772L;
  private BigInteger modulus;
  private BigInteger publicExponent;

  JCERSAPublicKey(RSAPublicKey paramRSAPublicKey)
  {
    this.modulus = paramRSAPublicKey.getModulus();
    this.publicExponent = paramRSAPublicKey.getPublicExponent();
  }

  JCERSAPublicKey(RSAPublicKeySpec paramRSAPublicKeySpec)
  {
    this.modulus = paramRSAPublicKeySpec.getModulus();
    this.publicExponent = paramRSAPublicKeySpec.getPublicExponent();
  }

  JCERSAPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    try
    {
      RSAPublicKeyStructure localRSAPublicKeyStructure = new RSAPublicKeyStructure((ASN1Sequence)paramSubjectPublicKeyInfo.getPublicKey());
      this.modulus = localRSAPublicKeyStructure.getModulus();
      this.publicExponent = localRSAPublicKeyStructure.getPublicExponent();
      return;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalArgumentException("invalid info structure in RSA public key");
  }

  JCERSAPublicKey(RSAKeyParameters paramRSAKeyParameters)
  {
    this.modulus = paramRSAKeyParameters.getModulus();
    this.publicExponent = paramRSAKeyParameters.getExponent();
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    RSAPublicKey localRSAPublicKey;
    do
    {
      return true;
      if (!(paramObject instanceof RSAPublicKey))
        return false;
      localRSAPublicKey = (RSAPublicKey)paramObject;
    }
    while ((getModulus().equals(localRSAPublicKey.getModulus())) && (getPublicExponent().equals(localRSAPublicKey.getPublicExponent())));
    return false;
  }

  public String getAlgorithm()
  {
    return "RSA";
  }

  public byte[] getEncoded()
  {
    return new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, new DERNull()), new RSAPublicKeyStructure(getModulus(), getPublicExponent()).getDERObject()).getDEREncoded();
  }

  public String getFormat()
  {
    return "X.509";
  }

  public BigInteger getModulus()
  {
    return this.modulus;
  }

  public BigInteger getPublicExponent()
  {
    return this.publicExponent;
  }

  public int hashCode()
  {
    return getModulus().hashCode() ^ getPublicExponent().hashCode();
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("RSA Public Key").append(str);
    localStringBuffer.append("            modulus: ").append(getModulus().toString(16)).append(str);
    localStringBuffer.append("    public exponent: ").append(getPublicExponent().toString(16)).append(str);
    return localStringBuffer.toString();
  }
}