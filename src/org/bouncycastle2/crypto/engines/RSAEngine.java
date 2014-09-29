package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;

public class RSAEngine
  implements AsymmetricBlockCipher
{
  private RSACoreEngine core;

  public int getInputBlockSize()
  {
    return this.core.getInputBlockSize();
  }

  public int getOutputBlockSize()
  {
    return this.core.getOutputBlockSize();
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (this.core == null)
      this.core = new RSACoreEngine();
    this.core.init(paramBoolean, paramCipherParameters);
  }

  public byte[] processBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (this.core == null)
      throw new IllegalStateException("RSA engine not initialised");
    return this.core.convertOutput(this.core.processBlock(this.core.convertInput(paramArrayOfByte, paramInt1, paramInt2)));
  }
}