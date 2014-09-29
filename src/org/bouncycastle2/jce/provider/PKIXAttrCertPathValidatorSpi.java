package org.bouncycastle2.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPath;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertPathValidatorSpi;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import org.bouncycastle2.jce.exception.ExtCertPathValidatorException;
import org.bouncycastle2.util.Selector;
import org.bouncycastle2.x509.ExtendedPKIXParameters;
import org.bouncycastle2.x509.X509AttributeCertStoreSelector;
import org.bouncycastle2.x509.X509AttributeCertificate;

public class PKIXAttrCertPathValidatorSpi extends CertPathValidatorSpi
{
  public CertPathValidatorResult engineValidate(CertPath paramCertPath, CertPathParameters paramCertPathParameters)
    throws CertPathValidatorException, InvalidAlgorithmParameterException
  {
    if (!(paramCertPathParameters instanceof ExtendedPKIXParameters))
      throw new InvalidAlgorithmParameterException("Parameters must be a " + ExtendedPKIXParameters.class.getName() + " instance.");
    ExtendedPKIXParameters localExtendedPKIXParameters = (ExtendedPKIXParameters)paramCertPathParameters;
    Selector localSelector = localExtendedPKIXParameters.getTargetConstraints();
    if (!(localSelector instanceof X509AttributeCertStoreSelector))
      throw new InvalidAlgorithmParameterException("TargetConstraints must be an instance of " + X509AttributeCertStoreSelector.class.getName() + " for " + getClass().getName() + " class.");
    X509AttributeCertificate localX509AttributeCertificate = ((X509AttributeCertStoreSelector)localSelector).getAttributeCert();
    CertPath localCertPath = RFC3281CertPathUtilities.processAttrCert1(localX509AttributeCertificate, localExtendedPKIXParameters);
    CertPathValidatorResult localCertPathValidatorResult = RFC3281CertPathUtilities.processAttrCert2(paramCertPath, localExtendedPKIXParameters);
    X509Certificate localX509Certificate = (X509Certificate)paramCertPath.getCertificates().get(0);
    RFC3281CertPathUtilities.processAttrCert3(localX509Certificate, localExtendedPKIXParameters);
    RFC3281CertPathUtilities.processAttrCert4(localX509Certificate, localExtendedPKIXParameters);
    RFC3281CertPathUtilities.processAttrCert5(localX509AttributeCertificate, localExtendedPKIXParameters);
    RFC3281CertPathUtilities.processAttrCert7(localX509AttributeCertificate, paramCertPath, localCertPath, localExtendedPKIXParameters);
    RFC3281CertPathUtilities.additionalChecks(localX509AttributeCertificate, localExtendedPKIXParameters);
    try
    {
      Date localDate = CertPathValidatorUtilities.getValidCertDateFromValidityModel(localExtendedPKIXParameters, null, -1);
      RFC3281CertPathUtilities.checkCRLs(localX509AttributeCertificate, localExtendedPKIXParameters, localX509Certificate, localDate, paramCertPath.getCertificates());
      return localCertPathValidatorResult;
    }
    catch (AnnotatedException localAnnotatedException)
    {
      throw new ExtCertPathValidatorException("Could not get validity date from attribute certificate.", localAnnotatedException);
    }
  }
}