package org.bouncycastle2.jce.provider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1OutputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle2.asn1.misc.NetscapeCertType;
import org.bouncycastle2.asn1.misc.NetscapeRevocationURL;
import org.bouncycastle2.asn1.misc.VerisignCzagExtension;
import org.bouncycastle2.asn1.util.ASN1Dump;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.BasicConstraints;
import org.bouncycastle2.asn1.x509.KeyUsage;
import org.bouncycastle2.asn1.x509.TBSCertificateStructure;
import org.bouncycastle2.asn1.x509.Time;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;
import org.bouncycastle2.asn1.x509.X509Extension;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.jce.X509Principal;
import org.bouncycastle2.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle2.util.Arrays;
import org.bouncycastle2.util.encoders.Hex;

public class X509CertificateObject extends X509Certificate
  implements PKCS12BagAttributeCarrier
{
  private PKCS12BagAttributeCarrier attrCarrier;
  private BasicConstraints basicConstraints;
  private X509CertificateStructure c;
  private int hashValue;
  private boolean hashValueSet;
  private boolean[] keyUsage;

  // ERROR //
  public X509CertificateObject(X509CertificateStructure paramX509CertificateStructure)
    throws CertificateParsingException
  {
    // Byte code:
    //   0: bipush 9
    //   2: istore_2
    //   3: aload_0
    //   4: invokespecial 27	java/security/cert/X509Certificate:<init>	()V
    //   7: aload_0
    //   8: new 29	org/bouncycastle2/jce/provider/PKCS12BagAttributeCarrierImpl
    //   11: dup
    //   12: invokespecial 30	org/bouncycastle2/jce/provider/PKCS12BagAttributeCarrierImpl:<init>	()V
    //   15: putfield 32	org/bouncycastle2/jce/provider/X509CertificateObject:attrCarrier	Lorg/bouncycastle2/jce/interfaces/PKCS12BagAttributeCarrier;
    //   18: aload_0
    //   19: aload_1
    //   20: putfield 34	org/bouncycastle2/jce/provider/X509CertificateObject:c	Lorg/bouncycastle2/asn1/x509/X509CertificateStructure;
    //   23: aload_0
    //   24: ldc 36
    //   26: invokespecial 40	org/bouncycastle2/jce/provider/X509CertificateObject:getExtensionBytes	(Ljava/lang/String;)[B
    //   29: astore 4
    //   31: aload 4
    //   33: ifnull +15 -> 48
    //   36: aload_0
    //   37: aload 4
    //   39: invokestatic 46	org/bouncycastle2/asn1/ASN1Object:fromByteArray	([B)Lorg/bouncycastle2/asn1/ASN1Object;
    //   42: invokestatic 52	org/bouncycastle2/asn1/x509/BasicConstraints:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/x509/BasicConstraints;
    //   45: putfield 54	org/bouncycastle2/jce/provider/X509CertificateObject:basicConstraints	Lorg/bouncycastle2/asn1/x509/BasicConstraints;
    //   48: aload_0
    //   49: ldc 56
    //   51: invokespecial 40	org/bouncycastle2/jce/provider/X509CertificateObject:getExtensionBytes	(Ljava/lang/String;)[B
    //   54: astore 6
    //   56: aload 6
    //   58: ifnull +132 -> 190
    //   61: aload 6
    //   63: invokestatic 46	org/bouncycastle2/asn1/ASN1Object:fromByteArray	([B)Lorg/bouncycastle2/asn1/ASN1Object;
    //   66: invokestatic 61	org/bouncycastle2/asn1/DERBitString:getInstance	(Ljava/lang/Object;)Lorg/bouncycastle2/asn1/DERBitString;
    //   69: astore 7
    //   71: aload 7
    //   73: invokevirtual 65	org/bouncycastle2/asn1/DERBitString:getBytes	()[B
    //   76: astore 8
    //   78: bipush 8
    //   80: aload 8
    //   82: arraylength
    //   83: imul
    //   84: aload 7
    //   86: invokevirtual 69	org/bouncycastle2/asn1/DERBitString:getPadBits	()I
    //   89: isub
    //   90: istore 9
    //   92: iload 9
    //   94: iload_2
    //   95: if_icmpge +46 -> 141
    //   98: aload_0
    //   99: iload_2
    //   100: newarray boolean
    //   102: putfield 71	org/bouncycastle2/jce/provider/X509CertificateObject:keyUsage	[Z
    //   105: iconst_0
    //   106: istore 10
    //   108: iload 10
    //   110: iload 9
    //   112: if_icmpne +35 -> 147
    //   115: return
    //   116: astore_3
    //   117: new 22	java/security/cert/CertificateParsingException
    //   120: dup
    //   121: new 73	java/lang/StringBuilder
    //   124: dup
    //   125: ldc 75
    //   127: invokespecial 78	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   130: aload_3
    //   131: invokevirtual 82	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   134: invokevirtual 86	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   137: invokespecial 87	java/security/cert/CertificateParsingException:<init>	(Ljava/lang/String;)V
    //   140: athrow
    //   141: iload 9
    //   143: istore_2
    //   144: goto -46 -> 98
    //   147: aload_0
    //   148: getfield 71	org/bouncycastle2/jce/provider/X509CertificateObject:keyUsage	[Z
    //   151: astore 11
    //   153: aload 8
    //   155: iload 10
    //   157: bipush 8
    //   159: idiv
    //   160: baload
    //   161: sipush 128
    //   164: iload 10
    //   166: bipush 8
    //   168: irem
    //   169: iushr
    //   170: iand
    //   171: ifeq +52 -> 223
    //   174: iconst_1
    //   175: istore 12
    //   177: aload 11
    //   179: iload 10
    //   181: iload 12
    //   183: bastore
    //   184: iinc 10 1
    //   187: goto -79 -> 108
    //   190: aload_0
    //   191: aconst_null
    //   192: putfield 71	org/bouncycastle2/jce/provider/X509CertificateObject:keyUsage	[Z
    //   195: return
    //   196: astore 5
    //   198: new 22	java/security/cert/CertificateParsingException
    //   201: dup
    //   202: new 73	java/lang/StringBuilder
    //   205: dup
    //   206: ldc 89
    //   208: invokespecial 78	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   211: aload 5
    //   213: invokevirtual 82	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   216: invokevirtual 86	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   219: invokespecial 87	java/security/cert/CertificateParsingException:<init>	(Ljava/lang/String;)V
    //   222: athrow
    //   223: iconst_0
    //   224: istore 12
    //   226: goto -49 -> 177
    //
    // Exception table:
    //   from	to	target	type
    //   23	31	116	java/lang/Exception
    //   36	48	116	java/lang/Exception
    //   48	56	196	java/lang/Exception
    //   61	92	196	java/lang/Exception
    //   98	105	196	java/lang/Exception
    //   147	174	196	java/lang/Exception
    //   177	184	196	java/lang/Exception
    //   190	195	196	java/lang/Exception
  }

  private int calculateHashCode()
  {
    int i = 0;
    try
    {
      byte[] arrayOfByte = getEncoded();
      for (int j = 1; ; j++)
      {
        if (j >= arrayOfByte.length)
          return i;
        int k = arrayOfByte[j];
        i += k * j;
      }
    }
    catch (CertificateEncodingException localCertificateEncodingException)
    {
    }
    return 0;
  }

  private void checkSignature(PublicKey paramPublicKey, Signature paramSignature)
    throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    if (!isAlgIdEqual(this.c.getSignatureAlgorithm(), this.c.getTBSCertificate().getSignature()))
      throw new CertificateException("signature algorithm in TBS cert not same as outer cert");
    X509SignatureUtil.setSignatureParameters(paramSignature, this.c.getSignatureAlgorithm().getParameters());
    paramSignature.initVerify(paramPublicKey);
    paramSignature.update(getTBSCertificate());
    if (!paramSignature.verify(getSignature()))
      throw new InvalidKeyException("Public key presented not for certificate signature");
  }

  private byte[] getExtensionBytes(String paramString)
  {
    X509Extensions localX509Extensions = this.c.getTBSCertificate().getExtensions();
    if (localX509Extensions != null)
    {
      X509Extension localX509Extension = localX509Extensions.getExtension(new DERObjectIdentifier(paramString));
      if (localX509Extension != null)
        return localX509Extension.getValue().getOctets();
    }
    return null;
  }

  private boolean isAlgIdEqual(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2)
  {
    if (!paramAlgorithmIdentifier1.getObjectId().equals(paramAlgorithmIdentifier2.getObjectId()));
    do
    {
      do
      {
        return false;
        if (paramAlgorithmIdentifier1.getParameters() != null)
          break;
      }
      while ((paramAlgorithmIdentifier2.getParameters() != null) && (!paramAlgorithmIdentifier2.getParameters().equals(DERNull.INSTANCE)));
      return true;
      if (paramAlgorithmIdentifier2.getParameters() != null)
        break;
    }
    while ((paramAlgorithmIdentifier1.getParameters() != null) && (!paramAlgorithmIdentifier1.getParameters().equals(DERNull.INSTANCE)));
    return true;
    return paramAlgorithmIdentifier1.getParameters().equals(paramAlgorithmIdentifier2.getParameters());
  }

  public void checkValidity()
    throws CertificateExpiredException, CertificateNotYetValidException
  {
    checkValidity(new Date());
  }

  public void checkValidity(Date paramDate)
    throws CertificateExpiredException, CertificateNotYetValidException
  {
    if (paramDate.getTime() > getNotAfter().getTime())
      throw new CertificateExpiredException("certificate expired on " + this.c.getEndDate().getTime());
    if (paramDate.getTime() < getNotBefore().getTime())
      throw new CertificateNotYetValidException("certificate not valid till " + this.c.getStartDate().getTime());
  }

  public boolean equals(Object paramObject)
  {
    boolean bool2;
    if (paramObject == this)
      bool2 = true;
    boolean bool1;
    do
    {
      return bool2;
      bool1 = paramObject instanceof Certificate;
      bool2 = false;
    }
    while (!bool1);
    Certificate localCertificate = (Certificate)paramObject;
    try
    {
      boolean bool3 = Arrays.areEqual(getEncoded(), localCertificate.getEncoded());
      return bool3;
    }
    catch (CertificateEncodingException localCertificateEncodingException)
    {
    }
    return false;
  }

  public DEREncodable getBagAttribute(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return this.attrCarrier.getBagAttribute(paramDERObjectIdentifier);
  }

  public Enumeration getBagAttributeKeys()
  {
    return this.attrCarrier.getBagAttributeKeys();
  }

  public int getBasicConstraints()
  {
    int i = -1;
    if ((this.basicConstraints != null) && (this.basicConstraints.isCA()))
    {
      if (this.basicConstraints.getPathLenConstraint() == null)
        i = 2147483647;
    }
    else
      return i;
    return this.basicConstraints.getPathLenConstraint().intValue();
  }

  public Set getCriticalExtensionOIDs()
  {
    if (getVersion() == 3)
    {
      HashSet localHashSet = new HashSet();
      X509Extensions localX509Extensions = this.c.getTBSCertificate().getExtensions();
      if (localX509Extensions != null)
      {
        Enumeration localEnumeration = localX509Extensions.oids();
        while (true)
        {
          if (!localEnumeration.hasMoreElements())
            return localHashSet;
          DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
          if (localX509Extensions.getExtension(localDERObjectIdentifier).isCritical())
            localHashSet.add(localDERObjectIdentifier.getId());
        }
      }
    }
    return null;
  }

  public byte[] getEncoded()
    throws CertificateEncodingException
  {
    try
    {
      byte[] arrayOfByte = this.c.getEncoded("DER");
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new CertificateEncodingException(localIOException.toString());
    }
  }

  public List getExtendedKeyUsage()
    throws CertificateParsingException
  {
    byte[] arrayOfByte = getExtensionBytes("2.5.29.37");
    if (arrayOfByte != null)
      try
      {
        ASN1Sequence localASN1Sequence = (ASN1Sequence)new ASN1InputStream(arrayOfByte).readObject();
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; ; i++)
        {
          if (i == localASN1Sequence.size())
            return Collections.unmodifiableList(localArrayList);
          localArrayList.add(((DERObjectIdentifier)localASN1Sequence.getObjectAt(i)).getId());
        }
      }
      catch (Exception localException)
      {
        throw new CertificateParsingException("error processing extended key usage extension");
      }
    return null;
  }

  public byte[] getExtensionValue(String paramString)
  {
    X509Extensions localX509Extensions = this.c.getTBSCertificate().getExtensions();
    if (localX509Extensions != null)
    {
      X509Extension localX509Extension = localX509Extensions.getExtension(new DERObjectIdentifier(paramString));
      if (localX509Extension != null)
        try
        {
          byte[] arrayOfByte = localX509Extension.getValue().getEncoded();
          return arrayOfByte;
        }
        catch (Exception localException)
        {
          throw new IllegalStateException("error parsing " + localException.toString());
        }
    }
    return null;
  }

  public Principal getIssuerDN()
  {
    return new X509Principal(this.c.getIssuer());
  }

  public boolean[] getIssuerUniqueID()
  {
    DERBitString localDERBitString = this.c.getTBSCertificate().getIssuerUniqueId();
    if (localDERBitString != null)
    {
      byte[] arrayOfByte = localDERBitString.getBytes();
      boolean[] arrayOfBoolean = new boolean[8 * arrayOfByte.length - localDERBitString.getPadBits()];
      int i = 0;
      if (i == arrayOfBoolean.length)
        return arrayOfBoolean;
      if ((arrayOfByte[(i / 8)] & 128 >>> i % 8) != 0);
      for (int j = 1; ; j = 0)
      {
        arrayOfBoolean[i] = j;
        i++;
        break;
      }
    }
    return null;
  }

  public X500Principal getIssuerX500Principal()
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      new ASN1OutputStream(localByteArrayOutputStream).writeObject(this.c.getIssuer());
      X500Principal localX500Principal = new X500Principal(localByteArrayOutputStream.toByteArray());
      return localX500Principal;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalStateException("can't encode issuer DN");
  }

  public boolean[] getKeyUsage()
  {
    return this.keyUsage;
  }

  public Set getNonCriticalExtensionOIDs()
  {
    if (getVersion() == 3)
    {
      HashSet localHashSet = new HashSet();
      X509Extensions localX509Extensions = this.c.getTBSCertificate().getExtensions();
      if (localX509Extensions != null)
      {
        Enumeration localEnumeration = localX509Extensions.oids();
        while (true)
        {
          if (!localEnumeration.hasMoreElements())
            return localHashSet;
          DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
          if (!localX509Extensions.getExtension(localDERObjectIdentifier).isCritical())
            localHashSet.add(localDERObjectIdentifier.getId());
        }
      }
    }
    return null;
  }

  public Date getNotAfter()
  {
    return this.c.getEndDate().getDate();
  }

  public Date getNotBefore()
  {
    return this.c.getStartDate().getDate();
  }

  public PublicKey getPublicKey()
  {
    return JDKKeyFactory.createPublicKeyFromPublicKeyInfo(this.c.getSubjectPublicKeyInfo());
  }

  public BigInteger getSerialNumber()
  {
    return this.c.getSerialNumber().getValue();
  }

  public String getSigAlgName()
  {
    Provider localProvider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
    String str;
    if (localProvider != null)
    {
      str = localProvider.getProperty("Alg.Alias.Signature." + getSigAlgOID());
      if (str != null)
        return str;
    }
    Provider[] arrayOfProvider = Security.getProviders();
    for (int i = 0; ; i++)
    {
      if (i == arrayOfProvider.length)
        return getSigAlgOID();
      str = arrayOfProvider[i].getProperty("Alg.Alias.Signature." + getSigAlgOID());
      if (str != null)
        break;
    }
  }

  public String getSigAlgOID()
  {
    return this.c.getSignatureAlgorithm().getObjectId().getId();
  }

  public byte[] getSigAlgParams()
  {
    if (this.c.getSignatureAlgorithm().getParameters() != null)
      return this.c.getSignatureAlgorithm().getParameters().getDERObject().getDEREncoded();
    return null;
  }

  public byte[] getSignature()
  {
    return this.c.getSignature().getBytes();
  }

  public Principal getSubjectDN()
  {
    return new X509Principal(this.c.getSubject());
  }

  public boolean[] getSubjectUniqueID()
  {
    DERBitString localDERBitString = this.c.getTBSCertificate().getSubjectUniqueId();
    if (localDERBitString != null)
    {
      byte[] arrayOfByte = localDERBitString.getBytes();
      boolean[] arrayOfBoolean = new boolean[8 * arrayOfByte.length - localDERBitString.getPadBits()];
      int i = 0;
      if (i == arrayOfBoolean.length)
        return arrayOfBoolean;
      if ((arrayOfByte[(i / 8)] & 128 >>> i % 8) != 0);
      for (int j = 1; ; j = 0)
      {
        arrayOfBoolean[i] = j;
        i++;
        break;
      }
    }
    return null;
  }

  public X500Principal getSubjectX500Principal()
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      new ASN1OutputStream(localByteArrayOutputStream).writeObject(this.c.getSubject());
      X500Principal localX500Principal = new X500Principal(localByteArrayOutputStream.toByteArray());
      return localX500Principal;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalStateException("can't encode issuer DN");
  }

  public byte[] getTBSCertificate()
    throws CertificateEncodingException
  {
    try
    {
      byte[] arrayOfByte = this.c.getTBSCertificate().getEncoded("DER");
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new CertificateEncodingException(localIOException.toString());
    }
  }

  public int getVersion()
  {
    return this.c.getVersion();
  }

  public boolean hasUnsupportedCriticalExtension()
  {
    X509Extensions localX509Extensions;
    Enumeration localEnumeration;
    if (getVersion() == 3)
    {
      localX509Extensions = this.c.getTBSCertificate().getExtensions();
      if (localX509Extensions != null)
        localEnumeration = localX509Extensions.oids();
    }
    DERObjectIdentifier localDERObjectIdentifier;
    String str;
    do
    {
      if (!localEnumeration.hasMoreElements())
        return false;
      localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
      str = localDERObjectIdentifier.getId();
    }
    while ((str.equals(RFC3280CertPathUtilities.KEY_USAGE)) || (str.equals(RFC3280CertPathUtilities.CERTIFICATE_POLICIES)) || (str.equals(RFC3280CertPathUtilities.POLICY_MAPPINGS)) || (str.equals(RFC3280CertPathUtilities.INHIBIT_ANY_POLICY)) || (str.equals(RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS)) || (str.equals(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT)) || (str.equals(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR)) || (str.equals(RFC3280CertPathUtilities.POLICY_CONSTRAINTS)) || (str.equals(RFC3280CertPathUtilities.BASIC_CONSTRAINTS)) || (str.equals(RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME)) || (str.equals(RFC3280CertPathUtilities.NAME_CONSTRAINTS)) || (!localX509Extensions.getExtension(localDERObjectIdentifier).isCritical()));
    return true;
  }

  public int hashCode()
  {
    try
    {
      if (!this.hashValueSet)
      {
        this.hashValue = calculateHashCode();
        this.hashValueSet = true;
      }
      int i = this.hashValue;
      return i;
    }
    finally
    {
    }
  }

  public void setBagAttribute(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.attrCarrier.setBagAttribute(paramDERObjectIdentifier, paramDEREncodable);
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("  [0]         Version: ").append(getVersion()).append(str);
    localStringBuffer.append("         SerialNumber: ").append(getSerialNumber()).append(str);
    localStringBuffer.append("             IssuerDN: ").append(getIssuerDN()).append(str);
    localStringBuffer.append("           Start Date: ").append(getNotBefore()).append(str);
    localStringBuffer.append("           Final Date: ").append(getNotAfter()).append(str);
    localStringBuffer.append("            SubjectDN: ").append(getSubjectDN()).append(str);
    localStringBuffer.append("           Public Key: ").append(getPublicKey()).append(str);
    localStringBuffer.append("  Signature Algorithm: ").append(getSigAlgName()).append(str);
    byte[] arrayOfByte = getSignature();
    localStringBuffer.append("            Signature: ").append(new String(Hex.encode(arrayOfByte, 0, 20))).append(str);
    int i = 20;
    X509Extensions localX509Extensions;
    Enumeration localEnumeration;
    if (i >= arrayOfByte.length)
    {
      localX509Extensions = this.c.getTBSCertificate().getExtensions();
      if (localX509Extensions != null)
      {
        localEnumeration = localX509Extensions.oids();
        if (localEnumeration.hasMoreElements())
          localStringBuffer.append("       Extensions: \n");
      }
    }
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
      {
        return localStringBuffer.toString();
        if (i < -20 + arrayOfByte.length)
          localStringBuffer.append("                       ").append(new String(Hex.encode(arrayOfByte, i, 20))).append(str);
        while (true)
        {
          i += 20;
          break;
          localStringBuffer.append("                       ").append(new String(Hex.encode(arrayOfByte, i, arrayOfByte.length - i))).append(str);
        }
      }
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
      X509Extension localX509Extension = localX509Extensions.getExtension(localDERObjectIdentifier);
      if (localX509Extension.getValue() != null)
      {
        ASN1InputStream localASN1InputStream = new ASN1InputStream(localX509Extension.getValue().getOctets());
        localStringBuffer.append("                       critical(").append(localX509Extension.isCritical()).append(") ");
        try
        {
          if (!localDERObjectIdentifier.equals(X509Extensions.BasicConstraints))
            break label497;
          localStringBuffer.append(new BasicConstraints((ASN1Sequence)localASN1InputStream.readObject())).append(str);
        }
        catch (Exception localException)
        {
          localStringBuffer.append(localDERObjectIdentifier.getId());
          localStringBuffer.append(" value = ").append("*****").append(str);
        }
        continue;
        label497: if (localDERObjectIdentifier.equals(X509Extensions.KeyUsage))
        {
          localStringBuffer.append(new KeyUsage((DERBitString)localASN1InputStream.readObject())).append(str);
        }
        else if (localDERObjectIdentifier.equals(MiscObjectIdentifiers.netscapeCertType))
        {
          localStringBuffer.append(new NetscapeCertType((DERBitString)localASN1InputStream.readObject())).append(str);
        }
        else if (localDERObjectIdentifier.equals(MiscObjectIdentifiers.netscapeRevocationURL))
        {
          localStringBuffer.append(new NetscapeRevocationURL((DERIA5String)localASN1InputStream.readObject())).append(str);
        }
        else if (localDERObjectIdentifier.equals(MiscObjectIdentifiers.verisignCzagExtension))
        {
          localStringBuffer.append(new VerisignCzagExtension((DERIA5String)localASN1InputStream.readObject())).append(str);
        }
        else
        {
          localStringBuffer.append(localDERObjectIdentifier.getId());
          localStringBuffer.append(" value = ").append(ASN1Dump.dumpAsString(localASN1InputStream.readObject())).append(str);
        }
      }
      else
      {
        localStringBuffer.append(str);
      }
    }
  }

  public final void verify(PublicKey paramPublicKey)
    throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
  {
    String str = X509SignatureUtil.getSignatureName(this.c.getSignatureAlgorithm());
    try
    {
      Signature localSignature2 = Signature.getInstance(str, BouncyCastleProvider.PROVIDER_NAME);
      localSignature1 = localSignature2;
      checkSignature(paramPublicKey, localSignature1);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        Signature localSignature1 = Signature.getInstance(str);
    }
  }

  public final void verify(PublicKey paramPublicKey, String paramString)
    throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
  {
    checkSignature(paramPublicKey, Signature.getInstance(X509SignatureUtil.getSignatureName(this.c.getSignatureAlgorithm()), paramString));
  }
}