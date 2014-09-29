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
import org.bouncycastle2.asn1.oiw.ElGamalParameter;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.crypto.params.ElGamalParameters;
import org.bouncycastle2.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle2.jce.interfaces.ElGamalPublicKey;
import org.bouncycastle2.jce.spec.ElGamalParameterSpec;
import org.bouncycastle2.jce.spec.ElGamalPublicKeySpec;

public class JCEElGamalPublicKey
  implements ElGamalPublicKey, DHPublicKey
{
  static final long serialVersionUID = 8712728417091216948L;
  private ElGamalParameterSpec elSpec;
  private BigInteger y;

  JCEElGamalPublicKey(BigInteger paramBigInteger, ElGamalParameterSpec paramElGamalParameterSpec)
  {
    this.y = paramBigInteger;
    this.elSpec = paramElGamalParameterSpec;
  }

  JCEElGamalPublicKey(DHPublicKey paramDHPublicKey)
  {
    this.y = paramDHPublicKey.getY();
    this.elSpec = new ElGamalParameterSpec(paramDHPublicKey.getParams().getP(), paramDHPublicKey.getParams().getG());
  }

  JCEElGamalPublicKey(DHPublicKeySpec paramDHPublicKeySpec)
  {
    this.y = paramDHPublicKeySpec.getY();
    this.elSpec = new ElGamalParameterSpec(paramDHPublicKeySpec.getP(), paramDHPublicKeySpec.getG());
  }

  JCEElGamalPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    ElGamalParameter localElGamalParameter = new ElGamalParameter((ASN1Sequence)paramSubjectPublicKeyInfo.getAlgorithmId().getParameters());
    try
    {
      DERInteger localDERInteger = (DERInteger)paramSubjectPublicKeyInfo.getPublicKey();
      this.y = localDERInteger.getValue();
      this.elSpec = new ElGamalParameterSpec(localElGamalParameter.getP(), localElGamalParameter.getG());
      return;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalArgumentException("invalid info structure in DSA public key");
  }

  JCEElGamalPublicKey(ElGamalPublicKeyParameters paramElGamalPublicKeyParameters)
  {
    this.y = paramElGamalPublicKeyParameters.getY();
    this.elSpec = new ElGamalParameterSpec(paramElGamalPublicKeyParameters.getParameters().getP(), paramElGamalPublicKeyParameters.getParameters().getG());
  }

  JCEElGamalPublicKey(ElGamalPublicKey paramElGamalPublicKey)
  {
    this.y = paramElGamalPublicKey.getY();
    this.elSpec = paramElGamalPublicKey.getParameters();
  }

  JCEElGamalPublicKey(ElGamalPublicKeySpec paramElGamalPublicKeySpec)
  {
    this.y = paramElGamalPublicKeySpec.getY();
    this.elSpec = new ElGamalParameterSpec(paramElGamalPublicKeySpec.getParams().getP(), paramElGamalPublicKeySpec.getParams().getG());
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.y = ((BigInteger)paramObjectInputStream.readObject());
    this.elSpec = new ElGamalParameterSpec((BigInteger)paramObjectInputStream.readObject(), (BigInteger)paramObjectInputStream.readObject());
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(getY());
    paramObjectOutputStream.writeObject(this.elSpec.getP());
    paramObjectOutputStream.writeObject(this.elSpec.getG());
  }

  public String getAlgorithm()
  {
    return "ElGamal";
  }

  public byte[] getEncoded()
  {
    return new SubjectPublicKeyInfo(new AlgorithmIdentifier(OIWObjectIdentifiers.elGamalAlgorithm, new ElGamalParameter(this.elSpec.getP(), this.elSpec.getG()).getDERObject()), new DERInteger(this.y)).getDEREncoded();
  }

  public String getFormat()
  {
    return "X.509";
  }

  public ElGamalParameterSpec getParameters()
  {
    return this.elSpec;
  }

  public DHParameterSpec getParams()
  {
    return new DHParameterSpec(this.elSpec.getP(), this.elSpec.getG());
  }

  public BigInteger getY()
  {
    return this.y;
  }
}