package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Principal;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderSpi;
import java.security.cert.CertPathParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.jce.exception.ExtCertPathBuilderException;
import org.bouncycastle2.util.Selector;
import org.bouncycastle2.x509.AttributeCertificateIssuer;
import org.bouncycastle2.x509.ExtendedPKIXBuilderParameters;
import org.bouncycastle2.x509.X509AttributeCertStoreSelector;
import org.bouncycastle2.x509.X509AttributeCertificate;
import org.bouncycastle2.x509.X509CertStoreSelector;

public class PKIXAttrCertPathBuilderSpi extends CertPathBuilderSpi
{
  private Exception certPathException;

  // ERROR //
  private CertPathBuilderResult build(X509AttributeCertificate paramX509AttributeCertificate, X509Certificate paramX509Certificate, ExtendedPKIXBuilderParameters paramExtendedPKIXBuilderParameters, java.util.List paramList)
  {
    // Byte code:
    //   0: aload 4
    //   2: aload_2
    //   3: invokeinterface 24 2 0
    //   8: ifeq +9 -> 17
    //   11: aconst_null
    //   12: astore 6
    //   14: aload 6
    //   16: areturn
    //   17: aload_3
    //   18: invokevirtual 30	org/bouncycastle2/x509/ExtendedPKIXBuilderParameters:getExcludedCerts	()Ljava/util/Set;
    //   21: aload_2
    //   22: invokeinterface 33 2 0
    //   27: ifeq +5 -> 32
    //   30: aconst_null
    //   31: areturn
    //   32: aload_3
    //   33: invokevirtual 37	org/bouncycastle2/x509/ExtendedPKIXBuilderParameters:getMaxPathLength	()I
    //   36: iconst_m1
    //   37: if_icmpeq +21 -> 58
    //   40: iconst_m1
    //   41: aload 4
    //   43: invokeinterface 40 1 0
    //   48: iadd
    //   49: aload_3
    //   50: invokevirtual 37	org/bouncycastle2/x509/ExtendedPKIXBuilderParameters:getMaxPathLength	()I
    //   53: if_icmple +5 -> 58
    //   56: aconst_null
    //   57: areturn
    //   58: aload 4
    //   60: aload_2
    //   61: invokeinterface 43 2 0
    //   66: pop
    //   67: aconst_null
    //   68: astore 6
    //   70: ldc 45
    //   72: getstatic 51	org/bouncycastle2/jce/provider/BouncyCastleProvider:PROVIDER_NAME	Ljava/lang/String;
    //   75: invokestatic 57	java/security/cert/CertificateFactory:getInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/cert/CertificateFactory;
    //   78: astore 8
    //   80: ldc 59
    //   82: getstatic 51	org/bouncycastle2/jce/provider/BouncyCastleProvider:PROVIDER_NAME	Ljava/lang/String;
    //   85: invokestatic 64	java/security/cert/CertPathValidator:getInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/cert/CertPathValidator;
    //   88: astore 9
    //   90: aload_2
    //   91: aload_3
    //   92: invokevirtual 67	org/bouncycastle2/x509/ExtendedPKIXBuilderParameters:getTrustAnchors	()Ljava/util/Set;
    //   95: aload_3
    //   96: invokevirtual 71	org/bouncycastle2/x509/ExtendedPKIXBuilderParameters:getSigProvider	()Ljava/lang/String;
    //   99: invokestatic 77	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:findTrustAnchor	(Ljava/security/cert/X509Certificate;Ljava/util/Set;Ljava/lang/String;)Ljava/security/cert/TrustAnchor;
    //   102: astore 12
    //   104: aload 12
    //   106: ifnull +128 -> 234
    //   109: aload 8
    //   111: aload 4
    //   113: invokevirtual 81	java/security/cert/CertificateFactory:generateCertPath	(Ljava/util/List;)Ljava/security/cert/CertPath;
    //   116: astore 22
    //   118: aload 9
    //   120: aload 22
    //   122: aload_3
    //   123: invokevirtual 85	java/security/cert/CertPathValidator:validate	(Ljava/security/cert/CertPath;Ljava/security/cert/CertPathParameters;)Ljava/security/cert/CertPathValidatorResult;
    //   126: checkcast 87	java/security/cert/PKIXCertPathValidatorResult
    //   129: astore 24
    //   131: new 89	java/security/cert/PKIXCertPathBuilderResult
    //   134: dup
    //   135: aload 22
    //   137: aload 24
    //   139: invokevirtual 93	java/security/cert/PKIXCertPathValidatorResult:getTrustAnchor	()Ljava/security/cert/TrustAnchor;
    //   142: aload 24
    //   144: invokevirtual 97	java/security/cert/PKIXCertPathValidatorResult:getPolicyTree	()Ljava/security/cert/PolicyNode;
    //   147: aload 24
    //   149: invokevirtual 101	java/security/cert/PKIXCertPathValidatorResult:getPublicKey	()Ljava/security/PublicKey;
    //   152: invokespecial 104	java/security/cert/PKIXCertPathBuilderResult:<init>	(Ljava/security/cert/CertPath;Ljava/security/cert/TrustAnchor;Ljava/security/cert/PolicyNode;Ljava/security/PublicKey;)V
    //   155: astore 25
    //   157: aload 25
    //   159: areturn
    //   160: astore 7
    //   162: new 106	java/lang/RuntimeException
    //   165: dup
    //   166: ldc 108
    //   168: invokespecial 111	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   171: athrow
    //   172: astore 21
    //   174: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   177: dup
    //   178: ldc 113
    //   180: aload 21
    //   182: invokespecial 116	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   185: athrow
    //   186: astore 10
    //   188: aload_0
    //   189: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   192: dup
    //   193: ldc 118
    //   195: aload 10
    //   197: invokespecial 116	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   200: putfield 120	org/bouncycastle2/jce/provider/PKIXAttrCertPathBuilderSpi:certPathException	Ljava/lang/Exception;
    //   203: aload 6
    //   205: ifnonnull -191 -> 14
    //   208: aload 4
    //   210: aload_2
    //   211: invokeinterface 123 2 0
    //   216: pop
    //   217: aload 6
    //   219: areturn
    //   220: astore 23
    //   222: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   225: dup
    //   226: ldc 125
    //   228: aload 23
    //   230: invokespecial 116	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   233: athrow
    //   234: aload_2
    //   235: aload_3
    //   236: invokestatic 129	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:addAdditionalStoresFromAltNames	(Ljava/security/cert/X509Certificate;Lorg/bouncycastle2/x509/ExtendedPKIXParameters;)V
    //   239: new 131	java/util/HashSet
    //   242: dup
    //   243: invokespecial 132	java/util/HashSet:<init>	()V
    //   246: astore 14
    //   248: aload 14
    //   250: aload_2
    //   251: aload_3
    //   252: invokestatic 136	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:findIssuerCerts	(Ljava/security/cert/X509Certificate;Lorg/bouncycastle2/x509/ExtendedPKIXBuilderParameters;)Ljava/util/Collection;
    //   255: invokeinterface 142 2 0
    //   260: pop
    //   261: aload 14
    //   263: invokeinterface 146 1 0
    //   268: istore 17
    //   270: aconst_null
    //   271: astore 6
    //   273: iload 17
    //   275: ifeq +41 -> 316
    //   278: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   281: dup
    //   282: ldc 148
    //   284: invokespecial 149	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;)V
    //   287: athrow
    //   288: astore 13
    //   290: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   293: dup
    //   294: ldc 151
    //   296: aload 13
    //   298: invokespecial 116	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   301: athrow
    //   302: astore 15
    //   304: new 16	org/bouncycastle2/jce/provider/AnnotatedException
    //   307: dup
    //   308: ldc 153
    //   310: aload 15
    //   312: invokespecial 116	org/bouncycastle2/jce/provider/AnnotatedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   315: athrow
    //   316: aload 14
    //   318: invokeinterface 157 1 0
    //   323: astore 18
    //   325: aload 18
    //   327: invokeinterface 162 1 0
    //   332: ifeq -129 -> 203
    //   335: aload 6
    //   337: ifnonnull -134 -> 203
    //   340: aload 18
    //   342: invokeinterface 166 1 0
    //   347: checkcast 168	java/security/cert/X509Certificate
    //   350: astore 19
    //   352: aload 19
    //   354: invokevirtual 172	java/security/cert/X509Certificate:getIssuerX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   357: aload 19
    //   359: invokevirtual 175	java/security/cert/X509Certificate:getSubjectX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   362: invokevirtual 180	javax/security/auth/x500/X500Principal:equals	(Ljava/lang/Object;)Z
    //   365: ifne -40 -> 325
    //   368: aload_0
    //   369: aload_1
    //   370: aload 19
    //   372: aload_3
    //   373: aload 4
    //   375: invokespecial 182	org/bouncycastle2/jce/provider/PKIXAttrCertPathBuilderSpi:build	(Lorg/bouncycastle2/x509/X509AttributeCertificate;Ljava/security/cert/X509Certificate;Lorg/bouncycastle2/x509/ExtendedPKIXBuilderParameters;Ljava/util/List;)Ljava/security/cert/CertPathBuilderResult;
    //   378: astore 20
    //   380: aload 20
    //   382: astore 6
    //   384: goto -59 -> 325
    //
    // Exception table:
    //   from	to	target	type
    //   70	90	160	java/lang/Exception
    //   109	118	172	java/lang/Exception
    //   90	104	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   109	118	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   118	131	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   131	157	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   174	186	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   222	234	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   234	239	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   239	248	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   261	270	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   278	288	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   290	302	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   304	316	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   316	325	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   325	335	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   340	380	186	org/bouncycastle2/jce/provider/AnnotatedException
    //   118	131	220	java/lang/Exception
    //   234	239	288	java/security/cert/CertificateParsingException
    //   248	261	302	org/bouncycastle2/jce/provider/AnnotatedException
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
      if ((localSelector instanceof X509AttributeCertStoreSelector))
        break;
      throw new CertPathBuilderException("TargetConstraints must be an instance of " + X509AttributeCertStoreSelector.class.getName() + " for " + getClass().getName() + " class.");
    }
    Collection localCollection;
    try
    {
      localCollection = CertPathValidatorUtilities.findCertificates((X509AttributeCertStoreSelector)localSelector, localExtendedPKIXBuilderParameters.getStores());
      if (localCollection.isEmpty())
        throw new CertPathBuilderException("No attribute certificate found matching targetContraints.");
    }
    catch (AnnotatedException localAnnotatedException1)
    {
      throw new ExtCertPathBuilderException("Error finding target attribute certificate.", localAnnotatedException1);
    }
    CertPathBuilderResult localCertPathBuilderResult = null;
    Iterator localIterator1 = localCollection.iterator();
    while (true)
      if ((!localIterator1.hasNext()) || (localCertPathBuilderResult != null))
      {
        if ((localCertPathBuilderResult == null) && (this.certPathException != null))
          throw new ExtCertPathBuilderException("Possible certificate chain could not be validated.", this.certPathException);
      }
      else
      {
        X509AttributeCertificate localX509AttributeCertificate = (X509AttributeCertificate)localIterator1.next();
        X509CertStoreSelector localX509CertStoreSelector = new X509CertStoreSelector();
        Principal[] arrayOfPrincipal = localX509AttributeCertificate.getIssuer().getPrincipals();
        HashSet localHashSet = new HashSet();
        int i = 0;
        while (true)
          if (i >= arrayOfPrincipal.length)
          {
            if (localHashSet.isEmpty())
              throw new CertPathBuilderException("Public key certificate for attribute certificate cannot be found.");
          }
          else
            try
            {
              if ((arrayOfPrincipal[i] instanceof X500Principal))
                localX509CertStoreSelector.setSubject(((X500Principal)arrayOfPrincipal[i]).getEncoded());
              localHashSet.addAll(CertPathValidatorUtilities.findCertificates(localX509CertStoreSelector, localExtendedPKIXBuilderParameters.getStores()));
              localHashSet.addAll(CertPathValidatorUtilities.findCertificates(localX509CertStoreSelector, localExtendedPKIXBuilderParameters.getCertStores()));
              i++;
            }
            catch (AnnotatedException localAnnotatedException2)
            {
              throw new ExtCertPathBuilderException("Public key certificate for attribute certificate cannot be searched.", localAnnotatedException2);
            }
            catch (IOException localIOException)
            {
              throw new ExtCertPathBuilderException("cannot encode X500Principal.", localIOException);
            }
        Iterator localIterator2 = localHashSet.iterator();
        while ((localIterator2.hasNext()) && (localCertPathBuilderResult == null))
          localCertPathBuilderResult = build(localX509AttributeCertificate, (X509Certificate)localIterator2.next(), localExtendedPKIXBuilderParameters, localArrayList);
      }
    if ((localCertPathBuilderResult == null) && (this.certPathException == null))
      throw new CertPathBuilderException("Unable to find certificate chain.");
    return localCertPathBuilderResult;
  }
}