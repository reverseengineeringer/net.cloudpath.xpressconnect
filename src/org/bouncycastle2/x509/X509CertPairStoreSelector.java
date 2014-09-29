package org.bouncycastle2.x509;

import org.bouncycastle2.util.Selector;

public class X509CertPairStoreSelector
  implements Selector
{
  private X509CertificatePair certPair;
  private X509CertStoreSelector forwardSelector;
  private X509CertStoreSelector reverseSelector;

  public Object clone()
  {
    X509CertPairStoreSelector localX509CertPairStoreSelector = new X509CertPairStoreSelector();
    localX509CertPairStoreSelector.certPair = this.certPair;
    if (this.forwardSelector != null)
      localX509CertPairStoreSelector.setForwardSelector((X509CertStoreSelector)this.forwardSelector.clone());
    if (this.reverseSelector != null)
      localX509CertPairStoreSelector.setReverseSelector((X509CertStoreSelector)this.reverseSelector.clone());
    return localX509CertPairStoreSelector;
  }

  public X509CertificatePair getCertPair()
  {
    return this.certPair;
  }

  public X509CertStoreSelector getForwardSelector()
  {
    return this.forwardSelector;
  }

  public X509CertStoreSelector getReverseSelector()
  {
    return this.reverseSelector;
  }

  public boolean match(Object paramObject)
  {
    try
    {
      if (!(paramObject instanceof X509CertificatePair))
        return false;
      X509CertificatePair localX509CertificatePair = (X509CertificatePair)paramObject;
      if (((this.forwardSelector == null) || (this.forwardSelector.match(localX509CertificatePair.getForward()))) && ((this.reverseSelector == null) || (this.reverseSelector.match(localX509CertificatePair.getReverse()))))
      {
        if (this.certPair != null)
        {
          boolean bool = this.certPair.equals(paramObject);
          return bool;
        }
        return true;
      }
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  public void setCertPair(X509CertificatePair paramX509CertificatePair)
  {
    this.certPair = paramX509CertificatePair;
  }

  public void setForwardSelector(X509CertStoreSelector paramX509CertStoreSelector)
  {
    this.forwardSelector = paramX509CertStoreSelector;
  }

  public void setReverseSelector(X509CertStoreSelector paramX509CertStoreSelector)
  {
    this.reverseSelector = paramX509CertStoreSelector;
  }
}