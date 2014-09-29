package org.bouncycastle2.jce.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.PBEParametersGenerator;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle2.crypto.io.DigestInputStream;
import org.bouncycastle2.crypto.io.DigestOutputStream;
import org.bouncycastle2.crypto.io.MacInputStream;
import org.bouncycastle2.crypto.io.MacOutputStream;
import org.bouncycastle2.crypto.macs.HMac;
import org.bouncycastle2.jce.interfaces.BCKeyStore;
import org.bouncycastle2.util.Arrays;
import org.bouncycastle2.util.io.Streams;

public class JDKKeyStore extends KeyStoreSpi
  implements BCKeyStore
{
  static final int CERTIFICATE = 1;
  static final int KEY = 2;
  private static final String KEY_CIPHER = "PBEWithSHAAnd3-KeyTripleDES-CBC";
  static final int KEY_PRIVATE = 0;
  static final int KEY_PUBLIC = 1;
  private static final int KEY_SALT_SIZE = 20;
  static final int KEY_SECRET = 2;
  private static final int MIN_ITERATIONS = 1024;
  static final int NULL = 0;
  static final int SEALED = 4;
  static final int SECRET = 3;
  private static final String STORE_CIPHER = "PBEWithSHAAndTwofish-CBC";
  private static final int STORE_SALT_SIZE = 20;
  private static final int STORE_VERSION = 1;
  protected SecureRandom random = new SecureRandom();
  protected Hashtable table = new Hashtable();

  private Certificate decodeCertificate(DataInputStream paramDataInputStream)
    throws IOException
  {
    String str = paramDataInputStream.readUTF();
    byte[] arrayOfByte = new byte[paramDataInputStream.readInt()];
    paramDataInputStream.readFully(arrayOfByte);
    try
    {
      Certificate localCertificate = CertificateFactory.getInstance(str, BouncyCastleProvider.PROVIDER_NAME).generateCertificate(new ByteArrayInputStream(arrayOfByte));
      return localCertificate;
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
      throw new IOException(localNoSuchProviderException.toString());
    }
    catch (CertificateException localCertificateException)
    {
      throw new IOException(localCertificateException.toString());
    }
  }

  private Key decodeKey(DataInputStream paramDataInputStream)
    throws IOException
  {
    int i = paramDataInputStream.read();
    String str1 = paramDataInputStream.readUTF();
    String str2 = paramDataInputStream.readUTF();
    byte[] arrayOfByte = new byte[paramDataInputStream.readInt()];
    paramDataInputStream.readFully(arrayOfByte);
    if ((str1.equals("PKCS#8")) || (str1.equals("PKCS8")));
    for (Object localObject = new PKCS8EncodedKeySpec(arrayOfByte); ; localObject = new X509EncodedKeySpec(arrayOfByte))
      switch (i)
      {
      default:
        try
        {
          throw new IOException("Key type " + i + " not recognised!");
        }
        catch (Exception localException)
        {
          throw new IOException("Exception creating key: " + localException.toString());
        }
        if ((!str1.equals("X.509")) && (!str1.equals("X509")))
          break label179;
      case 0:
      case 1:
      case 2:
      }
    label179: if (str1.equals("RAW"))
      return new SecretKeySpec(arrayOfByte, str2);
    throw new IOException("Key format " + str1 + " not recognised!");
    return KeyFactory.getInstance(str2, BouncyCastleProvider.PROVIDER_NAME).generatePrivate((KeySpec)localObject);
    return KeyFactory.getInstance(str2, BouncyCastleProvider.PROVIDER_NAME).generatePublic((KeySpec)localObject);
    SecretKey localSecretKey = SecretKeyFactory.getInstance(str2, BouncyCastleProvider.PROVIDER_NAME).generateSecret((KeySpec)localObject);
    return localSecretKey;
  }

  private void encodeCertificate(Certificate paramCertificate, DataOutputStream paramDataOutputStream)
    throws IOException
  {
    try
    {
      byte[] arrayOfByte = paramCertificate.getEncoded();
      paramDataOutputStream.writeUTF(paramCertificate.getType());
      paramDataOutputStream.writeInt(arrayOfByte.length);
      paramDataOutputStream.write(arrayOfByte);
      return;
    }
    catch (CertificateEncodingException localCertificateEncodingException)
    {
      throw new IOException(localCertificateEncodingException.toString());
    }
  }

  private void encodeKey(Key paramKey, DataOutputStream paramDataOutputStream)
    throws IOException
  {
    byte[] arrayOfByte = paramKey.getEncoded();
    if ((paramKey instanceof PrivateKey))
      paramDataOutputStream.write(0);
    while (true)
    {
      paramDataOutputStream.writeUTF(paramKey.getFormat());
      paramDataOutputStream.writeUTF(paramKey.getAlgorithm());
      paramDataOutputStream.writeInt(arrayOfByte.length);
      paramDataOutputStream.write(arrayOfByte);
      return;
      if ((paramKey instanceof PublicKey))
        paramDataOutputStream.write(1);
      else
        paramDataOutputStream.write(2);
    }
  }

  public Enumeration engineAliases()
  {
    return this.table.keys();
  }

  public boolean engineContainsAlias(String paramString)
  {
    return this.table.get(paramString) != null;
  }

  public void engineDeleteEntry(String paramString)
    throws KeyStoreException
  {
    if (this.table.get(paramString) == null)
      throw new KeyStoreException("no such entry as " + paramString);
    this.table.remove(paramString);
  }

  public Certificate engineGetCertificate(String paramString)
  {
    StoreEntry localStoreEntry = (StoreEntry)this.table.get(paramString);
    if (localStoreEntry != null)
    {
      if (localStoreEntry.getType() == 1)
        return (Certificate)localStoreEntry.getObject();
      Certificate[] arrayOfCertificate = localStoreEntry.getCertificateChain();
      if (arrayOfCertificate != null)
        return arrayOfCertificate[0];
    }
    return null;
  }

  public String engineGetCertificateAlias(Certificate paramCertificate)
  {
    Enumeration localEnumeration = this.table.elements();
    StoreEntry localStoreEntry;
    Certificate[] arrayOfCertificate;
    do
    {
      do
      {
        if (!localEnumeration.hasMoreElements())
          return null;
        localStoreEntry = (StoreEntry)localEnumeration.nextElement();
        if (!(localStoreEntry.getObject() instanceof Certificate))
          break;
      }
      while (!((Certificate)localStoreEntry.getObject()).equals(paramCertificate));
      return localStoreEntry.getAlias();
      arrayOfCertificate = localStoreEntry.getCertificateChain();
    }
    while ((arrayOfCertificate == null) || (!arrayOfCertificate[0].equals(paramCertificate)));
    return localStoreEntry.getAlias();
  }

  public Certificate[] engineGetCertificateChain(String paramString)
  {
    StoreEntry localStoreEntry = (StoreEntry)this.table.get(paramString);
    if (localStoreEntry != null)
      return localStoreEntry.getCertificateChain();
    return null;
  }

  public Date engineGetCreationDate(String paramString)
  {
    StoreEntry localStoreEntry = (StoreEntry)this.table.get(paramString);
    if (localStoreEntry != null)
      return localStoreEntry.getDate();
    return null;
  }

  public Key engineGetKey(String paramString, char[] paramArrayOfChar)
    throws NoSuchAlgorithmException, UnrecoverableKeyException
  {
    StoreEntry localStoreEntry = (StoreEntry)this.table.get(paramString);
    if ((localStoreEntry == null) || (localStoreEntry.getType() == 1))
      return null;
    return (Key)localStoreEntry.getObject(paramArrayOfChar);
  }

  public boolean engineIsCertificateEntry(String paramString)
  {
    StoreEntry localStoreEntry = (StoreEntry)this.table.get(paramString);
    return (localStoreEntry != null) && (localStoreEntry.getType() == 1);
  }

  public boolean engineIsKeyEntry(String paramString)
  {
    StoreEntry localStoreEntry = (StoreEntry)this.table.get(paramString);
    return (localStoreEntry != null) && (localStoreEntry.getType() != 1);
  }

  public void engineLoad(InputStream paramInputStream, char[] paramArrayOfChar)
    throws IOException
  {
    this.table.clear();
    if (paramInputStream == null);
    DataInputStream localDataInputStream;
    HMac localHMac;
    byte[] arrayOfByte3;
    byte[] arrayOfByte4;
    do
    {
      return;
      localDataInputStream = new DataInputStream(paramInputStream);
      int i = localDataInputStream.readInt();
      if ((i != 1) && (i != 0))
        throw new IOException("Wrong version of key store.");
      byte[] arrayOfByte1 = new byte[localDataInputStream.readInt()];
      localDataInputStream.readFully(arrayOfByte1);
      int j = localDataInputStream.readInt();
      localHMac = new HMac(new SHA1Digest());
      if ((paramArrayOfChar == null) || (paramArrayOfChar.length == 0))
        break;
      byte[] arrayOfByte2 = PBEParametersGenerator.PKCS12PasswordToBytes(paramArrayOfChar);
      PKCS12ParametersGenerator localPKCS12ParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
      localPKCS12ParametersGenerator.init(arrayOfByte2, arrayOfByte1, j);
      CipherParameters localCipherParameters = localPKCS12ParametersGenerator.generateDerivedMacParameters(localHMac.getMacSize());
      Arrays.fill(arrayOfByte2, (byte)0);
      localHMac.init(localCipherParameters);
      loadStore(new MacInputStream(localDataInputStream, localHMac));
      arrayOfByte3 = new byte[localHMac.getMacSize()];
      localHMac.doFinal(arrayOfByte3, 0);
      arrayOfByte4 = new byte[localHMac.getMacSize()];
      localDataInputStream.readFully(arrayOfByte4);
    }
    while (Arrays.constantTimeAreEqual(arrayOfByte3, arrayOfByte4));
    this.table.clear();
    throw new IOException("KeyStore integrity check failed.");
    loadStore(localDataInputStream);
    localDataInputStream.readFully(new byte[localHMac.getMacSize()]);
  }

  public void engineSetCertificateEntry(String paramString, Certificate paramCertificate)
    throws KeyStoreException
  {
    StoreEntry localStoreEntry = (StoreEntry)this.table.get(paramString);
    if ((localStoreEntry != null) && (localStoreEntry.getType() != 1))
      throw new KeyStoreException("key store already has a key entry with alias " + paramString);
    this.table.put(paramString, new StoreEntry(paramString, paramCertificate));
  }

  public void engineSetKeyEntry(String paramString, Key paramKey, char[] paramArrayOfChar, Certificate[] paramArrayOfCertificate)
    throws KeyStoreException
  {
    if (((paramKey instanceof PrivateKey)) && (paramArrayOfCertificate == null))
      throw new KeyStoreException("no certificate chain for private key");
    try
    {
      this.table.put(paramString, new StoreEntry(paramString, paramKey, paramArrayOfChar, paramArrayOfCertificate));
      return;
    }
    catch (Exception localException)
    {
      throw new KeyStoreException(localException.toString());
    }
  }

  public void engineSetKeyEntry(String paramString, byte[] paramArrayOfByte, Certificate[] paramArrayOfCertificate)
    throws KeyStoreException
  {
    this.table.put(paramString, new StoreEntry(paramString, paramArrayOfByte, paramArrayOfCertificate));
  }

  public int engineSize()
  {
    return this.table.size();
  }

  public void engineStore(OutputStream paramOutputStream, char[] paramArrayOfChar)
    throws IOException
  {
    DataOutputStream localDataOutputStream = new DataOutputStream(paramOutputStream);
    byte[] arrayOfByte1 = new byte[20];
    int i = 1024 + (0x3FF & this.random.nextInt());
    this.random.nextBytes(arrayOfByte1);
    localDataOutputStream.writeInt(1);
    localDataOutputStream.writeInt(arrayOfByte1.length);
    localDataOutputStream.write(arrayOfByte1);
    localDataOutputStream.writeInt(i);
    HMac localHMac = new HMac(new SHA1Digest());
    MacOutputStream localMacOutputStream = new MacOutputStream(localDataOutputStream, localHMac);
    PKCS12ParametersGenerator localPKCS12ParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
    byte[] arrayOfByte2 = PBEParametersGenerator.PKCS12PasswordToBytes(paramArrayOfChar);
    localPKCS12ParametersGenerator.init(arrayOfByte2, arrayOfByte1, i);
    localHMac.init(localPKCS12ParametersGenerator.generateDerivedMacParameters(localHMac.getMacSize()));
    for (int j = 0; ; j++)
    {
      if (j == arrayOfByte2.length)
      {
        saveStore(localMacOutputStream);
        byte[] arrayOfByte3 = new byte[localHMac.getMacSize()];
        localHMac.doFinal(arrayOfByte3, 0);
        localDataOutputStream.write(arrayOfByte3);
        localDataOutputStream.close();
        return;
      }
      arrayOfByte2[j] = 0;
    }
  }

  protected void loadStore(InputStream paramInputStream)
    throws IOException
  {
    DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
    int i = localDataInputStream.read();
    if (i <= 0)
      return;
    String str = localDataInputStream.readUTF();
    Date localDate = new Date(localDataInputStream.readLong());
    int j = localDataInputStream.readInt();
    Certificate[] arrayOfCertificate = (Certificate[])null;
    if (j != 0)
      arrayOfCertificate = new Certificate[j];
    for (int k = 0; ; k++)
    {
      if (k == j);
      switch (i)
      {
      default:
        throw new RuntimeException("Unknown object type in store.");
        arrayOfCertificate[k] = decodeCertificate(localDataInputStream);
      case 1:
      case 2:
      case 3:
      case 4:
      }
    }
    Certificate localCertificate = decodeCertificate(localDataInputStream);
    this.table.put(str, new StoreEntry(str, localDate, 1, localCertificate));
    while (true)
    {
      i = localDataInputStream.read();
      break;
      Key localKey = decodeKey(localDataInputStream);
      this.table.put(str, new StoreEntry(str, localDate, 2, localKey, arrayOfCertificate));
      continue;
      byte[] arrayOfByte = new byte[localDataInputStream.readInt()];
      localDataInputStream.readFully(arrayOfByte);
      this.table.put(str, new StoreEntry(str, localDate, i, arrayOfByte, arrayOfCertificate));
    }
  }

  protected Cipher makePBECipher(String paramString, int paramInt1, char[] paramArrayOfChar, byte[] paramArrayOfByte, int paramInt2)
    throws IOException
  {
    try
    {
      PBEKeySpec localPBEKeySpec = new PBEKeySpec(paramArrayOfChar);
      SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance(paramString, BouncyCastleProvider.PROVIDER_NAME);
      PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(paramArrayOfByte, paramInt2);
      Cipher localCipher = Cipher.getInstance(paramString, BouncyCastleProvider.PROVIDER_NAME);
      localCipher.init(paramInt1, localSecretKeyFactory.generateSecret(localPBEKeySpec), localPBEParameterSpec);
      return localCipher;
    }
    catch (Exception localException)
    {
      throw new IOException("Error initialising store of key store: " + localException);
    }
  }

  protected void saveStore(OutputStream paramOutputStream)
    throws IOException
  {
    Enumeration localEnumeration = this.table.elements();
    DataOutputStream localDataOutputStream = new DataOutputStream(paramOutputStream);
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
      {
        localDataOutputStream.write(0);
        return;
      }
      StoreEntry localStoreEntry = (StoreEntry)localEnumeration.nextElement();
      localDataOutputStream.write(localStoreEntry.getType());
      localDataOutputStream.writeUTF(localStoreEntry.getAlias());
      localDataOutputStream.writeLong(localStoreEntry.getDate().getTime());
      Certificate[] arrayOfCertificate = localStoreEntry.getCertificateChain();
      if (arrayOfCertificate == null)
        localDataOutputStream.writeInt(0);
      switch (localStoreEntry.getType())
      {
      default:
        throw new RuntimeException("Unknown object type in store.");
        localDataOutputStream.writeInt(arrayOfCertificate.length);
        for (int i = 0; i != arrayOfCertificate.length; i++)
          encodeCertificate(arrayOfCertificate[i], localDataOutputStream);
      case 1:
        encodeCertificate((Certificate)localStoreEntry.getObject(), localDataOutputStream);
        break;
      case 2:
        encodeKey((Key)localStoreEntry.getObject(), localDataOutputStream);
        break;
      case 3:
      case 4:
        byte[] arrayOfByte = (byte[])localStoreEntry.getObject();
        localDataOutputStream.writeInt(arrayOfByte.length);
        localDataOutputStream.write(arrayOfByte);
      }
    }
  }

  public void setRandom(SecureRandom paramSecureRandom)
  {
    this.random = paramSecureRandom;
  }

  public static class BouncyCastleStore extends JDKKeyStore
  {
    public void engineLoad(InputStream paramInputStream, char[] paramArrayOfChar)
      throws IOException
    {
      this.table.clear();
      if (paramInputStream == null)
        return;
      DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
      int i = localDataInputStream.readInt();
      if ((i != 1) && (i != 0))
        throw new IOException("Wrong version of key store.");
      byte[] arrayOfByte1 = new byte[localDataInputStream.readInt()];
      if (arrayOfByte1.length != 20)
        throw new IOException("Key store corrupted.");
      localDataInputStream.readFully(arrayOfByte1);
      int j = localDataInputStream.readInt();
      if ((j < 0) || (j > 4096))
        throw new IOException("Key store corrupted.");
      if (i == 0);
      for (String str = "OldPBEWithSHAAndTwofish-CBC"; ; str = "PBEWithSHAAndTwofish-CBC")
      {
        CipherInputStream localCipherInputStream = new CipherInputStream(localDataInputStream, makePBECipher(str, 2, paramArrayOfChar, arrayOfByte1, j));
        SHA1Digest localSHA1Digest = new SHA1Digest();
        loadStore(new DigestInputStream(localCipherInputStream, localSHA1Digest));
        byte[] arrayOfByte2 = new byte[localSHA1Digest.getDigestSize()];
        localSHA1Digest.doFinal(arrayOfByte2, 0);
        byte[] arrayOfByte3 = new byte[localSHA1Digest.getDigestSize()];
        Streams.readFully(localCipherInputStream, arrayOfByte3);
        if (Arrays.constantTimeAreEqual(arrayOfByte2, arrayOfByte3))
          break;
        this.table.clear();
        throw new IOException("KeyStore integrity check failed.");
      }
    }

    public void engineStore(OutputStream paramOutputStream, char[] paramArrayOfChar)
      throws IOException
    {
      DataOutputStream localDataOutputStream = new DataOutputStream(paramOutputStream);
      byte[] arrayOfByte1 = new byte[20];
      int i = 1024 + (0x3FF & this.random.nextInt());
      this.random.nextBytes(arrayOfByte1);
      localDataOutputStream.writeInt(1);
      localDataOutputStream.writeInt(arrayOfByte1.length);
      localDataOutputStream.write(arrayOfByte1);
      localDataOutputStream.writeInt(i);
      CipherOutputStream localCipherOutputStream = new CipherOutputStream(localDataOutputStream, makePBECipher("PBEWithSHAAndTwofish-CBC", 1, paramArrayOfChar, arrayOfByte1, i));
      DigestOutputStream localDigestOutputStream = new DigestOutputStream(localCipherOutputStream, new SHA1Digest());
      saveStore(localDigestOutputStream);
      Digest localDigest = localDigestOutputStream.getDigest();
      byte[] arrayOfByte2 = new byte[localDigest.getDigestSize()];
      localDigest.doFinal(arrayOfByte2, 0);
      localCipherOutputStream.write(arrayOfByte2);
      localCipherOutputStream.close();
    }
  }

  private class StoreEntry
  {
    String alias;
    Certificate[] certChain;
    Date date = new Date();
    Object obj;
    int type;

    StoreEntry(String paramKey, Key paramArrayOfChar, char[] paramArrayOfCertificate, Certificate[] arg5)
      throws Exception
    {
      this.type = 4;
      this.alias = paramKey;
      Object localObject;
      this.certChain = localObject;
      byte[] arrayOfByte = new byte[20];
      JDKKeyStore.this.random.setSeed(System.currentTimeMillis());
      JDKKeyStore.this.random.nextBytes(arrayOfByte);
      int i = 1024 + (0x3FF & JDKKeyStore.this.random.nextInt());
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      DataOutputStream localDataOutputStream1 = new DataOutputStream(localByteArrayOutputStream);
      localDataOutputStream1.writeInt(arrayOfByte.length);
      localDataOutputStream1.write(arrayOfByte);
      localDataOutputStream1.writeInt(i);
      DataOutputStream localDataOutputStream2 = new DataOutputStream(new CipherOutputStream(localDataOutputStream1, JDKKeyStore.this.makePBECipher("PBEWithSHAAnd3-KeyTripleDES-CBC", 1, paramArrayOfCertificate, arrayOfByte, i)));
      JDKKeyStore.this.encodeKey(paramArrayOfChar, localDataOutputStream2);
      localDataOutputStream2.close();
      this.obj = localByteArrayOutputStream.toByteArray();
    }

    StoreEntry(String paramCertificate, Certificate arg3)
    {
      this.type = 1;
      this.alias = paramCertificate;
      Object localObject;
      this.obj = localObject;
      this.certChain = null;
    }

    StoreEntry(String paramDate, Date paramInt, int paramObject, Object arg5)
    {
      this.alias = paramDate;
      this.date = paramInt;
      this.type = paramObject;
      Object localObject;
      this.obj = localObject;
    }

    StoreEntry(String paramDate, Date paramInt, int paramObject, Object paramArrayOfCertificate, Certificate[] arg6)
    {
      this.alias = paramDate;
      this.date = paramInt;
      this.type = paramObject;
      this.obj = paramArrayOfCertificate;
      Object localObject;
      this.certChain = localObject;
    }

    StoreEntry(String paramArrayOfByte, byte[] paramArrayOfCertificate, Certificate[] arg4)
    {
      this.type = 3;
      this.alias = paramArrayOfByte;
      this.obj = paramArrayOfCertificate;
      Object localObject;
      this.certChain = localObject;
    }

    String getAlias()
    {
      return this.alias;
    }

    Certificate[] getCertificateChain()
    {
      return this.certChain;
    }

    Date getDate()
    {
      return this.date;
    }

    Object getObject()
    {
      return this.obj;
    }

    // ERROR //
    Object getObject(char[] paramArrayOfChar)
      throws NoSuchAlgorithmException, UnrecoverableKeyException
    {
      // Byte code:
      //   0: aload_1
      //   1: ifnull +8 -> 9
      //   4: aload_1
      //   5: arraylength
      //   6: ifne +18 -> 24
      //   9: aload_0
      //   10: getfield 101	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:obj	Ljava/lang/Object;
      //   13: instanceof 120
      //   16: ifeq +8 -> 24
      //   19: aload_0
      //   20: getfield 101	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:obj	Ljava/lang/Object;
      //   23: areturn
      //   24: aload_0
      //   25: getfield 32	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:type	I
      //   28: iconst_4
      //   29: if_icmpne +419 -> 448
      //   32: new 122	java/io/DataInputStream
      //   35: dup
      //   36: new 124	java/io/ByteArrayInputStream
      //   39: dup
      //   40: aload_0
      //   41: getfield 101	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:obj	Ljava/lang/Object;
      //   44: checkcast 126	[B
      //   47: invokespecial 128	java/io/ByteArrayInputStream:<init>	([B)V
      //   50: invokespecial 131	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
      //   53: astore_2
      //   54: aload_2
      //   55: invokevirtual 134	java/io/DataInputStream:readInt	()I
      //   58: newarray byte
      //   60: astore 4
      //   62: aload_2
      //   63: aload 4
      //   65: invokevirtual 137	java/io/DataInputStream:readFully	([B)V
      //   68: aload_2
      //   69: invokevirtual 134	java/io/DataInputStream:readInt	()I
      //   72: istore 5
      //   74: new 139	javax/crypto/CipherInputStream
      //   77: dup
      //   78: aload_2
      //   79: aload_0
      //   80: getfield 22	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:this$0	Lorg/bouncycastle2/jce/provider/JDKKeyStore;
      //   83: ldc 81
      //   85: iconst_2
      //   86: aload_1
      //   87: aload 4
      //   89: iload 5
      //   91: invokevirtual 85	org/bouncycastle2/jce/provider/JDKKeyStore:makePBECipher	(Ljava/lang/String;I[C[BI)Ljavax/crypto/Cipher;
      //   94: invokespecial 142	javax/crypto/CipherInputStream:<init>	(Ljava/io/InputStream;Ljavax/crypto/Cipher;)V
      //   97: astore 6
      //   99: aload_0
      //   100: getfield 22	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:this$0	Lorg/bouncycastle2/jce/provider/JDKKeyStore;
      //   103: new 122	java/io/DataInputStream
      //   106: dup
      //   107: aload 6
      //   109: invokespecial 131	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
      //   112: invokestatic 146	org/bouncycastle2/jce/provider/JDKKeyStore:access$1	(Lorg/bouncycastle2/jce/provider/JDKKeyStore;Ljava/io/DataInputStream;)Ljava/security/Key;
      //   115: astore 25
      //   117: aload 25
      //   119: areturn
      //   120: astore 7
      //   122: new 124	java/io/ByteArrayInputStream
      //   125: dup
      //   126: aload_0
      //   127: getfield 101	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:obj	Ljava/lang/Object;
      //   130: checkcast 126	[B
      //   133: invokespecial 128	java/io/ByteArrayInputStream:<init>	([B)V
      //   136: astore 8
      //   138: new 122	java/io/DataInputStream
      //   141: dup
      //   142: aload 8
      //   144: invokespecial 131	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
      //   147: astore 9
      //   149: aload 9
      //   151: invokevirtual 134	java/io/DataInputStream:readInt	()I
      //   154: newarray byte
      //   156: astore 11
      //   158: aload 9
      //   160: aload 11
      //   162: invokevirtual 137	java/io/DataInputStream:readFully	([B)V
      //   165: aload 9
      //   167: invokevirtual 134	java/io/DataInputStream:readInt	()I
      //   170: istore 12
      //   172: new 139	javax/crypto/CipherInputStream
      //   175: dup
      //   176: aload 9
      //   178: aload_0
      //   179: getfield 22	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:this$0	Lorg/bouncycastle2/jce/provider/JDKKeyStore;
      //   182: ldc 148
      //   184: iconst_2
      //   185: aload_1
      //   186: aload 11
      //   188: iload 12
      //   190: invokevirtual 85	org/bouncycastle2/jce/provider/JDKKeyStore:makePBECipher	(Ljava/lang/String;I[C[BI)Ljavax/crypto/Cipher;
      //   193: invokespecial 142	javax/crypto/CipherInputStream:<init>	(Ljava/io/InputStream;Ljavax/crypto/Cipher;)V
      //   196: astore 13
      //   198: aload_0
      //   199: getfield 22	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:this$0	Lorg/bouncycastle2/jce/provider/JDKKeyStore;
      //   202: new 122	java/io/DataInputStream
      //   205: dup
      //   206: aload 13
      //   208: invokespecial 131	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
      //   211: invokestatic 146	org/bouncycastle2/jce/provider/JDKKeyStore:access$1	(Lorg/bouncycastle2/jce/provider/JDKKeyStore;Ljava/io/DataInputStream;)Ljava/security/Key;
      //   214: astore 23
      //   216: aload 23
      //   218: astore 18
      //   220: aload 18
      //   222: ifnull +216 -> 438
      //   225: new 64	java/io/ByteArrayOutputStream
      //   228: dup
      //   229: invokespecial 65	java/io/ByteArrayOutputStream:<init>	()V
      //   232: astore 19
      //   234: new 67	java/io/DataOutputStream
      //   237: dup
      //   238: aload 19
      //   240: invokespecial 70	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
      //   243: astore 20
      //   245: aload 20
      //   247: aload 11
      //   249: arraylength
      //   250: invokevirtual 74	java/io/DataOutputStream:writeInt	(I)V
      //   253: aload 20
      //   255: aload 11
      //   257: invokevirtual 77	java/io/DataOutputStream:write	([B)V
      //   260: aload 20
      //   262: iload 12
      //   264: invokevirtual 74	java/io/DataOutputStream:writeInt	(I)V
      //   267: new 67	java/io/DataOutputStream
      //   270: dup
      //   271: new 79	javax/crypto/CipherOutputStream
      //   274: dup
      //   275: aload 20
      //   277: aload_0
      //   278: getfield 22	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:this$0	Lorg/bouncycastle2/jce/provider/JDKKeyStore;
      //   281: ldc 81
      //   283: iconst_1
      //   284: aload_1
      //   285: aload 11
      //   287: iload 12
      //   289: invokevirtual 85	org/bouncycastle2/jce/provider/JDKKeyStore:makePBECipher	(Ljava/lang/String;I[C[BI)Ljavax/crypto/Cipher;
      //   292: invokespecial 88	javax/crypto/CipherOutputStream:<init>	(Ljava/io/OutputStream;Ljavax/crypto/Cipher;)V
      //   295: invokespecial 70	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
      //   298: astore 21
      //   300: aload_0
      //   301: getfield 22	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:this$0	Lorg/bouncycastle2/jce/provider/JDKKeyStore;
      //   304: aload 18
      //   306: aload 21
      //   308: invokestatic 92	org/bouncycastle2/jce/provider/JDKKeyStore:access$0	(Lorg/bouncycastle2/jce/provider/JDKKeyStore;Ljava/security/Key;Ljava/io/DataOutputStream;)V
      //   311: aload 21
      //   313: invokevirtual 95	java/io/DataOutputStream:close	()V
      //   316: aload_0
      //   317: aload 19
      //   319: invokevirtual 99	java/io/ByteArrayOutputStream:toByteArray	()[B
      //   322: putfield 101	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:obj	Ljava/lang/Object;
      //   325: aload 18
      //   327: areturn
      //   328: astore_3
      //   329: new 118	java/security/UnrecoverableKeyException
      //   332: dup
      //   333: ldc 150
      //   335: invokespecial 153	java/security/UnrecoverableKeyException:<init>	(Ljava/lang/String;)V
      //   338: athrow
      //   339: astore 14
      //   341: new 124	java/io/ByteArrayInputStream
      //   344: dup
      //   345: aload_0
      //   346: getfield 101	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:obj	Ljava/lang/Object;
      //   349: checkcast 126	[B
      //   352: invokespecial 128	java/io/ByteArrayInputStream:<init>	([B)V
      //   355: astore 15
      //   357: new 122	java/io/DataInputStream
      //   360: dup
      //   361: aload 15
      //   363: invokespecial 131	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
      //   366: astore 16
      //   368: aload 16
      //   370: invokevirtual 134	java/io/DataInputStream:readInt	()I
      //   373: newarray byte
      //   375: astore 11
      //   377: aload 16
      //   379: aload 11
      //   381: invokevirtual 137	java/io/DataInputStream:readFully	([B)V
      //   384: aload 16
      //   386: invokevirtual 134	java/io/DataInputStream:readInt	()I
      //   389: istore 12
      //   391: new 139	javax/crypto/CipherInputStream
      //   394: dup
      //   395: aload 16
      //   397: aload_0
      //   398: getfield 22	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:this$0	Lorg/bouncycastle2/jce/provider/JDKKeyStore;
      //   401: ldc 155
      //   403: iconst_2
      //   404: aload_1
      //   405: aload 11
      //   407: iload 12
      //   409: invokevirtual 85	org/bouncycastle2/jce/provider/JDKKeyStore:makePBECipher	(Ljava/lang/String;I[C[BI)Ljavax/crypto/Cipher;
      //   412: invokespecial 142	javax/crypto/CipherInputStream:<init>	(Ljava/io/InputStream;Ljavax/crypto/Cipher;)V
      //   415: astore 17
      //   417: aload_0
      //   418: getfield 22	org/bouncycastle2/jce/provider/JDKKeyStore$StoreEntry:this$0	Lorg/bouncycastle2/jce/provider/JDKKeyStore;
      //   421: new 122	java/io/DataInputStream
      //   424: dup
      //   425: aload 17
      //   427: invokespecial 131	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
      //   430: invokestatic 146	org/bouncycastle2/jce/provider/JDKKeyStore:access$1	(Lorg/bouncycastle2/jce/provider/JDKKeyStore;Ljava/io/DataInputStream;)Ljava/security/Key;
      //   433: astore 18
      //   435: goto -215 -> 220
      //   438: new 118	java/security/UnrecoverableKeyException
      //   441: dup
      //   442: ldc 150
      //   444: invokespecial 153	java/security/UnrecoverableKeyException:<init>	(Ljava/lang/String;)V
      //   447: athrow
      //   448: new 157	java/lang/RuntimeException
      //   451: dup
      //   452: ldc 159
      //   454: invokespecial 160	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
      //   457: athrow
      //   458: astore 24
      //   460: goto -131 -> 329
      //   463: astore 10
      //   465: goto -136 -> 329
      //   468: astore 22
      //   470: goto -141 -> 329
      //
      // Exception table:
      //   from	to	target	type
      //   99	117	120	java/lang/Exception
      //   54	99	328	java/lang/Exception
      //   122	138	328	java/lang/Exception
      //   225	325	328	java/lang/Exception
      //   368	435	328	java/lang/Exception
      //   438	448	328	java/lang/Exception
      //   198	216	339	java/lang/Exception
      //   138	149	458	java/lang/Exception
      //   149	198	463	java/lang/Exception
      //   341	357	463	java/lang/Exception
      //   357	368	468	java/lang/Exception
    }

    int getType()
    {
      return this.type;
    }
  }
}