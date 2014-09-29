package org.bouncycastle2.crypto.agreement.kdf;

import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.DerivationFunction;
import org.bouncycastle2.crypto.DerivationParameters;
import org.bouncycastle2.crypto.Digest;

public class DHKEKGenerator
  implements DerivationFunction
{
  private DERObjectIdentifier algorithm;
  private final Digest digest;
  private int keySize;
  private byte[] partyAInfo;
  private byte[] z;

  public DHKEKGenerator(Digest paramDigest)
  {
    this.digest = paramDigest;
  }

  private byte[] integerToBytes(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = ((byte)(paramInt >> 24));
    arrayOfByte[1] = ((byte)(paramInt >> 16));
    arrayOfByte[2] = ((byte)(paramInt >> 8));
    arrayOfByte[3] = ((byte)paramInt);
    return arrayOfByte;
  }

  public int generateBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws DataLengthException, IllegalArgumentException
  {
    if (paramArrayOfByte.length - paramInt2 < paramInt1)
      throw new DataLengthException("output buffer too small");
    long l = paramInt2;
    int i = this.digest.getDigestSize();
    if (l > 8589934591L)
      throw new IllegalArgumentException("Output length too large");
    int j = (int)((l + i - 1L) / i);
    byte[] arrayOfByte1 = new byte[this.digest.getDigestSize()];
    int k = 1;
    int m = 0;
    if (m >= j)
    {
      this.digest.reset();
      return paramInt2;
    }
    this.digest.update(this.z, 0, this.z.length);
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
    localASN1EncodableVector2.add(this.algorithm);
    localASN1EncodableVector2.add(new DEROctetString(integerToBytes(k)));
    localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
    if (this.partyAInfo != null)
      localASN1EncodableVector1.add(new DERTaggedObject(true, 0, new DEROctetString(this.partyAInfo)));
    localASN1EncodableVector1.add(new DERTaggedObject(true, 2, new DEROctetString(integerToBytes(this.keySize))));
    byte[] arrayOfByte2 = new DERSequence(localASN1EncodableVector1).getDEREncoded();
    this.digest.update(arrayOfByte2, 0, arrayOfByte2.length);
    this.digest.doFinal(arrayOfByte1, 0);
    if (paramInt2 > i)
    {
      System.arraycopy(arrayOfByte1, 0, paramArrayOfByte, paramInt1, i);
      paramInt1 += i;
      paramInt2 -= i;
    }
    while (true)
    {
      k++;
      m++;
      break;
      System.arraycopy(arrayOfByte1, 0, paramArrayOfByte, paramInt1, paramInt2);
    }
  }

  public Digest getDigest()
  {
    return this.digest;
  }

  public void init(DerivationParameters paramDerivationParameters)
  {
    DHKDFParameters localDHKDFParameters = (DHKDFParameters)paramDerivationParameters;
    this.algorithm = localDHKDFParameters.getAlgorithm();
    this.keySize = localDHKDFParameters.getKeySize();
    this.z = localDHKDFParameters.getZ();
    this.partyAInfo = localDHKDFParameters.getExtraInfo();
  }
}