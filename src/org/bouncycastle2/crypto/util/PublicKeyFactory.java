package org.bouncycastle2.crypto.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.nist.NISTNamedCurves;
import org.bouncycastle2.asn1.oiw.ElGamalParameter;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.DHParameter;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.sec.SECNamedCurves;
import org.bouncycastle2.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.DSAParameter;
import org.bouncycastle2.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle2.asn1.x9.DHDomainParameters;
import org.bouncycastle2.asn1.x9.DHPublicKey;
import org.bouncycastle2.asn1.x9.DHValidationParms;
import org.bouncycastle2.asn1.x9.X962NamedCurves;
import org.bouncycastle2.asn1.x9.X962Parameters;
import org.bouncycastle2.asn1.x9.X9ECParameters;
import org.bouncycastle2.asn1.x9.X9ECPoint;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPublicKeyParameters;
import org.bouncycastle2.crypto.params.DHValidationParameters;
import org.bouncycastle2.crypto.params.DSAParameters;
import org.bouncycastle2.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.crypto.params.ECPublicKeyParameters;
import org.bouncycastle2.crypto.params.ElGamalParameters;
import org.bouncycastle2.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle2.crypto.params.RSAKeyParameters;

public class PublicKeyFactory
{
  public static AsymmetricKeyParameter createKey(InputStream paramInputStream)
    throws IOException
  {
    return createKey(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(paramInputStream).readObject()));
  }

  public static AsymmetricKeyParameter createKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
    throws IOException
  {
    AlgorithmIdentifier localAlgorithmIdentifier = paramSubjectPublicKeyInfo.getAlgorithmId();
    if ((localAlgorithmIdentifier.getObjectId().equals(PKCSObjectIdentifiers.rsaEncryption)) || (localAlgorithmIdentifier.getObjectId().equals(X509ObjectIdentifiers.id_ea_rsa)))
    {
      RSAPublicKeyStructure localRSAPublicKeyStructure = new RSAPublicKeyStructure((ASN1Sequence)paramSubjectPublicKeyInfo.getPublicKey());
      return new RSAKeyParameters(false, localRSAPublicKeyStructure.getModulus(), localRSAPublicKeyStructure.getPublicExponent());
    }
    if (localAlgorithmIdentifier.getObjectId().equals(X9ObjectIdentifiers.dhpublicnumber))
    {
      BigInteger localBigInteger5 = DHPublicKey.getInstance(paramSubjectPublicKeyInfo.getPublicKey()).getY().getValue();
      DHDomainParameters localDHDomainParameters = DHDomainParameters.getInstance(paramSubjectPublicKeyInfo.getAlgorithmId().getParameters());
      BigInteger localBigInteger6 = localDHDomainParameters.getP().getValue();
      BigInteger localBigInteger7 = localDHDomainParameters.getG().getValue();
      BigInteger localBigInteger8 = localDHDomainParameters.getQ().getValue();
      DERInteger localDERInteger3 = localDHDomainParameters.getJ();
      BigInteger localBigInteger9 = null;
      if (localDERInteger3 != null)
        localBigInteger9 = localDHDomainParameters.getJ().getValue();
      DHValidationParms localDHValidationParms = localDHDomainParameters.getValidationParms();
      DHValidationParameters localDHValidationParameters = null;
      if (localDHValidationParms != null)
        localDHValidationParameters = new DHValidationParameters(localDHValidationParms.getSeed().getBytes(), localDHValidationParms.getPgenCounter().getValue().intValue());
      return new DHPublicKeyParameters(localBigInteger5, new DHParameters(localBigInteger6, localBigInteger7, localBigInteger8, localBigInteger9, localDHValidationParameters));
    }
    if (localAlgorithmIdentifier.getObjectId().equals(PKCSObjectIdentifiers.dhKeyAgreement))
    {
      DHParameter localDHParameter = new DHParameter((ASN1Sequence)paramSubjectPublicKeyInfo.getAlgorithmId().getParameters());
      DERInteger localDERInteger1 = (DERInteger)paramSubjectPublicKeyInfo.getPublicKey();
      BigInteger localBigInteger1 = localDHParameter.getL();
      if (localBigInteger1 == null);
      for (int i = 0; ; i = localBigInteger1.intValue())
      {
        DHParameters localDHParameters = new DHParameters(localDHParameter.getP(), localDHParameter.getG(), null, i);
        return new DHPublicKeyParameters(localDERInteger1.getValue(), localDHParameters);
      }
    }
    if (localAlgorithmIdentifier.getObjectId().equals(OIWObjectIdentifiers.elGamalAlgorithm))
    {
      ElGamalParameter localElGamalParameter = new ElGamalParameter((ASN1Sequence)paramSubjectPublicKeyInfo.getAlgorithmId().getParameters());
      return new ElGamalPublicKeyParameters(((DERInteger)paramSubjectPublicKeyInfo.getPublicKey()).getValue(), new ElGamalParameters(localElGamalParameter.getP(), localElGamalParameter.getG()));
    }
    if ((localAlgorithmIdentifier.getObjectId().equals(X9ObjectIdentifiers.id_dsa)) || (localAlgorithmIdentifier.getObjectId().equals(OIWObjectIdentifiers.dsaWithSHA1)))
    {
      DERInteger localDERInteger2 = (DERInteger)paramSubjectPublicKeyInfo.getPublicKey();
      DEREncodable localDEREncodable = paramSubjectPublicKeyInfo.getAlgorithmId().getParameters();
      DSAParameters localDSAParameters = null;
      if (localDEREncodable != null)
      {
        DSAParameter localDSAParameter = DSAParameter.getInstance(localDEREncodable.getDERObject());
        BigInteger localBigInteger2 = localDSAParameter.getP();
        BigInteger localBigInteger3 = localDSAParameter.getQ();
        BigInteger localBigInteger4 = localDSAParameter.getG();
        localDSAParameters = new DSAParameters(localBigInteger2, localBigInteger3, localBigInteger4);
      }
      return new DSAPublicKeyParameters(localDERInteger2.getValue(), localDSAParameters);
    }
    if (localAlgorithmIdentifier.getObjectId().equals(X9ObjectIdentifiers.id_ecPublicKey))
    {
      X962Parameters localX962Parameters = new X962Parameters((DERObject)paramSubjectPublicKeyInfo.getAlgorithmId().getParameters());
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
        byte[] arrayOfByte = paramSubjectPublicKeyInfo.getPublicKeyData().getBytes();
        DEROctetString localDEROctetString = new DEROctetString(arrayOfByte);
        X9ECPoint localX9ECPoint = new X9ECPoint(localECDomainParameters.getCurve(), localDEROctetString);
        return new ECPublicKeyParameters(localX9ECPoint.getPoint(), localECDomainParameters);
        localX9ECParameters1 = new X9ECParameters((ASN1Sequence)localX962Parameters.getParameters());
      }
    }
    throw new RuntimeException("algorithm identifier in key not recognised");
  }

  public static AsymmetricKeyParameter createKey(byte[] paramArrayOfByte)
    throws IOException
  {
    return createKey(SubjectPublicKeyInfo.getInstance(ASN1Object.fromByteArray(paramArrayOfByte)));
  }
}