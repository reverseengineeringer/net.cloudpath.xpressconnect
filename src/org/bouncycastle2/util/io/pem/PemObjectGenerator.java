package org.bouncycastle2.util.io.pem;

public abstract interface PemObjectGenerator
{
  public abstract PemObject generate()
    throws PemGenerationException;
}