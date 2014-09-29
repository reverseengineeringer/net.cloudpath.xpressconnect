package org.bouncycastle2.jce.interfaces;

import org.bouncycastle2.jce.spec.GOST3410PublicKeyParameterSetSpec;

public abstract interface GOST3410Params
{
  public abstract String getDigestParamSetOID();

  public abstract String getEncryptionParamSetOID();

  public abstract String getPublicKeyParamSetOID();

  public abstract GOST3410PublicKeyParameterSetSpec getPublicKeyParameters();
}