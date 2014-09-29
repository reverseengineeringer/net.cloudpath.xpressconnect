package org.bouncycastle2.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXParameters;
import java.security.cert.PolicyNode;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.x509.AccessDescription;
import org.bouncycastle2.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle2.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle2.asn1.x509.BasicConstraints;
import org.bouncycastle2.asn1.x509.CRLDistPoint;
import org.bouncycastle2.asn1.x509.DistributionPoint;
import org.bouncycastle2.asn1.x509.DistributionPointName;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.GeneralNames;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.asn1.x509.qualified.Iso4217CurrencyCode;
import org.bouncycastle2.asn1.x509.qualified.MonetaryValue;
import org.bouncycastle2.asn1.x509.qualified.QCStatement;
import org.bouncycastle2.i18n.ErrorBundle;
import org.bouncycastle2.i18n.filter.TrustedInput;
import org.bouncycastle2.i18n.filter.UntrustedInput;
import org.bouncycastle2.jce.provider.AnnotatedException;
import org.bouncycastle2.jce.provider.CertPathValidatorUtilities;

public class PKIXCertPathReviewer extends CertPathValidatorUtilities
{
  private static final String AUTH_INFO_ACCESS = X509Extensions.AuthorityInfoAccess.getId();
  private static final String CRL_DIST_POINTS;
  private static final String QC_STATEMENT = X509Extensions.QCStatements.getId();
  private static final String RESOURCE_NAME = "org.bouncycastle2.x509.CertPathReviewerMessages";
  protected CertPath certPath;
  protected List certs;
  protected List[] errors;
  private boolean initialized;
  protected int n;
  protected List[] notifications;
  protected PKIXParameters pkixParams;
  protected PolicyNode policyTree;
  protected PublicKey subjectPublicKey;
  protected TrustAnchor trustAnchor;
  protected Date validDate;

  static
  {
    CRL_DIST_POINTS = X509Extensions.CRLDistributionPoints.getId();
  }

  public PKIXCertPathReviewer()
  {
  }

  public PKIXCertPathReviewer(CertPath paramCertPath, PKIXParameters paramPKIXParameters)
    throws CertPathReviewerException
  {
    init(paramCertPath, paramPKIXParameters);
  }

  private String IPtoString(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer;
    int i;
    try
    {
      String str = InetAddress.getByAddress(paramArrayOfByte).getHostAddress();
      return str;
    }
    catch (Exception localException)
    {
      localStringBuffer = new StringBuffer();
      i = 0;
    }
    while (true)
    {
      if (i == paramArrayOfByte.length)
        return localStringBuffer.toString();
      localStringBuffer.append(Integer.toHexString(0xFF & paramArrayOfByte[i]));
      localStringBuffer.append(' ');
      i++;
    }
  }

  private void checkCriticalExtensions()
  {
    List localList = this.pkixParams.getCertPathCheckers();
    Iterator localIterator1 = localList.iterator();
    while (true)
    {
      int j;
      try
      {
        boolean bool = localIterator1.hasNext();
        if (!bool)
        {
          int i = this.certs.size();
          j = i - 1;
          if (j >= 0);
        }
        else
        {
          ((PKIXCertPathChecker)localIterator1.next()).init(false);
          continue;
        }
      }
      catch (CertPathValidatorException localCertPathValidatorException1)
      {
        Object[] arrayOfObject1 = new Object[3];
        arrayOfObject1[0] = localCertPathValidatorException1.getMessage();
        arrayOfObject1[1] = localCertPathValidatorException1;
        arrayOfObject1[2] = localCertPathValidatorException1.getClass().getName();
        throw new CertPathReviewerException(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.certPathCheckerError", arrayOfObject1), localCertPathValidatorException1);
      }
      catch (CertPathReviewerException localCertPathReviewerException)
      {
        addError(localCertPathReviewerException.getErrorMessage(), localCertPathReviewerException.getIndex());
        return;
      }
      X509Certificate localX509Certificate = (X509Certificate)this.certs.get(j);
      Set localSet = localX509Certificate.getCriticalExtensionOIDs();
      if ((localSet != null) && (!localSet.isEmpty()))
      {
        localSet.remove(KEY_USAGE);
        localSet.remove(CERTIFICATE_POLICIES);
        localSet.remove(POLICY_MAPPINGS);
        localSet.remove(INHIBIT_ANY_POLICY);
        localSet.remove(ISSUING_DISTRIBUTION_POINT);
        localSet.remove(DELTA_CRL_INDICATOR);
        localSet.remove(POLICY_CONSTRAINTS);
        localSet.remove(BASIC_CONSTRAINTS);
        localSet.remove(SUBJECT_ALTERNATIVE_NAME);
        localSet.remove(NAME_CONSTRAINTS);
        if ((localSet.contains(QC_STATEMENT)) && (processQcStatements(localX509Certificate, j)))
          localSet.remove(QC_STATEMENT);
        Iterator localIterator2 = localList.iterator();
        while (true)
          if (!localIterator2.hasNext())
          {
            if (!localSet.isEmpty())
            {
              Iterator localIterator3 = localSet.iterator();
              while (localIterator3.hasNext())
              {
                Object[] arrayOfObject3 = new Object[1];
                DERObjectIdentifier localDERObjectIdentifier = new DERObjectIdentifier((String)localIterator3.next());
                arrayOfObject3[0] = localDERObjectIdentifier;
                addError(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.unknownCriticalExt", arrayOfObject3), j);
              }
            }
          }
          else
            try
            {
              ((PKIXCertPathChecker)localIterator2.next()).check(localX509Certificate, localSet);
            }
            catch (CertPathValidatorException localCertPathValidatorException2)
            {
              Object[] arrayOfObject2 = new Object[3];
              arrayOfObject2[0] = localCertPathValidatorException2.getMessage();
              arrayOfObject2[1] = localCertPathValidatorException2;
              arrayOfObject2[2] = localCertPathValidatorException2.getClass().getName();
              throw new CertPathReviewerException(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.criticalExtensionError", arrayOfObject2), localCertPathValidatorException2.getCause(), this.certPath, j);
            }
      }
      j--;
    }
  }

  // ERROR //
  private void checkNameConstraints()
  {
    // Byte code:
    //   0: new 262	org/bouncycastle2/jce/provider/PKIXNameConstraintValidator
    //   3: dup
    //   4: invokespecial 263	org/bouncycastle2/jce/provider/PKIXNameConstraintValidator:<init>	()V
    //   7: astore_1
    //   8: iconst_m1
    //   9: aload_0
    //   10: getfield 124	org/bouncycastle2/x509/PKIXCertPathReviewer:certs	Ljava/util/List;
    //   13: invokeinterface 128 1 0
    //   18: iadd
    //   19: istore_3
    //   20: goto +578 -> 598
    //   23: aload_0
    //   24: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   27: iload_3
    //   28: isub
    //   29: pop
    //   30: aload_0
    //   31: getfield 124	org/bouncycastle2/x509/PKIXCertPathReviewer:certs	Ljava/util/List;
    //   34: iload_3
    //   35: invokeinterface 176 2 0
    //   40: checkcast 178	java/security/cert/X509Certificate
    //   43: astore 5
    //   45: aload 5
    //   47: invokestatic 269	org/bouncycastle2/x509/PKIXCertPathReviewer:isSelfIssued	(Ljava/security/cert/X509Certificate;)Z
    //   50: ifne +88 -> 138
    //   53: aload 5
    //   55: invokestatic 273	org/bouncycastle2/x509/PKIXCertPathReviewer:getSubjectPrincipal	(Ljava/security/cert/X509Certificate;)Ljavax/security/auth/x500/X500Principal;
    //   58: astore 14
    //   60: new 275	org/bouncycastle2/asn1/ASN1InputStream
    //   63: dup
    //   64: new 277	java/io/ByteArrayInputStream
    //   67: dup
    //   68: aload 14
    //   70: invokevirtual 283	javax/security/auth/x500/X500Principal:getEncoded	()[B
    //   73: invokespecial 286	java/io/ByteArrayInputStream:<init>	([B)V
    //   76: invokespecial 289	org/bouncycastle2/asn1/ASN1InputStream:<init>	(Ljava/io/InputStream;)V
    //   79: astore 15
    //   81: aload 15
    //   83: invokevirtual 293	org/bouncycastle2/asn1/ASN1InputStream:readObject	()Lorg/bouncycastle2/asn1/DERObject;
    //   86: checkcast 295	org/bouncycastle2/asn1/ASN1Sequence
    //   89: astore 21
    //   91: aload_1
    //   92: aload 21
    //   94: invokevirtual 299	org/bouncycastle2/jce/provider/PKIXNameConstraintValidator:checkPermittedDN	(Lorg/bouncycastle2/asn1/ASN1Sequence;)V
    //   97: aload_1
    //   98: aload 21
    //   100: invokevirtual 302	org/bouncycastle2/jce/provider/PKIXNameConstraintValidator:checkExcludedDN	(Lorg/bouncycastle2/asn1/ASN1Sequence;)V
    //   103: aload 5
    //   105: getstatic 218	org/bouncycastle2/x509/PKIXCertPathReviewer:SUBJECT_ALTERNATIVE_NAME	Ljava/lang/String;
    //   108: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   111: checkcast 295	org/bouncycastle2/asn1/ASN1Sequence
    //   114: astore 33
    //   116: aload 33
    //   118: ifnull +20 -> 138
    //   121: iconst_0
    //   122: istore 34
    //   124: aload 33
    //   126: invokevirtual 307	org/bouncycastle2/asn1/ASN1Sequence:size	()I
    //   129: istore 35
    //   131: iload 34
    //   133: iload 35
    //   135: if_icmplt +317 -> 452
    //   138: aload 5
    //   140: getstatic 221	org/bouncycastle2/x509/PKIXCertPathReviewer:NAME_CONSTRAINTS	Ljava/lang/String;
    //   143: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   146: checkcast 295	org/bouncycastle2/asn1/ASN1Sequence
    //   149: astore 9
    //   151: aload 9
    //   153: ifnull +450 -> 603
    //   156: new 309	org/bouncycastle2/asn1/x509/NameConstraints
    //   159: dup
    //   160: aload 9
    //   162: invokespecial 311	org/bouncycastle2/asn1/x509/NameConstraints:<init>	(Lorg/bouncycastle2/asn1/ASN1Sequence;)V
    //   165: astore 10
    //   167: aload 10
    //   169: invokevirtual 315	org/bouncycastle2/asn1/x509/NameConstraints:getPermittedSubtrees	()Lorg/bouncycastle2/asn1/ASN1Sequence;
    //   172: astore 11
    //   174: aload 11
    //   176: ifnull +9 -> 185
    //   179: aload_1
    //   180: aload 11
    //   182: invokevirtual 318	org/bouncycastle2/jce/provider/PKIXNameConstraintValidator:intersectPermittedSubtree	(Lorg/bouncycastle2/asn1/ASN1Sequence;)V
    //   185: aload 10
    //   187: invokevirtual 321	org/bouncycastle2/asn1/x509/NameConstraints:getExcludedSubtrees	()Lorg/bouncycastle2/asn1/ASN1Sequence;
    //   190: astore 12
    //   192: aload 12
    //   194: ifnull +409 -> 603
    //   197: aload 12
    //   199: invokevirtual 325	org/bouncycastle2/asn1/ASN1Sequence:getObjects	()Ljava/util/Enumeration;
    //   202: astore 13
    //   204: aload 13
    //   206: invokeinterface 330 1 0
    //   211: ifne +370 -> 581
    //   214: goto +389 -> 603
    //   217: astore 16
    //   219: iconst_1
    //   220: anewarray 139	java/lang/Object
    //   223: astore 17
    //   225: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   228: dup
    //   229: aload 14
    //   231: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   234: astore 18
    //   236: aload 17
    //   238: iconst_0
    //   239: aload 18
    //   241: aastore
    //   242: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   245: dup
    //   246: ldc 11
    //   248: ldc_w 337
    //   251: aload 17
    //   253: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   256: astore 19
    //   258: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   261: dup
    //   262: aload 19
    //   264: aload 16
    //   266: aload_0
    //   267: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   270: iload_3
    //   271: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   274: astore 20
    //   276: aload 20
    //   278: athrow
    //   279: astore_2
    //   280: aload_0
    //   281: aload_2
    //   282: invokevirtual 165	org/bouncycastle2/x509/CertPathReviewerException:getErrorMessage	()Lorg/bouncycastle2/i18n/ErrorBundle;
    //   285: aload_2
    //   286: invokevirtual 168	org/bouncycastle2/x509/CertPathReviewerException:getIndex	()I
    //   289: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   292: return
    //   293: astore 22
    //   295: iconst_1
    //   296: anewarray 139	java/lang/Object
    //   299: astore 23
    //   301: aload 23
    //   303: iconst_0
    //   304: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   307: dup
    //   308: aload 14
    //   310: invokevirtual 338	javax/security/auth/x500/X500Principal:getName	()Ljava/lang/String;
    //   313: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   316: aastore
    //   317: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   320: dup
    //   321: ldc 11
    //   323: ldc_w 340
    //   326: aload 23
    //   328: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   331: astore 24
    //   333: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   336: dup
    //   337: aload 24
    //   339: aload 22
    //   341: aload_0
    //   342: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   345: iload_3
    //   346: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   349: astore 25
    //   351: aload 25
    //   353: athrow
    //   354: astore 26
    //   356: iconst_1
    //   357: anewarray 139	java/lang/Object
    //   360: astore 27
    //   362: aload 27
    //   364: iconst_0
    //   365: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   368: dup
    //   369: aload 14
    //   371: invokevirtual 338	javax/security/auth/x500/X500Principal:getName	()Ljava/lang/String;
    //   374: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   377: aastore
    //   378: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   381: dup
    //   382: ldc 11
    //   384: ldc_w 342
    //   387: aload 27
    //   389: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   392: astore 28
    //   394: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   397: dup
    //   398: aload 28
    //   400: aload 26
    //   402: aload_0
    //   403: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   406: iload_3
    //   407: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   410: astore 29
    //   412: aload 29
    //   414: athrow
    //   415: astore 30
    //   417: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   420: dup
    //   421: ldc 11
    //   423: ldc_w 344
    //   426: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   429: astore 31
    //   431: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   434: dup
    //   435: aload 31
    //   437: aload 30
    //   439: aload_0
    //   440: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   443: iload_3
    //   444: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   447: astore 32
    //   449: aload 32
    //   451: athrow
    //   452: aload 33
    //   454: iload 34
    //   456: invokevirtual 351	org/bouncycastle2/asn1/ASN1Sequence:getObjectAt	(I)Lorg/bouncycastle2/asn1/DEREncodable;
    //   459: invokestatic 357	org/bouncycastle2/asn1/x509/GeneralName:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/GeneralName;
    //   462: astore 36
    //   464: aload_1
    //   465: aload 36
    //   467: invokevirtual 361	org/bouncycastle2/jce/provider/PKIXNameConstraintValidator:checkPermitted	(Lorg/bouncycastle2/asn1/x509/GeneralName;)V
    //   470: aload_1
    //   471: aload 36
    //   473: invokevirtual 364	org/bouncycastle2/jce/provider/PKIXNameConstraintValidator:checkExcluded	(Lorg/bouncycastle2/asn1/x509/GeneralName;)V
    //   476: iinc 34 1
    //   479: goto -355 -> 124
    //   482: astore 37
    //   484: iconst_1
    //   485: anewarray 139	java/lang/Object
    //   488: astore 38
    //   490: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   493: dup
    //   494: aload 36
    //   496: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   499: astore 39
    //   501: aload 38
    //   503: iconst_0
    //   504: aload 39
    //   506: aastore
    //   507: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   510: dup
    //   511: ldc 11
    //   513: ldc_w 366
    //   516: aload 38
    //   518: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   521: astore 40
    //   523: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   526: dup
    //   527: aload 40
    //   529: aload 37
    //   531: aload_0
    //   532: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   535: iload_3
    //   536: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   539: astore 41
    //   541: aload 41
    //   543: athrow
    //   544: astore 6
    //   546: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   549: dup
    //   550: ldc 11
    //   552: ldc_w 368
    //   555: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   558: astore 7
    //   560: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   563: dup
    //   564: aload 7
    //   566: aload 6
    //   568: aload_0
    //   569: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   572: iload_3
    //   573: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   576: astore 8
    //   578: aload 8
    //   580: athrow
    //   581: aload_1
    //   582: aload 13
    //   584: invokeinterface 371 1 0
    //   589: invokestatic 376	org/bouncycastle2/asn1/x509/GeneralSubtree:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/GeneralSubtree;
    //   592: invokevirtual 380	org/bouncycastle2/jce/provider/PKIXNameConstraintValidator:addExcludedSubtree	(Lorg/bouncycastle2/asn1/x509/GeneralSubtree;)V
    //   595: goto -391 -> 204
    //   598: iload_3
    //   599: ifgt -576 -> 23
    //   602: return
    //   603: iinc 3 255
    //   606: goto -8 -> 598
    //
    // Exception table:
    //   from	to	target	type
    //   81	91	217	java/io/IOException
    //   8	20	279	org/bouncycastle2/x509/CertPathReviewerException
    //   23	81	279	org/bouncycastle2/x509/CertPathReviewerException
    //   81	91	279	org/bouncycastle2/x509/CertPathReviewerException
    //   91	97	279	org/bouncycastle2/x509/CertPathReviewerException
    //   97	103	279	org/bouncycastle2/x509/CertPathReviewerException
    //   103	116	279	org/bouncycastle2/x509/CertPathReviewerException
    //   124	131	279	org/bouncycastle2/x509/CertPathReviewerException
    //   138	151	279	org/bouncycastle2/x509/CertPathReviewerException
    //   156	174	279	org/bouncycastle2/x509/CertPathReviewerException
    //   179	185	279	org/bouncycastle2/x509/CertPathReviewerException
    //   185	192	279	org/bouncycastle2/x509/CertPathReviewerException
    //   197	204	279	org/bouncycastle2/x509/CertPathReviewerException
    //   204	214	279	org/bouncycastle2/x509/CertPathReviewerException
    //   219	279	279	org/bouncycastle2/x509/CertPathReviewerException
    //   295	354	279	org/bouncycastle2/x509/CertPathReviewerException
    //   356	415	279	org/bouncycastle2/x509/CertPathReviewerException
    //   417	452	279	org/bouncycastle2/x509/CertPathReviewerException
    //   452	464	279	org/bouncycastle2/x509/CertPathReviewerException
    //   464	476	279	org/bouncycastle2/x509/CertPathReviewerException
    //   484	544	279	org/bouncycastle2/x509/CertPathReviewerException
    //   546	581	279	org/bouncycastle2/x509/CertPathReviewerException
    //   581	595	279	org/bouncycastle2/x509/CertPathReviewerException
    //   91	97	293	org/bouncycastle2/jce/provider/PKIXNameConstraintValidatorException
    //   97	103	354	org/bouncycastle2/jce/provider/PKIXNameConstraintValidatorException
    //   103	116	415	org/bouncycastle2/jce/provider/AnnotatedException
    //   464	476	482	org/bouncycastle2/jce/provider/PKIXNameConstraintValidatorException
    //   138	151	544	org/bouncycastle2/jce/provider/AnnotatedException
  }

  private void checkPathLength()
  {
    int i = this.n;
    int j = 0;
    int k = -1 + this.certs.size();
    while (true)
    {
      if (k <= 0)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = new Integer(j);
        addNotification(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.totalPathLength", arrayOfObject));
        return;
      }
      (this.n - k);
      X509Certificate localX509Certificate = (X509Certificate)this.certs.get(k);
      if (!isSelfIssued(localX509Certificate))
      {
        if (i <= 0)
          addError(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.pathLenghtExtended"));
        i--;
        j++;
      }
      try
      {
        BasicConstraints localBasicConstraints2 = BasicConstraints.getInstance(getExtensionValue(localX509Certificate, BASIC_CONSTRAINTS));
        localBasicConstraints1 = localBasicConstraints2;
        if (localBasicConstraints1 != null)
        {
          BigInteger localBigInteger = localBasicConstraints1.getPathLenConstraint();
          if (localBigInteger != null)
          {
            int m = localBigInteger.intValue();
            if (m < i)
              i = m;
          }
        }
        k--;
      }
      catch (AnnotatedException localAnnotatedException)
      {
        while (true)
        {
          addError(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.processLengthConstError"), k);
          BasicConstraints localBasicConstraints1 = null;
        }
      }
    }
  }

  // ERROR //
  private void checkPolicy()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   4: invokevirtual 414	java/security/cert/PKIXParameters:getInitialPolicies	()Ljava/util/Set;
    //   7: astore_1
    //   8: iconst_1
    //   9: aload_0
    //   10: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   13: iadd
    //   14: anewarray 416	java/util/ArrayList
    //   17: astore_2
    //   18: iconst_0
    //   19: istore_3
    //   20: aload_2
    //   21: arraylength
    //   22: istore 4
    //   24: iload_3
    //   25: iload 4
    //   27: if_icmplt +253 -> 280
    //   30: new 418	java/util/HashSet
    //   33: dup
    //   34: invokespecial 419	java/util/HashSet:<init>	()V
    //   37: astore 5
    //   39: aload 5
    //   41: ldc_w 421
    //   44: invokeinterface 424 2 0
    //   49: pop
    //   50: new 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   53: dup
    //   54: new 416	java/util/ArrayList
    //   57: dup
    //   58: invokespecial 427	java/util/ArrayList:<init>	()V
    //   61: iconst_0
    //   62: aload 5
    //   64: aconst_null
    //   65: new 418	java/util/HashSet
    //   68: dup
    //   69: invokespecial 419	java/util/HashSet:<init>	()V
    //   72: ldc_w 421
    //   75: iconst_0
    //   76: invokespecial 430	org/bouncycastle2/jce/provider/PKIXPolicyNode:<init>	(Ljava/util/List;ILjava/util/Set;Ljava/security/cert/PolicyNode;Ljava/util/Set;Ljava/lang/String;Z)V
    //   79: astore 7
    //   81: aload_2
    //   82: iconst_0
    //   83: aaload
    //   84: aload 7
    //   86: invokeinterface 431 2 0
    //   91: pop
    //   92: aload_0
    //   93: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   96: invokevirtual 434	java/security/cert/PKIXParameters:isExplicitPolicyRequired	()Z
    //   99: ifeq +197 -> 296
    //   102: iconst_0
    //   103: istore 9
    //   105: aload_0
    //   106: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   109: invokevirtual 437	java/security/cert/PKIXParameters:isAnyPolicyInhibited	()Z
    //   112: ifeq +195 -> 307
    //   115: iconst_0
    //   116: istore 10
    //   118: aload_0
    //   119: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   122: invokevirtual 440	java/security/cert/PKIXParameters:isPolicyMappingInhibited	()Z
    //   125: ifeq +193 -> 318
    //   128: iconst_0
    //   129: istore 11
    //   131: aconst_null
    //   132: astore 12
    //   134: aconst_null
    //   135: astore 13
    //   137: iconst_m1
    //   138: aload_0
    //   139: getfield 124	org/bouncycastle2/x509/PKIXCertPathReviewer:certs	Ljava/util/List;
    //   142: invokeinterface 128 1 0
    //   147: iadd
    //   148: istore 15
    //   150: iload 15
    //   152: ifge +177 -> 329
    //   155: aload 13
    //   157: invokestatic 269	org/bouncycastle2/x509/PKIXCertPathReviewer:isSelfIssued	(Ljava/security/cert/X509Certificate;)Z
    //   160: istore 16
    //   162: iload 16
    //   164: ifne +11 -> 175
    //   167: iload 9
    //   169: ifle +6 -> 175
    //   172: iinc 9 255
    //   175: getstatic 212	org/bouncycastle2/x509/PKIXCertPathReviewer:POLICY_CONSTRAINTS	Ljava/lang/String;
    //   178: astore 19
    //   180: aload 13
    //   182: aload 19
    //   184: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   187: checkcast 295	org/bouncycastle2/asn1/ASN1Sequence
    //   190: astore 20
    //   192: aload 20
    //   194: ifnull +24 -> 218
    //   197: aload 20
    //   199: invokevirtual 325	org/bouncycastle2/asn1/ASN1Sequence:getObjects	()Ljava/util/Enumeration;
    //   202: astore 21
    //   204: aload 21
    //   206: invokeinterface 330 1 0
    //   211: istore 22
    //   213: iload 22
    //   215: ifne +1785 -> 2000
    //   218: aload 7
    //   220: ifnonnull +1907 -> 2127
    //   223: aload_0
    //   224: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   227: invokevirtual 434	java/security/cert/PKIXParameters:isExplicitPolicyRequired	()Z
    //   230: ifeq +2552 -> 2782
    //   233: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   236: dup
    //   237: ldc 11
    //   239: ldc_w 442
    //   242: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   245: astore 64
    //   247: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   250: dup
    //   251: aload 64
    //   253: aload_0
    //   254: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   257: iload 15
    //   259: invokespecial 445	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
    //   262: athrow
    //   263: astore 14
    //   265: aload_0
    //   266: aload 14
    //   268: invokevirtual 165	org/bouncycastle2/x509/CertPathReviewerException:getErrorMessage	()Lorg/bouncycastle2/i18n/ErrorBundle;
    //   271: aload 14
    //   273: invokevirtual 168	org/bouncycastle2/x509/CertPathReviewerException:getIndex	()I
    //   276: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   279: return
    //   280: aload_2
    //   281: iload_3
    //   282: new 416	java/util/ArrayList
    //   285: dup
    //   286: invokespecial 427	java/util/ArrayList:<init>	()V
    //   289: aastore
    //   290: iinc 3 1
    //   293: goto -273 -> 20
    //   296: iconst_1
    //   297: aload_0
    //   298: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   301: iadd
    //   302: istore 9
    //   304: goto -199 -> 105
    //   307: iconst_1
    //   308: aload_0
    //   309: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   312: iadd
    //   313: istore 10
    //   315: goto -197 -> 118
    //   318: iconst_1
    //   319: aload_0
    //   320: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   323: iadd
    //   324: istore 11
    //   326: goto -195 -> 131
    //   329: aload_0
    //   330: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   333: iload 15
    //   335: isub
    //   336: istore 65
    //   338: aload_0
    //   339: getfield 124	org/bouncycastle2/x509/PKIXCertPathReviewer:certs	Ljava/util/List;
    //   342: iload 15
    //   344: invokeinterface 176 2 0
    //   349: checkcast 178	java/security/cert/X509Certificate
    //   352: astore 13
    //   354: getstatic 197	org/bouncycastle2/x509/PKIXCertPathReviewer:CERTIFICATE_POLICIES	Ljava/lang/String;
    //   357: astore 68
    //   359: aload 13
    //   361: aload 68
    //   363: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   366: checkcast 295	org/bouncycastle2/asn1/ASN1Sequence
    //   369: astore 69
    //   371: aload 69
    //   373: ifnull +2368 -> 2741
    //   376: aload 7
    //   378: ifnull +2363 -> 2741
    //   381: aload 69
    //   383: invokevirtual 325	org/bouncycastle2/asn1/ASN1Sequence:getObjects	()Ljava/util/Enumeration;
    //   386: astore 119
    //   388: new 418	java/util/HashSet
    //   391: dup
    //   392: invokespecial 419	java/util/HashSet:<init>	()V
    //   395: astore 120
    //   397: aload 119
    //   399: invokeinterface 330 1 0
    //   404: ifne +191 -> 595
    //   407: aload 12
    //   409: ifnull +2316 -> 2725
    //   412: aload 12
    //   414: ldc_w 421
    //   417: invokeinterface 224 2 0
    //   422: ifeq +293 -> 715
    //   425: goto +2300 -> 2725
    //   428: iload 10
    //   430: ifgt +20 -> 450
    //   433: iload 65
    //   435: aload_0
    //   436: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   439: if_icmpge +2293 -> 2732
    //   442: aload 13
    //   444: invokestatic 269	org/bouncycastle2/x509/PKIXCertPathReviewer:isSelfIssued	(Ljava/security/cert/X509Certificate;)Z
    //   447: ifeq +2285 -> 2732
    //   450: aload 69
    //   452: invokevirtual 325	org/bouncycastle2/asn1/ASN1Sequence:getObjects	()Ljava/util/Enumeration;
    //   455: astore 131
    //   457: aload 131
    //   459: invokeinterface 330 1 0
    //   464: ifne +320 -> 784
    //   467: goto +2265 -> 2732
    //   470: iload 121
    //   472: ifge +627 -> 1099
    //   475: aload 13
    //   477: invokevirtual 182	java/security/cert/X509Certificate:getCriticalExtensionOIDs	()Ljava/util/Set;
    //   480: astore 122
    //   482: aload 122
    //   484: ifnull +2257 -> 2741
    //   487: aload 122
    //   489: getstatic 197	org/bouncycastle2/x509/PKIXCertPathReviewer:CERTIFICATE_POLICIES	Ljava/lang/String;
    //   492: invokeinterface 224 2 0
    //   497: istore 123
    //   499: aload_2
    //   500: iload 65
    //   502: aaload
    //   503: astore 124
    //   505: iconst_0
    //   506: istore 125
    //   508: aload 124
    //   510: invokeinterface 128 1 0
    //   515: istore 126
    //   517: iload 125
    //   519: iload 126
    //   521: if_icmplt +646 -> 1167
    //   524: goto +2217 -> 2741
    //   527: iload 9
    //   529: ifgt +661 -> 1190
    //   532: aload 7
    //   534: ifnonnull +656 -> 1190
    //   537: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   540: dup
    //   541: ldc 11
    //   543: ldc_w 447
    //   546: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   549: astore 118
    //   551: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   554: dup
    //   555: aload 118
    //   557: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   560: athrow
    //   561: astore 66
    //   563: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   566: dup
    //   567: ldc 11
    //   569: ldc_w 451
    //   572: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   575: astore 67
    //   577: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   580: dup
    //   581: aload 67
    //   583: aload 66
    //   585: aload_0
    //   586: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   589: iload 15
    //   591: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   594: athrow
    //   595: aload 119
    //   597: invokeinterface 371 1 0
    //   602: invokestatic 456	org/bouncycastle2/asn1/x509/PolicyInformation:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/PolicyInformation;
    //   605: astore 155
    //   607: aload 155
    //   609: invokevirtual 460	org/bouncycastle2/asn1/x509/PolicyInformation:getPolicyIdentifier	()Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   612: astore 156
    //   614: aload 120
    //   616: aload 156
    //   618: invokevirtual 461	org/bouncycastle2/asn1/DERObjectIdentifier:getId	()Ljava/lang/String;
    //   621: invokeinterface 424 2 0
    //   626: pop
    //   627: ldc_w 421
    //   630: aload 156
    //   632: invokevirtual 461	org/bouncycastle2/asn1/DERObjectIdentifier:getId	()Ljava/lang/String;
    //   635: invokevirtual 464	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   638: istore 158
    //   640: iload 158
    //   642: ifne -245 -> 397
    //   645: aload 155
    //   647: invokevirtual 467	org/bouncycastle2/asn1/x509/PolicyInformation:getPolicyQualifiers	()Lorg/bouncycastle2/asn1/ASN1Sequence;
    //   650: invokestatic 471	org/bouncycastle2/x509/PKIXCertPathReviewer:getQualifierSet	(Lorg/bouncycastle2/asn1/ASN1Sequence;)Ljava/util/Set;
    //   653: astore 161
    //   655: iload 65
    //   657: aload_2
    //   658: aload 156
    //   660: aload 161
    //   662: invokestatic 475	org/bouncycastle2/x509/PKIXCertPathReviewer:processCertD1i	(I[Ljava/util/List;Lorg/bouncycastle2/asn1/DERObjectIdentifier;Ljava/util/Set;)Z
    //   665: ifne -268 -> 397
    //   668: iload 65
    //   670: aload_2
    //   671: aload 156
    //   673: aload 161
    //   675: invokestatic 479	org/bouncycastle2/x509/PKIXCertPathReviewer:processCertD1ii	(I[Ljava/util/List;Lorg/bouncycastle2/asn1/DERObjectIdentifier;Ljava/util/Set;)V
    //   678: goto -281 -> 397
    //   681: astore 159
    //   683: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   686: dup
    //   687: ldc 11
    //   689: ldc_w 481
    //   692: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   695: astore 160
    //   697: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   700: dup
    //   701: aload 160
    //   703: aload 159
    //   705: aload_0
    //   706: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   709: iload 15
    //   711: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   714: athrow
    //   715: aload 12
    //   717: invokeinterface 229 1 0
    //   722: astore 151
    //   724: new 418	java/util/HashSet
    //   727: dup
    //   728: invokespecial 419	java/util/HashSet:<init>	()V
    //   731: astore 152
    //   733: aload 151
    //   735: invokeinterface 122 1 0
    //   740: ifne +10 -> 750
    //   743: aload 152
    //   745: astore 12
    //   747: goto -319 -> 428
    //   750: aload 151
    //   752: invokeinterface 132 1 0
    //   757: astore 153
    //   759: aload 120
    //   761: aload 153
    //   763: invokeinterface 224 2 0
    //   768: ifeq -35 -> 733
    //   771: aload 152
    //   773: aload 153
    //   775: invokeinterface 424 2 0
    //   780: pop
    //   781: goto -48 -> 733
    //   784: aload 131
    //   786: invokeinterface 371 1 0
    //   791: invokestatic 456	org/bouncycastle2/asn1/x509/PolicyInformation:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/PolicyInformation;
    //   794: astore 132
    //   796: ldc_w 421
    //   799: aload 132
    //   801: invokevirtual 460	org/bouncycastle2/asn1/x509/PolicyInformation:getPolicyIdentifier	()Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   804: invokevirtual 461	org/bouncycastle2/asn1/DERObjectIdentifier:getId	()Ljava/lang/String;
    //   807: invokevirtual 464	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   810: istore 133
    //   812: iload 133
    //   814: ifeq -357 -> 457
    //   817: aload 132
    //   819: invokevirtual 467	org/bouncycastle2/asn1/x509/PolicyInformation:getPolicyQualifiers	()Lorg/bouncycastle2/asn1/ASN1Sequence;
    //   822: invokestatic 471	org/bouncycastle2/x509/PKIXCertPathReviewer:getQualifierSet	(Lorg/bouncycastle2/asn1/ASN1Sequence;)Ljava/util/Set;
    //   825: astore 136
    //   827: iload 65
    //   829: iconst_1
    //   830: isub
    //   831: istore 137
    //   833: aload_2
    //   834: iload 137
    //   836: aaload
    //   837: astore 138
    //   839: iconst_0
    //   840: istore 139
    //   842: aload 138
    //   844: invokeinterface 128 1 0
    //   849: istore 140
    //   851: iload 139
    //   853: iload 140
    //   855: if_icmpge +1877 -> 2732
    //   858: aload 138
    //   860: iload 139
    //   862: invokeinterface 176 2 0
    //   867: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   870: astore 141
    //   872: aload 141
    //   874: invokevirtual 484	org/bouncycastle2/jce/provider/PKIXPolicyNode:getExpectedPolicies	()Ljava/util/Set;
    //   877: invokeinterface 229 1 0
    //   882: astore 142
    //   884: aload 142
    //   886: invokeinterface 122 1 0
    //   891: ifne +43 -> 934
    //   894: iinc 139 1
    //   897: goto -55 -> 842
    //   900: astore 134
    //   902: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   905: dup
    //   906: ldc 11
    //   908: ldc_w 481
    //   911: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   914: astore 135
    //   916: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   919: dup
    //   920: aload 135
    //   922: aload 134
    //   924: aload_0
    //   925: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   928: iload 15
    //   930: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   933: athrow
    //   934: aload 142
    //   936: invokeinterface 132 1 0
    //   941: astore 143
    //   943: aload 143
    //   945: instanceof 233
    //   948: ifeq +103 -> 1051
    //   951: aload 143
    //   953: checkcast 233	java/lang/String
    //   956: astore 144
    //   958: iconst_0
    //   959: istore 145
    //   961: aload 141
    //   963: invokevirtual 487	org/bouncycastle2/jce/provider/PKIXPolicyNode:getChildren	()Ljava/util/Iterator;
    //   966: astore 146
    //   968: aload 146
    //   970: invokeinterface 122 1 0
    //   975: ifne +97 -> 1072
    //   978: iload 145
    //   980: ifne -96 -> 884
    //   983: new 418	java/util/HashSet
    //   986: dup
    //   987: invokespecial 419	java/util/HashSet:<init>	()V
    //   990: astore 147
    //   992: aload 147
    //   994: aload 144
    //   996: invokeinterface 424 2 0
    //   1001: pop
    //   1002: new 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   1005: dup
    //   1006: new 416	java/util/ArrayList
    //   1009: dup
    //   1010: invokespecial 427	java/util/ArrayList:<init>	()V
    //   1013: iload 65
    //   1015: aload 147
    //   1017: aload 141
    //   1019: aload 136
    //   1021: aload 144
    //   1023: iconst_0
    //   1024: invokespecial 430	org/bouncycastle2/jce/provider/PKIXPolicyNode:<init>	(Ljava/util/List;ILjava/util/Set;Ljava/security/cert/PolicyNode;Ljava/util/Set;Ljava/lang/String;Z)V
    //   1027: astore 149
    //   1029: aload 141
    //   1031: aload 149
    //   1033: invokevirtual 491	org/bouncycastle2/jce/provider/PKIXPolicyNode:addChild	(Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;)V
    //   1036: aload_2
    //   1037: iload 65
    //   1039: aaload
    //   1040: aload 149
    //   1042: invokeinterface 431 2 0
    //   1047: pop
    //   1048: goto -164 -> 884
    //   1051: aload 143
    //   1053: instanceof 231
    //   1056: ifeq -172 -> 884
    //   1059: aload 143
    //   1061: checkcast 231	org/bouncycastle2/asn1/DERObjectIdentifier
    //   1064: invokevirtual 461	org/bouncycastle2/asn1/DERObjectIdentifier:getId	()Ljava/lang/String;
    //   1067: astore 144
    //   1069: goto -111 -> 958
    //   1072: aload 144
    //   1074: aload 146
    //   1076: invokeinterface 132 1 0
    //   1081: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   1084: invokevirtual 494	org/bouncycastle2/jce/provider/PKIXPolicyNode:getValidPolicy	()Ljava/lang/String;
    //   1087: invokevirtual 464	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1090: ifeq -122 -> 968
    //   1093: iconst_1
    //   1094: istore 145
    //   1096: goto -128 -> 968
    //   1099: aload_2
    //   1100: iload 121
    //   1102: aaload
    //   1103: astore 127
    //   1105: iconst_0
    //   1106: istore 128
    //   1108: aload 127
    //   1110: invokeinterface 128 1 0
    //   1115: istore 129
    //   1117: iload 128
    //   1119: iload 129
    //   1121: if_icmplt +6 -> 1127
    //   1124: goto +1628 -> 2752
    //   1127: aload 127
    //   1129: iload 128
    //   1131: invokeinterface 176 2 0
    //   1136: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   1139: astore 130
    //   1141: aload 130
    //   1143: invokevirtual 497	org/bouncycastle2/jce/provider/PKIXPolicyNode:hasChildren	()Z
    //   1146: ifne +1612 -> 2758
    //   1149: aload 7
    //   1151: aload_2
    //   1152: aload 130
    //   1154: invokestatic 501	org/bouncycastle2/x509/PKIXCertPathReviewer:removePolicyNode	(Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;[Ljava/util/List;Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;)Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;
    //   1157: astore 7
    //   1159: aload 7
    //   1161: ifnull +1591 -> 2752
    //   1164: goto +1594 -> 2758
    //   1167: aload 124
    //   1169: iload 125
    //   1171: invokeinterface 176 2 0
    //   1176: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   1179: iload 123
    //   1181: invokevirtual 504	org/bouncycastle2/jce/provider/PKIXPolicyNode:setCritical	(Z)V
    //   1184: iinc 125 1
    //   1187: goto -679 -> 508
    //   1190: aload_0
    //   1191: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   1194: istore 70
    //   1196: iload 65
    //   1198: iload 70
    //   1200: if_icmpeq +234 -> 1434
    //   1203: getstatic 200	org/bouncycastle2/x509/PKIXCertPathReviewer:POLICY_MAPPINGS	Ljava/lang/String;
    //   1206: astore 73
    //   1208: aload 13
    //   1210: aload 73
    //   1212: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   1215: astore 74
    //   1217: aload 74
    //   1219: ifnull +27 -> 1246
    //   1222: aload 74
    //   1224: checkcast 295	org/bouncycastle2/asn1/ASN1Sequence
    //   1227: astore 110
    //   1229: iconst_0
    //   1230: istore 111
    //   1232: aload 110
    //   1234: invokevirtual 307	org/bouncycastle2/asn1/ASN1Sequence:size	()I
    //   1237: istore 112
    //   1239: iload 111
    //   1241: iload 112
    //   1243: if_icmplt +231 -> 1474
    //   1246: aload 74
    //   1248: ifnull +64 -> 1312
    //   1251: aload 74
    //   1253: checkcast 295	org/bouncycastle2/asn1/ASN1Sequence
    //   1256: astore 75
    //   1258: new 506	java/util/HashMap
    //   1261: dup
    //   1262: invokespecial 507	java/util/HashMap:<init>	()V
    //   1265: astore 76
    //   1267: new 418	java/util/HashSet
    //   1270: dup
    //   1271: invokespecial 419	java/util/HashSet:<init>	()V
    //   1274: astore 77
    //   1276: iconst_0
    //   1277: istore 78
    //   1279: aload 75
    //   1281: invokevirtual 307	org/bouncycastle2/asn1/ASN1Sequence:size	()I
    //   1284: istore 79
    //   1286: iload 78
    //   1288: iload 79
    //   1290: if_icmplt +306 -> 1596
    //   1293: aload 77
    //   1295: invokeinterface 229 1 0
    //   1300: astore 80
    //   1302: aload 80
    //   1304: invokeinterface 122 1 0
    //   1309: ifne +406 -> 1715
    //   1312: aload 13
    //   1314: invokestatic 269	org/bouncycastle2/x509/PKIXCertPathReviewer:isSelfIssued	(Ljava/security/cert/X509Certificate;)Z
    //   1317: istore 81
    //   1319: iload 81
    //   1321: ifne +27 -> 1348
    //   1324: iload 9
    //   1326: ifeq +6 -> 1332
    //   1329: iinc 9 255
    //   1332: iload 11
    //   1334: ifeq +6 -> 1340
    //   1337: iinc 11 255
    //   1340: iload 10
    //   1342: ifeq +6 -> 1348
    //   1345: iinc 10 255
    //   1348: getstatic 212	org/bouncycastle2/x509/PKIXCertPathReviewer:POLICY_CONSTRAINTS	Ljava/lang/String;
    //   1351: astore 84
    //   1353: aload 13
    //   1355: aload 84
    //   1357: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   1360: checkcast 295	org/bouncycastle2/asn1/ASN1Sequence
    //   1363: astore 85
    //   1365: aload 85
    //   1367: ifnull +24 -> 1391
    //   1370: aload 85
    //   1372: invokevirtual 325	org/bouncycastle2/asn1/ASN1Sequence:getObjects	()Ljava/util/Enumeration;
    //   1375: astore 86
    //   1377: aload 86
    //   1379: invokeinterface 330 1 0
    //   1384: istore 87
    //   1386: iload 87
    //   1388: ifne +451 -> 1839
    //   1391: getstatic 203	org/bouncycastle2/x509/PKIXCertPathReviewer:INHIBIT_ANY_POLICY	Ljava/lang/String;
    //   1394: astore 93
    //   1396: aload 13
    //   1398: aload 93
    //   1400: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   1403: checkcast 509	org/bouncycastle2/asn1/DERInteger
    //   1406: astore 94
    //   1408: aload 94
    //   1410: ifnull +24 -> 1434
    //   1413: aload 94
    //   1415: invokevirtual 512	org/bouncycastle2/asn1/DERInteger:getValue	()Ljava/math/BigInteger;
    //   1418: invokevirtual 408	java/math/BigInteger:intValue	()I
    //   1421: istore 95
    //   1423: iload 95
    //   1425: iload 10
    //   1427: if_icmpge +7 -> 1434
    //   1430: iload 95
    //   1432: istore 10
    //   1434: iinc 15 255
    //   1437: goto -1287 -> 150
    //   1440: astore 71
    //   1442: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1445: dup
    //   1446: ldc 11
    //   1448: ldc_w 514
    //   1451: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1454: astore 72
    //   1456: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1459: dup
    //   1460: aload 72
    //   1462: aload 71
    //   1464: aload_0
    //   1465: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   1468: iload 15
    //   1470: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   1473: athrow
    //   1474: aload 110
    //   1476: iload 111
    //   1478: invokevirtual 351	org/bouncycastle2/asn1/ASN1Sequence:getObjectAt	(I)Lorg/bouncycastle2/asn1/DEREncodable;
    //   1481: checkcast 295	org/bouncycastle2/asn1/ASN1Sequence
    //   1484: astore 113
    //   1486: aload 113
    //   1488: iconst_0
    //   1489: invokevirtual 351	org/bouncycastle2/asn1/ASN1Sequence:getObjectAt	(I)Lorg/bouncycastle2/asn1/DEREncodable;
    //   1492: checkcast 231	org/bouncycastle2/asn1/DERObjectIdentifier
    //   1495: astore 114
    //   1497: aload 113
    //   1499: iconst_1
    //   1500: invokevirtual 351	org/bouncycastle2/asn1/ASN1Sequence:getObjectAt	(I)Lorg/bouncycastle2/asn1/DEREncodable;
    //   1503: checkcast 231	org/bouncycastle2/asn1/DERObjectIdentifier
    //   1506: astore 115
    //   1508: ldc_w 421
    //   1511: aload 114
    //   1513: invokevirtual 461	org/bouncycastle2/asn1/DERObjectIdentifier:getId	()Ljava/lang/String;
    //   1516: invokevirtual 464	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1519: ifeq +33 -> 1552
    //   1522: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1525: dup
    //   1526: ldc 11
    //   1528: ldc_w 516
    //   1531: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1534: astore 116
    //   1536: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1539: dup
    //   1540: aload 116
    //   1542: aload_0
    //   1543: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   1546: iload 15
    //   1548: invokespecial 445	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
    //   1551: athrow
    //   1552: ldc_w 421
    //   1555: aload 115
    //   1557: invokevirtual 461	org/bouncycastle2/asn1/DERObjectIdentifier:getId	()Ljava/lang/String;
    //   1560: invokevirtual 464	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1563: ifeq +1201 -> 2764
    //   1566: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1569: dup
    //   1570: ldc 11
    //   1572: ldc_w 516
    //   1575: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1578: astore 117
    //   1580: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1583: dup
    //   1584: aload 117
    //   1586: aload_0
    //   1587: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   1590: iload 15
    //   1592: invokespecial 445	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
    //   1595: athrow
    //   1596: aload 75
    //   1598: iload 78
    //   1600: invokevirtual 351	org/bouncycastle2/asn1/ASN1Sequence:getObjectAt	(I)Lorg/bouncycastle2/asn1/DEREncodable;
    //   1603: checkcast 295	org/bouncycastle2/asn1/ASN1Sequence
    //   1606: astore 102
    //   1608: aload 102
    //   1610: iconst_0
    //   1611: invokevirtual 351	org/bouncycastle2/asn1/ASN1Sequence:getObjectAt	(I)Lorg/bouncycastle2/asn1/DEREncodable;
    //   1614: checkcast 231	org/bouncycastle2/asn1/DERObjectIdentifier
    //   1617: invokevirtual 461	org/bouncycastle2/asn1/DERObjectIdentifier:getId	()Ljava/lang/String;
    //   1620: astore 103
    //   1622: aload 102
    //   1624: iconst_1
    //   1625: invokevirtual 351	org/bouncycastle2/asn1/ASN1Sequence:getObjectAt	(I)Lorg/bouncycastle2/asn1/DEREncodable;
    //   1628: checkcast 231	org/bouncycastle2/asn1/DERObjectIdentifier
    //   1631: invokevirtual 461	org/bouncycastle2/asn1/DERObjectIdentifier:getId	()Ljava/lang/String;
    //   1634: astore 104
    //   1636: aload 76
    //   1638: aload 103
    //   1640: invokeinterface 521 2 0
    //   1645: ifne +47 -> 1692
    //   1648: new 418	java/util/HashSet
    //   1651: dup
    //   1652: invokespecial 419	java/util/HashSet:<init>	()V
    //   1655: astore 105
    //   1657: aload 105
    //   1659: aload 104
    //   1661: invokeinterface 424 2 0
    //   1666: pop
    //   1667: aload 76
    //   1669: aload 103
    //   1671: aload 105
    //   1673: invokeinterface 525 3 0
    //   1678: pop
    //   1679: aload 77
    //   1681: aload 103
    //   1683: invokeinterface 424 2 0
    //   1688: pop
    //   1689: goto +1081 -> 2770
    //   1692: aload 76
    //   1694: aload 103
    //   1696: invokeinterface 528 2 0
    //   1701: checkcast 184	java/util/Set
    //   1704: aload 104
    //   1706: invokeinterface 424 2 0
    //   1711: pop
    //   1712: goto +1058 -> 2770
    //   1715: aload 80
    //   1717: invokeinterface 132 1 0
    //   1722: checkcast 233	java/lang/String
    //   1725: astore 96
    //   1727: iload 11
    //   1729: ifle +86 -> 1815
    //   1732: iload 65
    //   1734: aload_2
    //   1735: aload 96
    //   1737: aload 76
    //   1739: aload 13
    //   1741: invokestatic 532	org/bouncycastle2/x509/PKIXCertPathReviewer:prepareNextCertB1	(I[Ljava/util/List;Ljava/lang/String;Ljava/util/Map;Ljava/security/cert/X509Certificate;)V
    //   1744: goto -442 -> 1302
    //   1747: astore 100
    //   1749: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1752: dup
    //   1753: ldc 11
    //   1755: ldc_w 451
    //   1758: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1761: astore 101
    //   1763: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1766: dup
    //   1767: aload 101
    //   1769: aload 100
    //   1771: aload_0
    //   1772: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   1775: iload 15
    //   1777: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   1780: athrow
    //   1781: astore 98
    //   1783: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1786: dup
    //   1787: ldc 11
    //   1789: ldc_w 481
    //   1792: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1795: astore 99
    //   1797: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1800: dup
    //   1801: aload 99
    //   1803: aload 98
    //   1805: aload_0
    //   1806: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   1809: iload 15
    //   1811: invokespecial 253	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   1814: athrow
    //   1815: iload 11
    //   1817: ifgt -515 -> 1302
    //   1820: iload 65
    //   1822: aload_2
    //   1823: aload 96
    //   1825: aload 7
    //   1827: invokestatic 536	org/bouncycastle2/x509/PKIXCertPathReviewer:prepareNextCertB2	(I[Ljava/util/List;Ljava/lang/String;Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;)Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;
    //   1830: astore 97
    //   1832: aload 97
    //   1834: astore 7
    //   1836: goto -534 -> 1302
    //   1839: aload 86
    //   1841: invokeinterface 371 1 0
    //   1846: checkcast 538	org/bouncycastle2/asn1/ASN1TaggedObject
    //   1849: astore 88
    //   1851: aload 88
    //   1853: invokevirtual 541	org/bouncycastle2/asn1/ASN1TaggedObject:getTagNo	()I
    //   1856: tableswitch	default:+920 -> 2776, 0:+24->1880, 1:+52->1908
    //   1881: pop2
    //   1882: iconst_0
    //   1883: invokestatic 544	org/bouncycastle2/asn1/DERInteger:getInstance	(Lorg/bouncycastle2/asn1/ASN1TaggedObject;Z)Lorg/bouncycastle2/asn1/DERInteger;
    //   1886: invokevirtual 512	org/bouncycastle2/asn1/DERInteger:getValue	()Ljava/math/BigInteger;
    //   1889: invokevirtual 408	java/math/BigInteger:intValue	()I
    //   1892: istore 90
    //   1894: iload 90
    //   1896: iload 9
    //   1898: if_icmpge -521 -> 1377
    //   1901: iload 90
    //   1903: istore 9
    //   1905: goto -528 -> 1377
    //   1908: aload 88
    //   1910: iconst_0
    //   1911: invokestatic 544	org/bouncycastle2/asn1/DERInteger:getInstance	(Lorg/bouncycastle2/asn1/ASN1TaggedObject;Z)Lorg/bouncycastle2/asn1/DERInteger;
    //   1914: invokevirtual 512	org/bouncycastle2/asn1/DERInteger:getValue	()Ljava/math/BigInteger;
    //   1917: invokevirtual 408	java/math/BigInteger:intValue	()I
    //   1920: istore 89
    //   1922: iload 89
    //   1924: iload 11
    //   1926: if_icmpge -549 -> 1377
    //   1929: iload 89
    //   1931: istore 11
    //   1933: goto -556 -> 1377
    //   1936: astore 82
    //   1938: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1941: dup
    //   1942: ldc 11
    //   1944: ldc_w 546
    //   1947: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1950: astore 83
    //   1952: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1955: dup
    //   1956: aload 83
    //   1958: aload_0
    //   1959: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   1962: iload 15
    //   1964: invokespecial 445	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
    //   1967: athrow
    //   1968: astore 91
    //   1970: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1973: dup
    //   1974: ldc 11
    //   1976: ldc_w 548
    //   1979: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1982: astore 92
    //   1984: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1987: dup
    //   1988: aload 92
    //   1990: aload_0
    //   1991: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   1994: iload 15
    //   1996: invokespecial 445	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
    //   1999: athrow
    //   2000: aload 21
    //   2002: invokeinterface 371 1 0
    //   2007: checkcast 538	org/bouncycastle2/asn1/ASN1TaggedObject
    //   2010: astore 23
    //   2012: aload 23
    //   2014: invokevirtual 541	org/bouncycastle2/asn1/ASN1TaggedObject:getTagNo	()I
    //   2017: tableswitch	default:+762 -> 2779, 0:+19->2036
    //   2037: fload 3
    //   2039: invokestatic 544	org/bouncycastle2/asn1/DERInteger:getInstance	(Lorg/bouncycastle2/asn1/ASN1TaggedObject;Z)Lorg/bouncycastle2/asn1/DERInteger;
    //   2042: invokevirtual 512	org/bouncycastle2/asn1/DERInteger:getValue	()Ljava/math/BigInteger;
    //   2045: invokevirtual 408	java/math/BigInteger:intValue	()I
    //   2048: istore 24
    //   2050: iload 24
    //   2052: ifne -1848 -> 204
    //   2055: iconst_0
    //   2056: istore 9
    //   2058: goto -1854 -> 204
    //   2061: astore 17
    //   2063: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   2066: dup
    //   2067: ldc 11
    //   2069: ldc_w 546
    //   2072: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   2075: astore 18
    //   2077: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   2080: dup
    //   2081: aload 18
    //   2083: aload_0
    //   2084: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   2087: iload 15
    //   2089: invokespecial 445	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
    //   2092: athrow
    //   2093: iload 9
    //   2095: ifgt +626 -> 2721
    //   2098: aload 35
    //   2100: ifnonnull +621 -> 2721
    //   2103: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   2106: dup
    //   2107: ldc 11
    //   2109: ldc_w 550
    //   2112: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   2115: astore 37
    //   2117: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   2120: dup
    //   2121: aload 37
    //   2123: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   2126: athrow
    //   2127: aload_1
    //   2128: invokestatic 554	org/bouncycastle2/x509/PKIXCertPathReviewer:isAnyPolicy	(Ljava/util/Set;)Z
    //   2131: ifeq +302 -> 2433
    //   2134: aload_0
    //   2135: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   2138: invokevirtual 434	java/security/cert/PKIXParameters:isExplicitPolicyRequired	()Z
    //   2141: ifeq +652 -> 2793
    //   2144: aload 12
    //   2146: invokeinterface 187 1 0
    //   2151: ifeq +33 -> 2184
    //   2154: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   2157: dup
    //   2158: ldc 11
    //   2160: ldc_w 442
    //   2163: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   2166: astore 46
    //   2168: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   2171: dup
    //   2172: aload 46
    //   2174: aload_0
    //   2175: getfield 250	org/bouncycastle2/x509/PKIXCertPathReviewer:certPath	Ljava/security/cert/CertPath;
    //   2178: iload 15
    //   2180: invokespecial 445	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/security/cert/CertPath;I)V
    //   2183: athrow
    //   2184: new 418	java/util/HashSet
    //   2187: dup
    //   2188: invokespecial 419	java/util/HashSet:<init>	()V
    //   2191: astore 47
    //   2193: iconst_0
    //   2194: istore 48
    //   2196: aload_2
    //   2197: arraylength
    //   2198: istore 49
    //   2200: iload 48
    //   2202: iload 49
    //   2204: if_icmplt +38 -> 2242
    //   2207: aload 47
    //   2209: invokeinterface 229 1 0
    //   2214: astore 50
    //   2216: aload 50
    //   2218: invokeinterface 122 1 0
    //   2223: ifne +116 -> 2339
    //   2226: aload 7
    //   2228: ifnull +565 -> 2793
    //   2231: iconst_m1
    //   2232: aload_0
    //   2233: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   2236: iadd
    //   2237: istore 51
    //   2239: goto +549 -> 2788
    //   2242: aload_2
    //   2243: iload 48
    //   2245: aaload
    //   2246: astore 58
    //   2248: iconst_0
    //   2249: istore 59
    //   2251: aload 58
    //   2253: invokeinterface 128 1 0
    //   2258: istore 60
    //   2260: iload 59
    //   2262: iload 60
    //   2264: if_icmplt +9 -> 2273
    //   2267: iinc 48 1
    //   2270: goto -74 -> 2196
    //   2273: aload 58
    //   2275: iload 59
    //   2277: invokeinterface 176 2 0
    //   2282: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   2285: astore 61
    //   2287: ldc_w 421
    //   2290: aload 61
    //   2292: invokevirtual 494	org/bouncycastle2/jce/provider/PKIXPolicyNode:getValidPolicy	()Ljava/lang/String;
    //   2295: invokevirtual 464	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   2298: ifeq +502 -> 2800
    //   2301: aload 61
    //   2303: invokevirtual 487	org/bouncycastle2/jce/provider/PKIXPolicyNode:getChildren	()Ljava/util/Iterator;
    //   2306: astore 62
    //   2308: aload 62
    //   2310: invokeinterface 122 1 0
    //   2315: ifne +6 -> 2321
    //   2318: goto +482 -> 2800
    //   2321: aload 47
    //   2323: aload 62
    //   2325: invokeinterface 132 1 0
    //   2330: invokeinterface 424 2 0
    //   2335: pop
    //   2336: goto -28 -> 2308
    //   2339: aload 50
    //   2341: invokeinterface 132 1 0
    //   2346: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   2349: invokevirtual 494	org/bouncycastle2/jce/provider/PKIXPolicyNode:getValidPolicy	()Ljava/lang/String;
    //   2352: astore 56
    //   2354: aload 12
    //   2356: aload 56
    //   2358: invokeinterface 224 2 0
    //   2363: pop
    //   2364: goto -148 -> 2216
    //   2367: aload_2
    //   2368: iload 51
    //   2370: aaload
    //   2371: astore 52
    //   2373: iconst_0
    //   2374: istore 53
    //   2376: aload 52
    //   2378: invokeinterface 128 1 0
    //   2383: istore 54
    //   2385: iload 53
    //   2387: iload 54
    //   2389: if_icmplt +9 -> 2398
    //   2392: iinc 51 255
    //   2395: goto +393 -> 2788
    //   2398: aload 52
    //   2400: iload 53
    //   2402: invokeinterface 176 2 0
    //   2407: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   2410: astore 55
    //   2412: aload 55
    //   2414: invokevirtual 497	org/bouncycastle2/jce/provider/PKIXPolicyNode:hasChildren	()Z
    //   2417: ifne +389 -> 2806
    //   2420: aload 7
    //   2422: aload_2
    //   2423: aload 55
    //   2425: invokestatic 501	org/bouncycastle2/x509/PKIXCertPathReviewer:removePolicyNode	(Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;[Ljava/util/List;Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;)Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;
    //   2428: astore 7
    //   2430: goto +376 -> 2806
    //   2433: new 418	java/util/HashSet
    //   2436: dup
    //   2437: invokespecial 419	java/util/HashSet:<init>	()V
    //   2440: astore 25
    //   2442: iconst_0
    //   2443: istore 26
    //   2445: aload_2
    //   2446: arraylength
    //   2447: istore 27
    //   2449: iload 26
    //   2451: iload 27
    //   2453: if_icmplt +38 -> 2491
    //   2456: aload 25
    //   2458: invokeinterface 229 1 0
    //   2463: astore 28
    //   2465: aload 28
    //   2467: invokeinterface 122 1 0
    //   2472: ifne +137 -> 2609
    //   2475: aload 7
    //   2477: ifnull +340 -> 2817
    //   2480: iconst_m1
    //   2481: aload_0
    //   2482: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   2485: iadd
    //   2486: istore 29
    //   2488: goto +324 -> 2812
    //   2491: aload_2
    //   2492: iload 26
    //   2494: aaload
    //   2495: astore 39
    //   2497: iconst_0
    //   2498: istore 40
    //   2500: aload 39
    //   2502: invokeinterface 128 1 0
    //   2507: istore 41
    //   2509: iload 40
    //   2511: iload 41
    //   2513: if_icmplt +9 -> 2522
    //   2516: iinc 26 1
    //   2519: goto -74 -> 2445
    //   2522: aload 39
    //   2524: iload 40
    //   2526: invokeinterface 176 2 0
    //   2531: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   2534: astore 42
    //   2536: ldc_w 421
    //   2539: aload 42
    //   2541: invokevirtual 494	org/bouncycastle2/jce/provider/PKIXPolicyNode:getValidPolicy	()Ljava/lang/String;
    //   2544: invokevirtual 464	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   2547: ifeq +277 -> 2824
    //   2550: aload 42
    //   2552: invokevirtual 487	org/bouncycastle2/jce/provider/PKIXPolicyNode:getChildren	()Ljava/util/Iterator;
    //   2555: astore 43
    //   2557: aload 43
    //   2559: invokeinterface 122 1 0
    //   2564: ifne +6 -> 2570
    //   2567: goto +257 -> 2824
    //   2570: aload 43
    //   2572: invokeinterface 132 1 0
    //   2577: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   2580: astore 44
    //   2582: ldc_w 421
    //   2585: aload 44
    //   2587: invokevirtual 494	org/bouncycastle2/jce/provider/PKIXPolicyNode:getValidPolicy	()Ljava/lang/String;
    //   2590: invokevirtual 464	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   2593: ifne -36 -> 2557
    //   2596: aload 25
    //   2598: aload 44
    //   2600: invokeinterface 424 2 0
    //   2605: pop
    //   2606: goto -49 -> 2557
    //   2609: aload 28
    //   2611: invokeinterface 132 1 0
    //   2616: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   2619: astore 38
    //   2621: aload_1
    //   2622: aload 38
    //   2624: invokevirtual 494	org/bouncycastle2/jce/provider/PKIXPolicyNode:getValidPolicy	()Ljava/lang/String;
    //   2627: invokeinterface 224 2 0
    //   2632: ifne -167 -> 2465
    //   2635: aload 7
    //   2637: aload_2
    //   2638: aload 38
    //   2640: invokestatic 501	org/bouncycastle2/x509/PKIXCertPathReviewer:removePolicyNode	(Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;[Ljava/util/List;Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;)Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;
    //   2643: astore 7
    //   2645: goto -180 -> 2465
    //   2648: aload_2
    //   2649: iload 29
    //   2651: aaload
    //   2652: astore 30
    //   2654: iconst_0
    //   2655: istore 31
    //   2657: aload 30
    //   2659: invokeinterface 128 1 0
    //   2664: istore 32
    //   2666: iload 31
    //   2668: iload 32
    //   2670: if_icmplt +9 -> 2679
    //   2673: iinc 29 255
    //   2676: goto +136 -> 2812
    //   2679: aload 30
    //   2681: iload 31
    //   2683: invokeinterface 176 2 0
    //   2688: checkcast 426	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   2691: astore 33
    //   2693: aload 33
    //   2695: invokevirtual 497	org/bouncycastle2/jce/provider/PKIXPolicyNode:hasChildren	()Z
    //   2698: ifne +17 -> 2715
    //   2701: aload 7
    //   2703: aload_2
    //   2704: aload 33
    //   2706: invokestatic 501	org/bouncycastle2/x509/PKIXCertPathReviewer:removePolicyNode	(Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;[Ljava/util/List;Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;)Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;
    //   2709: astore 34
    //   2711: aload 34
    //   2713: astore 7
    //   2715: iinc 31 1
    //   2718: goto -61 -> 2657
    //   2721: aload 35
    //   2723: pop
    //   2724: return
    //   2725: aload 120
    //   2727: astore 12
    //   2729: goto -2301 -> 428
    //   2732: iload 65
    //   2734: iconst_1
    //   2735: isub
    //   2736: istore 121
    //   2738: goto -2268 -> 470
    //   2741: aload 69
    //   2743: ifnonnull -2216 -> 527
    //   2746: aconst_null
    //   2747: astore 7
    //   2749: goto -2222 -> 527
    //   2752: iinc 121 255
    //   2755: goto -2285 -> 470
    //   2758: iinc 128 1
    //   2761: goto -1653 -> 1108
    //   2764: iinc 111 1
    //   2767: goto -1535 -> 1232
    //   2770: iinc 78 1
    //   2773: goto -1494 -> 1279
    //   2776: goto -1399 -> 1377
    //   2779: goto -2575 -> 204
    //   2782: aconst_null
    //   2783: astore 35
    //   2785: goto -692 -> 2093
    //   2788: iload 51
    //   2790: ifge -423 -> 2367
    //   2793: aload 7
    //   2795: astore 35
    //   2797: goto -704 -> 2093
    //   2800: iinc 59 1
    //   2803: goto -552 -> 2251
    //   2806: iinc 53 1
    //   2809: goto -433 -> 2376
    //   2812: iload 29
    //   2814: ifge -166 -> 2648
    //   2817: aload 7
    //   2819: astore 35
    //   2821: goto -728 -> 2093
    //   2824: iinc 40 1
    //   2827: goto -327 -> 2500
    //
    // Exception table:
    //   from	to	target	type
    //   137	150	263	org/bouncycastle2/x509/CertPathReviewerException
    //   155	162	263	org/bouncycastle2/x509/CertPathReviewerException
    //   175	192	263	org/bouncycastle2/x509/CertPathReviewerException
    //   197	204	263	org/bouncycastle2/x509/CertPathReviewerException
    //   204	213	263	org/bouncycastle2/x509/CertPathReviewerException
    //   223	263	263	org/bouncycastle2/x509/CertPathReviewerException
    //   329	354	263	org/bouncycastle2/x509/CertPathReviewerException
    //   354	371	263	org/bouncycastle2/x509/CertPathReviewerException
    //   381	397	263	org/bouncycastle2/x509/CertPathReviewerException
    //   397	407	263	org/bouncycastle2/x509/CertPathReviewerException
    //   412	425	263	org/bouncycastle2/x509/CertPathReviewerException
    //   433	450	263	org/bouncycastle2/x509/CertPathReviewerException
    //   450	457	263	org/bouncycastle2/x509/CertPathReviewerException
    //   457	467	263	org/bouncycastle2/x509/CertPathReviewerException
    //   475	482	263	org/bouncycastle2/x509/CertPathReviewerException
    //   487	505	263	org/bouncycastle2/x509/CertPathReviewerException
    //   508	517	263	org/bouncycastle2/x509/CertPathReviewerException
    //   537	561	263	org/bouncycastle2/x509/CertPathReviewerException
    //   563	595	263	org/bouncycastle2/x509/CertPathReviewerException
    //   595	640	263	org/bouncycastle2/x509/CertPathReviewerException
    //   645	655	263	org/bouncycastle2/x509/CertPathReviewerException
    //   655	678	263	org/bouncycastle2/x509/CertPathReviewerException
    //   683	715	263	org/bouncycastle2/x509/CertPathReviewerException
    //   715	733	263	org/bouncycastle2/x509/CertPathReviewerException
    //   733	743	263	org/bouncycastle2/x509/CertPathReviewerException
    //   750	781	263	org/bouncycastle2/x509/CertPathReviewerException
    //   784	812	263	org/bouncycastle2/x509/CertPathReviewerException
    //   817	827	263	org/bouncycastle2/x509/CertPathReviewerException
    //   833	839	263	org/bouncycastle2/x509/CertPathReviewerException
    //   842	851	263	org/bouncycastle2/x509/CertPathReviewerException
    //   858	884	263	org/bouncycastle2/x509/CertPathReviewerException
    //   884	894	263	org/bouncycastle2/x509/CertPathReviewerException
    //   902	934	263	org/bouncycastle2/x509/CertPathReviewerException
    //   934	958	263	org/bouncycastle2/x509/CertPathReviewerException
    //   961	968	263	org/bouncycastle2/x509/CertPathReviewerException
    //   968	978	263	org/bouncycastle2/x509/CertPathReviewerException
    //   983	1048	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1051	1069	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1072	1093	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1099	1105	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1108	1117	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1127	1159	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1167	1184	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1190	1196	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1203	1217	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1222	1229	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1232	1239	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1251	1276	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1279	1286	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1293	1302	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1302	1312	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1312	1319	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1348	1365	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1370	1377	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1377	1386	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1391	1408	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1413	1423	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1442	1474	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1474	1552	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1552	1596	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1596	1689	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1692	1712	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1715	1727	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1732	1744	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1749	1781	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1783	1815	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1820	1832	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1839	1880	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1880	1894	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1908	1922	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1938	1968	263	org/bouncycastle2/x509/CertPathReviewerException
    //   1970	2000	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2000	2036	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2036	2050	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2063	2093	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2103	2127	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2127	2184	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2184	2193	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2196	2200	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2207	2216	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2216	2226	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2231	2239	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2242	2248	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2251	2260	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2273	2308	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2308	2318	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2321	2336	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2339	2364	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2367	2373	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2376	2385	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2398	2430	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2433	2442	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2445	2449	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2456	2465	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2465	2475	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2480	2488	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2491	2497	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2500	2509	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2522	2557	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2557	2567	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2570	2606	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2609	2645	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2648	2654	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2657	2666	263	org/bouncycastle2/x509/CertPathReviewerException
    //   2679	2711	263	org/bouncycastle2/x509/CertPathReviewerException
    //   354	371	561	org/bouncycastle2/jce/provider/AnnotatedException
    //   645	655	681	java/security/cert/CertPathValidatorException
    //   817	827	900	java/security/cert/CertPathValidatorException
    //   1203	1217	1440	org/bouncycastle2/jce/provider/AnnotatedException
    //   1732	1744	1747	org/bouncycastle2/jce/provider/AnnotatedException
    //   1732	1744	1781	java/security/cert/CertPathValidatorException
    //   1348	1365	1936	org/bouncycastle2/jce/provider/AnnotatedException
    //   1370	1377	1936	org/bouncycastle2/jce/provider/AnnotatedException
    //   1377	1386	1936	org/bouncycastle2/jce/provider/AnnotatedException
    //   1839	1880	1936	org/bouncycastle2/jce/provider/AnnotatedException
    //   1880	1894	1936	org/bouncycastle2/jce/provider/AnnotatedException
    //   1908	1922	1936	org/bouncycastle2/jce/provider/AnnotatedException
    //   1391	1408	1968	org/bouncycastle2/jce/provider/AnnotatedException
    //   1413	1423	1968	org/bouncycastle2/jce/provider/AnnotatedException
    //   175	192	2061	org/bouncycastle2/jce/provider/AnnotatedException
    //   197	204	2061	org/bouncycastle2/jce/provider/AnnotatedException
    //   204	213	2061	org/bouncycastle2/jce/provider/AnnotatedException
    //   2000	2036	2061	org/bouncycastle2/jce/provider/AnnotatedException
    //   2036	2050	2061	org/bouncycastle2/jce/provider/AnnotatedException
  }

  // ERROR //
  private void checkSignatures()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: iconst_2
    //   3: anewarray 139	java/lang/Object
    //   6: astore_2
    //   7: aload_2
    //   8: iconst_0
    //   9: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   12: dup
    //   13: aload_0
    //   14: getfield 571	org/bouncycastle2/x509/PKIXCertPathReviewer:validDate	Ljava/util/Date;
    //   17: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   20: aastore
    //   21: aload_2
    //   22: iconst_1
    //   23: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   26: dup
    //   27: new 574	java/util/Date
    //   30: dup
    //   31: invokespecial 575	java/util/Date:<init>	()V
    //   34: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   37: aastore
    //   38: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   41: dup
    //   42: ldc 11
    //   44: ldc_w 577
    //   47: aload_2
    //   48: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   51: astore_3
    //   52: aload_0
    //   53: aload_3
    //   54: invokevirtual 390	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   57: aload_0
    //   58: getfield 124	org/bouncycastle2/x509/PKIXCertPathReviewer:certs	Ljava/util/List;
    //   61: iconst_m1
    //   62: aload_0
    //   63: getfield 124	org/bouncycastle2/x509/PKIXCertPathReviewer:certs	Ljava/util/List;
    //   66: invokeinterface 128 1 0
    //   71: iadd
    //   72: invokeinterface 176 2 0
    //   77: checkcast 178	java/security/cert/X509Certificate
    //   80: astore 84
    //   82: aload_0
    //   83: aload 84
    //   85: aload_0
    //   86: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   89: invokevirtual 580	java/security/cert/PKIXParameters:getTrustAnchors	()Ljava/util/Set;
    //   92: invokevirtual 583	org/bouncycastle2/x509/PKIXCertPathReviewer:getTrustAnchors	(Ljava/security/cert/X509Certificate;Ljava/util/Set;)Ljava/util/Collection;
    //   95: astore 85
    //   97: aload 85
    //   99: invokeinterface 586 1 0
    //   104: istore 86
    //   106: aconst_null
    //   107: astore_1
    //   108: iload 86
    //   110: iconst_1
    //   111: if_icmple +219 -> 330
    //   114: iconst_2
    //   115: anewarray 139	java/lang/Object
    //   118: astore 87
    //   120: aload 87
    //   122: iconst_0
    //   123: new 88	java/lang/Integer
    //   126: dup
    //   127: aload 85
    //   129: invokeinterface 586 1 0
    //   134: invokespecial 384	java/lang/Integer:<init>	(I)V
    //   137: aastore
    //   138: aload 87
    //   140: iconst_1
    //   141: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   144: dup
    //   145: aload 84
    //   147: invokevirtual 590	java/security/cert/X509Certificate:getIssuerX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   150: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   153: aastore
    //   154: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   157: dup
    //   158: ldc 11
    //   160: ldc_w 592
    //   163: aload 87
    //   165: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   168: astore 88
    //   170: aload_0
    //   171: aload 88
    //   173: invokevirtual 394	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   176: aconst_null
    //   177: astore 8
    //   179: aload_1
    //   180: ifnull +69 -> 249
    //   183: aload_1
    //   184: invokevirtual 598	java/security/cert/TrustAnchor:getTrustedCert	()Ljava/security/cert/X509Certificate;
    //   187: astore 75
    //   189: aload 75
    //   191: ifnull +402 -> 593
    //   194: aload 75
    //   196: invokestatic 273	org/bouncycastle2/x509/PKIXCertPathReviewer:getSubjectPrincipal	(Ljava/security/cert/X509Certificate;)Ljavax/security/auth/x500/X500Principal;
    //   199: astore 82
    //   201: aload 82
    //   203: astore 8
    //   205: aload 75
    //   207: ifnull +42 -> 249
    //   210: aload 75
    //   212: invokevirtual 602	java/security/cert/X509Certificate:getKeyUsage	()[Z
    //   215: astore 77
    //   217: aload 77
    //   219: ifnull +30 -> 249
    //   222: aload 77
    //   224: iconst_5
    //   225: baload
    //   226: ifne +23 -> 249
    //   229: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   232: dup
    //   233: ldc 11
    //   235: ldc_w 604
    //   238: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   241: astore 78
    //   243: aload_0
    //   244: aload 78
    //   246: invokevirtual 390	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   249: aload 8
    //   251: astore 9
    //   253: aconst_null
    //   254: astore 10
    //   256: aconst_null
    //   257: astore 11
    //   259: aload_1
    //   260: ifnull +40 -> 300
    //   263: aload_1
    //   264: invokevirtual 598	java/security/cert/TrustAnchor:getTrustedCert	()Ljava/security/cert/X509Certificate;
    //   267: astore 10
    //   269: aload 10
    //   271: ifnull +393 -> 664
    //   274: aload 10
    //   276: invokevirtual 608	java/security/cert/X509Certificate:getPublicKey	()Ljava/security/PublicKey;
    //   279: astore 11
    //   281: aload 11
    //   283: invokestatic 612	org/bouncycastle2/x509/PKIXCertPathReviewer:getAlgorithmIdentifier	(Ljava/security/PublicKey;)Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   286: astore 72
    //   288: aload 72
    //   290: invokevirtual 617	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getObjectId	()Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   293: pop
    //   294: aload 72
    //   296: invokevirtual 621	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getParameters	()Lorg/bouncycastle2/asn1/DEREncodable;
    //   299: pop
    //   300: iconst_m1
    //   301: aload_0
    //   302: getfield 124	org/bouncycastle2/x509/PKIXCertPathReviewer:certs	Ljava/util/List;
    //   305: invokeinterface 128 1 0
    //   310: iadd
    //   311: istore 12
    //   313: iload 12
    //   315: ifge +383 -> 698
    //   318: aload_0
    //   319: aload_1
    //   320: putfield 623	org/bouncycastle2/x509/PKIXCertPathReviewer:trustAnchor	Ljava/security/cert/TrustAnchor;
    //   323: aload_0
    //   324: aload 11
    //   326: putfield 625	org/bouncycastle2/x509/PKIXCertPathReviewer:subjectPublicKey	Ljava/security/PublicKey;
    //   329: return
    //   330: aload 85
    //   332: invokeinterface 626 1 0
    //   337: istore 89
    //   339: aconst_null
    //   340: astore_1
    //   341: iload 89
    //   343: ifeq +89 -> 432
    //   346: iconst_2
    //   347: anewarray 139	java/lang/Object
    //   350: astore 90
    //   352: aload 90
    //   354: iconst_0
    //   355: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   358: dup
    //   359: aload 84
    //   361: invokevirtual 590	java/security/cert/X509Certificate:getIssuerX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   364: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   367: aastore
    //   368: aload 90
    //   370: iconst_1
    //   371: new 88	java/lang/Integer
    //   374: dup
    //   375: aload_0
    //   376: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   379: invokevirtual 580	java/security/cert/PKIXParameters:getTrustAnchors	()Ljava/util/Set;
    //   382: invokeinterface 627 1 0
    //   387: invokespecial 384	java/lang/Integer:<init>	(I)V
    //   390: aastore
    //   391: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   394: dup
    //   395: ldc 11
    //   397: ldc_w 629
    //   400: aload 90
    //   402: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   405: astore 91
    //   407: aload_0
    //   408: aload 91
    //   410: invokevirtual 394	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   413: aconst_null
    //   414: astore_1
    //   415: goto -239 -> 176
    //   418: astore 83
    //   420: aload_0
    //   421: aload 83
    //   423: invokevirtual 165	org/bouncycastle2/x509/CertPathReviewerException:getErrorMessage	()Lorg/bouncycastle2/i18n/ErrorBundle;
    //   426: invokevirtual 394	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   429: goto -253 -> 176
    //   432: aload 85
    //   434: invokeinterface 630 1 0
    //   439: invokeinterface 132 1 0
    //   444: checkcast 594	java/security/cert/TrustAnchor
    //   447: astore_1
    //   448: aload_1
    //   449: invokevirtual 598	java/security/cert/TrustAnchor:getTrustedCert	()Ljava/security/cert/X509Certificate;
    //   452: ifnull +128 -> 580
    //   455: aload_1
    //   456: invokevirtual 598	java/security/cert/TrustAnchor:getTrustedCert	()Ljava/security/cert/X509Certificate;
    //   459: invokevirtual 608	java/security/cert/X509Certificate:getPublicKey	()Ljava/security/PublicKey;
    //   462: astore 98
    //   464: aload 98
    //   466: astore 93
    //   468: aload_0
    //   469: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   472: invokevirtual 633	java/security/cert/PKIXParameters:getSigProvider	()Ljava/lang/String;
    //   475: astore 97
    //   477: aload 84
    //   479: aload 93
    //   481: aload 97
    //   483: invokestatic 637	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:verifyX509Certificate	(Ljava/security/cert/X509Certificate;Ljava/security/PublicKey;Ljava/lang/String;)V
    //   486: goto -310 -> 176
    //   489: astore 95
    //   491: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   494: dup
    //   495: ldc 11
    //   497: ldc_w 639
    //   500: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   503: astore 96
    //   505: aload_0
    //   506: aload 96
    //   508: invokevirtual 394	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   511: goto -335 -> 176
    //   514: astore 4
    //   516: iconst_2
    //   517: anewarray 139	java/lang/Object
    //   520: astore 5
    //   522: aload 5
    //   524: iconst_0
    //   525: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   528: dup
    //   529: aload 4
    //   531: invokevirtual 640	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   534: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   537: aastore
    //   538: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   541: dup
    //   542: aload 4
    //   544: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   547: astore 6
    //   549: aload 5
    //   551: iconst_1
    //   552: aload 6
    //   554: aastore
    //   555: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   558: dup
    //   559: ldc 11
    //   561: ldc_w 642
    //   564: aload 5
    //   566: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   569: astore 7
    //   571: aload_0
    //   572: aload 7
    //   574: invokevirtual 394	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   577: goto -401 -> 176
    //   580: aload_1
    //   581: invokevirtual 645	java/security/cert/TrustAnchor:getCAPublicKey	()Ljava/security/PublicKey;
    //   584: astore 92
    //   586: aload 92
    //   588: astore 93
    //   590: goto -122 -> 468
    //   593: new 279	javax/security/auth/x500/X500Principal
    //   596: dup
    //   597: aload_1
    //   598: invokevirtual 648	java/security/cert/TrustAnchor:getCAName	()Ljava/lang/String;
    //   601: invokespecial 649	javax/security/auth/x500/X500Principal:<init>	(Ljava/lang/String;)V
    //   604: astore 76
    //   606: aload 76
    //   608: astore 8
    //   610: goto -405 -> 205
    //   613: astore 79
    //   615: iconst_1
    //   616: anewarray 139	java/lang/Object
    //   619: astore 80
    //   621: aload 80
    //   623: iconst_0
    //   624: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   627: dup
    //   628: aload_1
    //   629: invokevirtual 648	java/security/cert/TrustAnchor:getCAName	()Ljava/lang/String;
    //   632: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   635: aastore
    //   636: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   639: dup
    //   640: ldc 11
    //   642: ldc_w 651
    //   645: aload 80
    //   647: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   650: astore 81
    //   652: aload_0
    //   653: aload 81
    //   655: invokevirtual 394	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   658: aconst_null
    //   659: astore 8
    //   661: goto -456 -> 205
    //   664: aload_1
    //   665: invokevirtual 645	java/security/cert/TrustAnchor:getCAPublicKey	()Ljava/security/PublicKey;
    //   668: astore 11
    //   670: goto -389 -> 281
    //   673: astore 70
    //   675: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   678: dup
    //   679: ldc 11
    //   681: ldc_w 653
    //   684: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   687: astore 71
    //   689: aload_0
    //   690: aload 71
    //   692: invokevirtual 394	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   695: goto -395 -> 300
    //   698: aload_0
    //   699: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   702: iload 12
    //   704: isub
    //   705: istore 13
    //   707: aload_0
    //   708: getfield 124	org/bouncycastle2/x509/PKIXCertPathReviewer:certs	Ljava/util/List;
    //   711: iload 12
    //   713: invokeinterface 176 2 0
    //   718: checkcast 178	java/security/cert/X509Certificate
    //   721: astore 14
    //   723: aload 11
    //   725: ifnull +480 -> 1205
    //   728: aload 14
    //   730: aload 11
    //   732: aload_0
    //   733: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   736: invokevirtual 633	java/security/cert/PKIXParameters:getSigProvider	()Ljava/lang/String;
    //   739: invokestatic 637	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:verifyX509Certificate	(Ljava/security/cert/X509Certificate;Ljava/security/PublicKey;Ljava/lang/String;)V
    //   742: aload 14
    //   744: aload_0
    //   745: getfield 571	org/bouncycastle2/x509/PKIXCertPathReviewer:validDate	Ljava/util/Date;
    //   748: invokevirtual 657	java/security/cert/X509Certificate:checkValidity	(Ljava/util/Date;)V
    //   751: aload_0
    //   752: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   755: invokevirtual 660	java/security/cert/PKIXParameters:isRevocationEnabled	()Z
    //   758: ifeq +135 -> 893
    //   761: aload 14
    //   763: getstatic 53	org/bouncycastle2/x509/PKIXCertPathReviewer:CRL_DIST_POINTS	Ljava/lang/String;
    //   766: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   769: astore 52
    //   771: aconst_null
    //   772: astore 37
    //   774: aload 52
    //   776: ifnull +14 -> 790
    //   779: aload 52
    //   781: invokestatic 665	org/bouncycastle2/asn1/x509/CRLDistPoint:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/CRLDistPoint;
    //   784: astore 53
    //   786: aload 53
    //   788: astore 37
    //   790: aload 14
    //   792: getstatic 58	org/bouncycastle2/x509/PKIXCertPathReviewer:AUTH_INFO_ACCESS	Ljava/lang/String;
    //   795: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   798: astore 50
    //   800: aconst_null
    //   801: astore 40
    //   803: aload 50
    //   805: ifnull +14 -> 819
    //   808: aload 50
    //   810: invokestatic 670	org/bouncycastle2/asn1/x509/AuthorityInformationAccess:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/AuthorityInformationAccess;
    //   813: astore 51
    //   815: aload 51
    //   817: astore 40
    //   819: aload_0
    //   820: aload 37
    //   822: invokevirtual 674	org/bouncycastle2/x509/PKIXCertPathReviewer:getCRLDistUrls	(Lorg/bouncycastle2/asn1/x509/CRLDistPoint;)Ljava/util/Vector;
    //   825: astore 41
    //   827: aload_0
    //   828: aload 40
    //   830: invokevirtual 678	org/bouncycastle2/x509/PKIXCertPathReviewer:getOCSPUrls	(Lorg/bouncycastle2/asn1/x509/AuthorityInformationAccess;)Ljava/util/Vector;
    //   833: astore 42
    //   835: aload 41
    //   837: invokevirtual 681	java/util/Vector:iterator	()Ljava/util/Iterator;
    //   840: astore 43
    //   842: aload 43
    //   844: invokeinterface 122 1 0
    //   849: ifne +796 -> 1645
    //   852: aload 42
    //   854: invokevirtual 681	java/util/Vector:iterator	()Ljava/util/Iterator;
    //   857: astore 46
    //   859: aload 46
    //   861: invokeinterface 122 1 0
    //   866: ifne +830 -> 1696
    //   869: aload_0
    //   870: aload_0
    //   871: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   874: aload 14
    //   876: aload_0
    //   877: getfield 571	org/bouncycastle2/x509/PKIXCertPathReviewer:validDate	Ljava/util/Date;
    //   880: aload 10
    //   882: aload 11
    //   884: aload 41
    //   886: aload 42
    //   888: iload 12
    //   890: invokevirtual 685	org/bouncycastle2/x509/PKIXCertPathReviewer:checkRevocation	(Ljava/security/cert/PKIXParameters;Ljava/security/cert/X509Certificate;Ljava/util/Date;Ljava/security/cert/X509Certificate;Ljava/security/PublicKey;Ljava/util/Vector;Ljava/util/Vector;I)V
    //   893: aload 9
    //   895: ifnull +67 -> 962
    //   898: aload 14
    //   900: invokevirtual 590	java/security/cert/X509Certificate:getIssuerX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   903: aload 9
    //   905: invokevirtual 686	javax/security/auth/x500/X500Principal:equals	(Ljava/lang/Object;)Z
    //   908: ifne +54 -> 962
    //   911: iconst_2
    //   912: anewarray 139	java/lang/Object
    //   915: astore 33
    //   917: aload 33
    //   919: iconst_0
    //   920: aload 9
    //   922: invokevirtual 338	javax/security/auth/x500/X500Principal:getName	()Ljava/lang/String;
    //   925: aastore
    //   926: aload 33
    //   928: iconst_1
    //   929: aload 14
    //   931: invokevirtual 590	java/security/cert/X509Certificate:getIssuerX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   934: invokevirtual 338	javax/security/auth/x500/X500Principal:getName	()Ljava/lang/String;
    //   937: aastore
    //   938: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   941: dup
    //   942: ldc 11
    //   944: ldc_w 688
    //   947: aload 33
    //   949: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   952: astore 34
    //   954: aload_0
    //   955: aload 34
    //   957: iload 12
    //   959: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   962: iload 13
    //   964: aload_0
    //   965: getfield 265	org/bouncycastle2/x509/PKIXCertPathReviewer:n	I
    //   968: if_icmpeq +128 -> 1096
    //   971: aload 14
    //   973: ifnull +34 -> 1007
    //   976: aload 14
    //   978: invokevirtual 691	java/security/cert/X509Certificate:getVersion	()I
    //   981: iconst_1
    //   982: if_icmpne +25 -> 1007
    //   985: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   988: dup
    //   989: ldc 11
    //   991: ldc_w 693
    //   994: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   997: astore 32
    //   999: aload_0
    //   1000: aload 32
    //   1002: iload 12
    //   1004: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1007: aload 14
    //   1009: getstatic 215	org/bouncycastle2/x509/PKIXCertPathReviewer:BASIC_CONSTRAINTS	Ljava/lang/String;
    //   1012: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   1015: invokestatic 399	org/bouncycastle2/asn1/x509/BasicConstraints:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/BasicConstraints;
    //   1018: astore 29
    //   1020: aload 29
    //   1022: ifnull +741 -> 1763
    //   1025: aload 29
    //   1027: invokevirtual 696	org/bouncycastle2/asn1/x509/BasicConstraints:isCA	()Z
    //   1030: ifne +25 -> 1055
    //   1033: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1036: dup
    //   1037: ldc 11
    //   1039: ldc_w 693
    //   1042: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1045: astore 30
    //   1047: aload_0
    //   1048: aload 30
    //   1050: iload 12
    //   1052: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1055: aload 14
    //   1057: invokevirtual 602	java/security/cert/X509Certificate:getKeyUsage	()[Z
    //   1060: astore 27
    //   1062: aload 27
    //   1064: ifnull +32 -> 1096
    //   1067: aload 27
    //   1069: iconst_5
    //   1070: baload
    //   1071: ifne +25 -> 1096
    //   1074: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1077: dup
    //   1078: ldc 11
    //   1080: ldc_w 698
    //   1083: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1086: astore 28
    //   1088: aload_0
    //   1089: aload 28
    //   1091: iload 12
    //   1093: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1096: aload 14
    //   1098: astore 10
    //   1100: aload 14
    //   1102: invokevirtual 701	java/security/cert/X509Certificate:getSubjectX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   1105: astore 9
    //   1107: aload_0
    //   1108: getfield 124	org/bouncycastle2/x509/PKIXCertPathReviewer:certs	Ljava/util/List;
    //   1111: iload 12
    //   1113: invokestatic 705	org/bouncycastle2/x509/PKIXCertPathReviewer:getNextWorkingKey	(Ljava/util/List;I)Ljava/security/PublicKey;
    //   1116: astore 11
    //   1118: aload 11
    //   1120: invokestatic 612	org/bouncycastle2/x509/PKIXCertPathReviewer:getAlgorithmIdentifier	(Ljava/security/PublicKey;)Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   1123: astore 22
    //   1125: aload 22
    //   1127: invokevirtual 617	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getObjectId	()Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   1130: pop
    //   1131: aload 22
    //   1133: invokevirtual 621	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getParameters	()Lorg/bouncycastle2/asn1/DEREncodable;
    //   1136: pop
    //   1137: iinc 12 255
    //   1140: goto -827 -> 313
    //   1143: astore 67
    //   1145: iconst_3
    //   1146: anewarray 139	java/lang/Object
    //   1149: astore 68
    //   1151: aload 68
    //   1153: iconst_0
    //   1154: aload 67
    //   1156: invokevirtual 706	java/security/GeneralSecurityException:getMessage	()Ljava/lang/String;
    //   1159: aastore
    //   1160: aload 68
    //   1162: iconst_1
    //   1163: aload 67
    //   1165: aastore
    //   1166: aload 68
    //   1168: iconst_2
    //   1169: aload 67
    //   1171: invokevirtual 146	java/lang/Object:getClass	()Ljava/lang/Class;
    //   1174: invokevirtual 151	java/lang/Class:getName	()Ljava/lang/String;
    //   1177: aastore
    //   1178: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1181: dup
    //   1182: ldc 11
    //   1184: ldc_w 708
    //   1187: aload 68
    //   1189: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1192: astore 69
    //   1194: aload_0
    //   1195: aload 69
    //   1197: iload 12
    //   1199: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1202: goto -460 -> 742
    //   1205: aload 14
    //   1207: invokestatic 269	org/bouncycastle2/x509/PKIXCertPathReviewer:isSelfIssued	(Ljava/security/cert/X509Certificate;)Z
    //   1210: ifeq +107 -> 1317
    //   1213: aload 14
    //   1215: aload 14
    //   1217: invokevirtual 608	java/security/cert/X509Certificate:getPublicKey	()Ljava/security/PublicKey;
    //   1220: aload_0
    //   1221: getfield 104	org/bouncycastle2/x509/PKIXCertPathReviewer:pkixParams	Ljava/security/cert/PKIXParameters;
    //   1224: invokevirtual 633	java/security/cert/PKIXParameters:getSigProvider	()Ljava/lang/String;
    //   1227: invokestatic 637	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:verifyX509Certificate	(Ljava/security/cert/X509Certificate;Ljava/security/PublicKey;Ljava/lang/String;)V
    //   1230: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1233: dup
    //   1234: ldc 11
    //   1236: ldc_w 710
    //   1239: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1242: astore 66
    //   1244: aload_0
    //   1245: aload 66
    //   1247: iload 12
    //   1249: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1252: goto -510 -> 742
    //   1255: astore 63
    //   1257: iconst_3
    //   1258: anewarray 139	java/lang/Object
    //   1261: astore 64
    //   1263: aload 64
    //   1265: iconst_0
    //   1266: aload 63
    //   1268: invokevirtual 706	java/security/GeneralSecurityException:getMessage	()Ljava/lang/String;
    //   1271: aastore
    //   1272: aload 64
    //   1274: iconst_1
    //   1275: aload 63
    //   1277: aastore
    //   1278: aload 64
    //   1280: iconst_2
    //   1281: aload 63
    //   1283: invokevirtual 146	java/lang/Object:getClass	()Ljava/lang/Class;
    //   1286: invokevirtual 151	java/lang/Class:getName	()Ljava/lang/String;
    //   1289: aastore
    //   1290: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1293: dup
    //   1294: ldc 11
    //   1296: ldc_w 708
    //   1299: aload 64
    //   1301: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1304: astore 65
    //   1306: aload_0
    //   1307: aload 65
    //   1309: iload 12
    //   1311: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1314: goto -572 -> 742
    //   1317: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1320: dup
    //   1321: ldc 11
    //   1323: ldc_w 712
    //   1326: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1329: astore 15
    //   1331: aload 14
    //   1333: getstatic 715	org/bouncycastle2/asn1/x509/X509Extensions:AuthorityKeyIdentifier	Lorg/bouncycastle2/asn1/ASN1ObjectIdentifier;
    //   1336: invokevirtual 46	org/bouncycastle2/asn1/ASN1ObjectIdentifier:getId	()Ljava/lang/String;
    //   1339: invokevirtual 718	java/security/cert/X509Certificate:getExtensionValue	(Ljava/lang/String;)[B
    //   1342: astore 16
    //   1344: aload 16
    //   1346: ifnull +126 -> 1472
    //   1349: aload 16
    //   1351: invokestatic 724	org/bouncycastle2/x509/extension/X509ExtensionUtil:fromExtensionValue	([B)Lorg/bouncycastle2/asn1/ASN1Object;
    //   1354: invokestatic 729	org/bouncycastle2/asn1/x509/AuthorityKeyIdentifier:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/AuthorityKeyIdentifier;
    //   1357: astore 58
    //   1359: aload 58
    //   1361: invokevirtual 733	org/bouncycastle2/asn1/x509/AuthorityKeyIdentifier:getAuthorityCertIssuer	()Lorg/bouncycastle2/asn1/x509/GeneralNames;
    //   1364: astore 59
    //   1366: aload 59
    //   1368: ifnull +104 -> 1472
    //   1371: aload 59
    //   1373: invokevirtual 739	org/bouncycastle2/asn1/x509/GeneralNames:getNames	()[Lorg/bouncycastle2/asn1/x509/GeneralName;
    //   1376: iconst_0
    //   1377: aaload
    //   1378: astore 60
    //   1380: aload 58
    //   1382: invokevirtual 742	org/bouncycastle2/asn1/x509/AuthorityKeyIdentifier:getAuthorityCertSerialNumber	()Ljava/math/BigInteger;
    //   1385: astore 61
    //   1387: aload 61
    //   1389: ifnull +83 -> 1472
    //   1392: bipush 7
    //   1394: anewarray 139	java/lang/Object
    //   1397: astore 62
    //   1399: aload 62
    //   1401: iconst_0
    //   1402: new 744	org/bouncycastle2/i18n/LocaleString
    //   1405: dup
    //   1406: ldc 11
    //   1408: ldc_w 746
    //   1411: invokespecial 747	org/bouncycastle2/i18n/LocaleString:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1414: aastore
    //   1415: aload 62
    //   1417: iconst_1
    //   1418: ldc_w 749
    //   1421: aastore
    //   1422: aload 62
    //   1424: iconst_2
    //   1425: aload 60
    //   1427: aastore
    //   1428: aload 62
    //   1430: iconst_3
    //   1431: ldc_w 751
    //   1434: aastore
    //   1435: aload 62
    //   1437: iconst_4
    //   1438: new 744	org/bouncycastle2/i18n/LocaleString
    //   1441: dup
    //   1442: ldc 11
    //   1444: ldc_w 753
    //   1447: invokespecial 747	org/bouncycastle2/i18n/LocaleString:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1450: aastore
    //   1451: aload 62
    //   1453: iconst_5
    //   1454: ldc_w 755
    //   1457: aastore
    //   1458: aload 62
    //   1460: bipush 6
    //   1462: aload 61
    //   1464: aastore
    //   1465: aload 15
    //   1467: aload 62
    //   1469: invokevirtual 759	org/bouncycastle2/i18n/ErrorBundle:setExtraArguments	([Ljava/lang/Object;)V
    //   1472: aload_0
    //   1473: aload 15
    //   1475: iload 12
    //   1477: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1480: goto -738 -> 742
    //   1483: astore 54
    //   1485: iconst_1
    //   1486: anewarray 139	java/lang/Object
    //   1489: astore 55
    //   1491: aload 55
    //   1493: iconst_0
    //   1494: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   1497: dup
    //   1498: aload 14
    //   1500: invokevirtual 763	java/security/cert/X509Certificate:getNotBefore	()Ljava/util/Date;
    //   1503: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   1506: aastore
    //   1507: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1510: dup
    //   1511: ldc 11
    //   1513: ldc_w 765
    //   1516: aload 55
    //   1518: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1521: astore 56
    //   1523: aload_0
    //   1524: aload 56
    //   1526: iload 12
    //   1528: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1531: goto -780 -> 751
    //   1534: astore 17
    //   1536: iconst_1
    //   1537: anewarray 139	java/lang/Object
    //   1540: astore 18
    //   1542: aload 18
    //   1544: iconst_0
    //   1545: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   1548: dup
    //   1549: aload 14
    //   1551: invokevirtual 768	java/security/cert/X509Certificate:getNotAfter	()Ljava/util/Date;
    //   1554: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   1557: aastore
    //   1558: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1561: dup
    //   1562: ldc 11
    //   1564: ldc_w 770
    //   1567: aload 18
    //   1569: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1572: astore 19
    //   1574: aload_0
    //   1575: aload 19
    //   1577: iload 12
    //   1579: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1582: goto -831 -> 751
    //   1585: astore 35
    //   1587: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1590: dup
    //   1591: ldc 11
    //   1593: ldc_w 772
    //   1596: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1599: astore 36
    //   1601: aload_0
    //   1602: aload 36
    //   1604: iload 12
    //   1606: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1609: aconst_null
    //   1610: astore 37
    //   1612: goto -822 -> 790
    //   1615: astore 38
    //   1617: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1620: dup
    //   1621: ldc 11
    //   1623: ldc_w 774
    //   1626: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1629: astore 39
    //   1631: aload_0
    //   1632: aload 39
    //   1634: iload 12
    //   1636: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1639: aconst_null
    //   1640: astore 40
    //   1642: goto -823 -> 819
    //   1645: iconst_1
    //   1646: anewarray 139	java/lang/Object
    //   1649: astore 44
    //   1651: aload 44
    //   1653: iconst_0
    //   1654: new 776	org/bouncycastle2/i18n/filter/UntrustedUrlInput
    //   1657: dup
    //   1658: aload 43
    //   1660: invokeinterface 132 1 0
    //   1665: invokespecial 777	org/bouncycastle2/i18n/filter/UntrustedUrlInput:<init>	(Ljava/lang/Object;)V
    //   1668: aastore
    //   1669: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1672: dup
    //   1673: ldc 11
    //   1675: ldc_w 779
    //   1678: aload 44
    //   1680: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1683: astore 45
    //   1685: aload_0
    //   1686: aload 45
    //   1688: iload 12
    //   1690: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1693: goto -851 -> 842
    //   1696: iconst_1
    //   1697: anewarray 139	java/lang/Object
    //   1700: astore 47
    //   1702: aload 47
    //   1704: iconst_0
    //   1705: new 776	org/bouncycastle2/i18n/filter/UntrustedUrlInput
    //   1708: dup
    //   1709: aload 46
    //   1711: invokeinterface 132 1 0
    //   1716: invokespecial 777	org/bouncycastle2/i18n/filter/UntrustedUrlInput:<init>	(Ljava/lang/Object;)V
    //   1719: aastore
    //   1720: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1723: dup
    //   1724: ldc 11
    //   1726: ldc_w 783
    //   1729: aload 47
    //   1731: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1734: astore 48
    //   1736: aload_0
    //   1737: aload 48
    //   1739: iload 12
    //   1741: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1744: goto -885 -> 859
    //   1747: astore 49
    //   1749: aload_0
    //   1750: aload 49
    //   1752: invokevirtual 165	org/bouncycastle2/x509/CertPathReviewerException:getErrorMessage	()Lorg/bouncycastle2/i18n/ErrorBundle;
    //   1755: iload 12
    //   1757: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1760: goto -867 -> 893
    //   1763: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1766: dup
    //   1767: ldc 11
    //   1769: ldc_w 785
    //   1772: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1775: astore 31
    //   1777: aload_0
    //   1778: aload 31
    //   1780: iload 12
    //   1782: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1785: goto -730 -> 1055
    //   1788: astore 25
    //   1790: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1793: dup
    //   1794: ldc 11
    //   1796: ldc_w 787
    //   1799: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1802: astore 26
    //   1804: aload_0
    //   1805: aload 26
    //   1807: iload 12
    //   1809: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1812: goto -757 -> 1055
    //   1815: astore 20
    //   1817: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1820: dup
    //   1821: ldc 11
    //   1823: ldc_w 789
    //   1826: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1829: astore 21
    //   1831: aload_0
    //   1832: aload 21
    //   1834: iload 12
    //   1836: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1839: goto -702 -> 1137
    //   1842: astore 57
    //   1844: goto -372 -> 1472
    //   1847: astore 94
    //   1849: goto -1673 -> 176
    //
    // Exception table:
    //   from	to	target	type
    //   57	106	418	org/bouncycastle2/x509/CertPathReviewerException
    //   114	176	418	org/bouncycastle2/x509/CertPathReviewerException
    //   330	339	418	org/bouncycastle2/x509/CertPathReviewerException
    //   346	413	418	org/bouncycastle2/x509/CertPathReviewerException
    //   432	464	418	org/bouncycastle2/x509/CertPathReviewerException
    //   468	486	418	org/bouncycastle2/x509/CertPathReviewerException
    //   491	511	418	org/bouncycastle2/x509/CertPathReviewerException
    //   580	586	418	org/bouncycastle2/x509/CertPathReviewerException
    //   468	486	489	java/security/SignatureException
    //   57	106	514	java/lang/Throwable
    //   114	176	514	java/lang/Throwable
    //   330	339	514	java/lang/Throwable
    //   346	413	514	java/lang/Throwable
    //   432	464	514	java/lang/Throwable
    //   468	486	514	java/lang/Throwable
    //   491	511	514	java/lang/Throwable
    //   580	586	514	java/lang/Throwable
    //   194	201	613	java/lang/IllegalArgumentException
    //   593	606	613	java/lang/IllegalArgumentException
    //   281	300	673	java/security/cert/CertPathValidatorException
    //   728	742	1143	java/security/GeneralSecurityException
    //   1213	1252	1255	java/security/GeneralSecurityException
    //   742	751	1483	java/security/cert/CertificateNotYetValidException
    //   742	751	1534	java/security/cert/CertificateExpiredException
    //   761	771	1585	org/bouncycastle2/jce/provider/AnnotatedException
    //   779	786	1585	org/bouncycastle2/jce/provider/AnnotatedException
    //   790	800	1615	org/bouncycastle2/jce/provider/AnnotatedException
    //   808	815	1615	org/bouncycastle2/jce/provider/AnnotatedException
    //   869	893	1747	org/bouncycastle2/x509/CertPathReviewerException
    //   1007	1020	1788	org/bouncycastle2/jce/provider/AnnotatedException
    //   1025	1055	1788	org/bouncycastle2/jce/provider/AnnotatedException
    //   1763	1785	1788	org/bouncycastle2/jce/provider/AnnotatedException
    //   1107	1137	1815	java/security/cert/CertPathValidatorException
    //   1349	1366	1842	java/io/IOException
    //   1371	1387	1842	java/io/IOException
    //   1392	1472	1842	java/io/IOException
    //   468	486	1847	java/lang/Exception
  }

  private X509CRL getCRL(String paramString)
    throws CertPathReviewerException
  {
    try
    {
      URL localURL = new URL(paramString);
      if ((localURL.getProtocol().equals("http")) || (localURL.getProtocol().equals("https")))
      {
        HttpURLConnection localHttpURLConnection = (HttpURLConnection)localURL.openConnection();
        localHttpURLConnection.setUseCaches(false);
        localHttpURLConnection.setDoInput(true);
        localHttpURLConnection.connect();
        if (localHttpURLConnection.getResponseCode() == 200)
          return (X509CRL)CertificateFactory.getInstance("X.509", "BC").generateCRL(localHttpURLConnection.getInputStream());
        throw new Exception(localHttpURLConnection.getResponseMessage());
      }
    }
    catch (Exception localException)
    {
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = new UntrustedInput(paramString);
      arrayOfObject[1] = localException.getMessage();
      arrayOfObject[2] = localException;
      arrayOfObject[3] = localException.getClass().getName();
      throw new CertPathReviewerException(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.loadCrlDistPointError", arrayOfObject));
    }
    return null;
  }

  private boolean processQcStatements(X509Certificate paramX509Certificate, int paramInt)
  {
    int i = 0;
    while (true)
    {
      int j;
      QCStatement localQCStatement;
      try
      {
        ASN1Sequence localASN1Sequence = (ASN1Sequence)getExtensionValue(paramX509Certificate, QC_STATEMENT);
        j = 0;
        if (j >= localASN1Sequence.size())
        {
          if (i == 0)
            break label418;
          return false;
        }
        localQCStatement = QCStatement.getInstance(localASN1Sequence.getObjectAt(j));
        if (QCStatement.id_etsi_qcs_QcCompliance.equals(localQCStatement.getStatementId()))
          addNotification(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.QcEuCompliance"), paramInt);
        else if (!QCStatement.id_qcs_pkixQCSyntax_v1.equals(localQCStatement.getStatementId()))
          if (QCStatement.id_etsi_qcs_QcSSCD.equals(localQCStatement.getStatementId()))
            addNotification(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.QcSSCD"), paramInt);
      }
      catch (AnnotatedException localAnnotatedException)
      {
        addError(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.QcStatementExtError"), paramInt);
        return false;
      }
      if (QCStatement.id_etsi_qcs_LimiteValue.equals(localQCStatement.getStatementId()))
      {
        MonetaryValue localMonetaryValue = MonetaryValue.getInstance(localQCStatement.getStatementInfo());
        localMonetaryValue.getCurrency();
        double d = localMonetaryValue.getAmount().doubleValue() * Math.pow(10.0D, localMonetaryValue.getExponent().doubleValue());
        Object[] arrayOfObject3;
        if (localMonetaryValue.getCurrency().isAlphabetic())
        {
          arrayOfObject3 = new Object[3];
          arrayOfObject3[0] = localMonetaryValue.getCurrency().getAlphabetic();
          Double localDouble2 = new Double(d);
          arrayOfObject3[1] = new TrustedInput(localDouble2);
          arrayOfObject3[2] = localMonetaryValue;
        }
        Object[] arrayOfObject2;
        for (ErrorBundle localErrorBundle = new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.QcLimitValueAlpha", arrayOfObject3); ; localErrorBundle = new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.QcLimitValueNum", arrayOfObject2))
        {
          addNotification(localErrorBundle, paramInt);
          break;
          arrayOfObject2 = new Object[3];
          arrayOfObject2[0] = new Integer(localMonetaryValue.getCurrency().getNumeric());
          Double localDouble1 = new Double(d);
          arrayOfObject2[1] = new TrustedInput(localDouble1);
          arrayOfObject2[2] = localMonetaryValue;
        }
      }
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = localQCStatement.getStatementId();
      UntrustedInput localUntrustedInput = new UntrustedInput(localQCStatement);
      arrayOfObject1[1] = localUntrustedInput;
      addNotification(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.QcUnknownStatement", arrayOfObject1), paramInt);
      i = 1;
      break label420;
      label418: return true;
      label420: j++;
    }
  }

  protected void addError(ErrorBundle paramErrorBundle)
  {
    this.errors[0].add(paramErrorBundle);
  }

  protected void addError(ErrorBundle paramErrorBundle, int paramInt)
  {
    if ((paramInt < -1) || (paramInt >= this.n))
      throw new IndexOutOfBoundsException();
    this.errors[(paramInt + 1)].add(paramErrorBundle);
  }

  protected void addNotification(ErrorBundle paramErrorBundle)
  {
    this.notifications[0].add(paramErrorBundle);
  }

  protected void addNotification(ErrorBundle paramErrorBundle, int paramInt)
  {
    if ((paramInt < -1) || (paramInt >= this.n))
      throw new IndexOutOfBoundsException();
    this.notifications[(paramInt + 1)].add(paramErrorBundle);
  }

  // ERROR //
  protected void checkCRLs(PKIXParameters paramPKIXParameters, X509Certificate paramX509Certificate1, Date paramDate, X509Certificate paramX509Certificate2, PublicKey paramPublicKey, Vector paramVector, int paramInt)
    throws CertPathReviewerException
  {
    // Byte code:
    //   0: new 936	org/bouncycastle2/x509/X509CRLStoreSelector
    //   3: dup
    //   4: invokespecial 937	org/bouncycastle2/x509/X509CRLStoreSelector:<init>	()V
    //   7: astore 8
    //   9: aload 8
    //   11: aload_2
    //   12: invokestatic 941	org/bouncycastle2/x509/PKIXCertPathReviewer:getEncodedIssuerPrincipal	(Ljava/lang/Object;)Ljavax/security/auth/x500/X500Principal;
    //   15: invokevirtual 283	javax/security/auth/x500/X500Principal:getEncoded	()[B
    //   18: invokevirtual 944	org/bouncycastle2/x509/X509CRLStoreSelector:addIssuerName	([B)V
    //   21: aload 8
    //   23: aload_2
    //   24: invokevirtual 948	org/bouncycastle2/x509/X509CRLStoreSelector:setCertificateChecking	(Ljava/security/cert/X509Certificate;)V
    //   27: getstatic 952	org/bouncycastle2/x509/PKIXCertPathReviewer:CRL_UTIL	Lorg/bouncycastle2/jce/provider/PKIXCRLUtil;
    //   30: aload 8
    //   32: aload_1
    //   33: invokevirtual 958	org/bouncycastle2/jce/provider/PKIXCRLUtil:findCRLs	(Lorg/bouncycastle2/x509/X509CRLStoreSelector;Ljava/security/cert/PKIXParameters;)Ljava/util/Set;
    //   36: astore 101
    //   38: aload 101
    //   40: invokeinterface 630 1 0
    //   45: astore 15
    //   47: aload 101
    //   49: invokeinterface 626 1 0
    //   54: ifeq +132 -> 186
    //   57: getstatic 952	org/bouncycastle2/x509/PKIXCertPathReviewer:CRL_UTIL	Lorg/bouncycastle2/jce/provider/PKIXCRLUtil;
    //   60: new 936	org/bouncycastle2/x509/X509CRLStoreSelector
    //   63: dup
    //   64: invokespecial 937	org/bouncycastle2/x509/X509CRLStoreSelector:<init>	()V
    //   67: aload_1
    //   68: invokevirtual 958	org/bouncycastle2/jce/provider/PKIXCRLUtil:findCRLs	(Lorg/bouncycastle2/x509/X509CRLStoreSelector;Ljava/security/cert/PKIXParameters;)Ljava/util/Set;
    //   71: invokeinterface 630 1 0
    //   76: astore 102
    //   78: new 416	java/util/ArrayList
    //   81: dup
    //   82: invokespecial 427	java/util/ArrayList:<init>	()V
    //   85: astore 103
    //   87: aload 102
    //   89: invokeinterface 122 1 0
    //   94: ifne +232 -> 326
    //   97: aload 103
    //   99: invokeinterface 128 1 0
    //   104: istore 105
    //   106: iconst_3
    //   107: anewarray 139	java/lang/Object
    //   110: astore 106
    //   112: aload 106
    //   114: iconst_0
    //   115: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   118: dup
    //   119: aload 8
    //   121: invokevirtual 962	org/bouncycastle2/x509/X509CRLStoreSelector:getIssuerNames	()Ljava/util/Collection;
    //   124: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   127: aastore
    //   128: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   131: dup
    //   132: aload 103
    //   134: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   137: astore 107
    //   139: aload 106
    //   141: iconst_1
    //   142: aload 107
    //   144: aastore
    //   145: new 88	java/lang/Integer
    //   148: dup
    //   149: iload 105
    //   151: invokespecial 384	java/lang/Integer:<init>	(I)V
    //   154: astore 108
    //   156: aload 106
    //   158: iconst_2
    //   159: aload 108
    //   161: aastore
    //   162: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   165: dup
    //   166: ldc 11
    //   168: ldc_w 964
    //   171: aload 106
    //   173: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   176: astore 109
    //   178: aload_0
    //   179: aload 109
    //   181: iload 7
    //   183: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   186: aconst_null
    //   187: astore 16
    //   189: aload 15
    //   191: invokeinterface 122 1 0
    //   196: istore 17
    //   198: iconst_0
    //   199: istore 18
    //   201: iload 17
    //   203: ifne +230 -> 433
    //   206: iload 18
    //   208: ifne +20 -> 228
    //   211: aload 6
    //   213: invokevirtual 681	java/util/Vector:iterator	()Ljava/util/Iterator;
    //   216: astore 86
    //   218: aload 86
    //   220: invokeinterface 122 1 0
    //   225: ifne +379 -> 604
    //   228: aload 16
    //   230: ifnull +1719 -> 1949
    //   233: aload 4
    //   235: ifnull +709 -> 944
    //   238: aload 4
    //   240: invokevirtual 602	java/security/cert/X509Certificate:getKeyUsage	()[Z
    //   243: astore 83
    //   245: aload 83
    //   247: ifnull +697 -> 944
    //   250: aload 83
    //   252: arraylength
    //   253: bipush 7
    //   255: if_icmplt +11 -> 266
    //   258: aload 83
    //   260: bipush 6
    //   262: baload
    //   263: ifne +681 -> 944
    //   266: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   269: dup
    //   270: ldc 11
    //   272: ldc_w 966
    //   275: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   278: astore 84
    //   280: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   283: dup
    //   284: aload 84
    //   286: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   289: astore 85
    //   291: aload 85
    //   293: athrow
    //   294: astore 9
    //   296: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   299: dup
    //   300: ldc 11
    //   302: ldc_w 968
    //   305: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   308: astore 10
    //   310: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   313: dup
    //   314: aload 10
    //   316: aload 9
    //   318: invokespecial 161	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;)V
    //   321: astore 11
    //   323: aload 11
    //   325: athrow
    //   326: aload 103
    //   328: aload 102
    //   330: invokeinterface 132 1 0
    //   335: checkcast 838	java/security/cert/X509CRL
    //   338: invokevirtual 969	java/security/cert/X509CRL:getIssuerX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   341: invokeinterface 431 2 0
    //   346: pop
    //   347: goto -260 -> 87
    //   350: astore 12
    //   352: iconst_3
    //   353: anewarray 139	java/lang/Object
    //   356: astore 13
    //   358: aload 13
    //   360: iconst_0
    //   361: aload 12
    //   363: invokevirtual 970	org/bouncycastle2/jce/provider/AnnotatedException:getCause	()Ljava/lang/Throwable;
    //   366: invokevirtual 640	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   369: aastore
    //   370: aload 13
    //   372: iconst_1
    //   373: aload 12
    //   375: invokevirtual 970	org/bouncycastle2/jce/provider/AnnotatedException:getCause	()Ljava/lang/Throwable;
    //   378: aastore
    //   379: aload 13
    //   381: iconst_2
    //   382: aload 12
    //   384: invokevirtual 970	org/bouncycastle2/jce/provider/AnnotatedException:getCause	()Ljava/lang/Throwable;
    //   387: invokevirtual 146	java/lang/Object:getClass	()Ljava/lang/Class;
    //   390: invokevirtual 151	java/lang/Class:getName	()Ljava/lang/String;
    //   393: aastore
    //   394: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   397: dup
    //   398: ldc 11
    //   400: ldc_w 972
    //   403: aload 13
    //   405: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   408: astore 14
    //   410: aload_0
    //   411: aload 14
    //   413: iload 7
    //   415: invokevirtual 172	org/bouncycastle2/x509/PKIXCertPathReviewer:addError	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   418: new 416	java/util/ArrayList
    //   421: dup
    //   422: invokespecial 427	java/util/ArrayList:<init>	()V
    //   425: invokevirtual 973	java/util/ArrayList:iterator	()Ljava/util/Iterator;
    //   428: astore 15
    //   430: goto -244 -> 186
    //   433: aload 15
    //   435: invokeinterface 132 1 0
    //   440: checkcast 838	java/security/cert/X509CRL
    //   443: astore 16
    //   445: aload 16
    //   447: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   450: ifnull +21 -> 471
    //   453: new 574	java/util/Date
    //   456: dup
    //   457: invokespecial 575	java/util/Date:<init>	()V
    //   460: aload 16
    //   462: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   465: invokevirtual 980	java/util/Date:before	(Ljava/util/Date;)Z
    //   468: ifeq +71 -> 539
    //   471: iconst_1
    //   472: istore 18
    //   474: iconst_2
    //   475: anewarray 139	java/lang/Object
    //   478: astore 19
    //   480: aload 19
    //   482: iconst_0
    //   483: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   486: dup
    //   487: aload 16
    //   489: invokevirtual 983	java/security/cert/X509CRL:getThisUpdate	()Ljava/util/Date;
    //   492: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   495: aastore
    //   496: aload 19
    //   498: iconst_1
    //   499: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   502: dup
    //   503: aload 16
    //   505: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   508: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   511: aastore
    //   512: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   515: dup
    //   516: ldc 11
    //   518: ldc_w 985
    //   521: aload 19
    //   523: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   526: astore 20
    //   528: aload_0
    //   529: aload 20
    //   531: iload 7
    //   533: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   536: goto -330 -> 206
    //   539: iconst_2
    //   540: anewarray 139	java/lang/Object
    //   543: astore 99
    //   545: aload 99
    //   547: iconst_0
    //   548: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   551: dup
    //   552: aload 16
    //   554: invokevirtual 983	java/security/cert/X509CRL:getThisUpdate	()Ljava/util/Date;
    //   557: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   560: aastore
    //   561: aload 99
    //   563: iconst_1
    //   564: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   567: dup
    //   568: aload 16
    //   570: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   573: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   576: aastore
    //   577: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   580: dup
    //   581: ldc 11
    //   583: ldc_w 987
    //   586: aload 99
    //   588: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   591: astore 100
    //   593: aload_0
    //   594: aload 100
    //   596: iload 7
    //   598: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   601: goto -412 -> 189
    //   604: aload 86
    //   606: invokeinterface 132 1 0
    //   611: checkcast 233	java/lang/String
    //   614: astore 88
    //   616: aload_0
    //   617: aload 88
    //   619: invokespecial 989	org/bouncycastle2/x509/PKIXCertPathReviewer:getCRL	(Ljava/lang/String;)Ljava/security/cert/X509CRL;
    //   622: astore 89
    //   624: aload 89
    //   626: ifnull -408 -> 218
    //   629: aload_2
    //   630: invokevirtual 590	java/security/cert/X509Certificate:getIssuerX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   633: aload 89
    //   635: invokevirtual 969	java/security/cert/X509CRL:getIssuerX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   638: invokevirtual 686	javax/security/auth/x500/X500Principal:equals	(Ljava/lang/Object;)Z
    //   641: ifne +106 -> 747
    //   644: iconst_3
    //   645: anewarray 139	java/lang/Object
    //   648: astore 96
    //   650: aload 96
    //   652: iconst_0
    //   653: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   656: dup
    //   657: aload 89
    //   659: invokevirtual 969	java/security/cert/X509CRL:getIssuerX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   662: invokevirtual 338	javax/security/auth/x500/X500Principal:getName	()Ljava/lang/String;
    //   665: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   668: aastore
    //   669: aload 96
    //   671: iconst_1
    //   672: new 332	org/bouncycastle2/i18n/filter/UntrustedInput
    //   675: dup
    //   676: aload_2
    //   677: invokevirtual 590	java/security/cert/X509Certificate:getIssuerX500Principal	()Ljavax/security/auth/x500/X500Principal;
    //   680: invokevirtual 338	javax/security/auth/x500/X500Principal:getName	()Ljava/lang/String;
    //   683: invokespecial 335	org/bouncycastle2/i18n/filter/UntrustedInput:<init>	(Ljava/lang/Object;)V
    //   686: aastore
    //   687: new 776	org/bouncycastle2/i18n/filter/UntrustedUrlInput
    //   690: dup
    //   691: aload 88
    //   693: invokespecial 777	org/bouncycastle2/i18n/filter/UntrustedUrlInput:<init>	(Ljava/lang/Object;)V
    //   696: astore 97
    //   698: aload 96
    //   700: iconst_2
    //   701: aload 97
    //   703: aastore
    //   704: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   707: dup
    //   708: ldc 11
    //   710: ldc_w 991
    //   713: aload 96
    //   715: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   718: astore 98
    //   720: aload_0
    //   721: aload 98
    //   723: iload 7
    //   725: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   728: goto -510 -> 218
    //   731: astore 87
    //   733: aload_0
    //   734: aload 87
    //   736: invokevirtual 165	org/bouncycastle2/x509/CertPathReviewerException:getErrorMessage	()Lorg/bouncycastle2/i18n/ErrorBundle;
    //   739: iload 7
    //   741: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   744: goto -526 -> 218
    //   747: aload 89
    //   749: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   752: ifnull +21 -> 773
    //   755: new 574	java/util/Date
    //   758: dup
    //   759: invokespecial 575	java/util/Date:<init>	()V
    //   762: aload 89
    //   764: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   767: invokevirtual 980	java/util/Date:before	(Ljava/util/Date;)Z
    //   770: ifeq +92 -> 862
    //   773: iconst_1
    //   774: istore 18
    //   776: iconst_3
    //   777: anewarray 139	java/lang/Object
    //   780: astore 90
    //   782: aload 90
    //   784: iconst_0
    //   785: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   788: dup
    //   789: aload 89
    //   791: invokevirtual 983	java/security/cert/X509CRL:getThisUpdate	()Ljava/util/Date;
    //   794: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   797: aastore
    //   798: aload 90
    //   800: iconst_1
    //   801: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   804: dup
    //   805: aload 89
    //   807: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   810: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   813: aastore
    //   814: new 776	org/bouncycastle2/i18n/filter/UntrustedUrlInput
    //   817: dup
    //   818: aload 88
    //   820: invokespecial 777	org/bouncycastle2/i18n/filter/UntrustedUrlInput:<init>	(Ljava/lang/Object;)V
    //   823: astore 91
    //   825: aload 90
    //   827: iconst_2
    //   828: aload 91
    //   830: aastore
    //   831: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   834: dup
    //   835: ldc 11
    //   837: ldc_w 993
    //   840: aload 90
    //   842: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   845: astore 92
    //   847: aload_0
    //   848: aload 92
    //   850: iload 7
    //   852: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   855: aload 89
    //   857: astore 16
    //   859: goto -631 -> 228
    //   862: iconst_3
    //   863: anewarray 139	java/lang/Object
    //   866: astore 93
    //   868: aload 93
    //   870: iconst_0
    //   871: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   874: dup
    //   875: aload 89
    //   877: invokevirtual 983	java/security/cert/X509CRL:getThisUpdate	()Ljava/util/Date;
    //   880: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   883: aastore
    //   884: aload 93
    //   886: iconst_1
    //   887: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   890: dup
    //   891: aload 89
    //   893: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   896: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   899: aastore
    //   900: new 776	org/bouncycastle2/i18n/filter/UntrustedUrlInput
    //   903: dup
    //   904: aload 88
    //   906: invokespecial 777	org/bouncycastle2/i18n/filter/UntrustedUrlInput:<init>	(Ljava/lang/Object;)V
    //   909: astore 94
    //   911: aload 93
    //   913: iconst_2
    //   914: aload 94
    //   916: aastore
    //   917: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   920: dup
    //   921: ldc 11
    //   923: ldc_w 995
    //   926: aload 93
    //   928: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   931: astore 95
    //   933: aload_0
    //   934: aload 95
    //   936: iload 7
    //   938: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   941: goto -723 -> 218
    //   944: aload 5
    //   946: ifnull +210 -> 1156
    //   949: aload 16
    //   951: aload 5
    //   953: ldc_w 823
    //   956: invokevirtual 999	java/security/cert/X509CRL:verify	(Ljava/security/PublicKey;Ljava/lang/String;)V
    //   959: aload 16
    //   961: aload_2
    //   962: invokevirtual 1002	java/security/cert/X509Certificate:getSerialNumber	()Ljava/math/BigInteger;
    //   965: invokevirtual 1006	java/security/cert/X509CRL:getRevokedCertificate	(Ljava/math/BigInteger;)Ljava/security/cert/X509CRLEntry;
    //   968: astore 28
    //   970: aload 28
    //   972: ifnull +520 -> 1492
    //   975: aload 28
    //   977: invokevirtual 1011	java/security/cert/X509CRLEntry:hasExtensions	()Z
    //   980: istore 71
    //   982: aconst_null
    //   983: astore 72
    //   985: iload 71
    //   987: ifeq +41 -> 1028
    //   990: aload 28
    //   992: getstatic 1014	org/bouncycastle2/asn1/x509/X509Extensions:ReasonCode	Lorg/bouncycastle2/asn1/ASN1ObjectIdentifier;
    //   995: invokevirtual 46	org/bouncycastle2/asn1/ASN1ObjectIdentifier:getId	()Ljava/lang/String;
    //   998: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   1001: invokestatic 1019	org/bouncycastle2/asn1/DEREnumerated:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/DEREnumerated;
    //   1004: astore 82
    //   1006: aconst_null
    //   1007: astore 72
    //   1009: aload 82
    //   1011: ifnull +17 -> 1028
    //   1014: getstatic 1023	org/bouncycastle2/x509/PKIXCertPathReviewer:crlReasons	[Ljava/lang/String;
    //   1017: aload 82
    //   1019: invokevirtual 1024	org/bouncycastle2/asn1/DEREnumerated:getValue	()Ljava/math/BigInteger;
    //   1022: invokevirtual 408	java/math/BigInteger:intValue	()I
    //   1025: aaload
    //   1026: astore 72
    //   1028: aload 72
    //   1030: ifnonnull +11 -> 1041
    //   1033: getstatic 1023	org/bouncycastle2/x509/PKIXCertPathReviewer:crlReasons	[Ljava/lang/String;
    //   1036: bipush 7
    //   1038: aaload
    //   1039: astore 72
    //   1041: new 744	org/bouncycastle2/i18n/LocaleString
    //   1044: dup
    //   1045: ldc 11
    //   1047: aload 72
    //   1049: invokespecial 747	org/bouncycastle2/i18n/LocaleString:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1052: astore 73
    //   1054: aload_3
    //   1055: aload 28
    //   1057: invokevirtual 1027	java/security/cert/X509CRLEntry:getRevocationDate	()Ljava/util/Date;
    //   1060: invokevirtual 980	java/util/Date:before	(Ljava/util/Date;)Z
    //   1063: ifne +153 -> 1216
    //   1066: iconst_2
    //   1067: anewarray 139	java/lang/Object
    //   1070: astore 76
    //   1072: aload 76
    //   1074: iconst_0
    //   1075: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   1078: dup
    //   1079: aload 28
    //   1081: invokevirtual 1027	java/security/cert/X509CRLEntry:getRevocationDate	()Ljava/util/Date;
    //   1084: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   1087: aastore
    //   1088: aload 76
    //   1090: iconst_1
    //   1091: aload 73
    //   1093: aastore
    //   1094: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1097: dup
    //   1098: ldc 11
    //   1100: ldc_w 1029
    //   1103: aload 76
    //   1105: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1108: astore 77
    //   1110: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1113: dup
    //   1114: aload 77
    //   1116: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   1119: astore 78
    //   1121: aload 78
    //   1123: athrow
    //   1124: astore 25
    //   1126: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1129: dup
    //   1130: ldc 11
    //   1132: ldc_w 1031
    //   1135: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1138: astore 26
    //   1140: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1143: dup
    //   1144: aload 26
    //   1146: aload 25
    //   1148: invokespecial 161	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;)V
    //   1151: astore 27
    //   1153: aload 27
    //   1155: athrow
    //   1156: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1159: dup
    //   1160: ldc 11
    //   1162: ldc_w 1033
    //   1165: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1168: astore 23
    //   1170: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1173: dup
    //   1174: aload 23
    //   1176: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   1179: astore 24
    //   1181: aload 24
    //   1183: athrow
    //   1184: astore 79
    //   1186: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1189: dup
    //   1190: ldc 11
    //   1192: ldc_w 1035
    //   1195: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1198: astore 80
    //   1200: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1203: dup
    //   1204: aload 80
    //   1206: aload 79
    //   1208: invokespecial 161	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;)V
    //   1211: astore 81
    //   1213: aload 81
    //   1215: athrow
    //   1216: iconst_2
    //   1217: anewarray 139	java/lang/Object
    //   1220: astore 74
    //   1222: aload 74
    //   1224: iconst_0
    //   1225: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   1228: dup
    //   1229: aload 28
    //   1231: invokevirtual 1027	java/security/cert/X509CRLEntry:getRevocationDate	()Ljava/util/Date;
    //   1234: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   1237: aastore
    //   1238: aload 74
    //   1240: iconst_1
    //   1241: aload 73
    //   1243: aastore
    //   1244: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1247: dup
    //   1248: ldc 11
    //   1250: ldc_w 1037
    //   1253: aload 74
    //   1255: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1258: astore 75
    //   1260: aload_0
    //   1261: aload 75
    //   1263: iload 7
    //   1265: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1268: aload 16
    //   1270: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   1273: ifnull +67 -> 1340
    //   1276: aload 16
    //   1278: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   1281: new 574	java/util/Date
    //   1284: dup
    //   1285: invokespecial 575	java/util/Date:<init>	()V
    //   1288: invokevirtual 980	java/util/Date:before	(Ljava/util/Date;)Z
    //   1291: ifeq +49 -> 1340
    //   1294: iconst_1
    //   1295: anewarray 139	java/lang/Object
    //   1298: astore 69
    //   1300: aload 69
    //   1302: iconst_0
    //   1303: new 569	org/bouncycastle2/i18n/filter/TrustedInput
    //   1306: dup
    //   1307: aload 16
    //   1309: invokevirtual 976	java/security/cert/X509CRL:getNextUpdate	()Ljava/util/Date;
    //   1312: invokespecial 572	org/bouncycastle2/i18n/filter/TrustedInput:<init>	(Ljava/lang/Object;)V
    //   1315: aastore
    //   1316: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1319: dup
    //   1320: ldc 11
    //   1322: ldc_w 1039
    //   1325: aload 69
    //   1327: invokespecial 158	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1330: astore 70
    //   1332: aload_0
    //   1333: aload 70
    //   1335: iload 7
    //   1337: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1340: aload 16
    //   1342: getstatic 206	org/bouncycastle2/x509/PKIXCertPathReviewer:ISSUING_DISTRIBUTION_POINT	Ljava/lang/String;
    //   1345: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   1348: astore 33
    //   1350: aload 16
    //   1352: getstatic 209	org/bouncycastle2/x509/PKIXCertPathReviewer:DELTA_CRL_INDICATOR	Ljava/lang/String;
    //   1355: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   1358: astore 37
    //   1360: aload 37
    //   1362: ifnull +397 -> 1759
    //   1365: new 936	org/bouncycastle2/x509/X509CRLStoreSelector
    //   1368: dup
    //   1369: invokespecial 937	org/bouncycastle2/x509/X509CRLStoreSelector:<init>	()V
    //   1372: astore 38
    //   1374: aload 38
    //   1376: aload 16
    //   1378: invokestatic 1043	org/bouncycastle2/x509/PKIXCertPathReviewer:getIssuerPrincipal	(Ljava/security/cert/X509CRL;)Ljavax/security/auth/x500/X500Principal;
    //   1381: invokevirtual 283	javax/security/auth/x500/X500Principal:getEncoded	()[B
    //   1384: invokevirtual 944	org/bouncycastle2/x509/X509CRLStoreSelector:addIssuerName	([B)V
    //   1387: aload 38
    //   1389: aload 37
    //   1391: checkcast 509	org/bouncycastle2/asn1/DERInteger
    //   1394: invokevirtual 1046	org/bouncycastle2/asn1/DERInteger:getPositiveValue	()Ljava/math/BigInteger;
    //   1397: invokevirtual 1050	org/bouncycastle2/x509/X509CRLStoreSelector:setMinCRLNumber	(Ljava/math/BigInteger;)V
    //   1400: aload 38
    //   1402: aload 16
    //   1404: getstatic 1053	org/bouncycastle2/x509/PKIXCertPathReviewer:CRL_NUMBER	Ljava/lang/String;
    //   1407: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   1410: checkcast 509	org/bouncycastle2/asn1/DERInteger
    //   1413: invokevirtual 1046	org/bouncycastle2/asn1/DERInteger:getPositiveValue	()Ljava/math/BigInteger;
    //   1416: lconst_1
    //   1417: invokestatic 1057	java/math/BigInteger:valueOf	(J)Ljava/math/BigInteger;
    //   1420: invokevirtual 1061	java/math/BigInteger:subtract	(Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   1423: invokevirtual 1064	org/bouncycastle2/x509/X509CRLStoreSelector:setMaxCRLNumber	(Ljava/math/BigInteger;)V
    //   1426: getstatic 952	org/bouncycastle2/x509/PKIXCertPathReviewer:CRL_UTIL	Lorg/bouncycastle2/jce/provider/PKIXCRLUtil;
    //   1429: aload 38
    //   1431: aload_1
    //   1432: invokevirtual 958	org/bouncycastle2/jce/provider/PKIXCRLUtil:findCRLs	(Lorg/bouncycastle2/x509/X509CRLStoreSelector;Ljava/security/cert/PKIXParameters;)Ljava/util/Set;
    //   1435: invokeinterface 229 1 0
    //   1440: astore 48
    //   1442: aload 48
    //   1444: invokeinterface 122 1 0
    //   1449: istore 49
    //   1451: iconst_0
    //   1452: istore 50
    //   1454: iload 49
    //   1456: ifne +217 -> 1673
    //   1459: iload 50
    //   1461: ifne +298 -> 1759
    //   1464: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1467: dup
    //   1468: ldc 11
    //   1470: ldc_w 1066
    //   1473: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1476: astore 56
    //   1478: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1481: dup
    //   1482: aload 56
    //   1484: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   1487: astore 57
    //   1489: aload 57
    //   1491: athrow
    //   1492: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1495: dup
    //   1496: ldc 11
    //   1498: ldc_w 1068
    //   1501: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1504: astore 29
    //   1506: aload_0
    //   1507: aload 29
    //   1509: iload 7
    //   1511: invokevirtual 781	org/bouncycastle2/x509/PKIXCertPathReviewer:addNotification	(Lorg/bouncycastle2/i18n/ErrorBundle;I)V
    //   1514: goto -246 -> 1268
    //   1517: astore 30
    //   1519: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1522: dup
    //   1523: ldc 11
    //   1525: ldc_w 1070
    //   1528: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1531: astore 31
    //   1533: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1536: dup
    //   1537: aload 31
    //   1539: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   1542: astore 32
    //   1544: aload 32
    //   1546: athrow
    //   1547: astore 34
    //   1549: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1552: dup
    //   1553: ldc 11
    //   1555: ldc_w 1072
    //   1558: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1561: astore 35
    //   1563: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1566: dup
    //   1567: aload 35
    //   1569: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   1572: astore 36
    //   1574: aload 36
    //   1576: athrow
    //   1577: astore 39
    //   1579: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1582: dup
    //   1583: ldc 11
    //   1585: ldc_w 968
    //   1588: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1591: astore 40
    //   1593: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1596: dup
    //   1597: aload 40
    //   1599: aload 39
    //   1601: invokespecial 161	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;)V
    //   1604: astore 41
    //   1606: aload 41
    //   1608: athrow
    //   1609: astore 42
    //   1611: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1614: dup
    //   1615: ldc 11
    //   1617: ldc_w 1074
    //   1620: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1623: astore 43
    //   1625: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1628: dup
    //   1629: aload 43
    //   1631: aload 42
    //   1633: invokespecial 161	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;)V
    //   1636: astore 44
    //   1638: aload 44
    //   1640: athrow
    //   1641: astore 45
    //   1643: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1646: dup
    //   1647: ldc 11
    //   1649: ldc_w 972
    //   1652: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1655: astore 46
    //   1657: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1660: dup
    //   1661: aload 46
    //   1663: aload 45
    //   1665: invokespecial 161	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;)V
    //   1668: astore 47
    //   1670: aload 47
    //   1672: athrow
    //   1673: aload 48
    //   1675: invokeinterface 132 1 0
    //   1680: checkcast 838	java/security/cert/X509CRL
    //   1683: astore 51
    //   1685: aload 51
    //   1687: getstatic 206	org/bouncycastle2/x509/PKIXCertPathReviewer:ISSUING_DISTRIBUTION_POINT	Ljava/lang/String;
    //   1690: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   1693: astore 55
    //   1695: aload 33
    //   1697: ifnonnull +46 -> 1743
    //   1700: aload 55
    //   1702: ifnonnull -260 -> 1442
    //   1705: iconst_1
    //   1706: istore 50
    //   1708: goto -249 -> 1459
    //   1711: astore 52
    //   1713: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1716: dup
    //   1717: ldc 11
    //   1719: ldc_w 1070
    //   1722: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1725: astore 53
    //   1727: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1730: dup
    //   1731: aload 53
    //   1733: aload 52
    //   1735: invokespecial 161	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;)V
    //   1738: astore 54
    //   1740: aload 54
    //   1742: athrow
    //   1743: aload 33
    //   1745: aload 55
    //   1747: invokevirtual 1077	org/bouncycastle2/asn1/DERObject:equals	(Ljava/lang/Object;)Z
    //   1750: ifeq -308 -> 1442
    //   1753: iconst_1
    //   1754: istore 50
    //   1756: goto -297 -> 1459
    //   1759: aload 33
    //   1761: ifnull +188 -> 1949
    //   1764: aload 33
    //   1766: invokestatic 1082	org/bouncycastle2/asn1/x509/IssuingDistributionPoint:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/IssuingDistributionPoint;
    //   1769: astore 58
    //   1771: aload_2
    //   1772: getstatic 215	org/bouncycastle2/x509/PKIXCertPathReviewer:BASIC_CONSTRAINTS	Ljava/lang/String;
    //   1775: invokestatic 306	org/bouncycastle2/x509/PKIXCertPathReviewer:getExtensionValue	(Ljava/security/cert/X509Extension;Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObject;
    //   1778: invokestatic 399	org/bouncycastle2/asn1/x509/BasicConstraints:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/BasicConstraints;
    //   1781: astore 62
    //   1783: aload 58
    //   1785: invokevirtual 1085	org/bouncycastle2/asn1/x509/IssuingDistributionPoint:onlyContainsUserCerts	()Z
    //   1788: ifeq +76 -> 1864
    //   1791: aload 62
    //   1793: ifnull +71 -> 1864
    //   1796: aload 62
    //   1798: invokevirtual 696	org/bouncycastle2/asn1/x509/BasicConstraints:isCA	()Z
    //   1801: ifeq +63 -> 1864
    //   1804: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1807: dup
    //   1808: ldc 11
    //   1810: ldc_w 1087
    //   1813: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1816: astore 67
    //   1818: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1821: dup
    //   1822: aload 67
    //   1824: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   1827: astore 68
    //   1829: aload 68
    //   1831: athrow
    //   1832: astore 59
    //   1834: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1837: dup
    //   1838: ldc 11
    //   1840: ldc_w 1089
    //   1843: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1846: astore 60
    //   1848: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1851: dup
    //   1852: aload 60
    //   1854: aload 59
    //   1856: invokespecial 161	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;Ljava/lang/Throwable;)V
    //   1859: astore 61
    //   1861: aload 61
    //   1863: athrow
    //   1864: aload 58
    //   1866: invokevirtual 1092	org/bouncycastle2/asn1/x509/IssuingDistributionPoint:onlyContainsCACerts	()Z
    //   1869: ifeq +44 -> 1913
    //   1872: aload 62
    //   1874: ifnull +11 -> 1885
    //   1877: aload 62
    //   1879: invokevirtual 696	org/bouncycastle2/asn1/x509/BasicConstraints:isCA	()Z
    //   1882: ifne +31 -> 1913
    //   1885: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1888: dup
    //   1889: ldc 11
    //   1891: ldc_w 1094
    //   1894: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1897: astore 65
    //   1899: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1902: dup
    //   1903: aload 65
    //   1905: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   1908: astore 66
    //   1910: aload 66
    //   1912: athrow
    //   1913: aload 58
    //   1915: invokevirtual 1097	org/bouncycastle2/asn1/x509/IssuingDistributionPoint:onlyContainsAttributeCerts	()Z
    //   1918: ifeq +31 -> 1949
    //   1921: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1924: dup
    //   1925: ldc 11
    //   1927: ldc_w 1099
    //   1930: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1933: astore 63
    //   1935: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1938: dup
    //   1939: aload 63
    //   1941: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   1944: astore 64
    //   1946: aload 64
    //   1948: athrow
    //   1949: iload 18
    //   1951: ifne +31 -> 1982
    //   1954: new 153	org/bouncycastle2/i18n/ErrorBundle
    //   1957: dup
    //   1958: ldc 11
    //   1960: ldc_w 1101
    //   1963: invokespecial 347	org/bouncycastle2/i18n/ErrorBundle:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1966: astore 21
    //   1968: new 64	org/bouncycastle2/x509/CertPathReviewerException
    //   1971: dup
    //   1972: aload 21
    //   1974: invokespecial 449	org/bouncycastle2/x509/CertPathReviewerException:<init>	(Lorg/bouncycastle2/i18n/ErrorBundle;)V
    //   1977: astore 22
    //   1979: aload 22
    //   1981: athrow
    //   1982: return
    //
    // Exception table:
    //   from	to	target	type
    //   9	21	294	java/io/IOException
    //   27	87	350	org/bouncycastle2/jce/provider/AnnotatedException
    //   87	186	350	org/bouncycastle2/jce/provider/AnnotatedException
    //   326	347	350	org/bouncycastle2/jce/provider/AnnotatedException
    //   604	624	731	org/bouncycastle2/x509/CertPathReviewerException
    //   629	728	731	org/bouncycastle2/x509/CertPathReviewerException
    //   747	773	731	org/bouncycastle2/x509/CertPathReviewerException
    //   776	855	731	org/bouncycastle2/x509/CertPathReviewerException
    //   862	941	731	org/bouncycastle2/x509/CertPathReviewerException
    //   949	959	1124	java/lang/Exception
    //   990	1006	1184	org/bouncycastle2/jce/provider/AnnotatedException
    //   1340	1350	1517	org/bouncycastle2/jce/provider/AnnotatedException
    //   1350	1360	1547	org/bouncycastle2/jce/provider/AnnotatedException
    //   1374	1387	1577	java/io/IOException
    //   1400	1426	1609	org/bouncycastle2/jce/provider/AnnotatedException
    //   1426	1442	1641	org/bouncycastle2/jce/provider/AnnotatedException
    //   1685	1695	1711	org/bouncycastle2/jce/provider/AnnotatedException
    //   1771	1783	1832	org/bouncycastle2/jce/provider/AnnotatedException
  }

  protected void checkRevocation(PKIXParameters paramPKIXParameters, X509Certificate paramX509Certificate1, Date paramDate, X509Certificate paramX509Certificate2, PublicKey paramPublicKey, Vector paramVector1, Vector paramVector2, int paramInt)
    throws CertPathReviewerException
  {
    checkCRLs(paramPKIXParameters, paramX509Certificate1, paramDate, paramX509Certificate2, paramPublicKey, paramVector1, paramInt);
  }

  protected void doChecks()
  {
    if (!this.initialized)
      throw new IllegalStateException("Object not initialized. Call init() first.");
    if (this.notifications == null)
    {
      this.notifications = new List[1 + this.n];
      this.errors = new List[1 + this.n];
    }
    for (int i = 0; ; i++)
    {
      if (i >= this.notifications.length)
      {
        checkSignatures();
        checkNameConstraints();
        checkPathLength();
        checkPolicy();
        checkCriticalExtensions();
        return;
      }
      this.notifications[i] = new ArrayList();
      this.errors[i] = new ArrayList();
    }
  }

  protected Vector getCRLDistUrls(CRLDistPoint paramCRLDistPoint)
  {
    Vector localVector = new Vector();
    DistributionPoint[] arrayOfDistributionPoint;
    int i;
    if (paramCRLDistPoint != null)
    {
      arrayOfDistributionPoint = paramCRLDistPoint.getDistributionPoints();
      i = 0;
      if (i < arrayOfDistributionPoint.length);
    }
    else
    {
      return localVector;
    }
    DistributionPointName localDistributionPointName = arrayOfDistributionPoint[i].getDistributionPoint();
    GeneralName[] arrayOfGeneralName;
    if (localDistributionPointName.getType() == 0)
      arrayOfGeneralName = GeneralNames.getInstance(localDistributionPointName.getName()).getNames();
    for (int j = 0; ; j++)
    {
      if (j >= arrayOfGeneralName.length)
      {
        i++;
        break;
      }
      if (arrayOfGeneralName[j].getTagNo() == 6)
        localVector.add(((DERIA5String)arrayOfGeneralName[j].getName()).getString());
    }
  }

  public CertPath getCertPath()
  {
    return this.certPath;
  }

  public int getCertPathSize()
  {
    return this.n;
  }

  public List getErrors(int paramInt)
  {
    doChecks();
    return this.errors[(paramInt + 1)];
  }

  public List[] getErrors()
  {
    doChecks();
    return this.errors;
  }

  public List getNotifications(int paramInt)
  {
    doChecks();
    return this.notifications[(paramInt + 1)];
  }

  public List[] getNotifications()
  {
    doChecks();
    return this.notifications;
  }

  protected Vector getOCSPUrls(AuthorityInformationAccess paramAuthorityInformationAccess)
  {
    Vector localVector = new Vector();
    AccessDescription[] arrayOfAccessDescription;
    if (paramAuthorityInformationAccess != null)
      arrayOfAccessDescription = paramAuthorityInformationAccess.getAccessDescriptions();
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfAccessDescription.length)
        return localVector;
      if (arrayOfAccessDescription[i].getAccessMethod().equals(AccessDescription.id_ad_ocsp))
      {
        GeneralName localGeneralName = arrayOfAccessDescription[i].getAccessLocation();
        if (localGeneralName.getTagNo() == 6)
          localVector.add(((DERIA5String)localGeneralName.getName()).getString());
      }
    }
  }

  public PolicyNode getPolicyTree()
  {
    doChecks();
    return this.policyTree;
  }

  public PublicKey getSubjectPublicKey()
  {
    doChecks();
    return this.subjectPublicKey;
  }

  public TrustAnchor getTrustAnchor()
  {
    doChecks();
    return this.trustAnchor;
  }

  protected Collection getTrustAnchors(X509Certificate paramX509Certificate, Set paramSet)
    throws CertPathReviewerException
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramSet.iterator();
    X509CertSelector localX509CertSelector = new X509CertSelector();
    while (true)
    {
      try
      {
        localX509CertSelector.setSubject(getEncodedIssuerPrincipal(paramX509Certificate).getEncoded());
        byte[] arrayOfByte1 = paramX509Certificate.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
        if (arrayOfByte1 != null)
        {
          AuthorityKeyIdentifier localAuthorityKeyIdentifier = AuthorityKeyIdentifier.getInstance(ASN1Object.fromByteArray(((ASN1OctetString)ASN1Object.fromByteArray(arrayOfByte1)).getOctets()));
          localX509CertSelector.setSerialNumber(localAuthorityKeyIdentifier.getAuthorityCertSerialNumber());
          byte[] arrayOfByte2 = localAuthorityKeyIdentifier.getKeyIdentifier();
          if (arrayOfByte2 != null)
            localX509CertSelector.setSubjectKeyIdentifier(new DEROctetString(arrayOfByte2).getEncoded());
        }
        if (!localIterator.hasNext())
          return localArrayList;
      }
      catch (IOException localIOException)
      {
        throw new CertPathReviewerException(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.trustAnchorIssuerError"));
      }
      TrustAnchor localTrustAnchor = (TrustAnchor)localIterator.next();
      if (localTrustAnchor.getTrustedCert() != null)
      {
        if (localX509CertSelector.match(localTrustAnchor.getTrustedCert()))
          localArrayList.add(localTrustAnchor);
      }
      else if ((localTrustAnchor.getCAName() != null) && (localTrustAnchor.getCAPublicKey() != null) && (getEncodedIssuerPrincipal(paramX509Certificate).equals(new X500Principal(localTrustAnchor.getCAName()))))
        localArrayList.add(localTrustAnchor);
    }
  }

  public void init(CertPath paramCertPath, PKIXParameters paramPKIXParameters)
    throws CertPathReviewerException
  {
    if (this.initialized)
      throw new IllegalStateException("object is already initialized!");
    this.initialized = true;
    if (paramCertPath == null)
      throw new NullPointerException("certPath was null");
    this.certPath = paramCertPath;
    this.certs = paramCertPath.getCertificates();
    this.n = this.certs.size();
    if (this.certs.isEmpty())
      throw new CertPathReviewerException(new ErrorBundle("org.bouncycastle2.x509.CertPathReviewerMessages", "CertPathReviewer.emptyCertPath"));
    this.pkixParams = ((PKIXParameters)paramPKIXParameters.clone());
    this.validDate = getValidDate(this.pkixParams);
    this.notifications = null;
    this.errors = null;
    this.trustAnchor = null;
    this.subjectPublicKey = null;
    this.policyTree = null;
  }

  public boolean isValidCertPath()
  {
    doChecks();
    for (int i = 0; ; i++)
    {
      if (i >= this.errors.length)
        return true;
      if (!this.errors[i].isEmpty())
        return false;
    }
  }
}