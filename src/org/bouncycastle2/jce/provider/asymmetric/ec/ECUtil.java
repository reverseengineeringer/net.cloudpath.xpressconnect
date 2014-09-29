package org.bouncycastle2.jce.provider.asymmetric.ec;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle2.asn1.nist.NISTNamedCurves;
import org.bouncycastle2.asn1.sec.SECNamedCurves;
import org.bouncycastle2.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle2.asn1.x9.X962NamedCurves;
import org.bouncycastle2.asn1.x9.X9ECParameters;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ECPublicKeyParameters;
import org.bouncycastle2.jce.interfaces.ECPrivateKey;
import org.bouncycastle2.jce.provider.JCEECPublicKey;
import org.bouncycastle2.jce.provider.ProviderUtil;
import org.bouncycastle2.jce.spec.ECParameterSpec;

public class ECUtil
{
  static int[] convertMidTerms(int[] paramArrayOfInt)
  {
    int[] arrayOfInt = new int[3];
    if (paramArrayOfInt.length == 1)
    {
      arrayOfInt[0] = paramArrayOfInt[0];
      return arrayOfInt;
    }
    if (paramArrayOfInt.length != 3)
      throw new IllegalArgumentException("Only Trinomials and pentanomials supported");
    if ((paramArrayOfInt[0] < paramArrayOfInt[1]) && (paramArrayOfInt[0] < paramArrayOfInt[2]))
    {
      arrayOfInt[0] = paramArrayOfInt[0];
      if (paramArrayOfInt[1] < paramArrayOfInt[2])
      {
        arrayOfInt[1] = paramArrayOfInt[1];
        arrayOfInt[2] = paramArrayOfInt[2];
        return arrayOfInt;
      }
      arrayOfInt[1] = paramArrayOfInt[2];
      arrayOfInt[2] = paramArrayOfInt[1];
      return arrayOfInt;
    }
    if (paramArrayOfInt[1] < paramArrayOfInt[2])
    {
      arrayOfInt[0] = paramArrayOfInt[1];
      if (paramArrayOfInt[0] < paramArrayOfInt[2])
      {
        arrayOfInt[1] = paramArrayOfInt[0];
        arrayOfInt[2] = paramArrayOfInt[2];
        return arrayOfInt;
      }
      arrayOfInt[1] = paramArrayOfInt[2];
      arrayOfInt[2] = paramArrayOfInt[0];
      return arrayOfInt;
    }
    arrayOfInt[0] = paramArrayOfInt[2];
    if (paramArrayOfInt[0] < paramArrayOfInt[1])
    {
      arrayOfInt[1] = paramArrayOfInt[0];
      arrayOfInt[2] = paramArrayOfInt[1];
      return arrayOfInt;
    }
    arrayOfInt[1] = paramArrayOfInt[1];
    arrayOfInt[2] = paramArrayOfInt[0];
    return arrayOfInt;
  }

  public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    if ((paramPrivateKey instanceof ECPrivateKey))
    {
      ECPrivateKey localECPrivateKey = (ECPrivateKey)paramPrivateKey;
      ECParameterSpec localECParameterSpec = localECPrivateKey.getParameters();
      if (localECParameterSpec == null)
        localECParameterSpec = ProviderUtil.getEcImplicitlyCa();
      return new ECPrivateKeyParameters(localECPrivateKey.getD(), new ECDomainParameters(localECParameterSpec.getCurve(), localECParameterSpec.getG(), localECParameterSpec.getN(), localECParameterSpec.getH(), localECParameterSpec.getSeed()));
    }
    throw new InvalidKeyException("can't identify EC private key.");
  }

  public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    if ((paramPublicKey instanceof org.bouncycastle2.jce.interfaces.ECPublicKey))
    {
      org.bouncycastle2.jce.interfaces.ECPublicKey localECPublicKey1 = (org.bouncycastle2.jce.interfaces.ECPublicKey)paramPublicKey;
      ECParameterSpec localECParameterSpec2 = localECPublicKey1.getParameters();
      if (localECParameterSpec2 == null)
      {
        ECParameterSpec localECParameterSpec3 = ProviderUtil.getEcImplicitlyCa();
        return new ECPublicKeyParameters(((JCEECPublicKey)localECPublicKey1).engineGetQ(), new ECDomainParameters(localECParameterSpec3.getCurve(), localECParameterSpec3.getG(), localECParameterSpec3.getN(), localECParameterSpec3.getH(), localECParameterSpec3.getSeed()));
      }
      return new ECPublicKeyParameters(localECPublicKey1.getQ(), new ECDomainParameters(localECParameterSpec2.getCurve(), localECParameterSpec2.getG(), localECParameterSpec2.getN(), localECParameterSpec2.getH(), localECParameterSpec2.getSeed()));
    }
    if ((paramPublicKey instanceof java.security.interfaces.ECPublicKey))
    {
      java.security.interfaces.ECPublicKey localECPublicKey = (java.security.interfaces.ECPublicKey)paramPublicKey;
      ECParameterSpec localECParameterSpec1 = EC5Util.convertSpec(localECPublicKey.getParams(), false);
      return new ECPublicKeyParameters(EC5Util.convertPoint(localECPublicKey.getParams(), localECPublicKey.getW(), false), new ECDomainParameters(localECParameterSpec1.getCurve(), localECParameterSpec1.getG(), localECParameterSpec1.getN(), localECParameterSpec1.getH(), localECParameterSpec1.getSeed()));
    }
    throw new InvalidKeyException("cannot identify EC public key.");
  }

  public static String getCurveName(DERObjectIdentifier paramDERObjectIdentifier)
  {
    String str = X962NamedCurves.getName(paramDERObjectIdentifier);
    if (str == null)
    {
      str = SECNamedCurves.getName(paramDERObjectIdentifier);
      if (str == null)
        str = NISTNamedCurves.getName(paramDERObjectIdentifier);
      if (str == null)
        str = TeleTrusTNamedCurves.getName(paramDERObjectIdentifier);
      if (str == null)
        str = ECGOST3410NamedCurves.getName(paramDERObjectIdentifier);
    }
    return str;
  }

  public static X9ECParameters getNamedCurveByOid(DERObjectIdentifier paramDERObjectIdentifier)
  {
    X9ECParameters localX9ECParameters = X962NamedCurves.getByOID(paramDERObjectIdentifier);
    if (localX9ECParameters == null)
    {
      localX9ECParameters = SECNamedCurves.getByOID(paramDERObjectIdentifier);
      if (localX9ECParameters == null)
        localX9ECParameters = NISTNamedCurves.getByOID(paramDERObjectIdentifier);
      if (localX9ECParameters == null)
        localX9ECParameters = TeleTrusTNamedCurves.getByOID(paramDERObjectIdentifier);
    }
    return localX9ECParameters;
  }

  public static DERObjectIdentifier getNamedCurveOid(String paramString)
  {
    DERObjectIdentifier localDERObjectIdentifier = X962NamedCurves.getOID(paramString);
    if (localDERObjectIdentifier == null)
    {
      localDERObjectIdentifier = SECNamedCurves.getOID(paramString);
      if (localDERObjectIdentifier == null)
        localDERObjectIdentifier = NISTNamedCurves.getOID(paramString);
      if (localDERObjectIdentifier == null)
        localDERObjectIdentifier = TeleTrusTNamedCurves.getOID(paramString);
      if (localDERObjectIdentifier == null)
        localDERObjectIdentifier = ECGOST3410NamedCurves.getOID(paramString);
    }
    return localDERObjectIdentifier;
  }
}