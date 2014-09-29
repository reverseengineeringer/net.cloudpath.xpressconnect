package org.bouncycastle2.crypto.agreement.kdf;

import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.DerivationFunction;
import org.bouncycastle2.crypto.DerivationParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle2.crypto.params.KDFParameters;

public class ECDHKEKGenerator
  implements DerivationFunction
{
  private DERObjectIdentifier algorithm;
  private DerivationFunction kdf;
  private int keySize;
  private byte[] z;

  public ECDHKEKGenerator(Digest paramDigest)
  {
    this.kdf = new KDF2BytesGenerator(paramDigest);
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
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(new AlgorithmIdentifier(this.algorithm, new DERNull()));
    localASN1EncodableVector.add(new DERTaggedObject(true, 2, new DEROctetString(integerToBytes(this.keySize))));
    this.kdf.init(new KDFParameters(this.z, new DERSequence(localASN1EncodableVector).getDEREncoded()));
    return this.kdf.generateBytes(paramArrayOfByte, paramInt1, paramInt2);
  }

  public Digest getDigest()
  {
    return this.kdf.getDigest();
  }

  public void init(DerivationParameters paramDerivationParameters)
  {
    DHKDFParameters localDHKDFParameters = (DHKDFParameters)paramDerivationParameters;
    this.algorithm = localDHKDFParameters.getAlgorithm();
    this.keySize = localDHKDFParameters.getKeySize();
    this.z = localDHKDFParameters.getZ();
  }
}