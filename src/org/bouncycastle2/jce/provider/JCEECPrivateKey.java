package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.spec.EllipticCurve;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.sec.ECPrivateKeyStructure;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x9.X962Parameters;
import org.bouncycastle2.asn1.x9.X9ECParameters;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.jce.interfaces.ECPointEncoder;
import org.bouncycastle2.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle2.jce.provider.asymmetric.ec.EC5Util;
import org.bouncycastle2.jce.provider.asymmetric.ec.ECUtil;
import org.bouncycastle2.jce.spec.ECNamedCurveSpec;
import org.bouncycastle2.math.ec.ECCurve;
import org.bouncycastle2.math.ec.ECFieldElement;

public class JCEECPrivateKey
  implements java.security.interfaces.ECPrivateKey, org.bouncycastle2.jce.interfaces.ECPrivateKey, PKCS12BagAttributeCarrier, ECPointEncoder
{
  private String algorithm = "EC";
  private PKCS12BagAttributeCarrierImpl attrCarrier = new PKCS12BagAttributeCarrierImpl();
  private BigInteger d;
  private java.security.spec.ECParameterSpec ecSpec;
  private DERBitString publicKey;
  private boolean withCompression;

  protected JCEECPrivateKey()
  {
  }

  public JCEECPrivateKey(String paramString, java.security.spec.ECPrivateKeySpec paramECPrivateKeySpec)
  {
    this.algorithm = paramString;
    this.d = paramECPrivateKeySpec.getS();
    this.ecSpec = paramECPrivateKeySpec.getParams();
  }

  public JCEECPrivateKey(String paramString, ECPrivateKeyParameters paramECPrivateKeyParameters)
  {
    this.algorithm = paramString;
    this.d = paramECPrivateKeyParameters.getD();
    this.ecSpec = null;
  }

  public JCEECPrivateKey(String paramString, ECPrivateKeyParameters paramECPrivateKeyParameters, JCEECPublicKey paramJCEECPublicKey, java.security.spec.ECParameterSpec paramECParameterSpec)
  {
    ECDomainParameters localECDomainParameters = paramECPrivateKeyParameters.getParameters();
    this.algorithm = paramString;
    this.d = paramECPrivateKeyParameters.getD();
    if (paramECParameterSpec == null);
    for (this.ecSpec = new java.security.spec.ECParameterSpec(EC5Util.convertCurve(localECDomainParameters.getCurve(), localECDomainParameters.getSeed()), new java.security.spec.ECPoint(localECDomainParameters.getG().getX().toBigInteger(), localECDomainParameters.getG().getY().toBigInteger()), localECDomainParameters.getN(), localECDomainParameters.getH().intValue()); ; this.ecSpec = paramECParameterSpec)
    {
      this.publicKey = getPublicKeyDetails(paramJCEECPublicKey);
      return;
    }
  }

  public JCEECPrivateKey(String paramString, ECPrivateKeyParameters paramECPrivateKeyParameters, JCEECPublicKey paramJCEECPublicKey, org.bouncycastle2.jce.spec.ECParameterSpec paramECParameterSpec)
  {
    ECDomainParameters localECDomainParameters = paramECPrivateKeyParameters.getParameters();
    this.algorithm = paramString;
    this.d = paramECPrivateKeyParameters.getD();
    if (paramECParameterSpec == null);
    for (this.ecSpec = new java.security.spec.ECParameterSpec(EC5Util.convertCurve(localECDomainParameters.getCurve(), localECDomainParameters.getSeed()), new java.security.spec.ECPoint(localECDomainParameters.getG().getX().toBigInteger(), localECDomainParameters.getG().getY().toBigInteger()), localECDomainParameters.getN(), localECDomainParameters.getH().intValue()); ; this.ecSpec = new java.security.spec.ECParameterSpec(EC5Util.convertCurve(paramECParameterSpec.getCurve(), paramECParameterSpec.getSeed()), new java.security.spec.ECPoint(paramECParameterSpec.getG().getX().toBigInteger(), paramECParameterSpec.getG().getY().toBigInteger()), paramECParameterSpec.getN(), paramECParameterSpec.getH().intValue()))
    {
      this.publicKey = getPublicKeyDetails(paramJCEECPublicKey);
      return;
    }
  }

  public JCEECPrivateKey(String paramString, JCEECPrivateKey paramJCEECPrivateKey)
  {
    this.algorithm = paramString;
    this.d = paramJCEECPrivateKey.d;
    this.ecSpec = paramJCEECPrivateKey.ecSpec;
    this.withCompression = paramJCEECPrivateKey.withCompression;
    this.attrCarrier = paramJCEECPrivateKey.attrCarrier;
    this.publicKey = paramJCEECPrivateKey.publicKey;
  }

  public JCEECPrivateKey(String paramString, org.bouncycastle2.jce.spec.ECPrivateKeySpec paramECPrivateKeySpec)
  {
    this.algorithm = paramString;
    this.d = paramECPrivateKeySpec.getD();
    if (paramECPrivateKeySpec.getParams() != null)
    {
      this.ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(paramECPrivateKeySpec.getParams().getCurve(), paramECPrivateKeySpec.getParams().getSeed()), paramECPrivateKeySpec.getParams());
      return;
    }
    this.ecSpec = null;
  }

  public JCEECPrivateKey(java.security.interfaces.ECPrivateKey paramECPrivateKey)
  {
    this.d = paramECPrivateKey.getS();
    this.algorithm = paramECPrivateKey.getAlgorithm();
    this.ecSpec = paramECPrivateKey.getParams();
  }

  JCEECPrivateKey(PrivateKeyInfo paramPrivateKeyInfo)
  {
    populateFromPrivKeyInfo(paramPrivateKeyInfo);
  }

  private DERBitString getPublicKeyDetails(JCEECPublicKey paramJCEECPublicKey)
  {
    try
    {
      DERBitString localDERBitString = SubjectPublicKeyInfo.getInstance(ASN1Object.fromByteArray(paramJCEECPublicKey.getEncoded())).getPublicKeyData();
      return localDERBitString;
    }
    catch (IOException localIOException)
    {
    }
    return null;
  }

  private void populateFromPrivKeyInfo(PrivateKeyInfo paramPrivateKeyInfo)
  {
    X962Parameters localX962Parameters = new X962Parameters((DERObject)paramPrivateKeyInfo.getAlgorithmId().getParameters());
    DERObjectIdentifier localDERObjectIdentifier;
    X9ECParameters localX9ECParameters2;
    if (localX962Parameters.isNamedCurve())
    {
      localDERObjectIdentifier = (DERObjectIdentifier)localX962Parameters.getParameters();
      localX9ECParameters2 = ECUtil.getNamedCurveByOid(localDERObjectIdentifier);
      if (localX9ECParameters2 == null)
      {
        ECDomainParameters localECDomainParameters = ECGOST3410NamedCurves.getByOID(localDERObjectIdentifier);
        EllipticCurve localEllipticCurve2 = EC5Util.convertCurve(localECDomainParameters.getCurve(), localECDomainParameters.getSeed());
        this.ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(localDERObjectIdentifier), localEllipticCurve2, new java.security.spec.ECPoint(localECDomainParameters.getG().getX().toBigInteger(), localECDomainParameters.getG().getY().toBigInteger()), localECDomainParameters.getN(), localECDomainParameters.getH());
      }
    }
    while ((paramPrivateKeyInfo.getPrivateKey() instanceof DERInteger))
    {
      this.d = ((DERInteger)paramPrivateKeyInfo.getPrivateKey()).getValue();
      return;
      EllipticCurve localEllipticCurve1 = EC5Util.convertCurve(localX9ECParameters2.getCurve(), localX9ECParameters2.getSeed());
      this.ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName(localDERObjectIdentifier), localEllipticCurve1, new java.security.spec.ECPoint(localX9ECParameters2.getG().getX().toBigInteger(), localX9ECParameters2.getG().getY().toBigInteger()), localX9ECParameters2.getN(), localX9ECParameters2.getH());
      continue;
      if (localX962Parameters.isImplicitlyCA())
      {
        this.ecSpec = null;
      }
      else
      {
        X9ECParameters localX9ECParameters1 = new X9ECParameters((ASN1Sequence)localX962Parameters.getParameters());
        this.ecSpec = new java.security.spec.ECParameterSpec(EC5Util.convertCurve(localX9ECParameters1.getCurve(), localX9ECParameters1.getSeed()), new java.security.spec.ECPoint(localX9ECParameters1.getG().getX().toBigInteger(), localX9ECParameters1.getG().getY().toBigInteger()), localX9ECParameters1.getN(), localX9ECParameters1.getH().intValue());
      }
    }
    ECPrivateKeyStructure localECPrivateKeyStructure = new ECPrivateKeyStructure((ASN1Sequence)paramPrivateKeyInfo.getPrivateKey());
    this.d = localECPrivateKeyStructure.getKey();
    this.publicKey = localECPrivateKeyStructure.getPublicKey();
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    populateFromPrivKeyInfo(PrivateKeyInfo.getInstance(ASN1Object.fromByteArray((byte[])paramObjectInputStream.readObject())));
    this.algorithm = ((String)paramObjectInputStream.readObject());
    this.withCompression = paramObjectInputStream.readBoolean();
    this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    this.attrCarrier.readObject(paramObjectInputStream);
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(getEncoded());
    paramObjectOutputStream.writeObject(this.algorithm);
    paramObjectOutputStream.writeBoolean(this.withCompression);
    this.attrCarrier.writeObject(paramObjectOutputStream);
  }

  org.bouncycastle2.jce.spec.ECParameterSpec engineGetSpec()
  {
    if (this.ecSpec != null)
      return EC5Util.convertSpec(this.ecSpec, this.withCompression);
    return ProviderUtil.getEcImplicitlyCa();
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof JCEECPrivateKey));
    JCEECPrivateKey localJCEECPrivateKey;
    do
    {
      return false;
      localJCEECPrivateKey = (JCEECPrivateKey)paramObject;
    }
    while ((!getD().equals(localJCEECPrivateKey.getD())) || (!engineGetSpec().equals(localJCEECPrivateKey.engineGetSpec())));
    return true;
  }

  public String getAlgorithm()
  {
    return this.algorithm;
  }

  public DEREncodable getBagAttribute(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return this.attrCarrier.getBagAttribute(paramDERObjectIdentifier);
  }

  public Enumeration getBagAttributeKeys()
  {
    return this.attrCarrier.getBagAttributeKeys();
  }

  public BigInteger getD()
  {
    return this.d;
  }

  public byte[] getEncoded()
  {
    X962Parameters localX962Parameters;
    ECPrivateKeyStructure localECPrivateKeyStructure;
    if ((this.ecSpec instanceof ECNamedCurveSpec))
    {
      DERObjectIdentifier localDERObjectIdentifier = ECUtil.getNamedCurveOid(((ECNamedCurveSpec)this.ecSpec).getName());
      if (localDERObjectIdentifier == null)
        localDERObjectIdentifier = new DERObjectIdentifier(((ECNamedCurveSpec)this.ecSpec).getName());
      localX962Parameters = new X962Parameters(localDERObjectIdentifier);
      if (this.publicKey == null)
        break label224;
      localECPrivateKeyStructure = new ECPrivateKeyStructure(getS(), this.publicKey, localX962Parameters);
      label83: if (!this.algorithm.equals("ECGOST3410"))
        break label240;
    }
    label224: label240: for (PrivateKeyInfo localPrivateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_2001, localX962Parameters.getDERObject()), localECPrivateKeyStructure.getDERObject()); ; localPrivateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, localX962Parameters.getDERObject()), localECPrivateKeyStructure.getDERObject()))
    {
      return localPrivateKeyInfo.getDEREncoded();
      if (this.ecSpec == null)
      {
        localX962Parameters = new X962Parameters(DERNull.INSTANCE);
        break;
      }
      ECCurve localECCurve = EC5Util.convertCurve(this.ecSpec.getCurve());
      localX962Parameters = new X962Parameters(new X9ECParameters(localECCurve, EC5Util.convertPoint(localECCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
      break;
      localECPrivateKeyStructure = new ECPrivateKeyStructure(getS(), localX962Parameters);
      break label83;
    }
  }

  public String getFormat()
  {
    return "PKCS#8";
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

  public BigInteger getS()
  {
    return this.d;
  }

  public int hashCode()
  {
    return getD().hashCode() ^ engineGetSpec().hashCode();
  }

  public void setBagAttribute(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.attrCarrier.setBagAttribute(paramDERObjectIdentifier, paramDEREncodable);
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
    localStringBuffer.append("EC Private Key").append(str);
    localStringBuffer.append("             S: ").append(this.d.toString(16)).append(str);
    return localStringBuffer.toString();
  }
}