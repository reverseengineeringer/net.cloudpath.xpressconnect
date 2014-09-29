package org.bouncycastle2.jce.provider;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.PBEParametersGenerator;
import org.bouncycastle2.crypto.digests.MD5Digest;
import org.bouncycastle2.crypto.digests.RIPEMD160Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle2.crypto.generators.PKCS5S1ParametersGenerator;
import org.bouncycastle2.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public abstract interface BrokenPBE
{
  public static final int MD5 = 0;
  public static final int OLD_PKCS12 = 3;
  public static final int PKCS12 = 2;
  public static final int PKCS5S1 = 0;
  public static final int PKCS5S2 = 1;
  public static final int RIPEMD160 = 2;
  public static final int SHA1 = 1;

  public static class Util
  {
    private static PBEParametersGenerator makePBEGenerator(int paramInt1, int paramInt2)
    {
      if (paramInt1 == 0)
      {
        switch (paramInt2)
        {
        default:
          throw new IllegalStateException("PKCS5 scheme 1 only supports only MD5 and SHA1.");
        case 0:
          return new PKCS5S1ParametersGenerator(new MD5Digest());
        case 1:
        }
        return new PKCS5S1ParametersGenerator(new SHA1Digest());
      }
      if (paramInt1 == 1)
        return new PKCS5S2ParametersGenerator();
      if (paramInt1 == 3)
      {
        switch (paramInt2)
        {
        default:
          throw new IllegalStateException("unknown digest scheme for PBE encryption.");
        case 0:
          return new OldPKCS12ParametersGenerator(new MD5Digest());
        case 1:
          return new OldPKCS12ParametersGenerator(new SHA1Digest());
        case 2:
        }
        return new OldPKCS12ParametersGenerator(new RIPEMD160Digest());
      }
      switch (paramInt2)
      {
      default:
        throw new IllegalStateException("unknown digest scheme for PBE encryption.");
      case 0:
        return new PKCS12ParametersGenerator(new MD5Digest());
      case 1:
        return new PKCS12ParametersGenerator(new SHA1Digest());
      case 2:
      }
      return new PKCS12ParametersGenerator(new RIPEMD160Digest());
    }

    static CipherParameters makePBEMacParameters(JCEPBEKey paramJCEPBEKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, int paramInt1, int paramInt2, int paramInt3)
    {
      if ((paramAlgorithmParameterSpec == null) || (!(paramAlgorithmParameterSpec instanceof PBEParameterSpec)))
        throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
      PBEParameterSpec localPBEParameterSpec = (PBEParameterSpec)paramAlgorithmParameterSpec;
      PBEParametersGenerator localPBEParametersGenerator = makePBEGenerator(paramInt1, paramInt2);
      byte[] arrayOfByte = paramJCEPBEKey.getEncoded();
      localPBEParametersGenerator.init(arrayOfByte, localPBEParameterSpec.getSalt(), localPBEParameterSpec.getIterationCount());
      CipherParameters localCipherParameters = localPBEParametersGenerator.generateDerivedMacParameters(paramInt3);
      for (int i = 0; ; i++)
      {
        if (i == arrayOfByte.length)
          return localCipherParameters;
        arrayOfByte[i] = 0;
      }
    }

    static CipherParameters makePBEParameters(JCEPBEKey paramJCEPBEKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4)
    {
      if ((paramAlgorithmParameterSpec == null) || (!(paramAlgorithmParameterSpec instanceof PBEParameterSpec)))
        throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
      PBEParameterSpec localPBEParameterSpec = (PBEParameterSpec)paramAlgorithmParameterSpec;
      PBEParametersGenerator localPBEParametersGenerator = makePBEGenerator(paramInt1, paramInt2);
      byte[] arrayOfByte = paramJCEPBEKey.getEncoded();
      localPBEParametersGenerator.init(arrayOfByte, localPBEParameterSpec.getSalt(), localPBEParameterSpec.getIterationCount());
      CipherParameters localCipherParameters;
      if (paramInt4 != 0)
      {
        localCipherParameters = localPBEParametersGenerator.generateDerivedParameters(paramInt3, paramInt4);
        if (paramString.startsWith("DES"))
        {
          if (!(localCipherParameters instanceof ParametersWithIV))
            break label134;
          setOddParity(((KeyParameter)((ParametersWithIV)localCipherParameters).getParameters()).getKey());
        }
      }
      label108: for (int i = 0; ; i++)
      {
        if (i == arrayOfByte.length)
        {
          return localCipherParameters;
          localCipherParameters = localPBEParametersGenerator.generateDerivedParameters(paramInt3);
          break;
          setOddParity(((KeyParameter)localCipherParameters).getKey());
          break label108;
        }
        arrayOfByte[i] = 0;
      }
    }

    private static void setOddParity(byte[] paramArrayOfByte)
    {
      for (int i = 0; ; i++)
      {
        if (i >= paramArrayOfByte.length)
          return;
        int j = paramArrayOfByte[i];
        paramArrayOfByte[i] = ((byte)(j & 0xFE | 0x1 ^ (j >> 1 ^ j >> 2 ^ j >> 3 ^ j >> 4 ^ j >> 5 ^ j >> 6 ^ j >> 7)));
      }
    }
  }
}