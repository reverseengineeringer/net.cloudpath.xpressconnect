package org.bouncycastle2.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle2.jce.MultiCertStoreParameters;

public class MultiCertStoreSpi extends CertStoreSpi
{
  private MultiCertStoreParameters params;

  public MultiCertStoreSpi(CertStoreParameters paramCertStoreParameters)
    throws InvalidAlgorithmParameterException
  {
    super(paramCertStoreParameters);
    if (!(paramCertStoreParameters instanceof MultiCertStoreParameters))
      throw new InvalidAlgorithmParameterException("org.bouncycastle2.jce.provider.MultiCertStoreSpi: parameter must be a MultiCertStoreParameters object\n" + paramCertStoreParameters.toString());
    this.params = ((MultiCertStoreParameters)paramCertStoreParameters);
  }

  public Collection engineGetCRLs(CRLSelector paramCRLSelector)
    throws CertStoreException
  {
    boolean bool = this.params.getSearchAllStores();
    Iterator localIterator = this.params.getCertStores().iterator();
    Object localObject;
    if (bool)
      localObject = new ArrayList();
    Collection localCollection;
    do
      while (true)
        if (!localIterator.hasNext())
        {
          return localObject;
          localObject = Collections.EMPTY_LIST;
        }
        else
        {
          localCollection = ((CertStore)localIterator.next()).getCRLs(paramCRLSelector);
          if (!bool)
            break;
          ((List)localObject).addAll(localCollection);
        }
    while (localCollection.isEmpty());
    return localCollection;
  }

  public Collection engineGetCertificates(CertSelector paramCertSelector)
    throws CertStoreException
  {
    boolean bool = this.params.getSearchAllStores();
    Iterator localIterator = this.params.getCertStores().iterator();
    Object localObject;
    if (bool)
      localObject = new ArrayList();
    Collection localCollection;
    do
      while (true)
        if (!localIterator.hasNext())
        {
          return localObject;
          localObject = Collections.EMPTY_LIST;
        }
        else
        {
          localCollection = ((CertStore)localIterator.next()).getCertificates(paramCertSelector);
          if (!bool)
            break;
          ((List)localObject).addAll(localCollection);
        }
    while (localCollection.isEmpty());
    return localCollection;
  }
}