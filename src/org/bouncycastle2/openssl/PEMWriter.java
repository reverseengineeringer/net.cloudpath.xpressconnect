package org.bouncycastle2.openssl;

import java.io.IOException;
import java.io.Writer;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import org.bouncycastle2.util.io.pem.PemGenerationException;
import org.bouncycastle2.util.io.pem.PemObjectGenerator;
import org.bouncycastle2.util.io.pem.PemWriter;

public class PEMWriter extends PemWriter
{
  private String provider;

  public PEMWriter(Writer paramWriter)
  {
    this(paramWriter, "BC");
  }

  public PEMWriter(Writer paramWriter, String paramString)
  {
    super(paramWriter);
    this.provider = paramString;
  }

  public void writeObject(Object paramObject)
    throws IOException
  {
    try
    {
      super.writeObject(new MiscPEMGenerator(paramObject));
      return;
    }
    catch (PemGenerationException localPemGenerationException)
    {
      if ((localPemGenerationException.getCause() instanceof IOException))
        throw ((IOException)localPemGenerationException.getCause());
      throw localPemGenerationException;
    }
  }

  public void writeObject(Object paramObject, String paramString, char[] paramArrayOfChar, SecureRandom paramSecureRandom)
    throws IOException
  {
    try
    {
      super.writeObject(new MiscPEMGenerator(paramObject, paramString, paramArrayOfChar, paramSecureRandom, this.provider));
      return;
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
      throw new EncryptionException(localNoSuchProviderException.getMessage(), localNoSuchProviderException);
    }
  }

  public void writeObject(PemObjectGenerator paramPemObjectGenerator)
    throws IOException
  {
    super.writeObject(paramPemObjectGenerator);
  }
}