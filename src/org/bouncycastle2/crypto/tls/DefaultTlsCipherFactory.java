package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.MD5Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.digests.SHA256Digest;
import org.bouncycastle2.crypto.digests.SHA384Digest;
import org.bouncycastle2.crypto.engines.AESFastEngine;
import org.bouncycastle2.crypto.engines.DESedeEngine;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;

public class DefaultTlsCipherFactory
  implements TlsCipherFactory
{
  protected BlockCipher createAESBlockCipher()
  {
    return new CBCBlockCipher(new AESFastEngine());
  }

  protected TlsCipher createAESCipher(TlsClientContext paramTlsClientContext, int paramInt1, int paramInt2)
    throws IOException
  {
    return new TlsBlockCipher(paramTlsClientContext, createAESBlockCipher(), createAESBlockCipher(), createDigest(paramInt2), createDigest(paramInt2), paramInt1);
  }

  public TlsCipher createCipher(TlsClientContext paramTlsClientContext, int paramInt1, int paramInt2)
    throws IOException
  {
    switch (paramInt1)
    {
    default:
      throw new TlsFatalAlert((short)80);
    case 7:
      return createDESedeCipher(paramTlsClientContext, 24, paramInt2);
    case 8:
      return createAESCipher(paramTlsClientContext, 16, paramInt2);
    case 9:
    }
    return createAESCipher(paramTlsClientContext, 32, paramInt2);
  }

  protected BlockCipher createDESedeBlockCipher()
  {
    return new CBCBlockCipher(new DESedeEngine());
  }

  protected TlsCipher createDESedeCipher(TlsClientContext paramTlsClientContext, int paramInt1, int paramInt2)
    throws IOException
  {
    return new TlsBlockCipher(paramTlsClientContext, createDESedeBlockCipher(), createDESedeBlockCipher(), createDigest(paramInt2), createDigest(paramInt2), paramInt1);
  }

  protected Digest createDigest(int paramInt)
    throws IOException
  {
    switch (paramInt)
    {
    default:
      throw new TlsFatalAlert((short)80);
    case 1:
      return new MD5Digest();
    case 2:
      return new SHA1Digest();
    case 3:
      return new SHA256Digest();
    case 4:
    }
    return new SHA384Digest();
  }
}