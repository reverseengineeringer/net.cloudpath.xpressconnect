package org.bouncycastle2.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderSpi;
import java.security.cert.CertPathParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.bouncycastle2.jce.exception.ExtCertPathBuilderException;
import org.bouncycastle2.util.Selector;
import org.bouncycastle2.x509.ExtendedPKIXBuilderParameters;
import org.bouncycastle2.x509.X509CertStoreSelector;

public class PKIXCertPathBuilderSpi extends CertPathBuilderSpi
{
  private Exception certPathException;

  // ERROR //
  protected CertPathBuilderResult build(X509Certificate paramX509Certificate, ExtendedPKIXBuilderParameters paramExtendedPKIXBuilderParameters, java.util.List paramList)
  {
    // Byte code:
    //   0: aload_3
    //   1: aload_1
    //   2: invokeinterface 24 2 0
    //   7: ifeq +9 -> 16
    //   10: aconst_null
    //   11: astore 5
    //   13: aload 5
    //   15: areturn
    //   16: aload_2
    //   17: invokevirtual 30	org/bouncycastle2/x509/ExtendedPKIXBuilderParameters:getExcludedCerts	()Ljava/util/Set;
    //   20: aload_1
    //   21: invokeinterface 33 2 0
    //   26: ifeq +5 -> 31
    //   29: aconst_null
    //   30: areturn
    //   31: aload_2
    //   32: invokevirtual 37	org/bouncycastle2/x509/ExtendedPKIXBuilderParameters:getMaxPathLength	()I
    //   35: iconst_m1
    //   36: if_icmpeq +20 -> 56
    //   39: iconst_m1
    //   40: aload_3
    //   41: invokeinterface 40 1 0
    //   46: iadd
    //   47: aload_2
    //   48: invokevirtual 37	org/bouncycastle2/x509/ExtendedPKIXBuilderParameters:getMaxPathLength	()I
    //   51: if_icmple +5 -> 56
    //   54: aconst_null
    //   55: areturn
    //   56: aload_3
    //   57: aload_1
    //   58: invokeinterface 43 2 0
    //   63: pop
    //   64: aconst_null
    //   65: astore 5
    //   67: ldc 45
    //   69: getstatic 51	org/bouncycastle2/jce/provider/BouncyCastleProvider:PROVIDER_NAME	Ljava/lang/String;
    //   72: invokestatic 57	java/security/cert/CertificateFactory:getInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/cert/CertificateFactory;
    //   75: astore 7
    //   77: ldc 59
    //   79: getstatic 51	org/bouncycastle2/jce/provider/BouncyCastleProvider:PROVIDER_NAME	Ljava/lang/String;
    //   82: invokestatic 64	java/security/cert/CertPathValidator:getInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/cert/CertPathValidator;
    //   85: astore 8
    //   87: aload_1
    //   88: aload_2
    //   89: invokevirtual 67	org/bouncycastle2/x509/ExtendedPKIXBuilderParameters:getTrustAnchors	()Ljava/util/Set;
    //   92: aload_2
    //   93: invokevirtual 71	org/bouncycastle2/x509/ExtendedPKIXBuilderParameters:getSigProvider	()Ljava/lang/String;
    //   96: invokestatic 77	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:findTrustAnchor	(Ljava/security/cert/X509Certificate;Ljava/util/Set;Ljava/lang/String;)Ljava/security/cert/TrustAnchor;
    //   99: astore 11
    //   101: aload 11
    //   103: ifnull +117 -> 220
    //   106: aload 7
    //   108: aload_3
    //   109: invokevirtual 81	java/security/cert/CertificateFactory:generateCertPath	(Ljava/util/List;)Ljava/security/cert/CertPath;
    //   112: astore 20
    //   114: aload 8
    //   116: aload 20
    //   118: aload_2
    //   119: invokevirtual 85	java/security/cert/CertPathValidator:validate	(Ljava/security/cert/CertPath;Ljava/security/cert/CertPathParameters;)Ljava/security/cert/CertPathValidatorResult;
    //   122: checkcast 87	java/security/cert/PKIXCertPathValidatorResult
    //   125: astore 22
    //   127: new 89	java/security/cert/PKIXCertPathBuilderResult
    //   130: dup
    //   131: aload 20
    //   133: aload 22
    //   135: invokevirtual 93	java/security/cert/PKIXCertPathValidatorResult:getTrustAnchor	()Ljava/security/cert/TrustAnchor;
    //   138: aload 22
    //   140: invokevirtual 97	java/security/cert/PKIXCertPathValidatorResult:getPolicyTree	()Ljava/security/cert/PolicyNode;
    //   143: aload 22
    //   145: invokevirtual 101	java/security/cert/PKIXCertPathValidatorResult:getPublicKey	()Ljava/security/PublicKey;
    //   148: invokespecial 104	java/security/cert/PKIXCertPathBuilderResult:<init>	(Ljava/security/cert/CertPath;Ljava/security/cert/TrustAnchor;Ljava/security/cert/PolicyNode;Ljava/security/PublicKey;)V
    //   151: astore 23
    //   153: aload 23
    //   155: areturn
    //   156: astore 6
    //   158: new 106	java/lang/RuntimeException
    //   161: dup
    //   162: ldc 108
    //   164: invokespecial 111	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   167: athrow
    //   168: astore 19
    //   170: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   173: dup
    //   174: ldc 113
    //   176: aload 19
    //   178: invokespecial 116	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   181: athrow
    //   182: astore 9
    //   184: aload_0
    //   185: aload 9
    //   187: putfield 118	org/bouncycastle2/jce/provider/PKIXCertPathBuilderSpi:certPathException	Ljava/lang/Exception;
    //   190: aload 5
    //   192: ifnonnull -179 -> 13
    //   195: aload_3
    //   196: aload_1
    //   197: invokeinterface 121 2 0
    //   202: pop
    //   203: aload 5
    //   205: areturn
    //   206: astore 21
    //   208: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   211: dup
    //   212: ldc 123
    //   214: aload 21
    //   216: invokespecial 116	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   219: athrow
    //   220: aload_1
    //   221: aload_2
    //   222: invokestatic 127	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:addAdditionalStoresFromAltNames	(Ljava/security/cert/X509Certificate;Lorg/bouncycastle2/x509/ExtendedPKIXParameters;)V
    //   225: new 129	java/util/HashSet
    //   228: dup
    //   229: invokespecial 130	java/util/HashSet:<init>	()V
    //   232: astore 13
    //   234: aload 13
    //   236: aload_1
    //   237: aload_2
    //   238: invokestatic 134	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:findIssuerCerts	(Ljava/security/cert/X509Certificate;Lorg/bouncycastle2/x509/ExtendedPKIXBuilderParameters;)Ljava/util/Collection;
    //   241: invokeinterface 140 2 0
    //   246: pop
    //   247: aload 13
    //   249: invokeinterface 144 1 0
    //   254: istore 16
    //   256: aconst_null
    //   257: astore 5
    //   259: iload 16
    //   261: ifeq +41 -> 302
    //   264: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   267: dup
    //   268: ldc 146
    //   270: invokespecial 147	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;)V
    //   273: athrow
    //   274: astore 12
    //   276: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   279: dup
    //   280: ldc 149
    //   282: aload 12
    //   284: invokespecial 116	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   287: athrow
    //   288: astore 14
    //   290: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   293: dup
    //   294: ldc 151
    //   296: aload 14
    //   298: invokespecial 116	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   301: athrow
    //   302: aload 13
    //   304: invokeinterface 155 1 0
    //   309: astore 17
    //   311: aload 17
    //   313: invokeinterface 160 1 0
    //   318: ifeq -128 -> 190
    //   321: aload 5
    //   323: ifnonnull -133 -> 190
    //   326: aload_0
    //   327: aload 17
    //   329: invokeinterface 164 1 0
    //   334: checkcast 166	java/security/cert/X509Certificate
    //   337: aload_2
    //   338: aload_3
    //   339: invokevirtual 168	org/bouncycastle2/jce/provider/PKIXCertPathBuilderSpi:build	(Ljava/security/cert/X509Certificate;Lorg/bouncycastle2/x509/ExtendedPKIXBuilderParameters;Ljava/util/List;)Ljava/security/cert/CertPathBuilderResult;
    //   342: astore 18
    //   344: aload 18
    //   346: astore 5
    //   348: goto -37 -> 311
    //
    // Exception table:
    //   from	to	target	type
    //   67	87	156	java/lang/Exception
    //   106	114	168	java/lang/Exception
    //   87	101	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   106	114	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   114	127	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   127	153	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   170	182	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   208	220	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   220	225	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   225	234	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   247	256	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   264	274	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   276	288	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   290	302	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   302	311	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   311	321	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   326	344	182	org/bouncycastle2/jce/provider/AnnotatedException
    //   114	127	206	java/lang/Exception
    //   220	225	274	java/security/cert/CertificateParsingException
    //   234	247	288	org/bouncycastle2/jce/provider/AnnotatedException
  }

  public CertPathBuilderResult engineBuild(CertPathParameters paramCertPathParameters)
    throws CertPathBuilderException, InvalidAlgorithmParameterException
  {
    if ((!(paramCertPathParameters instanceof PKIXBuilderParameters)) && (!(paramCertPathParameters instanceof ExtendedPKIXBuilderParameters)))
      throw new InvalidAlgorithmParameterException("Parameters must be an instance of " + PKIXBuilderParameters.class.getName() + " or " + ExtendedPKIXBuilderParameters.class.getName() + ".");
    if ((paramCertPathParameters instanceof ExtendedPKIXBuilderParameters));
    ArrayList localArrayList;
    Selector localSelector;
    for (ExtendedPKIXBuilderParameters localExtendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)paramCertPathParameters; ; localExtendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)ExtendedPKIXBuilderParameters.getInstance((PKIXBuilderParameters)paramCertPathParameters))
    {
      localArrayList = new ArrayList();
      localSelector = localExtendedPKIXBuilderParameters.getTargetConstraints();
      if ((localSelector instanceof X509CertStoreSelector))
        break;
      throw new CertPathBuilderException("TargetConstraints must be an instance of " + X509CertStoreSelector.class.getName() + " for " + getClass().getName() + " class.");
    }
    Collection localCollection;
    try
    {
      localCollection = CertPathValidatorUtilities.findCertificates((X509CertStoreSelector)localSelector, localExtendedPKIXBuilderParameters.getStores());
      localCollection.addAll(CertPathValidatorUtilities.findCertificates((X509CertStoreSelector)localSelector, localExtendedPKIXBuilderParameters.getCertStores()));
      if (localCollection.isEmpty())
        throw new CertPathBuilderException("No certificate found matching targetContraints.");
    }
    catch (AnnotatedException localAnnotatedException)
    {
      throw new ExtCertPathBuilderException("Error finding target certificate.", localAnnotatedException);
    }
    CertPathBuilderResult localCertPathBuilderResult = null;
    Iterator localIterator = localCollection.iterator();
    while (true)
    {
      if ((!localIterator.hasNext()) || (localCertPathBuilderResult != null))
      {
        if ((localCertPathBuilderResult != null) || (this.certPathException == null))
          break label330;
        if (!(this.certPathException instanceof AnnotatedException))
          break;
        throw new CertPathBuilderException(this.certPathException.getMessage(), this.certPathException.getCause());
      }
      localCertPathBuilderResult = build((X509Certificate)localIterator.next(), localExtendedPKIXBuilderParameters, localArrayList);
    }
    throw new CertPathBuilderException("Possible certificate chain could not be validated.", this.certPathException);
    label330: if ((localCertPathBuilderResult == null) && (this.certPathException == null))
      throw new CertPathBuilderException("Unable to find certificate chain.");
    return localCertPathBuilderResult;
  }
}