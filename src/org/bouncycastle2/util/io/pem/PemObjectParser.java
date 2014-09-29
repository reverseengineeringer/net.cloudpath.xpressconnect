package org.bouncycastle2.util.io.pem;

import java.io.IOException;

public abstract interface PemObjectParser
{
  public abstract Object parseObject(PemObject paramPemObject)
    throws IOException;
}