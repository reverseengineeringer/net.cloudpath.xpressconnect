package org.bouncycastle2.crypto.generators;

import org.bouncycastle2.crypto.Digest;

public class KDF2BytesGenerator extends BaseKDFBytesGenerator
{
  public KDF2BytesGenerator(Digest paramDigest)
  {
    super(1, paramDigest);
  }
}