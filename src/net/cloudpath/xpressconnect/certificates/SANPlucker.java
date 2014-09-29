package net.cloudpath.xpressconnect.certificates;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import net.cloudpath.xpressconnect.Debug;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.DERUTF8String;

public class SANPlucker
{
  private static final int OTHER_ALT_NAME;
  private ArrayList<String> mASN1Id = new ArrayList();
  private X509Certificate mCert = null;
  private Logger mLogger = null;
  private ArrayList<String> mValue = new ArrayList();

  public SANPlucker(Logger paramLogger, X509Certificate paramX509Certificate)
  {
    this.mLogger = paramLogger;
    this.mCert = paramX509Certificate;
  }

  private boolean haveOtherAltName(Collection<?> paramCollection)
  {
    boolean bool = false;
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      List localList = (List)localIterator.next();
      Integer localInteger = (Integer)localList.get(0);
      if (localInteger.intValue() == 0)
      {
        bool = true;
        parseOtherAltName((byte[])localList.get(1));
      }
      else
      {
        Util.log(this.mLogger, "Unknown SAN type is " + localInteger);
      }
    }
    return bool;
  }

  private void parseDERTaggedObjectL1(DERTaggedObject paramDERTaggedObject)
  {
    if ((paramDERTaggedObject.getObject() instanceof DERSequence))
      parseSequence((DERSequence)paramDERTaggedObject.getObject());
  }

  private void parseOtherAltName(byte[] paramArrayOfByte)
  {
    ASN1InputStream localASN1InputStream = new ASN1InputStream(new ByteArrayInputStream(paramArrayOfByte));
    try
    {
      localDERObject = localASN1InputStream.readObject();
    }
    catch (IOException localIOException1)
    {
      try
      {
        DERObject localDERObject;
        localASN1InputStream.close();
        if ((localDERObject instanceof DERTaggedObject))
          parseDERTaggedObjectL1((DERTaggedObject)localDERObject);
        return;
        localIOException1 = localIOException1;
        localIOException1.printStackTrace();
        Util.log(this.mLogger, "Failed to parse ASN.1 object in.");
        try
        {
          localASN1InputStream.close();
          return;
        }
        catch (IOException localIOException2)
        {
          localIOException2.printStackTrace();
          return;
        }
      }
      catch (IOException localIOException3)
      {
        while (true)
          localIOException3.printStackTrace();
      }
    }
  }

  private void parseSequence(DERSequence paramDERSequence)
  {
    Enumeration localEnumeration = paramDERSequence.getObjects();
    ASN1ObjectIdentifier localASN1ObjectIdentifier = (ASN1ObjectIdentifier)localEnumeration.nextElement();
    DERUTF8String localDERUTF8String = (DERUTF8String)((DERTaggedObject)localEnumeration.nextElement()).getObject();
    this.mASN1Id.add(localASN1ObjectIdentifier.toString());
    this.mValue.add(localDERUTF8String.getString());
    if (Debug.inDebugMode)
    {
      Util.log(this.mLogger, "Found SAN value : " + localASN1ObjectIdentifier.toString() + ", " + localDERUTF8String.getString());
      return;
    }
    Util.log(this.mLogger, "Found SAN value : " + localASN1ObjectIdentifier.toString() + ", <user info filtered>");
  }

  public String getValueForOid(String paramString)
  {
    if (Util.stringIsEmpty(paramString))
    {
      Util.log(this.mLogger, "OID to match is null in getValueForOid()!");
      return null;
    }
    for (int i = 0; ; i++)
      if ((i >= this.mASN1Id.size()) || (((String)this.mASN1Id.get(i)).contentEquals(paramString)))
      {
        if (i >= this.mValue.size())
          break;
        return (String)this.mValue.get(i);
      }
    return null;
  }

  public boolean parse()
  {
    try
    {
      Collection localCollection = this.mCert.getSubjectAlternativeNames();
      boolean bool = false;
      if (localCollection != null)
      {
        int i = localCollection.size();
        bool = false;
        if (i > 0)
          bool = haveOtherAltName(localCollection);
      }
      return bool;
    }
    catch (CertificateParsingException localCertificateParsingException)
    {
      localCertificateParsingException.printStackTrace();
    }
    return false;
  }
}