package org.bouncycastle2.jce.spec;

import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.asn1.cryptopro.GOST3410NamedParameters;
import org.bouncycastle2.asn1.cryptopro.GOST3410ParamSetParameters;
import org.bouncycastle2.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle2.jce.interfaces.GOST3410Params;

public class GOST3410ParameterSpec
  implements AlgorithmParameterSpec, GOST3410Params
{
  private String digestParamSetOID;
  private String encryptionParamSetOID;
  private String keyParamSetOID;
  private GOST3410PublicKeyParameterSetSpec keyParameters;

  public GOST3410ParameterSpec(String paramString)
  {
    this(paramString, CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet.getId(), null);
  }

  public GOST3410ParameterSpec(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, null);
  }

  public GOST3410ParameterSpec(String paramString1, String paramString2, String paramString3)
  {
    try
    {
      GOST3410ParamSetParameters localGOST3410ParamSetParameters2 = GOST3410NamedParameters.getByOID(new DERObjectIdentifier(paramString1));
      localGOST3410ParamSetParameters1 = localGOST3410ParamSetParameters2;
      if (localGOST3410ParamSetParameters1 == null)
        throw new IllegalArgumentException("no key parameter set for passed in name/OID.");
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      GOST3410ParamSetParameters localGOST3410ParamSetParameters1;
      while (true)
      {
        DERObjectIdentifier localDERObjectIdentifier = GOST3410NamedParameters.getOID(paramString1);
        localGOST3410ParamSetParameters1 = null;
        if (localDERObjectIdentifier != null)
        {
          paramString1 = localDERObjectIdentifier.getId();
          localGOST3410ParamSetParameters1 = GOST3410NamedParameters.getByOID(localDERObjectIdentifier);
        }
      }
      this.keyParameters = new GOST3410PublicKeyParameterSetSpec(localGOST3410ParamSetParameters1.getP(), localGOST3410ParamSetParameters1.getQ(), localGOST3410ParamSetParameters1.getA());
      this.keyParamSetOID = paramString1;
      this.digestParamSetOID = paramString2;
      this.encryptionParamSetOID = paramString3;
    }
  }

  public GOST3410ParameterSpec(GOST3410PublicKeyParameterSetSpec paramGOST3410PublicKeyParameterSetSpec)
  {
    this.keyParameters = paramGOST3410PublicKeyParameterSetSpec;
    this.digestParamSetOID = CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet.getId();
    this.encryptionParamSetOID = null;
  }

  public static GOST3410ParameterSpec fromPublicKeyAlg(GOST3410PublicKeyAlgParameters paramGOST3410PublicKeyAlgParameters)
  {
    if (paramGOST3410PublicKeyAlgParameters.getEncryptionParamSet() != null)
      return new GOST3410ParameterSpec(paramGOST3410PublicKeyAlgParameters.getPublicKeyParamSet().getId(), paramGOST3410PublicKeyAlgParameters.getDigestParamSet().getId(), paramGOST3410PublicKeyAlgParameters.getEncryptionParamSet().getId());
    return new GOST3410ParameterSpec(paramGOST3410PublicKeyAlgParameters.getPublicKeyParamSet().getId(), paramGOST3410PublicKeyAlgParameters.getDigestParamSet().getId());
  }

  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof GOST3410ParameterSpec;
    boolean bool2 = false;
    if (bool1)
    {
      GOST3410ParameterSpec localGOST3410ParameterSpec = (GOST3410ParameterSpec)paramObject;
      boolean bool3 = this.keyParameters.equals(localGOST3410ParameterSpec.keyParameters);
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = this.digestParamSetOID.equals(localGOST3410ParameterSpec.digestParamSetOID);
        bool2 = false;
        if (bool4)
          if (this.encryptionParamSetOID != localGOST3410ParameterSpec.encryptionParamSetOID)
          {
            String str = this.encryptionParamSetOID;
            bool2 = false;
            if (str != null)
            {
              boolean bool5 = this.encryptionParamSetOID.equals(localGOST3410ParameterSpec.encryptionParamSetOID);
              bool2 = false;
              if (!bool5);
            }
          }
          else
          {
            bool2 = true;
          }
      }
    }
    return bool2;
  }

  public String getDigestParamSetOID()
  {
    return this.digestParamSetOID;
  }

  public String getEncryptionParamSetOID()
  {
    return this.encryptionParamSetOID;
  }

  public String getPublicKeyParamSetOID()
  {
    return this.keyParamSetOID;
  }

  public GOST3410PublicKeyParameterSetSpec getPublicKeyParameters()
  {
    return this.keyParameters;
  }

  public int hashCode()
  {
    int i = this.keyParameters.hashCode() ^ this.digestParamSetOID.hashCode();
    if (this.encryptionParamSetOID != null);
    for (int j = this.encryptionParamSetOID.hashCode(); ; j = 0)
      return j ^ i;
  }
}