package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.CipherParameters;

public class AEADParameters
  implements CipherParameters
{
  private byte[] associatedText;
  private KeyParameter key;
  private int macSize;
  private byte[] nonce;

  public AEADParameters(KeyParameter paramKeyParameter, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this.key = paramKeyParameter;
    this.nonce = paramArrayOfByte1;
    this.macSize = paramInt;
    this.associatedText = paramArrayOfByte2;
  }

  public byte[] getAssociatedText()
  {
    return this.associatedText;
  }

  public KeyParameter getKey()
  {
    return this.key;
  }

  public int getMacSize()
  {
    return this.macSize;
  }

  public byte[] getNonce()
  {
    return this.nonce;
  }
}