package org.bouncycastle2.x509;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bouncycastle2.util.Selector;
import org.bouncycastle2.util.Store;

public class ExtendedPKIXParameters extends PKIXParameters
{
  public static final int CHAIN_VALIDITY_MODEL = 1;
  public static final int PKIX_VALIDITY_MODEL;
  private boolean additionalLocationsEnabled;
  private List additionalStores = new ArrayList();
  private Set attrCertCheckers = new HashSet();
  private Set necessaryACAttributes = new HashSet();
  private Set prohibitedACAttributes = new HashSet();
  private Selector selector;
  private List stores = new ArrayList();
  private Set trustedACIssuers = new HashSet();
  private boolean useDeltas = false;
  private int validityModel = 0;

  public ExtendedPKIXParameters(Set paramSet)
    throws InvalidAlgorithmParameterException
  {
    super(paramSet);
  }

  public static ExtendedPKIXParameters getInstance(PKIXParameters paramPKIXParameters)
  {
    try
    {
      ExtendedPKIXParameters localExtendedPKIXParameters = new ExtendedPKIXParameters(paramPKIXParameters.getTrustAnchors());
      localExtendedPKIXParameters.setParams(paramPKIXParameters);
      return localExtendedPKIXParameters;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException.getMessage());
    }
  }

  public void addAddionalStore(Store paramStore)
  {
    addAdditionalStore(paramStore);
  }

  public void addAdditionalStore(Store paramStore)
  {
    if (paramStore != null)
      this.additionalStores.add(paramStore);
  }

  public void addStore(Store paramStore)
  {
    if (paramStore != null)
      this.stores.add(paramStore);
  }

  public Object clone()
  {
    try
    {
      ExtendedPKIXParameters localExtendedPKIXParameters = new ExtendedPKIXParameters(getTrustAnchors());
      localExtendedPKIXParameters.setParams(this);
      return localExtendedPKIXParameters;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException.getMessage());
    }
  }

  public List getAdditionalStores()
  {
    return Collections.unmodifiableList(this.additionalStores);
  }

  public Set getAttrCertCheckers()
  {
    return Collections.unmodifiableSet(this.attrCertCheckers);
  }

  public Set getNecessaryACAttributes()
  {
    return Collections.unmodifiableSet(this.necessaryACAttributes);
  }

  public Set getProhibitedACAttributes()
  {
    return Collections.unmodifiableSet(this.prohibitedACAttributes);
  }

  public List getStores()
  {
    return Collections.unmodifiableList(new ArrayList(this.stores));
  }

  public Selector getTargetConstraints()
  {
    if (this.selector != null)
      return (Selector)this.selector.clone();
    return null;
  }

  public Set getTrustedACIssuers()
  {
    return Collections.unmodifiableSet(this.trustedACIssuers);
  }

  public int getValidityModel()
  {
    return this.validityModel;
  }

  public boolean isAdditionalLocationsEnabled()
  {
    return this.additionalLocationsEnabled;
  }

  public boolean isUseDeltasEnabled()
  {
    return this.useDeltas;
  }

  public void setAdditionalLocationsEnabled(boolean paramBoolean)
  {
    this.additionalLocationsEnabled = paramBoolean;
  }

  public void setAttrCertCheckers(Set paramSet)
  {
    if (paramSet == null)
    {
      this.attrCertCheckers.clear();
      return;
    }
    Iterator localIterator = paramSet.iterator();
    do
      if (!localIterator.hasNext())
      {
        this.attrCertCheckers.clear();
        this.attrCertCheckers.addAll(paramSet);
        return;
      }
    while ((localIterator.next() instanceof PKIXAttrCertChecker));
    throw new ClassCastException("All elements of set must be of type " + PKIXAttrCertChecker.class.getName() + ".");
  }

  public void setCertStores(List paramList)
  {
    Iterator localIterator;
    if (paramList != null)
      localIterator = paramList.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      addCertStore((CertStore)localIterator.next());
    }
  }

  public void setNecessaryACAttributes(Set paramSet)
  {
    if (paramSet == null)
    {
      this.necessaryACAttributes.clear();
      return;
    }
    Iterator localIterator = paramSet.iterator();
    do
      if (!localIterator.hasNext())
      {
        this.necessaryACAttributes.clear();
        this.necessaryACAttributes.addAll(paramSet);
        return;
      }
    while ((localIterator.next() instanceof String));
    throw new ClassCastException("All elements of set must be of type String.");
  }

  protected void setParams(PKIXParameters paramPKIXParameters)
  {
    setDate(paramPKIXParameters.getDate());
    setCertPathCheckers(paramPKIXParameters.getCertPathCheckers());
    setCertStores(paramPKIXParameters.getCertStores());
    setAnyPolicyInhibited(paramPKIXParameters.isAnyPolicyInhibited());
    setExplicitPolicyRequired(paramPKIXParameters.isExplicitPolicyRequired());
    setPolicyMappingInhibited(paramPKIXParameters.isPolicyMappingInhibited());
    setRevocationEnabled(paramPKIXParameters.isRevocationEnabled());
    setInitialPolicies(paramPKIXParameters.getInitialPolicies());
    setPolicyQualifiersRejected(paramPKIXParameters.getPolicyQualifiersRejected());
    setSigProvider(paramPKIXParameters.getSigProvider());
    setTargetCertConstraints(paramPKIXParameters.getTargetCertConstraints());
    while (true)
    {
      ExtendedPKIXParameters localExtendedPKIXParameters;
      try
      {
        setTrustAnchors(paramPKIXParameters.getTrustAnchors());
        if ((paramPKIXParameters instanceof ExtendedPKIXParameters))
        {
          localExtendedPKIXParameters = (ExtendedPKIXParameters)paramPKIXParameters;
          this.validityModel = localExtendedPKIXParameters.validityModel;
          this.useDeltas = localExtendedPKIXParameters.useDeltas;
          this.additionalLocationsEnabled = localExtendedPKIXParameters.additionalLocationsEnabled;
          if (localExtendedPKIXParameters.selector == null)
          {
            localSelector = null;
            this.selector = localSelector;
            this.stores = new ArrayList(localExtendedPKIXParameters.stores);
            this.additionalStores = new ArrayList(localExtendedPKIXParameters.additionalStores);
            this.trustedACIssuers = new HashSet(localExtendedPKIXParameters.trustedACIssuers);
            this.prohibitedACAttributes = new HashSet(localExtendedPKIXParameters.prohibitedACAttributes);
            this.necessaryACAttributes = new HashSet(localExtendedPKIXParameters.necessaryACAttributes);
            this.attrCertCheckers = new HashSet(localExtendedPKIXParameters.attrCertCheckers);
          }
        }
        else
        {
          return;
        }
      }
      catch (Exception localException)
      {
        throw new RuntimeException(localException.getMessage());
      }
      Selector localSelector = (Selector)localExtendedPKIXParameters.selector.clone();
    }
  }

  public void setProhibitedACAttributes(Set paramSet)
  {
    if (paramSet == null)
    {
      this.prohibitedACAttributes.clear();
      return;
    }
    Iterator localIterator = paramSet.iterator();
    do
      if (!localIterator.hasNext())
      {
        this.prohibitedACAttributes.clear();
        this.prohibitedACAttributes.addAll(paramSet);
        return;
      }
    while ((localIterator.next() instanceof String));
    throw new ClassCastException("All elements of set must be of type String.");
  }

  public void setStores(List paramList)
  {
    if (paramList == null)
    {
      this.stores = new ArrayList();
      return;
    }
    Iterator localIterator = paramList.iterator();
    do
      if (!localIterator.hasNext())
      {
        this.stores = new ArrayList(paramList);
        return;
      }
    while ((localIterator.next() instanceof Store));
    throw new ClassCastException("All elements of list must be of type org.bouncycastle2.util.Store.");
  }

  public void setTargetCertConstraints(CertSelector paramCertSelector)
  {
    super.setTargetCertConstraints(paramCertSelector);
    if (paramCertSelector != null)
    {
      this.selector = X509CertStoreSelector.getInstance((X509CertSelector)paramCertSelector);
      return;
    }
    this.selector = null;
  }

  public void setTargetConstraints(Selector paramSelector)
  {
    if (paramSelector != null)
    {
      this.selector = ((Selector)paramSelector.clone());
      return;
    }
    this.selector = null;
  }

  public void setTrustedACIssuers(Set paramSet)
  {
    if (paramSet == null)
    {
      this.trustedACIssuers.clear();
      return;
    }
    Iterator localIterator = paramSet.iterator();
    do
      if (!localIterator.hasNext())
      {
        this.trustedACIssuers.clear();
        this.trustedACIssuers.addAll(paramSet);
        return;
      }
    while ((localIterator.next() instanceof TrustAnchor));
    throw new ClassCastException("All elements of set must be of type " + TrustAnchor.class.getName() + ".");
  }

  public void setUseDeltasEnabled(boolean paramBoolean)
  {
    this.useDeltas = paramBoolean;
  }

  public void setValidityModel(int paramInt)
  {
    this.validityModel = paramInt;
  }
}