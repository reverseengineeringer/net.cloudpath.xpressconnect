package org.bouncycastle2.crypto.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.nist.NISTNamedCurves;
import org.bouncycastle2.asn1.oiw.ElGamalParameter;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.DHParameter;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle2.asn1.sec.ECPrivateKeyStructure;
import org.bouncycastle2.asn1.sec.SECNamedCurves;
import org.bouncycastle2.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.DSAParameter;
import org.bouncycastle2.asn1.x9.X962NamedCurves;
import org.bouncycastle2.asn1.x9.X962Parameters;
import org.bouncycastle2.asn1.x9.X9ECParameters;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle2.crypto.params.DSAParameters;
import org.bouncycastle2.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ElGamalParameters;
import org.bouncycastle2.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle2.crypto.params.RSAPrivateCrtKeyParameters;

public class PrivateKeyFactory
{
  public static AsymmetricKeyParameter createKey(InputStream paramInputStream)
    throws IOException
  {
    return createKey(PrivateKeyInfo.getInstance(new ASN1InputStream(paramInputStream).readObject()));
  }

  public static AsymmetricKeyParameter createKey(PrivateKeyInfo paramPrivateKeyInfo)
    throws IOException
  {
    AlgorithmIdentifier localAlgorithmIdentifier = paramPrivateKeyInfo.getAlgorithmId();
    if (localAlgorithmIdentifier.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption))
    {
      RSAPrivateKeyStructure localRSAPrivateKeyStructure = new RSAPrivateKeyStructure((ASN1Sequence)paramPrivateKeyInfo.getPrivateKey());
      return new RSAPrivateCrtKeyParameters(localRSAPrivateKeyStructure.getModulus(), localRSAPrivateKeyStructure.getPublicExponent(), localRSAPrivateKeyStructure.getPrivateExponent(), localRSAPrivateKeyStructure.getPrime1(), localRSAPrivateKeyStructure.getPrime2(), localRSAPrivateKeyStructure.getExponent1(), localRSAPrivateKeyStructure.getExponent2(), localRSAPrivateKeyStructure.getCoefficient());
    }
    if (localAlgorithmIdentifier.getObjectId().equals(PKCSObjectIdentifiers.dhKeyAgreement))
    {
      DHParameter localDHParameter = new DHParameter((ASN1Sequence)paramPrivateKeyInfo.getAlgorithmId().getParameters());
      DERInteger localDERInteger1 = (DERInteger)paramPrivateKeyInfo.getPrivateKey();
      BigInteger localBigInteger1 = localDHParameter.getL();
      if (localBigInteger1 == null);
      for (int i = 0; ; i = localBigInteger1.intValue())
      {
        DHParameters localDHParameters = new DHParameters(localDHParameter.getP(), localDHParameter.getG(), null, i);
        return new DHPrivateKeyParameters(localDERInteger1.getValue(), localDHParameters);
      }
    }
    if (localAlgorithmIdentifier.getObjectId().equals(OIWObjectIdentifiers.elGamalAlgorithm))
    {
      ElGamalParameter localElGamalParameter = new ElGamalParameter((ASN1Sequence)paramPrivateKeyInfo.getAlgorithmId().getParameters());
      return new ElGamalPrivateKeyParameters(((DERInteger)paramPrivateKeyInfo.getPrivateKey()).getValue(), new ElGamalParameters(localElGamalParameter.getP(), localElGamalParameter.getG()));
    }
    if (localAlgorithmIdentifier.getObjectId().equals(X9ObjectIdentifiers.id_dsa))
    {
      DERInteger localDERInteger2 = (DERInteger)paramPrivateKeyInfo.getPrivateKey();
      DEREncodable localDEREncodable = paramPrivateKeyInfo.getAlgorithmId().getParameters();
      DSAParameters localDSAParameters = null;
      if (localDEREncodable != null)
      {
        DSAParameter localDSAParameter = DSAParameter.getInstance(localDEREncodable.getDERObject());
        BigInteger localBigInteger2 = localDSAParameter.getP();
        BigInteger localBigInteger3 = localDSAParameter.getQ();
        BigInteger localBigInteger4 = localDSAParameter.getG();
        localDSAParameters = new DSAParameters(localBigInteger2, localBigInteger3, localBigInteger4);
      }
      return new DSAPrivateKeyParameters(localDERInteger2.getValue(), localDSAParameters);
    }
    if (localAlgorithmIdentifier.getObjectId().equals(X9ObjectIdentifiers.id_ecPublicKey))
    {
      X962Parameters localX962Parameters = new X962Parameters((DERObject)paramPrivateKeyInfo.getAlgorithmId().getParameters());
      X9ECParameters localX9ECParameters2;
      if (localX962Parameters.isNamedCurve())
      {
        DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localX962Parameters.getParameters();
        localX9ECParameters2 = X962NamedCurves.getByOID(localDERObjectIdentifier);
        if (localX9ECParameters2 == null)
        {
          localX9ECParameters2 = SECNamedCurves.getByOID(localDERObjectIdentifier);
          if (localX9ECParameters2 == null)
          {
            localX9ECParameters2 = NISTNamedCurves.getByOID(localDERObjectIdentifier);
            if (localX9ECParameters2 == null)
              localX9ECParameters2 = TeleTrusTNamedCurves.getByOID(localDERObjectIdentifier);
          }
        }
      }
      X9ECParameters localX9ECParameters1;
      for (ECDomainParameters localECDomainParameters = new ECDomainParameters(localX9ECParameters2.getCurve(), localX9ECParameters2.getG(), localX9ECParameters2.getN(), localX9ECParameters2.getH(), localX9ECParameters2.getSeed()); ; localECDomainParameters = new ECDomainParameters(localX9ECParameters1.getCurve(), localX9ECParameters1.getG(), localX9ECParameters1.getN(), localX9ECParameters1.getH(), localX9ECParameters1.getSeed()))
      {
        return new ECPrivateKeyParameters(new ECPrivateKeyStructure((ASN1Sequence)paramPrivateKeyInfo.getPrivateKey()).getKey(), localECDomainParameters);
        localX9ECParameters1 = new X9ECParameters((ASN1Sequence)localX962Parameters.getParameters());
      }
    }
    throw new RuntimeException("algorithm identifier in key not recognised");
  }

  public static AsymmetricKeyParameter createKey(byte[] paramArrayOfByte)
    throws IOException
  {
    return createKey(PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(paramArrayOfByte)));
  }
}