package org.bouncycastle2.jce.provider;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle2.jce.interfaces.GOST3410Params;
import org.bouncycastle2.jce.interfaces.GOST3410PrivateKey;
import org.bouncycastle2.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle2.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle2.jce.spec.GOST3410PrivateKeySpec;
import org.bouncycastle2.jce.spec.GOST3410PublicKeyParameterSetSpec;

public class JDKGOST3410PrivateKey
  implements GOST3410PrivateKey, PKCS12BagAttributeCarrier
{
  private PKCS12BagAttributeCarrier attrCarrier = new PKCS12BagAttributeCarrierImpl();
  GOST3410Params gost3410Spec;
  BigInteger x;

  protected JDKGOST3410PrivateKey()
  {
  }

  JDKGOST3410PrivateKey(PrivateKeyInfo paramPrivateKeyInfo)
  {
    GOST3410PublicKeyAlgParameters localGOST3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters((ASN1Sequence)paramPrivateKeyInfo.getAlgorithmId().getParameters());
    byte[] arrayOfByte1 = ((DEROctetString)paramPrivateKeyInfo.getPrivateKey()).getOctets();
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfByte1.length)
      {
        this.x = new BigInteger(1, arrayOfByte2);
        this.gost3410Spec = GOST3410ParameterSpec.fromPublicKeyAlg(localGOST3410PublicKeyAlgParameters);
        return;
      }
      arrayOfByte2[i] = arrayOfByte1[(-1 + arrayOfByte1.length - i)];
    }
  }

  JDKGOST3410PrivateKey(GOST3410PrivateKeyParameters paramGOST3410PrivateKeyParameters, GOST3410ParameterSpec paramGOST3410ParameterSpec)
  {
    this.x = paramGOST3410PrivateKeyParameters.getX();
    this.gost3410Spec = paramGOST3410ParameterSpec;
    if (paramGOST3410ParameterSpec == null)
      throw new IllegalArgumentException("spec is null");
  }

  JDKGOST3410PrivateKey(GOST3410PrivateKey paramGOST3410PrivateKey)
  {
    this.x = paramGOST3410PrivateKey.getX();
    this.gost3410Spec = paramGOST3410PrivateKey.getParameters();
  }

  JDKGOST3410PrivateKey(GOST3410PrivateKeySpec paramGOST3410PrivateKeySpec)
  {
    this.x = paramGOST3410PrivateKeySpec.getX();
    this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(paramGOST3410PrivateKeySpec.getP(), paramGOST3410PrivateKeySpec.getQ(), paramGOST3410PrivateKeySpec.getA()));
  }

  public String getAlgorithm()
  {
    return "GOST3410";
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
    byte[] arrayOfByte1 = getX().toByteArray();
    byte[] arrayOfByte2;
    int i;
    if (arrayOfByte1[0] == 0)
    {
      arrayOfByte2 = new byte[-1 + arrayOfByte1.length];
      i = 0;
      label23: if (i != arrayOfByte2.length)
        break label122;
      if (!(this.gost3410Spec instanceof GOST3410ParameterSpec))
        break label139;
    }
    label139: for (PrivateKeyInfo localPrivateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new DERObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new DERObjectIdentifier(this.gost3410Spec.getDigestParamSetOID())).getDERObject()), new DEROctetString(arrayOfByte2)); ; localPrivateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94), new DEROctetString(arrayOfByte2)))
    {
      return localPrivateKeyInfo.getDEREncoded();
      arrayOfByte2 = new byte[arrayOfByte1.length];
      break;
      label122: arrayOfByte2[i] = arrayOfByte1[(-1 + arrayOfByte1.length - i)];
      i++;
      break label23;
    }
  }

  public String getFormat()
  {
    return "PKCS#8";
  }

  public GOST3410Params getParameters()
  {
    return this.gost3410Spec;
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