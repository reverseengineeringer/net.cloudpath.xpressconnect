package org.bouncycastle2.crypto.agreement.kdf;

import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.crypto.DerivationParameters;

public class DHKDFParameters
  implements DerivationParameters
{
  private final DERObjectIdentifier algorithm;
  private final byte[] extraInfo;
  private final int keySize;
  private final byte[] z;

  public DHKDFParameters(DERObjectIdentifier paramDERObjectIdentifier, int paramInt, byte[] paramArrayOfByte)
  {
    this.algorithm = paramDERObjectIdentifier;
    this.keySize = paramInt;
    this.z = paramArrayOfByte;
    this.extraInfo = null;
  }

  public DHKDFParameters(DERObjectIdentifier paramDERObjectIdentifier, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this.algorithm = paramDERObjectIdentifier;
    this.keySize = paramInt;
    this.z = paramArrayOfByte1;
    this.extraInfo = paramArrayOfByte2;
  }

  public DERObjectIdentifier getAlgorithm()
  {
    return this.algorithm;
  }

  public byte[] getExtraInfo()
  {
    return this.extraInfo;
  }

  public int getKeySize()
  {
    return this.keySize;
  }

  public byte[] getZ()
  {
    return this.z;
  }
}