package org.bouncycastle2.jce.spec;

import java.security.PublicKey;
import java.security.spec.KeySpec;
import org.bouncycastle2.jce.interfaces.MQVPublicKey;

public class MQVPublicKeySpec
  implements KeySpec, MQVPublicKey
{
  private PublicKey ephemeralKey;
  private PublicKey staticKey;

  public MQVPublicKeySpec(PublicKey paramPublicKey1, PublicKey paramPublicKey2)
  {
    this.staticKey = paramPublicKey1;
    this.ephemeralKey = paramPublicKey2;
  }

  public String getAlgorithm()
  {
    return "ECMQV";
  }

  public byte[] getEncoded()
  {
    return null;
  }

  public PublicKey getEphemeralKey()
  {
    return this.ephemeralKey;
  }

  public String getFormat()
  {
    return null;
  }

  public PublicKey getStaticKey()
  {
    return this.staticKey;
  }
}