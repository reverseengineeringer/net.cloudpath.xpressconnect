package org.bouncycastle2.openssl;

import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.crypto.PBEParametersGenerator;
import org.bouncycastle2.crypto.generators.OpenSSLPBEParametersGenerator;
import org.bouncycastle2.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle2.crypto.params.KeyParameter;

final class PEMUtilities
{
  private static final Map KEYSIZES = new HashMap();
  private static final Set PKCS5_SCHEME_1 = new HashSet();
  private static final Set PKCS5_SCHEME_2 = new HashSet();

  static
  {
    PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithMD2AndDES_CBC);
    PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC);
    PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithMD5AndDES_CBC);
    PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC);
    PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithSHA1AndDES_CBC);
    PKCS5_SCHEME_1.add(PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC);
    PKCS5_SCHEME_2.add(PKCSObjectIdentifiers.id_PBES2);
    PKCS5_SCHEME_2.add(PKCSObjectIdentifiers.des_EDE3_CBC);
    PKCS5_SCHEME_2.add(NISTObjectIdentifiers.id_aes128_CBC);
    PKCS5_SCHEME_2.add(NISTObjectIdentifiers.id_aes192_CBC);
    PKCS5_SCHEME_2.add(NISTObjectIdentifiers.id_aes256_CBC);
    KEYSIZES.put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), new Integer(192));
    KEYSIZES.put(NISTObjectIdentifiers.id_aes128_CBC.getId(), new Integer(128));
    KEYSIZES.put(NISTObjectIdentifiers.id_aes192_CBC.getId(), new Integer(192));
    KEYSIZES.put(NISTObjectIdentifiers.id_aes256_CBC.getId(), new Integer(256));
  }

  static byte[] crypt(boolean paramBoolean, String paramString1, byte[] paramArrayOfByte1, char[] paramArrayOfChar, String paramString2, byte[] paramArrayOfByte2)
    throws IOException
  {
    Provider localProvider = null;
    if (paramString1 != null)
    {
      localProvider = Security.getProvider(paramString1);
      if (localProvider == null)
        throw new EncryptionException("cannot find provider: " + paramString1);
    }
    return crypt(paramBoolean, localProvider, paramArrayOfByte1, paramArrayOfChar, paramString2, paramArrayOfByte2);
  }

  static byte[] crypt(boolean paramBoolean, Provider paramProvider, byte[] paramArrayOfByte1, char[] paramArrayOfChar, String paramString, byte[] paramArrayOfByte2)
    throws IOException
  {
    Object localObject = new IvParameterSpec(paramArrayOfByte2);
    String str1 = "CBC";
    String str2 = "PKCS5Padding";
    if (paramString.endsWith("-CFB"))
    {
      str1 = "CFB";
      str2 = "NoPadding";
    }
    if ((paramString.endsWith("-ECB")) || ("DES-EDE".equals(paramString)) || ("DES-EDE3".equals(paramString)))
    {
      str1 = "ECB";
      localObject = null;
    }
    if (paramString.endsWith("-OFB"))
    {
      str1 = "OFB";
      str2 = "NoPadding";
    }
    String str3;
    boolean bool;
    if (paramString.startsWith("DES-EDE"))
    {
      str3 = "DESede";
      if (paramString.startsWith("DES-EDE3"))
        bool = false;
    }
    while (true)
    {
      SecretKey localSecretKey = getKey(paramArrayOfChar, str3, 24, paramArrayOfByte2, bool);
      label133: String str4 = str3 + "/" + str1 + "/" + str2;
      try
      {
        Cipher localCipher = Cipher.getInstance(str4, paramProvider);
        int j;
        if (paramBoolean)
        {
          j = 1;
          label185: if (localObject != null)
            break label512;
          localCipher.init(j, localSecretKey);
        }
        while (true)
        {
          byte[] arrayOfByte2 = localCipher.doFinal(paramArrayOfByte1);
          return arrayOfByte2;
          bool = true;
          break;
          if (paramString.startsWith("DES-"))
          {
            str3 = "DES";
            localSecretKey = getKey(paramArrayOfChar, str3, 8, paramArrayOfByte2);
            break label133;
          }
          if (paramString.startsWith("BF-"))
          {
            str3 = "Blowfish";
            localSecretKey = getKey(paramArrayOfChar, str3, 16, paramArrayOfByte2);
            break label133;
          }
          if (paramString.startsWith("RC2-"))
          {
            str3 = "RC2";
            int k = 128;
            if (paramString.startsWith("RC2-40-"))
              k = 40;
            while (true)
            {
              localSecretKey = getKey(paramArrayOfChar, str3, k / 8, paramArrayOfByte2);
              if (localObject != null)
                break label358;
              localObject = new RC2ParameterSpec(k);
              break;
              if (paramString.startsWith("RC2-64-"))
                k = 64;
            }
            label358: localObject = new RC2ParameterSpec(k, paramArrayOfByte2);
            break label133;
          }
          if (paramString.startsWith("AES-"))
          {
            str3 = "AES";
            byte[] arrayOfByte1 = paramArrayOfByte2;
            if (arrayOfByte1.length > 8)
            {
              arrayOfByte1 = new byte[8];
              System.arraycopy(paramArrayOfByte2, 0, arrayOfByte1, 0, 8);
            }
            int i;
            if (paramString.startsWith("AES-128-"))
              i = 128;
            while (true)
            {
              localSecretKey = getKey(paramArrayOfChar, "AES", i / 8, arrayOfByte1);
              break;
              if (paramString.startsWith("AES-192-"))
              {
                i = 192;
              }
              else
              {
                if (!paramString.startsWith("AES-256-"))
                  break label486;
                i = 256;
              }
            }
            label486: throw new EncryptionException("unknown AES encryption with private key");
          }
          throw new EncryptionException("unknown encryption with private key");
          j = 2;
          break label185;
          label512: localCipher.init(j, localSecretKey, (AlgorithmParameterSpec)localObject);
        }
      }
      catch (Exception localException)
      {
        throw new EncryptionException("exception using cipher - please check password and data.", localException);
      }
    }
  }

  static SecretKey generateSecretKeyForPKCS5Scheme2(String paramString, char[] paramArrayOfChar, byte[] paramArrayOfByte, int paramInt)
  {
    PKCS5S2ParametersGenerator localPKCS5S2ParametersGenerator = new PKCS5S2ParametersGenerator();
    localPKCS5S2ParametersGenerator.init(PBEParametersGenerator.PKCS5PasswordToBytes(paramArrayOfChar), paramArrayOfByte, paramInt);
    return new SecretKeySpec(((KeyParameter)localPKCS5S2ParametersGenerator.generateDerivedParameters(getKeySize(paramString))).getKey(), paramString);
  }

  private static SecretKey getKey(char[] paramArrayOfChar, String paramString, int paramInt, byte[] paramArrayOfByte)
  {
    return getKey(paramArrayOfChar, paramString, paramInt, paramArrayOfByte, false);
  }

  private static SecretKey getKey(char[] paramArrayOfChar, String paramString, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    OpenSSLPBEParametersGenerator localOpenSSLPBEParametersGenerator = new OpenSSLPBEParametersGenerator();
    localOpenSSLPBEParametersGenerator.init(PBEParametersGenerator.PKCS5PasswordToBytes(paramArrayOfChar), paramArrayOfByte);
    byte[] arrayOfByte = ((KeyParameter)localOpenSSLPBEParametersGenerator.generateDerivedParameters(paramInt * 8)).getKey();
    if ((paramBoolean) && (arrayOfByte.length >= 24))
      System.arraycopy(arrayOfByte, 0, arrayOfByte, 16, 8);
    return new SecretKeySpec(arrayOfByte, paramString);
  }

  static int getKeySize(String paramString)
  {
    if (!KEYSIZES.containsKey(paramString))
      throw new IllegalStateException("no key size for algorithm: " + paramString);
    return ((Integer)KEYSIZES.get(paramString)).intValue();
  }

  static boolean isPKCS12(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return paramDERObjectIdentifier.getId().startsWith(PKCSObjectIdentifiers.pkcs_12PbeIds.getId());
  }

  static boolean isPKCS5Scheme1(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return PKCS5_SCHEME_1.contains(paramDERObjectIdentifier);
  }

  static boolean isPKCS5Scheme2(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return PKCS5_SCHEME_2.contains(paramDERObjectIdentifier);
  }
}