package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.spec.EllipticCurve;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle2.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x9.X962Parameters;
import org.bouncycastle2.asn1.x9.X9ECParameters;
import org.bouncycastle2.asn1.x9.X9ECPoint;
import org.bouncycastle2.asn1.x9.X9IntegerConverter;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.crypto.params.ECPublicKeyParameters;
import org.bouncycastle2.jce.ECGOST3410NamedCurveTable;
import org.bouncycastle2.jce.interfaces.ECPointEncoder;
import org.bouncycastle2.jce.provider.asymmetric.ec.EC5Util;
import org.bouncycastle2.jce.provider.asymmetric.ec.ECUtil;
import org.bouncycastle2.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle2.jce.spec.ECNamedCurveSpec;
import org.bouncycastle2.math.ec.ECCurve;
import org.bouncycastle2.math.ec.ECFieldElement;
import org.bouncycastle2.math.ec.ECPoint.F2m;
import org.bouncycastle2.math.ec.ECPoint.Fp;

public class JCEECPublicKey
  implements java.security.interfaces.ECPublicKey, org.bouncycastle2.jce.interfaces.ECPublicKey, ECPointEncoder
{
  private String algorithm = "EC";
  private java.security.spec.ECParameterSpec ecSpec;
  private GOST3410PublicKeyAlgParameters gostParams;
  private org.bouncycastle2.math.ec.ECPoint q;
  private boolean withCompression;

  public JCEECPublicKey(String paramString, java.security.spec.ECPublicKeySpec paramECPublicKeySpec)
  {
    this.algorithm = paramString;
    this.ecSpec = paramECPublicKeySpec.getParams();
    this.q = EC5Util.convertPoint(this.ecSpec, paramECPublicKeySpec.getW(), false);
  }

  public JCEECPublicKey(String paramString, ECPublicKeyParameters paramECPublicKeyParameters)
  {
    this.algorithm = paramString;
    this.q = paramECPublicKeyParameters.getQ();
    this.ecSpec = null;
  }

  public JCEECPublicKey(String paramString, ECPublicKeyParameters paramECPublicKeyParameters, java.security.spec.ECParameterSpec paramECParameterSpec)
  {
    ECDomainParameters localECDomainParameters = paramECPublicKeyParameters.getParameters();
    this.algorithm = paramString;
    this.q = paramECPublicKeyParameters.getQ();
    if (paramECParameterSpec == null)
    {
      this.ecSpec = createSpec(EC5Util.convertCurve(localECDomainParameters.getCurve(), localECDomainParameters.getSeed()), localECDomainParameters);
      return;
    }
    this.ecSpec = paramECParameterSpec;
  }

  public JCEECPublicKey(String paramString, ECPublicKeyParameters paramECPublicKeyParameters, org.bouncycastle2.jce.spec.ECParameterSpec paramECParameterSpec)
  {
    ECDomainParameters localECDomainParameters = paramECPublicKeyParameters.getParameters();
    this.algorithm = paramString;
    this.q = paramECPublicKeyParameters.getQ();
    if (paramECParameterSpec == null)
    {
      this.ecSpec = createSpec(EC5Util.convertCurve(localECDomainParameters.getCurve(), localECDomainParameters.getSeed()), localECDomainParameters);
      return;
    }
    this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(paramECParameterSpec.getCurve(), paramECParameterSpec.getSeed()), paramECParameterSpec);
  }

  public JCEECPublicKey(String paramString, JCEECPublicKey paramJCEECPublicKey)
  {
    this.algorithm = paramString;
    this.q = paramJCEECPublicKey.q;
    this.ecSpec = paramJCEECPublicKey.ecSpec;
    this.withCompression = paramJCEECPublicKey.withCompression;
    this.gostParams = paramJCEECPublicKey.gostParams;
  }

  public JCEECPublicKey(String paramString, org.bouncycastle2.jce.spec.ECPublicKeySpec paramECPublicKeySpec)
  {
    this.algorithm = paramString;
    this.q = paramECPublicKeySpec.getQ();
    if (paramECPublicKeySpec.getParams() != null)
    {
      this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(paramECPublicKeySpec.getParams().getCurve(), paramECPublicKeySpec.getParams().getSeed()), paramECPublicKeySpec.getParams());
      return;
    }
    if (this.q.getCurve() == null)
      this.q = ProviderUtil.getEcImplicitlyCa().getCurve().createPoint(this.q.getX().toBigInteger(), this.q.getY().toBigInteger(), false);
    this.ecSpec = null;
  }

  public JCEECPublicKey(java.security.interfaces.ECPublicKey paramECPublicKey)
  {
    this.algorithm = paramECPublicKey.getAlgorithm();
    this.ecSpec = paramECPublicKey.getParams();
    this.q = EC5Util.convertPoint(this.ecSpec, paramECPublicKey.getW(), false);
  }

  JCEECPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    populateFromPubKeyInfo(paramSubjectPublicKeyInfo);
  }

  private java.security.spec.ECParameterSpec createSpec(EllipticCurve paramEllipticCurve, ECDomainParameters paramECDomainParameters)
  {
    return new java.security.spec.ECParameterSpec(paramEllipticCurve, new java.security.spec.ECPoint(paramECDomainParameters.getG().getX().toBigInteger(), paramECDomainParameters.getG().getY().toBigInteger()), paramECDomainParameters.getN(), paramECDomainParameters.getH().intValue());
  }

  private void extractBytes(byte[] paramArrayOfByte, int paramInt, BigInteger paramBigInteger)
  {
    Object localObject = paramBigInteger.toByteArray();
    if (localObject.length < 32)
    {
      byte[] arrayOfByte = new byte[32];
      System.arraycopy(localObject, 0, arrayOfByte, arrayOfByte.length - localObject.length, localObject.length);
      localObject = arrayOfByte;
    }
    for (int i = 0; ; i++)
    {
      if (i == 32)
        return;
      paramArrayOfByte[(paramInt + i)] = localObject[(-1 + localObject.length - i)];
    }
  }

  private void populateFromPubKeyInfo(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    if (paramSubjectPublicKeyInfo.getAlgorithmId().getObjectId().equals(CryptoProObjectIdentifiers.gostR3410_2001))
    {
      DERBitString localDERBitString = paramSubjectPublicKeyInfo.getPublicKeyData();
      this.algorithm = "ECGOST3410";
      while (true)
      {
        byte[] arrayOfByte2;
        byte[] arrayOfByte3;
        byte[] arrayOfByte4;
        int i;
        int j;
        try
        {
          ASN1OctetString localASN1OctetString = (ASN1OctetString)ASN1Object.fromByteArray(localDERBitString.getBytes());
          arrayOfByte2 = localASN1OctetString.getOctets();
          arrayOfByte3 = new byte[32];
          arrayOfByte4 = new byte[32];
          i = 0;
          if (i == arrayOfByte3.length)
          {
            j = 0;
            if (j != arrayOfByte4.length)
              break label262;
            this.gostParams = new GOST3410PublicKeyAlgParameters((ASN1Sequence)paramSubjectPublicKeyInfo.getAlgorithmId().getParameters());
            ECNamedCurveParameterSpec localECNamedCurveParameterSpec = ECGOST3410NamedCurveTable.getParameterSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()));
            ECCurve localECCurve2 = localECNamedCurveParameterSpec.getCurve();
            EllipticCurve localEllipticCurve2 = EC5Util.convertCurve(localECCurve2, localECNamedCurveParameterSpec.getSeed());
            this.q = localECCurve2.createPoint(new BigInteger(1, arrayOfByte3), new BigInteger(1, arrayOfByte4), false);
            this.ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()), localEllipticCurve2, new java.security.spec.ECPoint(localECNamedCurveParameterSpec.getG().getX().toBigInteger(), localECNamedCurveParameterSpec.getG().getY().toBigInteger()), localECNamedCurveParameterSpec.getN(), localECNamedCurveParameterSpec.getH());
            return;
          }
        }
        catch (IOException localIOException2)
        {
          throw new IllegalArgumentException("error recovering public key");
        }
        arrayOfByte3[i] = arrayOfByte2[(31 - i)];
        i++;
        continue;
        label262: arrayOfByte4[j] = arrayOfByte2[(63 - j)];
        j++;
      }
    }
    X962Parameters localX962Parameters = new X962Parameters((DERObject)paramSubjectPublicKeyInfo.getAlgorithmId().getParameters());
    ECCurve localECCurve1;
    if (localX962Parameters.isNamedCurve())
    {
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localX962Parameters.getParameters();
      X9ECParameters localX9ECParameters2 = ECUtil.getNamedCurveByOid(localDERObjectIdentifier);
      localECCurve1 = localX9ECParameters2.getCurve();
      EllipticCurve localEllipticCurve1 = EC5Util.convertCurve(localECCurve1, localX9ECParameters2.getSeed());
      this.ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName(localDERObjectIdentifier), localEllipticCurve1, new java.security.spec.ECPoint(localX9ECParameters2.getG().getX().toBigInteger(), localX9ECParameters2.getG().getY().toBigInteger()), localX9ECParameters2.getN(), localX9ECParameters2.getH());
    }
    while (true)
    {
      byte[] arrayOfByte1 = paramSubjectPublicKeyInfo.getPublicKeyData().getBytes();
      Object localObject = new DEROctetString(arrayOfByte1);
      if ((arrayOfByte1[0] == 4) && (arrayOfByte1[1] == -2 + arrayOfByte1.length) && ((arrayOfByte1[2] == 2) || (arrayOfByte1[2] == 3)) && (new X9IntegerConverter().getByteLength(localECCurve1) >= -3 + arrayOfByte1.length));
      try
      {
        localObject = (ASN1OctetString)ASN1Object.fromByteArray(arrayOfByte1);
        this.q = new X9ECPoint(localECCurve1, (ASN1OctetString)localObject).getPoint();
        return;
        if (localX962Parameters.isImplicitlyCA())
        {
          this.ecSpec = null;
          localECCurve1 = ProviderUtil.getEcImplicitlyCa().getCurve();
        }
        else
        {
          X9ECParameters localX9ECParameters1 = new X9ECParameters((ASN1Sequence)localX962Parameters.getParameters());
          localECCurve1 = localX9ECParameters1.getCurve();
          this.ecSpec = new java.security.spec.ECParameterSpec(EC5Util.convertCurve(localECCurve1, localX9ECParameters1.getSeed()), new java.security.spec.ECPoint(localX9ECParameters1.getG().getX().toBigInteger(), localX9ECParameters1.getG().getY().toBigInteger()), localX9ECParameters1.getN(), localX9ECParameters1.getH().intValue());
        }
      }
      catch (IOException localIOException1)
      {
      }
    }
    throw new IllegalArgumentException("error recovering public key");
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    populateFromPubKeyInfo(SubjectPublicKeyInfo.getInstance(ASN1Object.fromByteArray((byte[])paramObjectInputStream.readObject())));
    this.algorithm = ((String)paramObjectInputStream.readObject());
    this.withCompression = paramObjectInputStream.readBoolean();
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(getEncoded());
    paramObjectOutputStream.writeObject(this.algorithm);
    paramObjectOutputStream.writeBoolean(this.withCompression);
  }

  public org.bouncycastle2.math.ec.ECPoint engineGetQ()
  {
    return this.q;
  }

  org.bouncycastle2.jce.spec.ECParameterSpec engineGetSpec()
  {
    if (this.ecSpec != null)
      return EC5Util.convertSpec(this.ecSpec, this.withCompression);
    return ProviderUtil.getEcImplicitlyCa();
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof JCEECPublicKey));
    JCEECPublicKey localJCEECPublicKey;
    do
    {
      return false;
      localJCEECPublicKey = (JCEECPublicKey)paramObject;
    }
    while ((!engineGetQ().equals(localJCEECPublicKey.engineGetQ())) || (!engineGetSpec().equals(localJCEECPublicKey.engineGetSpec())));
    return true;
  }

  public String getAlgorithm()
  {
    return this.algorithm;
  }

  public byte[] getEncoded()
  {
    SubjectPublicKeyInfo localSubjectPublicKeyInfo;
    if (this.algorithm.equals("ECGOST3410"))
    {
      Object localObject;
      if (this.gostParams != null)
        localObject = this.gostParams;
      while (true)
      {
        BigInteger localBigInteger1 = this.q.getX().toBigInteger();
        BigInteger localBigInteger2 = this.q.getY().toBigInteger();
        byte[] arrayOfByte = new byte[64];
        extractBytes(arrayOfByte, 0, localBigInteger1);
        extractBytes(arrayOfByte, 32, localBigInteger2);
        localSubjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_2001, ((ASN1Encodable)localObject).getDERObject()), new DEROctetString(arrayOfByte));
        return localSubjectPublicKeyInfo.getDEREncoded();
        if ((this.ecSpec instanceof ECNamedCurveSpec))
        {
          localObject = new GOST3410PublicKeyAlgParameters(ECGOST3410NamedCurves.getOID(((ECNamedCurveSpec)this.ecSpec).getName()), CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet);
        }
        else
        {
          ECCurve localECCurve2 = EC5Util.convertCurve(this.ecSpec.getCurve());
          localObject = new X962Parameters(new X9ECParameters(localECCurve2, EC5Util.convertPoint(localECCurve2, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
        }
      }
    }
    X962Parameters localX962Parameters;
    if ((this.ecSpec instanceof ECNamedCurveSpec))
    {
      DERObjectIdentifier localDERObjectIdentifier = ECUtil.getNamedCurveOid(((ECNamedCurveSpec)this.ecSpec).getName());
      if (localDERObjectIdentifier == null)
        localDERObjectIdentifier = new DERObjectIdentifier(((ECNamedCurveSpec)this.ecSpec).getName());
      localX962Parameters = new X962Parameters(localDERObjectIdentifier);
    }
    while (true)
    {
      ASN1OctetString localASN1OctetString = (ASN1OctetString)new X9ECPoint(engineGetQ().getCurve().createPoint(getQ().getX().toBigInteger(), getQ().getY().toBigInteger(), this.withCompression)).getDERObject();
      localSubjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, localX962Parameters.getDERObject()), localASN1OctetString.getOctets());
      break;
      if (this.ecSpec == null)
      {
        localX962Parameters = new X962Parameters(DERNull.INSTANCE);
      }
      else
      {
        ECCurve localECCurve1 = EC5Util.convertCurve(this.ecSpec.getCurve());
        localX962Parameters = new X962Parameters(new X9ECParameters(localECCurve1, EC5Util.convertPoint(localECCurve1, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
      }
    }
  }

  public String getFormat()
  {
    return "X.509";
  }

  public org.bouncycastle2.jce.spec.ECParameterSpec getParameters()
  {
    if (this.ecSpec == null)
      return null;
    return EC5Util.convertSpec(this.ecSpec, this.withCompression);
  }

  public java.security.spec.ECParameterSpec getParams()
  {
    return this.ecSpec;
  }

  public org.bouncycastle2.math.ec.ECPoint getQ()
  {
    if (this.ecSpec == null)
    {
      if ((this.q instanceof ECPoint.Fp))
        return new ECPoint.Fp(null, this.q.getX(), this.q.getY());
      return new ECPoint.F2m(null, this.q.getX(), this.q.getY());
    }
    return this.q;
  }

  public java.security.spec.ECPoint getW()
  {
    return new java.security.spec.ECPoint(this.q.getX().toBigInteger(), this.q.getY().toBigInteger());
  }

  public int hashCode()
  {
    return engineGetQ().hashCode() ^ engineGetSpec().hashCode();
  }

  public void setPointFormat(String paramString)
  {
    if ("UNCOMPRESSED".equalsIgnoreCase(paramString));
    for (boolean bool = false; ; bool = true)
    {
      this.withCompression = bool;
      return;
    }
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("EC Public Key").append(str);
    localStringBuffer.append("            X: ").append(this.q.getX().toBigInteger().toString(16)).append(str);
    localStringBuffer.append("            Y: ").append(this.q.getY().toBigInteger().toString(16)).append(str);
    return localStringBuffer.toString();
  }
}