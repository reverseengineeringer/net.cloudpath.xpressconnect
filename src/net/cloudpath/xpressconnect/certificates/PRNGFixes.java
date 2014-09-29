package net.cloudpath.xpressconnect.certificates;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;

public final class PRNGFixes
{
  private static final byte[] BUILD_FINGERPRINT_AND_DEVICE_SERIAL = getBuildFingerprintAndDeviceSerial();
  private static final int VERSION_CODE_JELLY_BEAN = 16;
  private static final int VERSION_CODE_JELLY_BEAN_MR2 = 18;

  public static void apply()
  {
    applyOpenSSLFix();
    installLinuxPRNGSecureRandom();
  }

  private static void applyOpenSSLFix()
    throws SecurityException
  {
    if ((Build.VERSION.SDK_INT < 16) || (Build.VERSION.SDK_INT > 18));
    while (true)
    {
      return;
      try
      {
        Method localMethod1 = Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto").getMethod("RAND_seed", new Class[] { [B.class });
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = generateSeed();
        localMethod1.invoke(null, arrayOfObject1);
        Class localClass = Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto");
        Class[] arrayOfClass = new Class[2];
        arrayOfClass[0] = String.class;
        arrayOfClass[1] = Long.TYPE;
        Method localMethod2 = localClass.getMethod("RAND_load_file", arrayOfClass);
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = "/dev/urandom";
        arrayOfObject2[1] = Integer.valueOf(1024);
        int i = ((Integer)localMethod2.invoke(null, arrayOfObject2)).intValue();
        if (i == 1024)
          continue;
        throw new IOException("Unexpected number of bytes read from Linux PRNG: " + i);
      }
      catch (Exception localException)
      {
        throw new SecurityException("Failed to seed OpenSSL PRNG", localException);
      }
    }
  }

  private static byte[] generateSeed()
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
      localDataOutputStream.writeLong(System.currentTimeMillis());
      localDataOutputStream.writeLong(System.nanoTime());
      localDataOutputStream.writeInt(Process.myPid());
      localDataOutputStream.writeInt(Process.myUid());
      localDataOutputStream.write(BUILD_FINGERPRINT_AND_DEVICE_SERIAL);
      localDataOutputStream.close();
      byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new SecurityException("Failed to generate seed", localIOException);
    }
  }

  private static byte[] getBuildFingerprintAndDeviceSerial()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String str1 = Build.FINGERPRINT;
    if (str1 != null)
      localStringBuilder.append(str1);
    String str2 = getDeviceSerialNumber();
    if (str2 != null)
      localStringBuilder.append(str2);
    try
    {
      byte[] arrayOfByte = localStringBuilder.toString().getBytes("UTF-8");
      return arrayOfByte;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new RuntimeException("UTF-8 encoding not supported");
  }

  private static String getDeviceSerialNumber()
  {
    try
    {
      String str = (String)Build.class.getField("SERIAL").get(null);
      return str;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  private static void installLinuxPRNGSecureRandom()
    throws SecurityException
  {
    if (Build.VERSION.SDK_INT > 18);
    while (true)
    {
      return;
      Provider[] arrayOfProvider = Security.getProviders("SecureRandom.SHA1PRNG");
      if ((arrayOfProvider == null) || (arrayOfProvider.length < 1) || (!LinuxPRNGSecureRandomProvider.class.equals(arrayOfProvider[0].getClass())))
        Security.insertProviderAt(new LinuxPRNGSecureRandomProvider(), 1);
      SecureRandom localSecureRandom1 = new SecureRandom();
      if (!LinuxPRNGSecureRandomProvider.class.equals(localSecureRandom1.getProvider().getClass()))
        throw new SecurityException("new SecureRandom() backed by wrong Provider: " + localSecureRandom1.getProvider().getClass());
      try
      {
        SecureRandom localSecureRandom2 = SecureRandom.getInstance("SHA1PRNG");
        if (LinuxPRNGSecureRandomProvider.class.equals(localSecureRandom2.getProvider().getClass()))
          continue;
        throw new SecurityException("SecureRandom.getInstance(\"SHA1PRNG\") backed by wrong Provider: " + localSecureRandom2.getProvider().getClass());
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new SecurityException("SHA1PRNG not available", localNoSuchAlgorithmException);
      }
    }
  }

  public static class LinuxPRNGSecureRandom extends SecureRandomSpi
  {
    private static final File URANDOM_FILE = new File("/dev/urandom");
    private static final Object sLock = new Object();
    private static DataInputStream sUrandomIn;
    private static OutputStream sUrandomOut;
    private boolean mSeeded;

    private DataInputStream getUrandomInputStream()
    {
      synchronized (sLock)
      {
        DataInputStream localDataInputStream1 = sUrandomIn;
        if (localDataInputStream1 == null);
        try
        {
          sUrandomIn = new DataInputStream(new FileInputStream(URANDOM_FILE));
          DataInputStream localDataInputStream2 = sUrandomIn;
          return localDataInputStream2;
        }
        catch (IOException localIOException)
        {
          throw new SecurityException("Failed to open " + URANDOM_FILE + " for reading", localIOException);
        }
      }
    }

    private OutputStream getUrandomOutputStream()
      throws IOException
    {
      synchronized (sLock)
      {
        if (sUrandomOut == null)
          sUrandomOut = new FileOutputStream(URANDOM_FILE);
        OutputStream localOutputStream = sUrandomOut;
        return localOutputStream;
      }
    }

    protected byte[] engineGenerateSeed(int paramInt)
    {
      byte[] arrayOfByte = new byte[paramInt];
      engineNextBytes(arrayOfByte);
      return arrayOfByte;
    }

    // ERROR //
    protected void engineNextBytes(byte[] paramArrayOfByte)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 87	net/cloudpath/xpressconnect/certificates/PRNGFixes$LinuxPRNGSecureRandom:mSeeded	Z
      //   4: ifne +10 -> 14
      //   7: aload_0
      //   8: invokestatic 93	net/cloudpath/xpressconnect/certificates/PRNGFixes:access$000	()[B
      //   11: invokevirtual 96	net/cloudpath/xpressconnect/certificates/PRNGFixes$LinuxPRNGSecureRandom:engineSetSeed	([B)V
      //   14: getstatic 32	net/cloudpath/xpressconnect/certificates/PRNGFixes$LinuxPRNGSecureRandom:sLock	Ljava/lang/Object;
      //   17: astore_3
      //   18: aload_3
      //   19: monitorenter
      //   20: aload_0
      //   21: invokespecial 98	net/cloudpath/xpressconnect/certificates/PRNGFixes$LinuxPRNGSecureRandom:getUrandomInputStream	()Ljava/io/DataInputStream;
      //   24: astore 5
      //   26: aload_3
      //   27: monitorexit
      //   28: aload 5
      //   30: monitorenter
      //   31: aload 5
      //   33: aload_1
      //   34: invokevirtual 101	java/io/DataInputStream:readFully	([B)V
      //   37: aload 5
      //   39: monitorexit
      //   40: return
      //   41: astore 4
      //   43: aload_3
      //   44: monitorexit
      //   45: aload 4
      //   47: athrow
      //   48: astore_2
      //   49: new 51	java/lang/SecurityException
      //   52: dup
      //   53: new 53	java/lang/StringBuilder
      //   56: dup
      //   57: invokespecial 54	java/lang/StringBuilder:<init>	()V
      //   60: ldc 103
      //   62: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   65: getstatic 26	net/cloudpath/xpressconnect/certificates/PRNGFixes$LinuxPRNGSecureRandom:URANDOM_FILE	Ljava/io/File;
      //   68: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   71: invokevirtual 69	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   74: aload_2
      //   75: invokespecial 72	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   78: athrow
      //   79: astore 6
      //   81: aload 5
      //   83: monitorexit
      //   84: aload 6
      //   86: athrow
      //
      // Exception table:
      //   from	to	target	type
      //   20	28	41	finally
      //   43	45	41	finally
      //   14	20	48	java/io/IOException
      //   28	31	48	java/io/IOException
      //   45	48	48	java/io/IOException
      //   84	87	48	java/io/IOException
      //   31	40	79	finally
      //   81	84	79	finally
    }

    protected void engineSetSeed(byte[] paramArrayOfByte)
    {
      try
      {
        synchronized (sLock)
        {
          OutputStream localOutputStream = getUrandomOutputStream();
          localOutputStream.write(paramArrayOfByte);
          localOutputStream.flush();
          return;
        }
      }
      catch (IOException localIOException)
      {
        Log.w(PRNGFixes.class.getSimpleName(), "Failed to mix seed into " + URANDOM_FILE);
        return;
      }
      finally
      {
        this.mSeeded = true;
      }
    }
  }

  private static class LinuxPRNGSecureRandomProvider extends Provider
  {
    public LinuxPRNGSecureRandomProvider()
    {
      super(1.0D, "A Linux-specific random number provider that uses /dev/urandom");
      put("SecureRandom.SHA1PRNG", PRNGFixes.LinuxPRNGSecureRandom.class.getName());
      put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
    }
  }
}