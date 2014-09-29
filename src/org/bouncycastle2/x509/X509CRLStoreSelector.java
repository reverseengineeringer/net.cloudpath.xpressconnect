package org.bouncycastle2.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CRL;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLSelector;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.util.Arrays;
import org.bouncycastle2.util.Selector;
import org.bouncycastle2.x509.extension.X509ExtensionUtil;

public class X509CRLStoreSelector extends X509CRLSelector
  implements Selector
{
  private X509AttributeCertificate attrCertChecking;
  private boolean completeCRLEnabled = false;
  private boolean deltaCRLIndicator = false;
  private byte[] issuingDistributionPoint = null;
  private boolean issuingDistributionPointEnabled = false;
  private BigInteger maxBaseCRLNumber = null;

  public static X509CRLStoreSelector getInstance(X509CRLSelector paramX509CRLSelector)
  {
    if (paramX509CRLSelector == null)
      throw new IllegalArgumentException("cannot create from null selector");
    X509CRLStoreSelector localX509CRLStoreSelector = new X509CRLStoreSelector();
    localX509CRLStoreSelector.setCertificateChecking(paramX509CRLSelector.getCertificateChecking());
    localX509CRLStoreSelector.setDateAndTime(paramX509CRLSelector.getDateAndTime());
    try
    {
      localX509CRLStoreSelector.setIssuerNames(paramX509CRLSelector.getIssuerNames());
      localX509CRLStoreSelector.setIssuers(paramX509CRLSelector.getIssuers());
      localX509CRLStoreSelector.setMaxCRLNumber(paramX509CRLSelector.getMaxCRL());
      localX509CRLStoreSelector.setMinCRLNumber(paramX509CRLSelector.getMinCRL());
      return localX509CRLStoreSelector;
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException(localIOException.getMessage());
    }
  }

  public Object clone()
  {
    X509CRLStoreSelector localX509CRLStoreSelector = getInstance(this);
    localX509CRLStoreSelector.deltaCRLIndicator = this.deltaCRLIndicator;
    localX509CRLStoreSelector.completeCRLEnabled = this.completeCRLEnabled;
    localX509CRLStoreSelector.maxBaseCRLNumber = this.maxBaseCRLNumber;
    localX509CRLStoreSelector.attrCertChecking = this.attrCertChecking;
    localX509CRLStoreSelector.issuingDistributionPointEnabled = this.issuingDistributionPointEnabled;
    localX509CRLStoreSelector.issuingDistributionPoint = Arrays.clone(this.issuingDistributionPoint);
    return localX509CRLStoreSelector;
  }

  public X509AttributeCertificate getAttrCertificateChecking()
  {
    return this.attrCertChecking;
  }

  public byte[] getIssuingDistributionPoint()
  {
    return Arrays.clone(this.issuingDistributionPoint);
  }

  public BigInteger getMaxBaseCRLNumber()
  {
    return this.maxBaseCRLNumber;
  }

  public boolean isCompleteCRLEnabled()
  {
    return this.completeCRLEnabled;
  }

  public boolean isDeltaCRLIndicatorEnabled()
  {
    return this.deltaCRLIndicator;
  }

  public boolean isIssuingDistributionPointEnabled()
  {
    return this.issuingDistributionPointEnabled;
  }

  public boolean match(Object paramObject)
  {
    if (!(paramObject instanceof X509CRL))
      return false;
    X509CRL localX509CRL = (X509CRL)paramObject;
    byte[] arrayOfByte2;
    do
      try
      {
        byte[] arrayOfByte1 = localX509CRL.getExtensionValue(X509Extensions.DeltaCRLIndicator.getId());
        Object localObject = null;
        if (arrayOfByte1 != null)
        {
          DERInteger localDERInteger = DERInteger.getInstance(X509ExtensionUtil.fromExtensionValue(arrayOfByte1));
          localObject = localDERInteger;
        }
        if (((isDeltaCRLIndicatorEnabled()) && (localObject == null)) || ((isCompleteCRLEnabled()) && (localObject != null)) || ((localObject != null) && (this.maxBaseCRLNumber != null) && (localObject.getPositiveValue().compareTo(this.maxBaseCRLNumber) == 1)))
          break;
        if (this.issuingDistributionPointEnabled)
        {
          arrayOfByte2 = localX509CRL.getExtensionValue(X509Extensions.IssuingDistributionPoint.getId());
          if (this.issuingDistributionPoint != null)
            continue;
          if (arrayOfByte2 != null)
            break;
        }
        return super.match((X509CRL)paramObject);
      }
      catch (Exception localException)
      {
        return false;
      }
    while (Arrays.areEqual(arrayOfByte2, this.issuingDistributionPoint));
    return false;
  }

  public boolean match(CRL paramCRL)
  {
    return match(paramCRL);
  }

  public void setAttrCertificateChecking(X509AttributeCertificate paramX509AttributeCertificate)
  {
    this.attrCertChecking = paramX509AttributeCertificate;
  }

  public void setCompleteCRLEnabled(boolean paramBoolean)
  {
    this.completeCRLEnabled = paramBoolean;
  }

  public void setDeltaCRLIndicatorEnabled(boolean paramBoolean)
  {
    this.deltaCRLIndicator = paramBoolean;
  }

  public void setIssuingDistributionPoint(byte[] paramArrayOfByte)
  {
    this.issuingDistributionPoint = Arrays.clone(paramArrayOfByte);
  }

  public void setIssuingDistributionPointEnabled(boolean paramBoolean)
  {
    this.issuingDistributionPointEnabled = paramBoolean;
  }

  public void setMaxBaseCRLNumber(BigInteger paramBigInteger)
  {
    this.maxBaseCRLNumber = paramBigInteger;
  }
}