package org.bouncycastle2.jce.spec;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import org.bouncycastle2.jce.interfaces.IESKey;

public class IEKeySpec
  implements KeySpec, IESKey
{
  private PrivateKey privKey;
  private PublicKey pubKey;

  public IEKeySpec(PrivateKey paramPrivateKey, PublicKey paramPublicKey)
  {
    this.privKey = paramPrivateKey;
    this.pubKey = paramPublicKey;
  }

  public String getAlgorithm()
  {
    return "IES";
  }

  public byte[] getEncoded()
  {
    return null;
  }

  public String getFormat()
  {
    return null;
  }

  public PrivateKey getPrivate()
  {
    return this.privKey;
  }

  public PublicKey getPublic()
  {
    return this.pubKey;
  }
}