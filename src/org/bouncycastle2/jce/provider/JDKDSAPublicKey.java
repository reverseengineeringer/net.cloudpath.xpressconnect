package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAParameterSpec;
import java.security.spec.DSAPublicKeySpec;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.DSAParameter;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.crypto.params.DSAParameters;
import org.bouncycastle2.crypto.params.DSAPublicKeyParameters;

public class JDKDSAPublicKey
  implements DSAPublicKey
{
  private static final long serialVersionUID = 1752452449903495175L;
  private DSAParams dsaSpec;
  private BigInteger y;

  JDKDSAPublicKey(BigInteger paramBigInteger, DSAParameterSpec paramDSAParameterSpec)
  {
    this.y = paramBigInteger;
    this.dsaSpec = paramDSAParameterSpec;
  }

  JDKDSAPublicKey(DSAPublicKey paramDSAPublicKey)
  {
    this.y = paramDSAPublicKey.getY();
    this.dsaSpec = paramDSAPublicKey.getParams();
  }

  JDKDSAPublicKey(DSAPublicKeySpec paramDSAPublicKeySpec)
  {
    this.y = paramDSAPublicKeySpec.getY();
    this.dsaSpec = new DSAParameterSpec(paramDSAPublicKeySpec.getP(), paramDSAPublicKeySpec.getQ(), paramDSAPublicKeySpec.getG());
  }

  JDKDSAPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    try
    {
      DERInteger localDERInteger = (DERInteger)paramSubjectPublicKeyInfo.getPublicKey();
      this.y = localDERInteger.getValue();
      if (isNotNull(paramSubjectPublicKeyInfo.getAlgorithmId().getParameters()))
      {
        DSAParameter localDSAParameter = new DSAParameter((ASN1Sequence)paramSubjectPublicKeyInfo.getAlgorithmId().getParameters());
        this.dsaSpec = new DSAParameterSpec(localDSAParameter.getP(), localDSAParameter.getQ(), localDSAParameter.getG());
      }
      return;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalArgumentException("invalid info structure in DSA public key");
  }

  JDKDSAPublicKey(DSAPublicKeyParameters paramDSAPublicKeyParameters)
  {
    this.y = paramDSAPublicKeyParameters.getY();
    this.dsaSpec = new DSAParameterSpec(paramDSAPublicKeyParameters.getParameters().getP(), paramDSAPublicKeyParameters.getParameters().getQ(), paramDSAPublicKeyParameters.getParameters().getG());
  }

  private boolean isNotNull(DEREncodable paramDEREncodable)
  {
    return (paramDEREncodable != null) && (!DERNull.INSTANCE.equals(paramDEREncodable));
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.y = ((BigInteger)paramObjectInputStream.readObject());
    this.dsaSpec = new DSAParameterSpec((BigInteger)paramObjectInputStream.readObject(), (BigInteger)paramObjectInputStream.readObject(), (BigInteger)paramObjectInputStream.readObject());
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(this.y);
    paramObjectOutputStream.writeObject(this.dsaSpec.getP());
    paramObjectOutputStream.writeObject(this.dsaSpec.getQ());
    paramObjectOutputStream.writeObject(this.dsaSpec.getG());
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DSAPublicKey));
    DSAPublicKey localDSAPublicKey;
    do
    {
      return false;
      localDSAPublicKey = (DSAPublicKey)paramObject;
    }
    while ((!getY().equals(localDSAPublicKey.getY())) || (!getParams().getG().equals(localDSAPublicKey.getParams().getG())) || (!getParams().getP().equals(localDSAPublicKey.getParams().getP())) || (!getParams().getQ().equals(localDSAPublicKey.getParams().getQ())));
    return true;
  }

  public String getAlgorithm()
  {
    return "DSA";
  }

  public byte[] getEncoded()
  {
    if (this.dsaSpec == null)
      return new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa), new DERInteger(this.y)).getDEREncoded();
    return new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(this.dsaSpec.getP(), this.dsaSpec.getQ(), this.dsaSpec.getG()).getDERObject()), new DERInteger(this.y)).getDEREncoded();
  }

  public String getFormat()
  {
    return "X.509";
  }

  public DSAParams getParams()
  {
    return this.dsaSpec;
  }

  public BigInteger getY()
  {
    return this.y;
  }

  public int hashCode()
  {
    return getY().hashCode() ^ getParams().getG().hashCode() ^ getParams().getP().hashCode() ^ getParams().getQ().hashCode();
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("DSA Public Key").append(str);
    localStringBuffer.append("            y: ").append(getY().toString(16)).append(str);
    return localStringBuffer.toString();
  }
}