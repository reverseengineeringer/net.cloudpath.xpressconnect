package org.bouncycastle2.crypto.generators;

import org.bouncycastle2.crypto.Digest;

public class KDF1BytesGenerator extends BaseKDFBytesGenerator
{
  public KDF1BytesGenerator(Digest paramDigest)
  {
    super(0, paramDigest);
  }
}