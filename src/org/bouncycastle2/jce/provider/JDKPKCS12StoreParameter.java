package org.bouncycastle2.jce.provider;

import java.io.OutputStream;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.ProtectionParameter;

public class JDKPKCS12StoreParameter
  implements KeyStore.LoadStoreParameter
{
  private OutputStream outputStream;
  private KeyStore.ProtectionParameter protectionParameter;
  private boolean useDEREncoding;

  public OutputStream getOutputStream()
  {
    return this.outputStream;
  }

  public KeyStore.ProtectionParameter getProtectionParameter()
  {
    return this.protectionParameter;
  }

  public boolean isUseDEREncoding()
  {
    return this.useDEREncoding;
  }

  public void setOutputStream(OutputStream paramOutputStream)
  {
    this.outputStream = paramOutputStream;
  }

  public void setPassword(char[] paramArrayOfChar)
  {
    this.protectionParameter = new KeyStore.PasswordProtection(paramArrayOfChar);
  }

  public void setProtectionParameter(KeyStore.ProtectionParameter paramProtectionParameter)
  {
    this.protectionParameter = paramProtectionParameter;
  }

  public void setUseDEREncoding(boolean paramBoolean)
  {
    this.useDEREncoding = paramBoolean;
  }
}