package org.bouncycastle2.jce.provider;

import java.util.Collection;
import org.bouncycastle2.jce.X509LDAPCertStoreParameters;
import org.bouncycastle2.util.Selector;
import org.bouncycastle2.x509.X509StoreParameters;
import org.bouncycastle2.x509.X509StoreSpi;
import org.bouncycastle2.x509.util.LDAPStoreHelper;

public class X509StoreLDAPCerts extends X509StoreSpi
{
  private LDAPStoreHelper helper;

  public Collection engineGetMatches(Selector paramSelector)
  {
    return null;
  }

  public void engineInit(X509StoreParameters paramX509StoreParameters)
  {
    if (!(paramX509StoreParameters instanceof X509LDAPCertStoreParameters))
      throw new IllegalArgumentException("Initialization parameters must be an instance of " + X509LDAPCertStoreParameters.class.getName() + ".");
    this.helper = new LDAPStoreHelper((X509LDAPCertStoreParameters)paramX509StoreParameters);
  }
}