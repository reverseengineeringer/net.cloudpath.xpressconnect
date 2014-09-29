package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import org.bouncycastle2.asn1.DEROctetString;

public abstract class JDKECDSAAlgParameters extends AlgorithmParametersSpi
{
  public static class SigAlgParameters extends JDKAlgorithmParameters
  {
    protected byte[] engineGetEncoded()
      throws IOException
    {
      return engineGetEncoded("ASN.1");
    }

    protected byte[] engineGetEncoded(String paramString)
      throws IOException
    {
      if (paramString == null)
        return engineGetEncoded("ASN.1");
      if (paramString.equals("ASN.1"))
        return new DEROctetString(engineGetEncoded("RAW")).getEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      throw new InvalidParameterSpecException("unknown parameter spec passed to ECDSA parameters object.");
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      throw new IOException("Unknown parameters format in IV parameters object");
    }

    protected String engineToString()
    {
      return "ECDSA Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      throw new InvalidParameterSpecException("unknown parameter spec passed to ECDSA parameters object.");
    }
  }
}