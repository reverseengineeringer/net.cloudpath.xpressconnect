package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;

public abstract interface TlsAgreementCredentials extends TlsCredentials
{
  public abstract byte[] generateAgreement(AsymmetricKeyParameter paramAsymmetricKeyParameter)
    throws IOException;
}