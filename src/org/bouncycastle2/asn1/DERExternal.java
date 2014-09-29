package org.bouncycastle2.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERExternal extends ASN1Object
{
  private ASN1Object dataValueDescriptor;
  private DERObjectIdentifier directReference;
  private int encoding;
  private DERObject externalContent;
  private DERInteger indirectReference;

  public DERExternal(ASN1EncodableVector paramASN1EncodableVector)
  {
    DERObject localDERObject = getObjFromVector(paramASN1EncodableVector, 0);
    boolean bool = localDERObject instanceof DERObjectIdentifier;
    int i = 0;
    if (bool)
    {
      this.directReference = ((DERObjectIdentifier)localDERObject);
      i = 0 + 1;
      localDERObject = getObjFromVector(paramASN1EncodableVector, i);
    }
    if ((localDERObject instanceof DERInteger))
    {
      this.indirectReference = ((DERInteger)localDERObject);
      i++;
      localDERObject = getObjFromVector(paramASN1EncodableVector, i);
    }
    if (!(localDERObject instanceof DERTaggedObject))
    {
      this.dataValueDescriptor = ((ASN1Object)localDERObject);
      i++;
      localDERObject = getObjFromVector(paramASN1EncodableVector, i);
    }
    if (paramASN1EncodableVector.size() != i + 1)
      throw new IllegalArgumentException("input vector too large");
    if (!(localDERObject instanceof DERTaggedObject))
      throw new IllegalArgumentException("No tagged object found in vector. Structure doesn't seem to be of type External");
    DERTaggedObject localDERTaggedObject = (DERTaggedObject)localDERObject;
    setEncoding(localDERTaggedObject.getTagNo());
    this.externalContent = localDERTaggedObject.getObject();
  }

  public DERExternal(DERObjectIdentifier paramDERObjectIdentifier, DERInteger paramDERInteger, ASN1Object paramASN1Object, int paramInt, DERObject paramDERObject)
  {
    setDirectReference(paramDERObjectIdentifier);
    setIndirectReference(paramDERInteger);
    setDataValueDescriptor(paramASN1Object);
    setEncoding(paramInt);
    setExternalContent(paramDERObject.getDERObject());
  }

  public DERExternal(DERObjectIdentifier paramDERObjectIdentifier, DERInteger paramDERInteger, ASN1Object paramASN1Object, DERTaggedObject paramDERTaggedObject)
  {
    this(paramDERObjectIdentifier, paramDERInteger, paramASN1Object, paramDERTaggedObject.getTagNo(), paramDERTaggedObject.getDERObject());
  }

  private DERObject getObjFromVector(ASN1EncodableVector paramASN1EncodableVector, int paramInt)
  {
    if (paramASN1EncodableVector.size() <= paramInt)
      throw new IllegalArgumentException("too few objects in input vector");
    return paramASN1EncodableVector.get(paramInt).getDERObject();
  }

  private void setDataValueDescriptor(ASN1Object paramASN1Object)
  {
    this.dataValueDescriptor = paramASN1Object;
  }

  private void setDirectReference(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.directReference = paramDERObjectIdentifier;
  }

  private void setEncoding(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 2))
      throw new IllegalArgumentException("invalid encoding value: " + paramInt);
    this.encoding = paramInt;
  }

  private void setExternalContent(DERObject paramDERObject)
  {
    this.externalContent = paramDERObject;
  }

  private void setIndirectReference(DERInteger paramDERInteger)
  {
    this.indirectReference = paramDERInteger;
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERExternal));
    DERExternal localDERExternal;
    do
    {
      return false;
      if (this == paramDERObject)
        return true;
      localDERExternal = (DERExternal)paramDERObject;
    }
    while (((this.directReference != null) && ((localDERExternal.directReference == null) || (!localDERExternal.directReference.equals(this.directReference)))) || ((this.indirectReference != null) && ((localDERExternal.indirectReference == null) || (!localDERExternal.indirectReference.equals(this.indirectReference)))) || ((this.dataValueDescriptor != null) && ((localDERExternal.dataValueDescriptor == null) || (!localDERExternal.dataValueDescriptor.equals(this.dataValueDescriptor)))));
    return this.externalContent.equals(localDERExternal.externalContent);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    if (this.directReference != null)
      localByteArrayOutputStream.write(this.directReference.getDEREncoded());
    if (this.indirectReference != null)
      localByteArrayOutputStream.write(this.indirectReference.getDEREncoded());
    if (this.dataValueDescriptor != null)
      localByteArrayOutputStream.write(this.dataValueDescriptor.getDEREncoded());
    localByteArrayOutputStream.write(new DERTaggedObject(this.encoding, this.externalContent).getDEREncoded());
    paramDEROutputStream.writeEncoded(32, 8, localByteArrayOutputStream.toByteArray());
  }

  public ASN1Object getDataValueDescriptor()
  {
    return this.dataValueDescriptor;
  }

  public DERObjectIdentifier getDirectReference()
  {
    return this.directReference;
  }

  public int getEncoding()
  {
    return this.encoding;
  }

  public DERObject getExternalContent()
  {
    return this.externalContent;
  }

  public DERInteger getIndirectReference()
  {
    return this.indirectReference;
  }

  public int hashCode()
  {
    DERObjectIdentifier localDERObjectIdentifier = this.directReference;
    int i = 0;
    if (localDERObjectIdentifier != null)
      i = this.directReference.hashCode();
    if (this.indirectReference != null)
      i ^= this.indirectReference.hashCode();
    if (this.dataValueDescriptor != null)
      i ^= this.dataValueDescriptor.hashCode();
    return i ^ this.externalContent.hashCode();
  }
}