package org.bouncycastle2.asn1.isismtt.x509;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1String;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.isismtt.ISISMTTObjectIdentifiers;
import org.bouncycastle2.asn1.x500.DirectoryString;

public class NamingAuthority extends ASN1Encodable
{
  public static final DERObjectIdentifier id_isismtt_at_namingAuthorities_RechtWirtschaftSteuern = new DERObjectIdentifier(ISISMTTObjectIdentifiers.id_isismtt_at_namingAuthorities + ".1");
  private DERObjectIdentifier namingAuthorityId;
  private DirectoryString namingAuthorityText;
  private String namingAuthorityUrl;

  private NamingAuthority(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() > 3)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    DEREncodable localDEREncodable3;
    DEREncodable localDEREncodable2;
    if (localEnumeration.hasMoreElements())
    {
      localDEREncodable3 = (DEREncodable)localEnumeration.nextElement();
      if ((localDEREncodable3 instanceof DERObjectIdentifier))
        this.namingAuthorityId = ((DERObjectIdentifier)localDEREncodable3);
    }
    else if (localEnumeration.hasMoreElements())
    {
      localDEREncodable2 = (DEREncodable)localEnumeration.nextElement();
      if (!(localDEREncodable2 instanceof DERIA5String))
        break label227;
      this.namingAuthorityUrl = DERIA5String.getInstance(localDEREncodable2).getString();
    }
    DEREncodable localDEREncodable1;
    while (true)
    {
      if (localEnumeration.hasMoreElements())
      {
        localDEREncodable1 = (DEREncodable)localEnumeration.nextElement();
        if (!(localDEREncodable1 instanceof ASN1String))
          break label275;
        this.namingAuthorityText = DirectoryString.getInstance(localDEREncodable1);
      }
      return;
      if ((localDEREncodable3 instanceof DERIA5String))
      {
        this.namingAuthorityUrl = DERIA5String.getInstance(localDEREncodable3).getString();
        break;
      }
      if ((localDEREncodable3 instanceof ASN1String))
      {
        this.namingAuthorityText = DirectoryString.getInstance(localDEREncodable3);
        break;
      }
      throw new IllegalArgumentException("Bad object encountered: " + localDEREncodable3.getClass());
      label227: if (!(localDEREncodable2 instanceof ASN1String))
        break label247;
      this.namingAuthorityText = DirectoryString.getInstance(localDEREncodable2);
    }
    label247: throw new IllegalArgumentException("Bad object encountered: " + localDEREncodable2.getClass());
    label275: throw new IllegalArgumentException("Bad object encountered: " + localDEREncodable1.getClass());
  }

  public NamingAuthority(DERObjectIdentifier paramDERObjectIdentifier, String paramString, DirectoryString paramDirectoryString)
  {
    this.namingAuthorityId = paramDERObjectIdentifier;
    this.namingAuthorityUrl = paramString;
    this.namingAuthorityText = paramDirectoryString;
  }

  public static NamingAuthority getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof NamingAuthority)))
      return (NamingAuthority)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new NamingAuthority((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static NamingAuthority getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERObjectIdentifier getNamingAuthorityId()
  {
    return this.namingAuthorityId;
  }

  public DirectoryString getNamingAuthorityText()
  {
    return this.namingAuthorityText;
  }

  public String getNamingAuthorityUrl()
  {
    return this.namingAuthorityUrl;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.namingAuthorityId != null)
      localASN1EncodableVector.add(this.namingAuthorityId);
    if (this.namingAuthorityUrl != null)
      localASN1EncodableVector.add(new DERIA5String(this.namingAuthorityUrl, true));
    if (this.namingAuthorityText != null)
      localASN1EncodableVector.add(this.namingAuthorityText);
    return new DERSequence(localASN1EncodableVector);
  }
}