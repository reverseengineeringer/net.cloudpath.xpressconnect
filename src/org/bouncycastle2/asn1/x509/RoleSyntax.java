package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1String;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class RoleSyntax extends ASN1Encodable
{
  private GeneralNames roleAuthority;
  private GeneralName roleName;

  public RoleSyntax(String paramString)
  {
    this(new GeneralName(6, paramString));
  }

  private RoleSyntax(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    int i = 0;
    if (i == paramASN1Sequence.size())
      return;
    ASN1TaggedObject localASN1TaggedObject = ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(i));
    switch (localASN1TaggedObject.getTagNo())
    {
    default:
      throw new IllegalArgumentException("Unknown tag in RoleSyntax");
    case 0:
      this.roleAuthority = GeneralNames.getInstance(localASN1TaggedObject, false);
    case 1:
    }
    while (true)
    {
      i++;
      break;
      this.roleName = GeneralName.getInstance(localASN1TaggedObject, true);
    }
  }

  public RoleSyntax(GeneralName paramGeneralName)
  {
    this(null, paramGeneralName);
  }

  public RoleSyntax(GeneralNames paramGeneralNames, GeneralName paramGeneralName)
  {
    if ((paramGeneralName == null) || (paramGeneralName.getTagNo() != 6) || (((ASN1String)paramGeneralName.getName()).getString().equals("")))
      throw new IllegalArgumentException("the role name MUST be non empty and MUST use the URI option of GeneralName");
    this.roleAuthority = paramGeneralNames;
    this.roleName = paramGeneralName;
  }

  public static RoleSyntax getInstance(Object paramObject)
  {
    if ((paramObject instanceof RoleSyntax))
      return (RoleSyntax)paramObject;
    if (paramObject != null)
      return new RoleSyntax(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public GeneralNames getRoleAuthority()
  {
    return this.roleAuthority;
  }

  public String[] getRoleAuthorityAsString()
  {
    if (this.roleAuthority == null)
    {
      arrayOfString = new String[0];
      return arrayOfString;
    }
    GeneralName[] arrayOfGeneralName = this.roleAuthority.getNames();
    String[] arrayOfString = new String[arrayOfGeneralName.length];
    int i = 0;
    label30: DEREncodable localDEREncodable;
    if (i < arrayOfGeneralName.length)
    {
      localDEREncodable = arrayOfGeneralName[i].getName();
      if (!(localDEREncodable instanceof ASN1String))
        break label71;
      arrayOfString[i] = ((ASN1String)localDEREncodable).getString();
    }
    while (true)
    {
      i++;
      break label30;
      break;
      label71: arrayOfString[i] = localDEREncodable.toString();
    }
  }

  public GeneralName getRoleName()
  {
    return this.roleName;
  }

  public String getRoleNameAsString()
  {
    return ((ASN1String)this.roleName.getName()).getString();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.roleAuthority != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.roleAuthority));
    localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.roleName));
    return new DERSequence(localASN1EncodableVector);
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("Name: " + getRoleNameAsString() + " - Auth: ");
    if ((this.roleAuthority == null) || (this.roleAuthority.getNames().length == 0))
    {
      localStringBuffer.append("N/A");
      return localStringBuffer.toString();
    }
    String[] arrayOfString = getRoleAuthorityAsString();
    localStringBuffer.append('[').append(arrayOfString[0]);
    for (int i = 1; ; i++)
    {
      if (i >= arrayOfString.length)
      {
        localStringBuffer.append(']');
        break;
      }
      localStringBuffer.append(", ").append(arrayOfString[i]);
    }
  }
}