package org.bouncycastle2.jce.spec;

import java.security.spec.KeySpec;

public class ElGamalKeySpec
  implements KeySpec
{
  private ElGamalParameterSpec spec;

  public ElGamalKeySpec(ElGamalParameterSpec paramElGamalParameterSpec)
  {
    this.spec = paramElGamalParameterSpec;
  }

  public ElGamalParameterSpec getParams()
  {
    return this.spec;
  }
}