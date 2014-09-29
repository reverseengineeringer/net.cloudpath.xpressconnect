package org.bouncycastle2.jce.provider;

import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.PKIXParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bouncycastle2.util.StoreException;
import org.bouncycastle2.x509.ExtendedPKIXParameters;
import org.bouncycastle2.x509.X509CRLStoreSelector;
import org.bouncycastle2.x509.X509Store;

public class PKIXCRLUtil
{
  private final Collection findCRLs(X509CRLStoreSelector paramX509CRLStoreSelector, List paramList)
    throws AnnotatedException
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramList.iterator();
    AnnotatedException localAnnotatedException = null;
    int i = 0;
    while (true)
    {
      if (!localIterator.hasNext())
      {
        if ((i != 0) || (localAnnotatedException == null))
          break;
        throw localAnnotatedException;
      }
      Object localObject = localIterator.next();
      if ((localObject instanceof X509Store))
      {
        X509Store localX509Store = (X509Store)localObject;
        try
        {
          localHashSet.addAll(localX509Store.getMatches(paramX509CRLStoreSelector));
          i = 1;
        }
        catch (StoreException localStoreException)
        {
          localAnnotatedException = new AnnotatedException("Exception searching in X.509 CRL store.", localStoreException);
        }
      }
      else
      {
        CertStore localCertStore = (CertStore)localObject;
        try
        {
          localHashSet.addAll(localCertStore.getCRLs(paramX509CRLStoreSelector));
          i = 1;
        }
        catch (CertStoreException localCertStoreException)
        {
          localAnnotatedException = new AnnotatedException("Exception searching in X.509 CRL store.", localCertStoreException);
        }
      }
    }
    return localHashSet;
  }

  public Set findCRLs(X509CRLStoreSelector paramX509CRLStoreSelector, PKIXParameters paramPKIXParameters)
    throws AnnotatedException
  {
    HashSet localHashSet = new HashSet();
    try
    {
      localHashSet.addAll(findCRLs(paramX509CRLStoreSelector, paramPKIXParameters.getCertStores()));
      return localHashSet;
    }
    catch (AnnotatedException localAnnotatedException)
    {
      throw new AnnotatedException("Exception obtaining complete CRLs.", localAnnotatedException);
    }
  }

  public Set findCRLs(X509CRLStoreSelector paramX509CRLStoreSelector, ExtendedPKIXParameters paramExtendedPKIXParameters, Date paramDate)
    throws AnnotatedException
  {
    HashSet localHashSet1 = new HashSet();
    while (true)
    {
      HashSet localHashSet2;
      Date localDate;
      Iterator localIterator;
      try
      {
        localHashSet1.addAll(findCRLs(paramX509CRLStoreSelector, paramExtendedPKIXParameters.getAdditionalStores()));
        localHashSet1.addAll(findCRLs(paramX509CRLStoreSelector, paramExtendedPKIXParameters.getStores()));
        localHashSet1.addAll(findCRLs(paramX509CRLStoreSelector, paramExtendedPKIXParameters.getCertStores()));
        localHashSet2 = new HashSet();
        localDate = paramDate;
        if (paramExtendedPKIXParameters.getDate() != null)
          localDate = paramExtendedPKIXParameters.getDate();
        localIterator = localHashSet1.iterator();
        if (!localIterator.hasNext())
          return localHashSet2;
      }
      catch (AnnotatedException localAnnotatedException)
      {
        throw new AnnotatedException("Exception obtaining complete CRLs.", localAnnotatedException);
      }
      X509CRL localX509CRL = (X509CRL)localIterator.next();
      if (localX509CRL.getNextUpdate().after(localDate))
      {
        X509Certificate localX509Certificate = paramX509CRLStoreSelector.getCertificateChecking();
        if (localX509Certificate != null)
        {
          if (localX509CRL.getThisUpdate().before(localX509Certificate.getNotAfter()))
            localHashSet2.add(localX509CRL);
        }
        else
          localHashSet2.add(localX509CRL);
      }
    }
  }
}