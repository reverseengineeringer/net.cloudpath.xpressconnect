package org.bouncycastle2.x509;

import java.util.Collection;
import org.bouncycastle2.util.Selector;

public abstract class X509StoreSpi
{
  public abstract Collection engineGetMatches(Selector paramSelector);

  public abstract void engineInit(X509StoreParameters paramX509StoreParameters);
}