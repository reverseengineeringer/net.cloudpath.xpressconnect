package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle2.jce.interfaces.GOST3410Params;
import org.bouncycastle2.jce.interfaces.GOST3410PublicKey;
import org.bouncycastle2.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle2.jce.spec.GOST3410PublicKeyParameterSetSpec;
import org.bouncycastle2.jce.spec.GOST3410PublicKeySpec;

public class JDKGOST3410PublicKey
  implements GOST3410PublicKey
{
  private GOST3410Params gost3410Spec;
  private BigInteger y;

  JDKGOST3410PublicKey(BigInteger paramBigInteger, GOST3410ParameterSpec paramGOST3410ParameterSpec)
  {
    this.y = paramBigInteger;
    this.gost3410Spec = paramGOST3410ParameterSpec;
  }

  JDKGOST3410PublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    GOST3410PublicKeyAlgParameters localGOST3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters((ASN1Sequence)paramSubjectPublicKeyInfo.getAlgorithmId().getParameters());
    try
    {
      byte[] arrayOfByte1 = ((DEROctetString)paramSubjectPublicKeyInfo.getPublicKey()).getOctets();
      byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
      for (int i = 0; ; i++)
      {
        if (i == arrayOfByte1.length)
        {
          this.y = new BigInteger(1, arrayOfByte2);
          this.gost3410Spec = GOST3410ParameterSpec.fromPublicKeyAlg(localGOST3410PublicKeyAlgParameters);
          return;
        }
        arrayOfByte2[i] = arrayOfByte1[(-1 + arrayOfByte1.length - i)];
      }
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalArgumentException("invalid info structure in GOST3410 public key");
  }

  JDKGOST3410PublicKey(GOST3410PublicKeyParameters paramGOST3410PublicKeyParameters, GOST3410ParameterSpec paramGOST3410ParameterSpec)
  {
    this.y = paramGOST3410PublicKeyParameters.getY();
    this.gost3410Spec = paramGOST3410ParameterSpec;
  }

  JDKGOST3410PublicKey(GOST3410PublicKey paramGOST3410PublicKey)
  {
    this.y = paramGOST3410PublicKey.getY();
    this.gost3410Spec = paramGOST3410PublicKey.getParameters();
  }

  JDKGOST3410PublicKey(GOST3410PublicKeySpec paramGOST3410PublicKeySpec)
  {
    this.y = paramGOST3410PublicKeySpec.getY();
    this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(paramGOST3410PublicKeySpec.getP(), paramGOST3410PublicKeySpec.getQ(), paramGOST3410PublicKeySpec.getA()));
  }

  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof JDKGOST3410PublicKey;
    boolean bool2 = false;
    if (bool1)
    {
      JDKGOST3410PublicKey localJDKGOST3410PublicKey = (JDKGOST3410PublicKey)paramObject;
      boolean bool3 = this.y.equals(localJDKGOST3410PublicKey.y);
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = this.gost3410Spec.equals(localJDKGOST3410PublicKey.gost3410Spec);
        bool2 = false;
        if (bool4)
          bool2 = true;
      }
    }
    return bool2;
  }

  public String getAlgorithm()
  {
    return "GOST3410";
  }

  public byte[] getEncoded()
  {
    byte[] arrayOfByte1 = getY().toByteArray();
    byte[] arrayOfByte2;
    int i;
    label23: SubjectPublicKeyInfo localSubjectPublicKeyInfo;
    if (arrayOfByte1[0] == 0)
    {
      arrayOfByte2 = new byte[-1 + arrayOfByte1.length];
      i = 0;
      if (i != arrayOfByte2.length)
        break label150;
      if (!(this.gost3410Spec instanceof GOST3410ParameterSpec))
        break label239;
      if (this.gost3410Spec.getEncryptionParamSetOID() == null)
        break label167;
      localSubjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new DERObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new DERObjectIdentifier(this.gost3410Spec.getDigestParamSetOID()), new DERObjectIdentifier(this.gost3410Spec.getEncryptionParamSetOID())).getDERObject()), new DEROctetString(arrayOfByte2));
    }
    while (true)
    {
      return localSubjectPublicKeyInfo.getDEREncoded();
      arrayOfByte2 = new byte[arrayOfByte1.length];
      break;
      label150: arrayOfByte2[i] = arrayOfByte1[(-1 + arrayOfByte1.length - i)];
      i++;
      break label23;
      label167: localSubjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new DERObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new DERObjectIdentifier(this.gost3410Spec.getDigestParamSetOID())).getDERObject()), new DEROctetString(arrayOfByte2));
      continue;
      label239: localSubjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94), new DEROctetString(arrayOfByte2));
    }
  }

  public String getFormat()
  {
    return "X.509";
  }

  public GOST3410Params getParameters()
  {
    return this.gost3410Spec;
  }

  public BigInteger getY()
  {
    return this.y;
  }

  public int hashCode()
  {
    return this.y.hashCode() ^ this.gost3410Spec.hashCode();
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("GOST3410 Public Key").append(str);
    localStringBuffer.append("            y: ").append(getY().toString(16)).append(str);
    return localStringBuffer.toString();
  }
}