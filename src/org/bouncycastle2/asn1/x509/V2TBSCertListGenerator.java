package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.DERUTCTime;
import org.bouncycastle2.asn1.x500.X500Name;

public class V2TBSCertListGenerator
{
  private Vector crlentries = null;
  X509Extensions extensions = null;
  X509Name issuer;
  Time nextUpdate = null;
  AlgorithmIdentifier signature;
  Time thisUpdate;
  DERInteger version = new DERInteger(1);

  public void addCRLEntry(ASN1Sequence paramASN1Sequence)
  {
    if (this.crlentries == null)
      this.crlentries = new Vector();
    this.crlentries.addElement(paramASN1Sequence);
  }

  public void addCRLEntry(DERInteger paramDERInteger, DERUTCTime paramDERUTCTime, int paramInt)
  {
    addCRLEntry(paramDERInteger, new Time(paramDERUTCTime), paramInt);
  }

  public void addCRLEntry(DERInteger paramDERInteger, Time paramTime, int paramInt)
  {
    addCRLEntry(paramDERInteger, paramTime, paramInt, null);
  }

  // ERROR //
  public void addCRLEntry(DERInteger paramDERInteger, Time paramTime, int paramInt, org.bouncycastle2.asn1.DERGeneralizedTime paramDERGeneralizedTime)
  {
    // Byte code:
    //   0: new 38	java/util/Vector
    //   3: dup
    //   4: invokespecial 39	java/util/Vector:<init>	()V
    //   7: astore 5
    //   9: new 38	java/util/Vector
    //   12: dup
    //   13: invokespecial 39	java/util/Vector:<init>	()V
    //   16: astore 6
    //   18: iload_3
    //   19: ifeq +46 -> 65
    //   22: new 59	org/bouncycastle2/asn1/x509/CRLReason
    //   25: dup
    //   26: iload_3
    //   27: invokespecial 60	org/bouncycastle2/asn1/x509/CRLReason:<init>	(I)V
    //   30: astore 7
    //   32: aload 5
    //   34: getstatic 66	org/bouncycastle2/asn1/x509/X509Extension:reasonCode	Lorg/bouncycastle2/asn1/ASN1ObjectIdentifier;
    //   37: invokevirtual 43	java/util/Vector:addElement	(Ljava/lang/Object;)V
    //   40: aload 6
    //   42: new 62	org/bouncycastle2/asn1/x509/X509Extension
    //   45: dup
    //   46: iconst_0
    //   47: new 68	org/bouncycastle2/asn1/DEROctetString
    //   50: dup
    //   51: aload 7
    //   53: invokevirtual 72	org/bouncycastle2/asn1/x509/CRLReason:getEncoded	()[B
    //   56: invokespecial 75	org/bouncycastle2/asn1/DEROctetString:<init>	([B)V
    //   59: invokespecial 78	org/bouncycastle2/asn1/x509/X509Extension:<init>	(ZLorg/bouncycastle2/asn1/ASN1OctetString;)V
    //   62: invokevirtual 43	java/util/Vector:addElement	(Ljava/lang/Object;)V
    //   65: aload 4
    //   67: ifnull +36 -> 103
    //   70: aload 5
    //   72: getstatic 81	org/bouncycastle2/asn1/x509/X509Extension:invalidityDate	Lorg/bouncycastle2/asn1/ASN1ObjectIdentifier;
    //   75: invokevirtual 43	java/util/Vector:addElement	(Ljava/lang/Object;)V
    //   78: aload 6
    //   80: new 62	org/bouncycastle2/asn1/x509/X509Extension
    //   83: dup
    //   84: iconst_0
    //   85: new 68	org/bouncycastle2/asn1/DEROctetString
    //   88: dup
    //   89: aload 4
    //   91: invokevirtual 84	org/bouncycastle2/asn1/DERGeneralizedTime:getEncoded	()[B
    //   94: invokespecial 75	org/bouncycastle2/asn1/DEROctetString:<init>	([B)V
    //   97: invokespecial 78	org/bouncycastle2/asn1/x509/X509Extension:<init>	(ZLorg/bouncycastle2/asn1/ASN1OctetString;)V
    //   100: invokevirtual 43	java/util/Vector:addElement	(Ljava/lang/Object;)V
    //   103: aload 5
    //   105: invokevirtual 88	java/util/Vector:size	()I
    //   108: ifeq +75 -> 183
    //   111: aload_0
    //   112: aload_1
    //   113: aload_2
    //   114: new 90	org/bouncycastle2/asn1/x509/X509Extensions
    //   117: dup
    //   118: aload 5
    //   120: aload 6
    //   122: invokespecial 93	org/bouncycastle2/asn1/x509/X509Extensions:<init>	(Ljava/util/Vector;Ljava/util/Vector;)V
    //   125: invokevirtual 96	org/bouncycastle2/asn1/x509/V2TBSCertListGenerator:addCRLEntry	(Lorg/bouncycastle2/asn1/DERInteger;Lorg/bouncycastle2/asn1/x509/Time;Lorg/bouncycastle2/asn1/x509/X509Extensions;)V
    //   128: return
    //   129: astore 8
    //   131: new 98	java/lang/IllegalArgumentException
    //   134: dup
    //   135: new 100	java/lang/StringBuilder
    //   138: dup
    //   139: ldc 102
    //   141: invokespecial 105	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   144: aload 8
    //   146: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   149: invokevirtual 113	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   152: invokespecial 114	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   155: athrow
    //   156: astore 9
    //   158: new 98	java/lang/IllegalArgumentException
    //   161: dup
    //   162: new 100	java/lang/StringBuilder
    //   165: dup
    //   166: ldc 116
    //   168: invokespecial 105	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   171: aload 9
    //   173: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   176: invokevirtual 113	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   179: invokespecial 114	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   182: athrow
    //   183: aload_0
    //   184: aload_1
    //   185: aload_2
    //   186: aconst_null
    //   187: invokevirtual 96	org/bouncycastle2/asn1/x509/V2TBSCertListGenerator:addCRLEntry	(Lorg/bouncycastle2/asn1/DERInteger;Lorg/bouncycastle2/asn1/x509/Time;Lorg/bouncycastle2/asn1/x509/X509Extensions;)V
    //   190: return
    //
    // Exception table:
    //   from	to	target	type
    //   32	65	129	java/io/IOException
    //   70	103	156	java/io/IOException
  }

  public void addCRLEntry(DERInteger paramDERInteger, Time paramTime, X509Extensions paramX509Extensions)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(paramDERInteger);
    localASN1EncodableVector.add(paramTime);
    if (paramX509Extensions != null)
      localASN1EncodableVector.add(paramX509Extensions);
    addCRLEntry(new DERSequence(localASN1EncodableVector));
  }

  public TBSCertList generateTBSCertList()
  {
    if ((this.signature == null) || (this.issuer == null) || (this.thisUpdate == null))
      throw new IllegalStateException("Not all mandatory fields set in V2 TBSCertList generator.");
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    localASN1EncodableVector1.add(this.version);
    localASN1EncodableVector1.add(this.signature);
    localASN1EncodableVector1.add(this.issuer);
    localASN1EncodableVector1.add(this.thisUpdate);
    if (this.nextUpdate != null)
      localASN1EncodableVector1.add(this.nextUpdate);
    ASN1EncodableVector localASN1EncodableVector2;
    Enumeration localEnumeration;
    if (this.crlentries != null)
    {
      localASN1EncodableVector2 = new ASN1EncodableVector();
      localEnumeration = this.crlentries.elements();
    }
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
      {
        localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
        if (this.extensions != null)
          localASN1EncodableVector1.add(new DERTaggedObject(0, this.extensions));
        return new TBSCertList(new DERSequence(localASN1EncodableVector1));
      }
      localASN1EncodableVector2.add((ASN1Sequence)localEnumeration.nextElement());
    }
  }

  public void setExtensions(X509Extensions paramX509Extensions)
  {
    this.extensions = paramX509Extensions;
  }

  public void setIssuer(X500Name paramX500Name)
  {
    this.issuer = X509Name.getInstance(paramX500Name);
  }

  public void setIssuer(X509Name paramX509Name)
  {
    this.issuer = paramX509Name;
  }

  public void setNextUpdate(DERUTCTime paramDERUTCTime)
  {
    this.nextUpdate = new Time(paramDERUTCTime);
  }

  public void setNextUpdate(Time paramTime)
  {
    this.nextUpdate = paramTime;
  }

  public void setSignature(AlgorithmIdentifier paramAlgorithmIdentifier)
  {
    this.signature = paramAlgorithmIdentifier;
  }

  public void setThisUpdate(DERUTCTime paramDERUTCTime)
  {
    this.thisUpdate = new Time(paramDERUTCTime);
  }

  public void setThisUpdate(Time paramTime)
  {
    this.thisUpdate = paramTime;
  }
}