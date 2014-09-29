package org.bouncycastle2.crypto.params;

public class ElGamalKeyParameters extends AsymmetricKeyParameter
{
  private ElGamalParameters params;

  protected ElGamalKeyParameters(boolean paramBoolean, ElGamalParameters paramElGamalParameters)
  {
    super(paramBoolean);
    this.params = paramElGamalParameters;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ElGamalKeyParameters));
    ElGamalKeyParameters localElGamalKeyParameters;
    do
    {
      return false;
      localElGamalKeyParameters = (ElGamalKeyParameters)paramObject;
      if (this.params != null)
        break;
    }
    while (localElGamalKeyParameters.getParameters() != null);
    return true;
    return this.params.equals(localElGamalKeyParameters.getParameters());
  }

  public ElGamalParameters getParameters()
  {
    return this.params;
  }

  public int hashCode()
  {
    if (this.params != null)
      return this.params.hashCode();
    return 0;
  }
}