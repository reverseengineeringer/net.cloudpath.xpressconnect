package org.bouncycastle2.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CertStoreCollectionSpi extends CertStoreSpi
{
  private CollectionCertStoreParameters params;

  public CertStoreCollectionSpi(CertStoreParameters paramCertStoreParameters)
    throws InvalidAlgorithmParameterException
  {
    super(paramCertStoreParameters);
    if (!(paramCertStoreParameters instanceof CollectionCertStoreParameters))
      throw new InvalidAlgorithmParameterException("org.bouncycastle2.jce.provider.CertStoreCollectionSpi: parameter must be a CollectionCertStoreParameters object\n" + paramCertStoreParameters.toString());
    this.params = ((CollectionCertStoreParameters)paramCertStoreParameters);
  }

  public Collection engineGetCRLs(CRLSelector paramCRLSelector)
    throws CertStoreException
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.params.getCollection().iterator();
    if (paramCRLSelector == null)
      while (true)
      {
        if (!localIterator.hasNext())
          return localArrayList;
        localObject2 = localIterator.next();
        if ((localObject2 instanceof CRL))
          localArrayList.add(localObject2);
      }
    while (localIterator.hasNext())
    {
      Object localObject2;
      Object localObject1 = localIterator.next();
      if (((localObject1 instanceof CRL)) && (paramCRLSelector.match((CRL)localObject1)))
        localArrayList.add(localObject1);
    }
    return localArrayList;
  }

  public Collection engineGetCertificates(CertSelector paramCertSelector)
    throws CertStoreException
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.params.getCollection().iterator();
    if (paramCertSelector == null)
      while (true)
      {
        if (!localIterator.hasNext())
          return localArrayList;
        localObject2 = localIterator.next();
        if ((localObject2 instanceof Certificate))
          localArrayList.add(localObject2);
      }
    while (localIterator.hasNext())
    {
      Object localObject2;
      Object localObject1 = localIterator.next();
      if (((localObject1 instanceof Certificate)) && (paramCertSelector.match((Certificate)localObject1)))
        localArrayList.add(localObject1);
    }
    return localArrayList;
  }
}