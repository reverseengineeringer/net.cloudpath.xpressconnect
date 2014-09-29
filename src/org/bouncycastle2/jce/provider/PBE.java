package org.bouncycastle2.jce.provider;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.PBEParametersGenerator;
import org.bouncycastle2.crypto.digests.MD2Digest;
import org.bouncycastle2.crypto.digests.MD5Digest;
import org.bouncycastle2.crypto.digests.RIPEMD160Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.digests.SHA256Digest;
import org.bouncycastle2.crypto.digests.TigerDigest;
import org.bouncycastle2.crypto.generators.OpenSSLPBEParametersGenerator;
import org.bouncycastle2.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle2.crypto.generators.PKCS5S1ParametersGenerator;
import org.bouncycastle2.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle2.crypto.params.DESParameters;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public abstract interface PBE
{
  public static final int MD2 = 5;
  public static final int MD5 = 0;
  public static final int OPENSSL = 3;
  public static final int PKCS12 = 2;
  public static final int PKCS5S1 = 0;
  public static final int PKCS5S2 = 1;
  public static final int RIPEMD160 = 2;
  public static final int SHA1 = 1;
  public static final int SHA256 = 4;
  public static final int TIGER = 3;

  public static class Util
  {
    private static PBEParametersGenerator makePBEGenerator(int paramInt1, int paramInt2)
    {
      if (paramInt1 == 0)
      {
        switch (paramInt2)
        {
        case 2:
        case 3:
        case 4:
        default:
          throw new IllegalStateException("PKCS5 scheme 1 only supports MD2, MD5 and SHA1.");
        case 5:
          return new PKCS5S1ParametersGenerator(new MD2Digest());
        case 0:
          return new PKCS5S1ParametersGenerator(new MD5Digest());
        case 1:
        }
        return new PKCS5S1ParametersGenerator(new SHA1Digest());
      }
      if (paramInt1 == 1)
        return new PKCS5S2ParametersGenerator();
      if (paramInt1 == 2)
      {
        switch (paramInt2)
        {
        default:
          throw new IllegalStateException("unknown digest scheme for PBE encryption.");
        case 5:
          return new PKCS12ParametersGenerator(new MD2Digest());
        case 0:
          return new PKCS12ParametersGenerator(new MD5Digest());
        case 1:
          return new PKCS12ParametersGenerator(new SHA1Digest());
        case 2:
          return new PKCS12ParametersGenerator(new RIPEMD160Digest());
        case 3:
          return new PKCS12ParametersGenerator(new TigerDigest());
        case 4:
        }
        return new PKCS12ParametersGenerator(new SHA256Digest());
      }
      return new OpenSSLPBEParametersGenerator();
    }

    static CipherParameters makePBEMacParameters(PBEKeySpec paramPBEKeySpec, int paramInt1, int paramInt2, int paramInt3)
    {
      PBEParametersGenerator localPBEParametersGenerator = makePBEGenerator(paramInt1, paramInt2);
      byte[] arrayOfByte;
      CipherParameters localCipherParameters;
      if (paramInt1 == 2)
      {
        arrayOfByte = PBEParametersGenerator.PKCS12PasswordToBytes(paramPBEKeySpec.getPassword());
        localPBEParametersGenerator.init(arrayOfByte, paramPBEKeySpec.getSalt(), paramPBEKeySpec.getIterationCount());
        localCipherParameters = localPBEParametersGenerator.generateDerivedMacParameters(paramInt3);
      }
      for (int i = 0; ; i++)
      {
        if (i == arrayOfByte.length)
        {
          return localCipherParameters;
          arrayOfByte = PBEParametersGenerator.PKCS5PasswordToBytes(paramPBEKeySpec.getPassword());
          break;
        }
        arrayOfByte[i] = 0;
      }
    }

    static CipherParameters makePBEMacParameters(JCEPBEKey paramJCEPBEKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
    {
      if ((paramAlgorithmParameterSpec == null) || (!(paramAlgorithmParameterSpec instanceof PBEParameterSpec)))
        throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
      PBEParameterSpec localPBEParameterSpec = (PBEParameterSpec)paramAlgorithmParameterSpec;
      PBEParametersGenerator localPBEParametersGenerator = makePBEGenerator(paramJCEPBEKey.getType(), paramJCEPBEKey.getDigest());
      byte[] arrayOfByte = paramJCEPBEKey.getEncoded();
      if (paramJCEPBEKey.shouldTryWrongPKCS12())
        arrayOfByte = new byte[2];
      localPBEParametersGenerator.init(arrayOfByte, localPBEParameterSpec.getSalt(), localPBEParameterSpec.getIterationCount());
      CipherParameters localCipherParameters = localPBEParametersGenerator.generateDerivedMacParameters(paramJCEPBEKey.getKeySize());
      for (int i = 0; ; i++)
      {
        if (i == arrayOfByte.length)
          return localCipherParameters;
        arrayOfByte[i] = 0;
      }
    }

    static CipherParameters makePBEParameters(PBEKeySpec paramPBEKeySpec, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      PBEParametersGenerator localPBEParametersGenerator = makePBEGenerator(paramInt1, paramInt2);
      byte[] arrayOfByte;
      CipherParameters localCipherParameters;
      if (paramInt1 == 2)
      {
        arrayOfByte = PBEParametersGenerator.PKCS12PasswordToBytes(paramPBEKeySpec.getPassword());
        localPBEParametersGenerator.init(arrayOfByte, paramPBEKeySpec.getSalt(), paramPBEKeySpec.getIterationCount());
        if (paramInt4 == 0)
          break label77;
        localCipherParameters = localPBEParametersGenerator.generateDerivedParameters(paramInt3, paramInt4);
      }
      label51: for (int i = 0; ; i++)
      {
        if (i == arrayOfByte.length)
        {
          return localCipherParameters;
          arrayOfByte = PBEParametersGenerator.PKCS5PasswordToBytes(paramPBEKeySpec.getPassword());
          break;
          label77: localCipherParameters = localPBEParametersGenerator.generateDerivedParameters(paramInt3);
          break label51;
        }
        arrayOfByte[i] = 0;
      }
    }

    static CipherParameters makePBEParameters(JCEPBEKey paramJCEPBEKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, String paramString)
    {
      if ((paramAlgorithmParameterSpec == null) || (!(paramAlgorithmParameterSpec instanceof PBEParameterSpec)))
        throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
      PBEParameterSpec localPBEParameterSpec = (PBEParameterSpec)paramAlgorithmParameterSpec;
      PBEParametersGenerator localPBEParametersGenerator = makePBEGenerator(paramJCEPBEKey.getType(), paramJCEPBEKey.getDigest());
      byte[] arrayOfByte = paramJCEPBEKey.getEncoded();
      if (paramJCEPBEKey.shouldTryWrongPKCS12())
        arrayOfByte = new byte[2];
      localPBEParametersGenerator.init(arrayOfByte, localPBEParameterSpec.getSalt(), localPBEParameterSpec.getIterationCount());
      CipherParameters localCipherParameters;
      if (paramJCEPBEKey.getIvSize() != 0)
      {
        localCipherParameters = localPBEParametersGenerator.generateDerivedParameters(paramJCEPBEKey.getKeySize(), paramJCEPBEKey.getIvSize());
        if (paramString.startsWith("DES"))
        {
          if (!(localCipherParameters instanceof ParametersWithIV))
            break label156;
          DESParameters.setOddParity(((KeyParameter)((ParametersWithIV)localCipherParameters).getParameters()).getKey());
        }
      }
      label128: for (int i = 0; ; i++)
      {
        if (i == arrayOfByte.length)
        {
          return localCipherParameters;
          localCipherParameters = localPBEParametersGenerator.generateDerivedParameters(paramJCEPBEKey.getKeySize());
          break;
          label156: DESParameters.setOddParity(((KeyParameter)localCipherParameters).getKey());
          break label128;
        }
        arrayOfByte[i] = 0;
      }
    }
  }
}