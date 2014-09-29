package org.bouncycastle2.jce.provider;

import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.PBEParametersGenerator;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class JCEPBEKey
  implements PBEKey
{
  String algorithm;
  int digest;
  int ivSize;
  int keySize;
  DERObjectIdentifier oid;
  CipherParameters param;
  PBEKeySpec pbeKeySpec;
  boolean tryWrong = false;
  int type;

  public JCEPBEKey(String paramString, DERObjectIdentifier paramDERObjectIdentifier, int paramInt1, int paramInt2, int paramInt3, int paramInt4, PBEKeySpec paramPBEKeySpec, CipherParameters paramCipherParameters)
  {
    this.algorithm = paramString;
    this.oid = paramDERObjectIdentifier;
    this.type = paramInt1;
    this.digest = paramInt2;
    this.keySize = paramInt3;
    this.ivSize = paramInt4;
    this.pbeKeySpec = paramPBEKeySpec;
    this.param = paramCipherParameters;
  }

  public String getAlgorithm()
  {
    return this.algorithm;
  }

  int getDigest()
  {
    return this.digest;
  }

  public byte[] getEncoded()
  {
    if (this.param != null)
    {
      if ((this.param instanceof ParametersWithIV));
      for (KeyParameter localKeyParameter = (KeyParameter)((ParametersWithIV)this.param).getParameters(); ; localKeyParameter = (KeyParameter)this.param)
        return localKeyParameter.getKey();
    }
    if (this.type == 2)
      return PBEParametersGenerator.PKCS12PasswordToBytes(this.pbeKeySpec.getPassword());
    return PBEParametersGenerator.PKCS5PasswordToBytes(this.pbeKeySpec.getPassword());
  }

  public String getFormat()
  {
    return "RAW";
  }

  public int getIterationCount()
  {
    return this.pbeKeySpec.getIterationCount();
  }

  int getIvSize()
  {
    return this.ivSize;
  }

  int getKeySize()
  {
    return this.keySize;
  }

  public DERObjectIdentifier getOID()
  {
    return this.oid;
  }

  CipherParameters getParam()
  {
    return this.param;
  }

  public char[] getPassword()
  {
    return this.pbeKeySpec.getPassword();
  }

  public byte[] getSalt()
  {
    return this.pbeKeySpec.getSalt();
  }

  int getType()
  {
    return this.type;
  }

  void setTryWrongPKCS12Zero(boolean paramBoolean)
  {
    this.tryWrong = paramBoolean;
  }

  boolean shouldTryWrongPKCS12()
  {
    return this.tryWrong;
  }
}