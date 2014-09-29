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
import org.bouncycastle2.asn1.oiw.ElGamalParameter;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.crypto.params.ElGamalParameters;
import org.bouncycastle2.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle2.jce.interfaces.ElGamalPrivateKey;
import org.bouncycastle2.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle2.jce.spec.ElGamalParameterSpec;
import org.bouncycastle2.jce.spec.ElGamalPrivateKeySpec;

public class JCEElGamalPrivateKey
  implements ElGamalPrivateKey, DHPrivateKey, PKCS12BagAttributeCarrier
{
  static final long serialVersionUID = 4819350091141529678L;
  private PKCS12BagAttributeCarrierImpl attrCarrier = new PKCS12BagAttributeCarrierImpl();
  ElGamalParameterSpec elSpec;
  BigInteger x;

  protected JCEElGamalPrivateKey()
  {
  }

  JCEElGamalPrivateKey(DHPrivateKey paramDHPrivateKey)
  {
    this.x = paramDHPrivateKey.getX();
    this.elSpec = new ElGamalParameterSpec(paramDHPrivateKey.getParams().getP(), paramDHPrivateKey.getParams().getG());
  }

  JCEElGamalPrivateKey(DHPrivateKeySpec paramDHPrivateKeySpec)
  {
    this.x = paramDHPrivateKeySpec.getX();
    this.elSpec = new ElGamalParameterSpec(paramDHPrivateKeySpec.getP(), paramDHPrivateKeySpec.getG());
  }

  JCEElGamalPrivateKey(PrivateKeyInfo paramPrivateKeyInfo)
  {
    ElGamalParameter localElGamalParameter = new ElGamalParameter((ASN1Sequence)paramPrivateKeyInfo.getAlgorithmId().getParameters());
    this.x = ((DERInteger)paramPrivateKeyInfo.getPrivateKey()).getValue();
    this.elSpec = new ElGamalParameterSpec(localElGamalParameter.getP(), localElGamalParameter.getG());
  }

  JCEElGamalPrivateKey(ElGamalPrivateKeyParameters paramElGamalPrivateKeyParameters)
  {
    this.x = paramElGamalPrivateKeyParameters.getX();
    this.elSpec = new ElGamalParameterSpec(paramElGamalPrivateKeyParameters.getParameters().getP(), paramElGamalPrivateKeyParameters.getParameters().getG());
  }

  JCEElGamalPrivateKey(ElGamalPrivateKey paramElGamalPrivateKey)
  {
    this.x = paramElGamalPrivateKey.getX();
    this.elSpec = paramElGamalPrivateKey.getParameters();
  }

  JCEElGamalPrivateKey(ElGamalPrivateKeySpec paramElGamalPrivateKeySpec)
  {
    this.x = paramElGamalPrivateKeySpec.getX();
    this.elSpec = new ElGamalParameterSpec(paramElGamalPrivateKeySpec.getParams().getP(), paramElGamalPrivateKeySpec.getParams().getG());
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.x = ((BigInteger)paramObjectInputStream.readObject());
    this.elSpec = new ElGamalParameterSpec((BigInteger)paramObjectInputStream.readObject(), (BigInteger)paramObjectInputStream.readObject());
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(getX());
    paramObjectOutputStream.writeObject(this.elSpec.getP());
    paramObjectOutputStream.writeObject(this.elSpec.getG());
  }

  public String getAlgorithm()
  {
    return "ElGamal";
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
    return new PrivateKeyInfo(new AlgorithmIdentifier(OIWObjectIdentifiers.elGamalAlgorithm, new ElGamalParameter(this.elSpec.getP(), this.elSpec.getG()).getDERObject()), new DERInteger(getX())).getDEREncoded();
  }

  public String getFormat()
  {
    return "PKCS#8";
  }

  public ElGamalParameterSpec getParameters()
  {
    return this.elSpec;
  }

  public DHParameterSpec getParams()
  {
    return new DHParameterSpec(this.elSpec.getP(), this.elSpec.getG());
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