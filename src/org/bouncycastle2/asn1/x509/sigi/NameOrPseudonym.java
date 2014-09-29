package org.bouncycastle2.asn1.x509.sigi;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x500.DirectoryString;

public class NameOrPseudonym extends ASN1Encodable
  implements ASN1Choice
{
  private ASN1Sequence givenName;
  private DirectoryString pseudonym;
  private DirectoryString surname;

  public NameOrPseudonym(String paramString)
  {
    this(new DirectoryString(paramString));
  }

  private NameOrPseudonym(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    if (!(paramASN1Sequence.getObjectAt(0) instanceof ASN1String))
      throw new IllegalArgumentException("Bad object encountered: " + paramASN1Sequence.getObjectAt(0).getClass());
    this.surname = DirectoryString.getInstance(paramASN1Sequence.getObjectAt(0));
    this.givenName = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public NameOrPseudonym(DirectoryString paramDirectoryString)
  {
    this.pseudonym = paramDirectoryString;
  }

  public NameOrPseudonym(DirectoryString paramDirectoryString, ASN1Sequence paramASN1Sequence)
  {
    this.surname = paramDirectoryString;
    this.givenName = paramASN1Sequence;
  }

  public static NameOrPseudonym getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof NameOrPseudonym)))
      return (NameOrPseudonym)paramObject;
    if ((paramObject instanceof ASN1String))
      return new NameOrPseudonym(DirectoryString.getInstance(paramObject));
    if ((paramObject instanceof ASN1Sequence))
      return new NameOrPseudonym((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public DirectoryString[] getGivenName()
  {
    DirectoryString[] arrayOfDirectoryString = new DirectoryString[this.givenName.size()];
    int i = 0;
    Enumeration localEnumeration = this.givenName.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return arrayOfDirectoryString;
      int j = i + 1;
      arrayOfDirectoryString[i] = DirectoryString.getInstance(localEnumeration.nextElement());
      i = j;
    }
  }

  public DirectoryString getPseudonym()
  {
    return this.pseudonym;
  }

  public DirectoryString getSurname()
  {
    return this.surname;
  }

  public DERObject toASN1Object()
  {
    if (this.pseudonym != null)
      return this.pseudonym.toASN1Object();
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.surname);
    localASN1EncodableVector.add(this.givenName);
    return new DERSequence(localASN1EncodableVector);
  }
}