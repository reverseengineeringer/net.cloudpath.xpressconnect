package org.bouncycastle2.jce.provider;

import java.security.cert.CertPathValidatorSpi;

public class PKIXCertPathValidatorSpi extends CertPathValidatorSpi
{
  // ERROR //
  public java.security.cert.CertPathValidatorResult engineValidate(java.security.cert.CertPath paramCertPath, java.security.cert.CertPathParameters paramCertPathParameters)
    throws java.security.cert.CertPathValidatorException, java.security.InvalidAlgorithmParameterException
  {
    // Byte code:
    //   0: aload_2
    //   1: instanceof 20
    //   4: ifne +36 -> 40
    //   7: new 14	java/security/InvalidAlgorithmParameterException
    //   10: dup
    //   11: new 22	java/lang/StringBuilder
    //   14: dup
    //   15: ldc 24
    //   17: invokespecial 27	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   20: ldc 20
    //   22: invokevirtual 33	java/lang/Class:getName	()Ljava/lang/String;
    //   25: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: ldc 39
    //   30: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   33: invokevirtual 42	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   36: invokespecial 43	java/security/InvalidAlgorithmParameterException:<init>	(Ljava/lang/String;)V
    //   39: athrow
    //   40: aload_2
    //   41: instanceof 45
    //   44: ifeq +25 -> 69
    //   47: aload_2
    //   48: checkcast 45	org/bouncycastle2/x509/ExtendedPKIXParameters
    //   51: astore_3
    //   52: aload_3
    //   53: invokevirtual 49	org/bouncycastle2/x509/ExtendedPKIXParameters:getTrustAnchors	()Ljava/util/Set;
    //   56: ifnonnull +24 -> 80
    //   59: new 14	java/security/InvalidAlgorithmParameterException
    //   62: dup
    //   63: ldc 51
    //   65: invokespecial 43	java/security/InvalidAlgorithmParameterException:<init>	(Ljava/lang/String;)V
    //   68: athrow
    //   69: aload_2
    //   70: checkcast 20	java/security/cert/PKIXParameters
    //   73: invokestatic 55	org/bouncycastle2/x509/ExtendedPKIXParameters:getInstance	(Ljava/security/cert/PKIXParameters;)Lorg/bouncycastle2/x509/ExtendedPKIXParameters;
    //   76: astore_3
    //   77: goto -25 -> 52
    //   80: aload_1
    //   81: invokevirtual 61	java/security/cert/CertPath:getCertificates	()Ljava/util/List;
    //   84: astore 4
    //   86: aload 4
    //   88: invokeinterface 67 1 0
    //   93: istore 5
    //   95: aload 4
    //   97: invokeinterface 71 1 0
    //   102: ifeq +16 -> 118
    //   105: new 12	java/security/cert/CertPathValidatorException
    //   108: dup
    //   109: ldc 73
    //   111: aconst_null
    //   112: aload_1
    //   113: iconst_0
    //   114: invokespecial 76	java/security/cert/CertPathValidatorException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   117: athrow
    //   118: aload_3
    //   119: invokevirtual 79	org/bouncycastle2/x509/ExtendedPKIXParameters:getInitialPolicies	()Ljava/util/Set;
    //   122: astore 6
    //   124: aload 4
    //   126: iconst_m1
    //   127: aload 4
    //   129: invokeinterface 67 1 0
    //   134: iadd
    //   135: invokeinterface 83 2 0
    //   140: checkcast 85	java/security/cert/X509Certificate
    //   143: aload_3
    //   144: invokevirtual 49	org/bouncycastle2/x509/ExtendedPKIXParameters:getTrustAnchors	()Ljava/util/Set;
    //   147: aload_3
    //   148: invokevirtual 88	org/bouncycastle2/x509/ExtendedPKIXParameters:getSigProvider	()Ljava/lang/String;
    //   151: invokestatic 94	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:findTrustAnchor	(Ljava/security/cert/X509Certificate;Ljava/util/Set;Ljava/lang/String;)Ljava/security/cert/TrustAnchor;
    //   154: astore 8
    //   156: aload 8
    //   158: ifnonnull +43 -> 201
    //   161: new 12	java/security/cert/CertPathValidatorException
    //   164: dup
    //   165: ldc 96
    //   167: aconst_null
    //   168: aload_1
    //   169: iconst_m1
    //   170: invokespecial 76	java/security/cert/CertPathValidatorException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   173: athrow
    //   174: astore 7
    //   176: new 12	java/security/cert/CertPathValidatorException
    //   179: dup
    //   180: aload 7
    //   182: invokevirtual 99	org/bouncycastle2/jce/provider/AnnotatedException:getMessage	()Ljava/lang/String;
    //   185: aload 7
    //   187: aload_1
    //   188: iconst_m1
    //   189: aload 4
    //   191: invokeinterface 67 1 0
    //   196: iadd
    //   197: invokespecial 76	java/security/cert/CertPathValidatorException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   200: athrow
    //   201: iload 5
    //   203: iconst_1
    //   204: iadd
    //   205: anewarray 101	java/util/ArrayList
    //   208: astore 9
    //   210: iconst_0
    //   211: istore 10
    //   213: aload 9
    //   215: arraylength
    //   216: istore 11
    //   218: iload 10
    //   220: iload 11
    //   222: if_icmplt +208 -> 430
    //   225: new 103	java/util/HashSet
    //   228: dup
    //   229: invokespecial 104	java/util/HashSet:<init>	()V
    //   232: astore 12
    //   234: aload 12
    //   236: ldc 106
    //   238: invokeinterface 112 2 0
    //   243: pop
    //   244: new 114	org/bouncycastle2/jce/provider/PKIXPolicyNode
    //   247: dup
    //   248: new 101	java/util/ArrayList
    //   251: dup
    //   252: invokespecial 115	java/util/ArrayList:<init>	()V
    //   255: iconst_0
    //   256: aload 12
    //   258: aconst_null
    //   259: new 103	java/util/HashSet
    //   262: dup
    //   263: invokespecial 104	java/util/HashSet:<init>	()V
    //   266: ldc 106
    //   268: iconst_0
    //   269: invokespecial 118	org/bouncycastle2/jce/provider/PKIXPolicyNode:<init>	(Ljava/util/List;ILjava/util/Set;Ljava/security/cert/PolicyNode;Ljava/util/Set;Ljava/lang/String;Z)V
    //   272: astore 14
    //   274: aload 9
    //   276: iconst_0
    //   277: aaload
    //   278: aload 14
    //   280: invokeinterface 119 2 0
    //   285: pop
    //   286: new 121	org/bouncycastle2/jce/provider/PKIXNameConstraintValidator
    //   289: dup
    //   290: invokespecial 122	org/bouncycastle2/jce/provider/PKIXNameConstraintValidator:<init>	()V
    //   293: astore 16
    //   295: new 103	java/util/HashSet
    //   298: dup
    //   299: invokespecial 104	java/util/HashSet:<init>	()V
    //   302: astore 17
    //   304: aload_3
    //   305: invokevirtual 125	org/bouncycastle2/x509/ExtendedPKIXParameters:isExplicitPolicyRequired	()Z
    //   308: ifeq +140 -> 448
    //   311: iconst_0
    //   312: istore 18
    //   314: aload_3
    //   315: invokevirtual 128	org/bouncycastle2/x509/ExtendedPKIXParameters:isAnyPolicyInhibited	()Z
    //   318: ifeq +139 -> 457
    //   321: iconst_0
    //   322: istore 19
    //   324: aload_3
    //   325: invokevirtual 131	org/bouncycastle2/x509/ExtendedPKIXParameters:isPolicyMappingInhibited	()Z
    //   328: ifeq +138 -> 466
    //   331: iconst_0
    //   332: istore 20
    //   334: aload 8
    //   336: invokevirtual 137	java/security/cert/TrustAnchor:getTrustedCert	()Ljava/security/cert/X509Certificate;
    //   339: astore 21
    //   341: aload 21
    //   343: ifnull +132 -> 475
    //   346: aload 21
    //   348: invokestatic 141	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:getSubjectPrincipal	(Ljava/security/cert/X509Certificate;)Ljavax/security/auth/x500/X500Principal;
    //   351: astore 22
    //   353: aload 21
    //   355: invokevirtual 145	java/security/cert/X509Certificate:getPublicKey	()Ljava/security/PublicKey;
    //   358: astore 75
    //   360: aload 75
    //   362: astore 25
    //   364: aload 25
    //   366: invokestatic 149	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:getAlgorithmIdentifier	(Ljava/security/PublicKey;)Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   369: astore 27
    //   371: aload 27
    //   373: invokevirtual 155	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getObjectId	()Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   376: pop
    //   377: aload 27
    //   379: invokevirtual 159	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getParameters	()Lorg/bouncycastle2/asn1/DEREncodable;
    //   382: pop
    //   383: iload 5
    //   385: istore 30
    //   387: aload_3
    //   388: invokevirtual 163	org/bouncycastle2/x509/ExtendedPKIXParameters:getTargetConstraints	()Lorg/bouncycastle2/util/Selector;
    //   391: ifnull +144 -> 535
    //   394: aload_3
    //   395: invokevirtual 163	org/bouncycastle2/x509/ExtendedPKIXParameters:getTargetConstraints	()Lorg/bouncycastle2/util/Selector;
    //   398: aload 4
    //   400: iconst_0
    //   401: invokeinterface 83 2 0
    //   406: checkcast 85	java/security/cert/X509Certificate
    //   409: invokeinterface 168 2 0
    //   414: ifne +121 -> 535
    //   417: new 170	org/bouncycastle2/jce/exception/ExtCertPathValidatorException
    //   420: dup
    //   421: ldc 172
    //   423: aconst_null
    //   424: aload_1
    //   425: iconst_0
    //   426: invokespecial 173	org/bouncycastle2/jce/exception/ExtCertPathValidatorException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   429: athrow
    //   430: aload 9
    //   432: iload 10
    //   434: new 101	java/util/ArrayList
    //   437: dup
    //   438: invokespecial 115	java/util/ArrayList:<init>	()V
    //   441: aastore
    //   442: iinc 10 1
    //   445: goto -232 -> 213
    //   448: iload 5
    //   450: iconst_1
    //   451: iadd
    //   452: istore 18
    //   454: goto -140 -> 314
    //   457: iload 5
    //   459: iconst_1
    //   460: iadd
    //   461: istore 19
    //   463: goto -139 -> 324
    //   466: iload 5
    //   468: iconst_1
    //   469: iadd
    //   470: istore 20
    //   472: goto -138 -> 334
    //   475: new 175	javax/security/auth/x500/X500Principal
    //   478: dup
    //   479: aload 8
    //   481: invokevirtual 178	java/security/cert/TrustAnchor:getCAName	()Ljava/lang/String;
    //   484: invokespecial 179	javax/security/auth/x500/X500Principal:<init>	(Ljava/lang/String;)V
    //   487: astore 22
    //   489: aload 8
    //   491: invokevirtual 182	java/security/cert/TrustAnchor:getCAPublicKey	()Ljava/security/PublicKey;
    //   494: astore 24
    //   496: aload 24
    //   498: astore 25
    //   500: goto -136 -> 364
    //   503: astore 23
    //   505: new 170	org/bouncycastle2/jce/exception/ExtCertPathValidatorException
    //   508: dup
    //   509: ldc 184
    //   511: aload 23
    //   513: aload_1
    //   514: iconst_m1
    //   515: invokespecial 173	org/bouncycastle2/jce/exception/ExtCertPathValidatorException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   518: athrow
    //   519: astore 26
    //   521: new 170	org/bouncycastle2/jce/exception/ExtCertPathValidatorException
    //   524: dup
    //   525: ldc 186
    //   527: aload 26
    //   529: aload_1
    //   530: iconst_m1
    //   531: invokespecial 173	org/bouncycastle2/jce/exception/ExtCertPathValidatorException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   534: athrow
    //   535: aload_3
    //   536: invokevirtual 189	org/bouncycastle2/x509/ExtendedPKIXParameters:getCertPathCheckers	()Ljava/util/List;
    //   539: astore 31
    //   541: aload 31
    //   543: invokeinterface 193 1 0
    //   548: astore 32
    //   550: aload 32
    //   552: invokeinterface 198 1 0
    //   557: ifne +249 -> 806
    //   560: aconst_null
    //   561: astore 33
    //   563: iconst_m1
    //   564: aload 4
    //   566: invokeinterface 67 1 0
    //   571: iadd
    //   572: istore 34
    //   574: iload 34
    //   576: ifge +247 -> 823
    //   579: iload 18
    //   581: aload 33
    //   583: invokestatic 204	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:wrapupCertA	(ILjava/security/cert/X509Certificate;)I
    //   586: istore 58
    //   588: aload_1
    //   589: iload 34
    //   591: iconst_1
    //   592: iadd
    //   593: iload 58
    //   595: invokestatic 208	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:wrapupCertB	(Ljava/security/cert/CertPath;II)I
    //   598: istore 59
    //   600: aload 33
    //   602: invokevirtual 211	java/security/cert/X509Certificate:getCriticalExtensionOIDs	()Ljava/util/Set;
    //   605: astore 60
    //   607: aload 60
    //   609: ifnull +693 -> 1302
    //   612: new 103	java/util/HashSet
    //   615: dup
    //   616: aload 60
    //   618: invokespecial 214	java/util/HashSet:<init>	(Ljava/util/Collection;)V
    //   621: astore 61
    //   623: aload 61
    //   625: getstatic 218	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:KEY_USAGE	Ljava/lang/String;
    //   628: invokeinterface 221 2 0
    //   633: pop
    //   634: aload 61
    //   636: getstatic 224	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:CERTIFICATE_POLICIES	Ljava/lang/String;
    //   639: invokeinterface 221 2 0
    //   644: pop
    //   645: aload 61
    //   647: getstatic 227	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:POLICY_MAPPINGS	Ljava/lang/String;
    //   650: invokeinterface 221 2 0
    //   655: pop
    //   656: aload 61
    //   658: getstatic 230	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:INHIBIT_ANY_POLICY	Ljava/lang/String;
    //   661: invokeinterface 221 2 0
    //   666: pop
    //   667: aload 61
    //   669: getstatic 233	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:ISSUING_DISTRIBUTION_POINT	Ljava/lang/String;
    //   672: invokeinterface 221 2 0
    //   677: pop
    //   678: aload 61
    //   680: getstatic 236	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:DELTA_CRL_INDICATOR	Ljava/lang/String;
    //   683: invokeinterface 221 2 0
    //   688: pop
    //   689: aload 61
    //   691: getstatic 239	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:POLICY_CONSTRAINTS	Ljava/lang/String;
    //   694: invokeinterface 221 2 0
    //   699: pop
    //   700: aload 61
    //   702: getstatic 242	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:BASIC_CONSTRAINTS	Ljava/lang/String;
    //   705: invokeinterface 221 2 0
    //   710: pop
    //   711: aload 61
    //   713: getstatic 245	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:SUBJECT_ALTERNATIVE_NAME	Ljava/lang/String;
    //   716: invokeinterface 221 2 0
    //   721: pop
    //   722: aload 61
    //   724: getstatic 248	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:NAME_CONSTRAINTS	Ljava/lang/String;
    //   727: invokeinterface 221 2 0
    //   732: pop
    //   733: aload 61
    //   735: getstatic 251	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:CRL_DISTRIBUTION_POINTS	Ljava/lang/String;
    //   738: invokeinterface 221 2 0
    //   743: pop
    //   744: aload 61
    //   746: astore 73
    //   748: aload_1
    //   749: iload 34
    //   751: iconst_1
    //   752: iadd
    //   753: aload 31
    //   755: aload 73
    //   757: invokestatic 255	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:wrapupCertF	(Ljava/security/cert/CertPath;ILjava/util/List;Ljava/util/Set;)V
    //   760: aload_1
    //   761: aload_3
    //   762: aload 6
    //   764: iload 34
    //   766: iconst_1
    //   767: iadd
    //   768: aload 9
    //   770: aload 14
    //   772: aload 17
    //   774: invokestatic 259	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:wrapupCertG	(Ljava/security/cert/CertPath;Lorg/bouncycastle2/x509/ExtendedPKIXParameters;Ljava/util/Set;I[Ljava/util/List;Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;Ljava/util/Set;)Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;
    //   777: astore 74
    //   779: iload 59
    //   781: ifgt +8 -> 789
    //   784: aload 74
    //   786: ifnull +528 -> 1314
    //   789: new 261	java/security/cert/PKIXCertPathValidatorResult
    //   792: dup
    //   793: aload 8
    //   795: aload 74
    //   797: aload 33
    //   799: invokevirtual 145	java/security/cert/X509Certificate:getPublicKey	()Ljava/security/PublicKey;
    //   802: invokespecial 264	java/security/cert/PKIXCertPathValidatorResult:<init>	(Ljava/security/cert/TrustAnchor;Ljava/security/cert/PolicyNode;Ljava/security/PublicKey;)V
    //   805: areturn
    //   806: aload 32
    //   808: invokeinterface 268 1 0
    //   813: checkcast 270	java/security/cert/PKIXCertPathChecker
    //   816: iconst_0
    //   817: invokevirtual 274	java/security/cert/PKIXCertPathChecker:init	(Z)V
    //   820: goto -270 -> 550
    //   823: iload 5
    //   825: iload 34
    //   827: isub
    //   828: istore 35
    //   830: aload 4
    //   832: iload 34
    //   834: invokeinterface 83 2 0
    //   839: checkcast 85	java/security/cert/X509Certificate
    //   842: astore 33
    //   844: iload 34
    //   846: iconst_m1
    //   847: aload 4
    //   849: invokeinterface 67 1 0
    //   854: iadd
    //   855: if_icmpne +97 -> 952
    //   858: iconst_1
    //   859: istore 36
    //   861: aload_1
    //   862: aload_3
    //   863: iload 34
    //   865: aload 25
    //   867: iload 36
    //   869: aload 22
    //   871: aload 21
    //   873: invokestatic 278	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:processCertA	(Ljava/security/cert/CertPath;Lorg/bouncycastle2/x509/ExtendedPKIXParameters;ILjava/security/PublicKey;ZLjavax/security/auth/x500/X500Principal;Ljava/security/cert/X509Certificate;)V
    //   876: aload_1
    //   877: iload 34
    //   879: aload 16
    //   881: invokestatic 282	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:processCertBC	(Ljava/security/cert/CertPath;ILorg/bouncycastle2/jce/provider/PKIXNameConstraintValidator;)V
    //   884: aload_1
    //   885: iload 34
    //   887: aload_1
    //   888: iload 34
    //   890: aload 17
    //   892: aload 14
    //   894: aload 9
    //   896: iload 19
    //   898: invokestatic 286	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:processCertD	(Ljava/security/cert/CertPath;ILjava/util/Set;Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;[Ljava/util/List;I)Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;
    //   901: invokestatic 290	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:processCertE	(Ljava/security/cert/CertPath;ILorg/bouncycastle2/jce/provider/PKIXPolicyNode;)Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;
    //   904: astore 14
    //   906: aload_1
    //   907: iload 34
    //   909: aload 14
    //   911: iload 18
    //   913: invokestatic 294	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:processCertF	(Ljava/security/cert/CertPath;ILorg/bouncycastle2/jce/provider/PKIXPolicyNode;I)V
    //   916: iload 35
    //   918: iload 5
    //   920: if_icmpeq +346 -> 1266
    //   923: aload 33
    //   925: ifnull +33 -> 958
    //   928: aload 33
    //   930: invokevirtual 297	java/security/cert/X509Certificate:getVersion	()I
    //   933: iconst_1
    //   934: if_icmpne +24 -> 958
    //   937: new 12	java/security/cert/CertPathValidatorException
    //   940: dup
    //   941: ldc_w 299
    //   944: aconst_null
    //   945: aload_1
    //   946: iload 34
    //   948: invokespecial 76	java/security/cert/CertPathValidatorException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   951: athrow
    //   952: iconst_0
    //   953: istore 36
    //   955: goto -94 -> 861
    //   958: aload_1
    //   959: iload 34
    //   961: invokestatic 303	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertA	(Ljava/security/cert/CertPath;I)V
    //   964: aload_1
    //   965: iload 34
    //   967: aload 9
    //   969: aload 14
    //   971: iload 20
    //   973: invokestatic 307	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareCertB	(Ljava/security/cert/CertPath;I[Ljava/util/List;Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;I)Lorg/bouncycastle2/jce/provider/PKIXPolicyNode;
    //   976: astore 14
    //   978: aload_1
    //   979: iload 34
    //   981: aload 16
    //   983: invokestatic 310	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertG	(Ljava/security/cert/CertPath;ILorg/bouncycastle2/jce/provider/PKIXNameConstraintValidator;)V
    //   986: aload_1
    //   987: iload 34
    //   989: iload 18
    //   991: invokestatic 313	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertH1	(Ljava/security/cert/CertPath;II)I
    //   994: istore 37
    //   996: aload_1
    //   997: iload 34
    //   999: iload 20
    //   1001: invokestatic 316	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertH2	(Ljava/security/cert/CertPath;II)I
    //   1004: istore 38
    //   1006: aload_1
    //   1007: iload 34
    //   1009: iload 19
    //   1011: invokestatic 319	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertH3	(Ljava/security/cert/CertPath;II)I
    //   1014: istore 39
    //   1016: aload_1
    //   1017: iload 34
    //   1019: iload 37
    //   1021: invokestatic 322	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertI1	(Ljava/security/cert/CertPath;II)I
    //   1024: istore 18
    //   1026: aload_1
    //   1027: iload 34
    //   1029: iload 38
    //   1031: invokestatic 325	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertI2	(Ljava/security/cert/CertPath;II)I
    //   1034: istore 20
    //   1036: aload_1
    //   1037: iload 34
    //   1039: iload 39
    //   1041: invokestatic 328	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertJ	(Ljava/security/cert/CertPath;II)I
    //   1044: istore 19
    //   1046: aload_1
    //   1047: iload 34
    //   1049: invokestatic 331	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertK	(Ljava/security/cert/CertPath;I)V
    //   1052: aload_1
    //   1053: iload 34
    //   1055: aload_1
    //   1056: iload 34
    //   1058: iload 30
    //   1060: invokestatic 334	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertL	(Ljava/security/cert/CertPath;II)I
    //   1063: invokestatic 337	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertM	(Ljava/security/cert/CertPath;II)I
    //   1066: istore 30
    //   1068: aload_1
    //   1069: iload 34
    //   1071: invokestatic 340	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertN	(Ljava/security/cert/CertPath;I)V
    //   1074: aload 33
    //   1076: invokevirtual 211	java/security/cert/X509Certificate:getCriticalExtensionOIDs	()Ljava/util/Set;
    //   1079: astore 40
    //   1081: aload 40
    //   1083: ifnull +189 -> 1272
    //   1086: new 103	java/util/HashSet
    //   1089: dup
    //   1090: aload 40
    //   1092: invokespecial 214	java/util/HashSet:<init>	(Ljava/util/Collection;)V
    //   1095: astore 41
    //   1097: aload 41
    //   1099: getstatic 218	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:KEY_USAGE	Ljava/lang/String;
    //   1102: invokeinterface 221 2 0
    //   1107: pop
    //   1108: aload 41
    //   1110: getstatic 224	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:CERTIFICATE_POLICIES	Ljava/lang/String;
    //   1113: invokeinterface 221 2 0
    //   1118: pop
    //   1119: aload 41
    //   1121: getstatic 227	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:POLICY_MAPPINGS	Ljava/lang/String;
    //   1124: invokeinterface 221 2 0
    //   1129: pop
    //   1130: aload 41
    //   1132: getstatic 230	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:INHIBIT_ANY_POLICY	Ljava/lang/String;
    //   1135: invokeinterface 221 2 0
    //   1140: pop
    //   1141: aload 41
    //   1143: getstatic 233	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:ISSUING_DISTRIBUTION_POINT	Ljava/lang/String;
    //   1146: invokeinterface 221 2 0
    //   1151: pop
    //   1152: aload 41
    //   1154: getstatic 236	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:DELTA_CRL_INDICATOR	Ljava/lang/String;
    //   1157: invokeinterface 221 2 0
    //   1162: pop
    //   1163: aload 41
    //   1165: getstatic 239	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:POLICY_CONSTRAINTS	Ljava/lang/String;
    //   1168: invokeinterface 221 2 0
    //   1173: pop
    //   1174: aload 41
    //   1176: getstatic 242	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:BASIC_CONSTRAINTS	Ljava/lang/String;
    //   1179: invokeinterface 221 2 0
    //   1184: pop
    //   1185: aload 41
    //   1187: getstatic 245	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:SUBJECT_ALTERNATIVE_NAME	Ljava/lang/String;
    //   1190: invokeinterface 221 2 0
    //   1195: pop
    //   1196: aload 41
    //   1198: getstatic 248	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:NAME_CONSTRAINTS	Ljava/lang/String;
    //   1201: invokeinterface 221 2 0
    //   1206: pop
    //   1207: aload 41
    //   1209: astore 52
    //   1211: aload_1
    //   1212: iload 34
    //   1214: aload 52
    //   1216: aload 31
    //   1218: invokestatic 344	org/bouncycastle2/jce/provider/RFC3280CertPathUtilities:prepareNextCertO	(Ljava/security/cert/CertPath;ILjava/util/Set;Ljava/util/List;)V
    //   1221: aload 33
    //   1223: astore 21
    //   1225: aload 21
    //   1227: invokestatic 141	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:getSubjectPrincipal	(Ljava/security/cert/X509Certificate;)Ljavax/security/auth/x500/X500Principal;
    //   1230: astore 22
    //   1232: aload_1
    //   1233: invokevirtual 61	java/security/cert/CertPath:getCertificates	()Ljava/util/List;
    //   1236: iload 34
    //   1238: invokestatic 348	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:getNextWorkingKey	(Ljava/util/List;I)Ljava/security/PublicKey;
    //   1241: astore 54
    //   1243: aload 54
    //   1245: astore 25
    //   1247: aload 25
    //   1249: invokestatic 149	org/bouncycastle2/jce/provider/CertPathValidatorUtilities:getAlgorithmIdentifier	(Ljava/security/PublicKey;)Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   1252: astore 55
    //   1254: aload 55
    //   1256: invokevirtual 155	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getObjectId	()Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   1259: pop
    //   1260: aload 55
    //   1262: invokevirtual 159	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getParameters	()Lorg/bouncycastle2/asn1/DEREncodable;
    //   1265: pop
    //   1266: iinc 34 255
    //   1269: goto -695 -> 574
    //   1272: new 103	java/util/HashSet
    //   1275: dup
    //   1276: invokespecial 104	java/util/HashSet:<init>	()V
    //   1279: astore 52
    //   1281: goto -70 -> 1211
    //   1284: astore 53
    //   1286: new 12	java/security/cert/CertPathValidatorException
    //   1289: dup
    //   1290: ldc_w 350
    //   1293: aload 53
    //   1295: aload_1
    //   1296: iload 34
    //   1298: invokespecial 76	java/security/cert/CertPathValidatorException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   1301: athrow
    //   1302: new 103	java/util/HashSet
    //   1305: dup
    //   1306: invokespecial 104	java/util/HashSet:<init>	()V
    //   1309: astore 73
    //   1311: goto -563 -> 748
    //   1314: new 12	java/security/cert/CertPathValidatorException
    //   1317: dup
    //   1318: ldc_w 352
    //   1321: aconst_null
    //   1322: aload_1
    //   1323: iload 34
    //   1325: invokespecial 76	java/security/cert/CertPathValidatorException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;Ljava/security/cert/CertPath;I)V
    //   1328: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   124	156	174	org/bouncycastle2/jce/provider/AnnotatedException
    //   346	360	503	java/lang/IllegalArgumentException
    //   475	496	503	java/lang/IllegalArgumentException
    //   364	371	519	java/security/cert/CertPathValidatorException
    //   1232	1243	1284	java/security/cert/CertPathValidatorException
  }
}