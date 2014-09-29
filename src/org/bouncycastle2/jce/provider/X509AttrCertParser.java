package org.bouncycastle2.jce.provider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.SignedData;
import org.bouncycastle2.x509.X509AttributeCertificate;
import org.bouncycastle2.x509.X509StreamParserSpi;
import org.bouncycastle2.x509.X509V2AttributeCertificate;
import org.bouncycastle2.x509.util.StreamParsingException;

public class X509AttrCertParser extends X509StreamParserSpi
{
  private static final PEMUtil PEM_PARSER = new PEMUtil("ATTRIBUTE CERTIFICATE");
  private InputStream currentStream = null;
  private ASN1Set sData = null;
  private int sDataObjectCount = 0;

  private X509AttributeCertificate getCertificate()
    throws IOException
  {
    DEREncodable localDEREncodable;
    do
    {
      if ((this.sData == null) || (this.sDataObjectCount >= this.sData.size()))
        return null;
      ASN1Set localASN1Set = this.sData;
      int i = this.sDataObjectCount;
      this.sDataObjectCount = (i + 1);
      localDEREncodable = localASN1Set.getObjectAt(i);
    }
    while ((!(localDEREncodable instanceof ASN1TaggedObject)) || (((ASN1TaggedObject)localDEREncodable).getTagNo() != 2));
    return new X509V2AttributeCertificate(ASN1Sequence.getInstance((ASN1TaggedObject)localDEREncodable, false).getEncoded());
  }

  private X509AttributeCertificate readDERCertificate(InputStream paramInputStream)
    throws IOException
  {
    ASN1Sequence localASN1Sequence = (ASN1Sequence)new ASN1InputStream(paramInputStream, ProviderUtil.getReadLimit(paramInputStream)).readObject();
    if ((localASN1Sequence.size() > 1) && ((localASN1Sequence.getObjectAt(0) instanceof DERObjectIdentifier)) && (localASN1Sequence.getObjectAt(0).equals(PKCSObjectIdentifiers.signedData)))
    {
      this.sData = new SignedData(ASN1Sequence.getInstance((ASN1TaggedObject)localASN1Sequence.getObjectAt(1), true)).getCertificates();
      return getCertificate();
    }
    return new X509V2AttributeCertificate(localASN1Sequence.getEncoded());
  }

  private X509AttributeCertificate readPEMCertificate(InputStream paramInputStream)
    throws IOException
  {
    ASN1Sequence localASN1Sequence = PEM_PARSER.readPEMObject(paramInputStream);
    if (localASN1Sequence != null)
      return new X509V2AttributeCertificate(localASN1Sequence.getEncoded());
    return null;
  }

  public void engineInit(InputStream paramInputStream)
  {
    this.currentStream = paramInputStream;
    this.sData = null;
    this.sDataObjectCount = 0;
    if (!this.currentStream.markSupported())
      this.currentStream = new BufferedInputStream(this.currentStream);
  }

  public Object engineRead()
    throws StreamParsingException
  {
    try
    {
      if (this.sData != null)
      {
        if (this.sDataObjectCount != this.sData.size())
          return getCertificate();
        this.sData = null;
        this.sDataObjectCount = 0;
        return null;
      }
    }
    catch (Exception localException)
    {
      throw new StreamParsingException(localException.toString(), localException);
    }
    this.currentStream.mark(10);
    int i = this.currentStream.read();
    Object localObject = null;
    if (i != -1)
    {
      if (i != 48)
      {
        this.currentStream.reset();
        return readPEMCertificate(this.currentStream);
      }
      this.currentStream.reset();
      X509AttributeCertificate localX509AttributeCertificate = readDERCertificate(this.currentStream);
      localObject = localX509AttributeCertificate;
    }
    return localObject;
  }

  public Collection engineReadAll()
    throws StreamParsingException
  {
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      X509AttributeCertificate localX509AttributeCertificate = (X509AttributeCertificate)engineRead();
      if (localX509AttributeCertificate == null)
        return localArrayList;
      localArrayList.add(localX509AttributeCertificate);
    }
  }
}