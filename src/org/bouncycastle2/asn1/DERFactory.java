package org.bouncycastle2.asn1;

class DERFactory
{
  static final DERSequence EMPTY_SEQUENCE = new DERSequence();
  static final DERSet EMPTY_SET = new DERSet();

  static DERSequence createSequence(ASN1EncodableVector paramASN1EncodableVector)
  {
    if (paramASN1EncodableVector.size() < 1)
      return EMPTY_SEQUENCE;
    return new DERSequence(paramASN1EncodableVector);
  }

  static DERSet createSet(ASN1EncodableVector paramASN1EncodableVector)
  {
    if (paramASN1EncodableVector.size() < 1)
      return EMPTY_SET;
    return new DERSet(paramASN1EncodableVector);
  }

  static DERSet createSet(ASN1EncodableVector paramASN1EncodableVector, boolean paramBoolean)
  {
    if (paramASN1EncodableVector.size() < 1)
      return EMPTY_SET;
    return new DERSet(paramASN1EncodableVector, paramBoolean);
  }
}