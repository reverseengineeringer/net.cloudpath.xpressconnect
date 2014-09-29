package org.bouncycastle2.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.util.Selector;

public class X509AttributeCertStoreSelector
  implements Selector
{
  private X509AttributeCertificate attributeCert;
  private Date attributeCertificateValid;
  private AttributeCertificateHolder holder;
  private AttributeCertificateIssuer issuer;
  private BigInteger serialNumber;
  private Collection targetGroups = new HashSet();
  private Collection targetNames = new HashSet();

  private Set extractGeneralNames(Collection paramCollection)
    throws IOException
  {
    HashSet localHashSet;
    if ((paramCollection == null) || (paramCollection.isEmpty()))
      localHashSet = new HashSet();
    while (true)
    {
      return localHashSet;
      localHashSet = new HashSet();
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        if ((localObject instanceof GeneralName))
          localHashSet.add(localObject);
        else
          localHashSet.add(GeneralName.getInstance(ASN1Object.fromByteArray((byte[])localObject)));
      }
    }
  }

  public void addTargetGroup(GeneralName paramGeneralName)
  {
    this.targetGroups.add(paramGeneralName);
  }

  public void addTargetGroup(byte[] paramArrayOfByte)
    throws IOException
  {
    addTargetGroup(GeneralName.getInstance(ASN1Object.fromByteArray(paramArrayOfByte)));
  }

  public void addTargetName(GeneralName paramGeneralName)
  {
    this.targetNames.add(paramGeneralName);
  }

  public void addTargetName(byte[] paramArrayOfByte)
    throws IOException
  {
    addTargetName(GeneralName.getInstance(ASN1Object.fromByteArray(paramArrayOfByte)));
  }

  public Object clone()
  {
    X509AttributeCertStoreSelector localX509AttributeCertStoreSelector = new X509AttributeCertStoreSelector();
    localX509AttributeCertStoreSelector.attributeCert = this.attributeCert;
    localX509AttributeCertStoreSelector.attributeCertificateValid = getAttributeCertificateValid();
    localX509AttributeCertStoreSelector.holder = this.holder;
    localX509AttributeCertStoreSelector.issuer = this.issuer;
    localX509AttributeCertStoreSelector.serialNumber = this.serialNumber;
    localX509AttributeCertStoreSelector.targetGroups = getTargetGroups();
    localX509AttributeCertStoreSelector.targetNames = getTargetNames();
    return localX509AttributeCertStoreSelector;
  }

  public X509AttributeCertificate getAttributeCert()
  {
    return this.attributeCert;
  }

  public Date getAttributeCertificateValid()
  {
    if (this.attributeCertificateValid != null)
      return new Date(this.attributeCertificateValid.getTime());
    return null;
  }

  public AttributeCertificateHolder getHolder()
  {
    return this.holder;
  }

  public AttributeCertificateIssuer getIssuer()
  {
    return this.issuer;
  }

  public BigInteger getSerialNumber()
  {
    return this.serialNumber;
  }

  public Collection getTargetGroups()
  {
    return Collections.unmodifiableCollection(this.targetGroups);
  }

  public Collection getTargetNames()
  {
    return Collections.unmodifiableCollection(this.targetNames);
  }

  // ERROR //
  public boolean match(Object paramObject)
  {
    // Byte code:
    //   0: aload_1
    //   1: instanceof 137
    //   4: ifne +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_1
    //   10: checkcast 137	org/bouncycastle2/x509/X509AttributeCertificate
    //   13: astore_2
    //   14: aload_0
    //   15: getfield 86	org/bouncycastle2/x509/X509AttributeCertStoreSelector:attributeCert	Lorg/bouncycastle2/x509/X509AttributeCertificate;
    //   18: ifnull +16 -> 34
    //   21: aload_0
    //   22: getfield 86	org/bouncycastle2/x509/X509AttributeCertStoreSelector:attributeCert	Lorg/bouncycastle2/x509/X509AttributeCertificate;
    //   25: aload_2
    //   26: invokevirtual 140	java/lang/Object:equals	(Ljava/lang/Object;)Z
    //   29: ifne +5 -> 34
    //   32: iconst_0
    //   33: ireturn
    //   34: aload_0
    //   35: getfield 98	org/bouncycastle2/x509/X509AttributeCertStoreSelector:serialNumber	Ljava/math/BigInteger;
    //   38: ifnull +21 -> 59
    //   41: aload_2
    //   42: invokeinterface 142 1 0
    //   47: aload_0
    //   48: getfield 98	org/bouncycastle2/x509/X509AttributeCertStoreSelector:serialNumber	Ljava/math/BigInteger;
    //   51: invokevirtual 145	java/math/BigInteger:equals	(Ljava/lang/Object;)Z
    //   54: ifne +5 -> 59
    //   57: iconst_0
    //   58: ireturn
    //   59: aload_0
    //   60: getfield 94	org/bouncycastle2/x509/X509AttributeCertStoreSelector:holder	Lorg/bouncycastle2/x509/AttributeCertificateHolder;
    //   63: ifnull +21 -> 84
    //   66: aload_2
    //   67: invokeinterface 147 1 0
    //   72: aload_0
    //   73: getfield 94	org/bouncycastle2/x509/X509AttributeCertStoreSelector:holder	Lorg/bouncycastle2/x509/AttributeCertificateHolder;
    //   76: invokevirtual 150	org/bouncycastle2/x509/AttributeCertificateHolder:equals	(Ljava/lang/Object;)Z
    //   79: ifne +5 -> 84
    //   82: iconst_0
    //   83: ireturn
    //   84: aload_0
    //   85: getfield 96	org/bouncycastle2/x509/X509AttributeCertStoreSelector:issuer	Lorg/bouncycastle2/x509/AttributeCertificateIssuer;
    //   88: ifnull +21 -> 109
    //   91: aload_2
    //   92: invokeinterface 152 1 0
    //   97: aload_0
    //   98: getfield 96	org/bouncycastle2/x509/X509AttributeCertStoreSelector:issuer	Lorg/bouncycastle2/x509/AttributeCertificateIssuer;
    //   101: invokevirtual 155	org/bouncycastle2/x509/AttributeCertificateIssuer:equals	(Ljava/lang/Object;)Z
    //   104: ifne +5 -> 109
    //   107: iconst_0
    //   108: ireturn
    //   109: aload_0
    //   110: getfield 92	org/bouncycastle2/x509/X509AttributeCertStoreSelector:attributeCertificateValid	Ljava/util/Date;
    //   113: ifnull +13 -> 126
    //   116: aload_2
    //   117: aload_0
    //   118: getfield 92	org/bouncycastle2/x509/X509AttributeCertStoreSelector:attributeCertificateValid	Ljava/util/Date;
    //   121: invokeinterface 159 2 0
    //   126: aload_0
    //   127: getfield 28	org/bouncycastle2/x509/X509AttributeCertStoreSelector:targetNames	Ljava/util/Collection;
    //   130: invokeinterface 40 1 0
    //   135: ifeq +15 -> 150
    //   138: aload_0
    //   139: getfield 30	org/bouncycastle2/x509/X509AttributeCertStoreSelector:targetGroups	Ljava/util/Collection;
    //   142: invokeinterface 40 1 0
    //   147: ifne +258 -> 405
    //   150: aload_2
    //   151: getstatic 165	org/bouncycastle2/asn1/x509/X509Extensions:TargetInformation	Lorg/bouncycastle2/asn1/ASN1ObjectIdentifier;
    //   154: invokevirtual 171	org/bouncycastle2/asn1/ASN1ObjectIdentifier:getId	()Ljava/lang/String;
    //   157: invokeinterface 175 2 0
    //   162: astore_3
    //   163: aload_3
    //   164: ifnull +241 -> 405
    //   167: new 177	org/bouncycastle2/asn1/ASN1InputStream
    //   170: dup
    //   171: aload_3
    //   172: invokestatic 180	org/bouncycastle2/asn1/DEROctetString:fromByteArray	([B)Lorg/bouncycastle2/asn1/ASN1Object;
    //   175: checkcast 179	org/bouncycastle2/asn1/DEROctetString
    //   178: invokevirtual 184	org/bouncycastle2/asn1/DEROctetString:getOctets	()[B
    //   181: invokespecial 186	org/bouncycastle2/asn1/ASN1InputStream:<init>	([B)V
    //   184: invokevirtual 190	org/bouncycastle2/asn1/ASN1InputStream:readObject	()Lorg/bouncycastle2/asn1/DERObject;
    //   187: invokestatic 195	org/bouncycastle2/asn1/x509/TargetInformation:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/TargetInformation;
    //   190: astore 6
    //   192: aload 6
    //   194: invokevirtual 199	org/bouncycastle2/asn1/x509/TargetInformation:getTargetsObjects	()[Lorg/bouncycastle2/asn1/x509/Targets;
    //   197: astore 7
    //   199: aload_0
    //   200: getfield 28	org/bouncycastle2/x509/X509AttributeCertStoreSelector:targetNames	Ljava/util/Collection;
    //   203: invokeinterface 40 1 0
    //   208: ifne +102 -> 310
    //   211: iconst_0
    //   212: istore 12
    //   214: iconst_0
    //   215: istore 13
    //   217: iload 13
    //   219: aload 7
    //   221: arraylength
    //   222: if_icmplt +26 -> 248
    //   225: iload 12
    //   227: ifne +83 -> 310
    //   230: iconst_0
    //   231: ireturn
    //   232: astore 17
    //   234: iconst_0
    //   235: ireturn
    //   236: astore 16
    //   238: iconst_0
    //   239: ireturn
    //   240: astore 5
    //   242: iconst_0
    //   243: ireturn
    //   244: astore 4
    //   246: iconst_0
    //   247: ireturn
    //   248: aload 7
    //   250: iload 13
    //   252: aaload
    //   253: invokevirtual 205	org/bouncycastle2/asn1/x509/Targets:getTargets	()[Lorg/bouncycastle2/asn1/x509/Target;
    //   256: astore 14
    //   258: iconst_0
    //   259: istore 15
    //   261: iload 15
    //   263: aload 14
    //   265: arraylength
    //   266: if_icmplt +9 -> 275
    //   269: iinc 13 1
    //   272: goto -55 -> 217
    //   275: aload_0
    //   276: getfield 28	org/bouncycastle2/x509/X509AttributeCertStoreSelector:targetNames	Ljava/util/Collection;
    //   279: aload 14
    //   281: iload 15
    //   283: aaload
    //   284: invokevirtual 211	org/bouncycastle2/asn1/x509/Target:getTargetName	()Lorg/bouncycastle2/asn1/x509/GeneralName;
    //   287: invokestatic 73	org/bouncycastle2/asn1/x509/GeneralName:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/GeneralName;
    //   290: invokeinterface 214 2 0
    //   295: ifeq +9 -> 304
    //   298: iconst_1
    //   299: istore 12
    //   301: goto -32 -> 269
    //   304: iinc 15 1
    //   307: goto -46 -> 261
    //   310: aload_0
    //   311: getfield 30	org/bouncycastle2/x509/X509AttributeCertStoreSelector:targetGroups	Ljava/util/Collection;
    //   314: invokeinterface 40 1 0
    //   319: ifne +86 -> 405
    //   322: iconst_0
    //   323: istore 8
    //   325: iconst_0
    //   326: istore 9
    //   328: iload 9
    //   330: aload 7
    //   332: arraylength
    //   333: if_icmplt +10 -> 343
    //   336: iload 8
    //   338: ifne +67 -> 405
    //   341: iconst_0
    //   342: ireturn
    //   343: aload 7
    //   345: iload 9
    //   347: aaload
    //   348: invokevirtual 205	org/bouncycastle2/asn1/x509/Targets:getTargets	()[Lorg/bouncycastle2/asn1/x509/Target;
    //   351: astore 10
    //   353: iconst_0
    //   354: istore 11
    //   356: iload 11
    //   358: aload 10
    //   360: arraylength
    //   361: if_icmplt +9 -> 370
    //   364: iinc 9 1
    //   367: goto -39 -> 328
    //   370: aload_0
    //   371: getfield 30	org/bouncycastle2/x509/X509AttributeCertStoreSelector:targetGroups	Ljava/util/Collection;
    //   374: aload 10
    //   376: iload 11
    //   378: aaload
    //   379: invokevirtual 217	org/bouncycastle2/asn1/x509/Target:getTargetGroup	()Lorg/bouncycastle2/asn1/x509/GeneralName;
    //   382: invokestatic 73	org/bouncycastle2/asn1/x509/GeneralName:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/GeneralName;
    //   385: invokeinterface 214 2 0
    //   390: ifeq +9 -> 399
    //   393: iconst_1
    //   394: istore 8
    //   396: goto -32 -> 364
    //   399: iinc 11 1
    //   402: goto -46 -> 356
    //   405: iconst_1
    //   406: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   116	126	232	java/security/cert/CertificateExpiredException
    //   116	126	236	java/security/cert/CertificateNotYetValidException
    //   167	192	240	java/io/IOException
    //   167	192	244	java/lang/IllegalArgumentException
  }

  public void setAttributeCert(X509AttributeCertificate paramX509AttributeCertificate)
  {
    this.attributeCert = paramX509AttributeCertificate;
  }

  public void setAttributeCertificateValid(Date paramDate)
  {
    if (paramDate != null)
    {
      this.attributeCertificateValid = new Date(paramDate.getTime());
      return;
    }
    this.attributeCertificateValid = null;
  }

  public void setHolder(AttributeCertificateHolder paramAttributeCertificateHolder)
  {
    this.holder = paramAttributeCertificateHolder;
  }

  public void setIssuer(AttributeCertificateIssuer paramAttributeCertificateIssuer)
  {
    this.issuer = paramAttributeCertificateIssuer;
  }

  public void setSerialNumber(BigInteger paramBigInteger)
  {
    this.serialNumber = paramBigInteger;
  }

  public void setTargetGroups(Collection paramCollection)
    throws IOException
  {
    this.targetGroups = extractGeneralNames(paramCollection);
  }

  public void setTargetNames(Collection paramCollection)
    throws IOException
  {
    this.targetNames = extractGeneralNames(paramCollection);
  }
}