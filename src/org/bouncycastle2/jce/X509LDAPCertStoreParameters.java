package org.bouncycastle2.jce;

import java.security.cert.CertStoreParameters;
import java.security.cert.LDAPCertStoreParameters;
import org.bouncycastle2.x509.X509StoreParameters;

public class X509LDAPCertStoreParameters
  implements X509StoreParameters, CertStoreParameters
{
  private String aACertificateAttribute;
  private String aACertificateSubjectAttributeName;
  private String attributeAuthorityRevocationListAttribute;
  private String attributeAuthorityRevocationListIssuerAttributeName;
  private String attributeCertificateAttributeAttribute;
  private String attributeCertificateAttributeSubjectAttributeName;
  private String attributeCertificateRevocationListAttribute;
  private String attributeCertificateRevocationListIssuerAttributeName;
  private String attributeDescriptorCertificateAttribute;
  private String attributeDescriptorCertificateSubjectAttributeName;
  private String authorityRevocationListAttribute;
  private String authorityRevocationListIssuerAttributeName;
  private String baseDN;
  private String cACertificateAttribute;
  private String cACertificateSubjectAttributeName;
  private String certificateRevocationListAttribute;
  private String certificateRevocationListIssuerAttributeName;
  private String crossCertificateAttribute;
  private String crossCertificateSubjectAttributeName;
  private String deltaRevocationListAttribute;
  private String deltaRevocationListIssuerAttributeName;
  private String ldapAACertificateAttributeName;
  private String ldapAttributeAuthorityRevocationListAttributeName;
  private String ldapAttributeCertificateAttributeAttributeName;
  private String ldapAttributeCertificateRevocationListAttributeName;
  private String ldapAttributeDescriptorCertificateAttributeName;
  private String ldapAuthorityRevocationListAttributeName;
  private String ldapCACertificateAttributeName;
  private String ldapCertificateRevocationListAttributeName;
  private String ldapCrossCertificateAttributeName;
  private String ldapDeltaRevocationListAttributeName;
  private String ldapURL;
  private String ldapUserCertificateAttributeName;
  private String searchForSerialNumberIn;
  private String userCertificateAttribute;
  private String userCertificateSubjectAttributeName;

  private X509LDAPCertStoreParameters(Builder paramBuilder)
  {
    this.ldapURL = paramBuilder.ldapURL;
    this.baseDN = paramBuilder.baseDN;
    this.userCertificateAttribute = paramBuilder.userCertificateAttribute;
    this.cACertificateAttribute = paramBuilder.cACertificateAttribute;
    this.crossCertificateAttribute = paramBuilder.crossCertificateAttribute;
    this.certificateRevocationListAttribute = paramBuilder.certificateRevocationListAttribute;
    this.deltaRevocationListAttribute = paramBuilder.deltaRevocationListAttribute;
    this.authorityRevocationListAttribute = paramBuilder.authorityRevocationListAttribute;
    this.attributeCertificateAttributeAttribute = paramBuilder.attributeCertificateAttributeAttribute;
    this.aACertificateAttribute = paramBuilder.aACertificateAttribute;
    this.attributeDescriptorCertificateAttribute = paramBuilder.attributeDescriptorCertificateAttribute;
    this.attributeCertificateRevocationListAttribute = paramBuilder.attributeCertificateRevocationListAttribute;
    this.attributeAuthorityRevocationListAttribute = paramBuilder.attributeAuthorityRevocationListAttribute;
    this.ldapUserCertificateAttributeName = paramBuilder.ldapUserCertificateAttributeName;
    this.ldapCACertificateAttributeName = paramBuilder.ldapCACertificateAttributeName;
    this.ldapCrossCertificateAttributeName = paramBuilder.ldapCrossCertificateAttributeName;
    this.ldapCertificateRevocationListAttributeName = paramBuilder.ldapCertificateRevocationListAttributeName;
    this.ldapDeltaRevocationListAttributeName = paramBuilder.ldapDeltaRevocationListAttributeName;
    this.ldapAuthorityRevocationListAttributeName = paramBuilder.ldapAuthorityRevocationListAttributeName;
    this.ldapAttributeCertificateAttributeAttributeName = paramBuilder.ldapAttributeCertificateAttributeAttributeName;
    this.ldapAACertificateAttributeName = paramBuilder.ldapAACertificateAttributeName;
    this.ldapAttributeDescriptorCertificateAttributeName = paramBuilder.ldapAttributeDescriptorCertificateAttributeName;
    this.ldapAttributeCertificateRevocationListAttributeName = paramBuilder.ldapAttributeCertificateRevocationListAttributeName;
    this.ldapAttributeAuthorityRevocationListAttributeName = paramBuilder.ldapAttributeAuthorityRevocationListAttributeName;
    this.userCertificateSubjectAttributeName = paramBuilder.userCertificateSubjectAttributeName;
    this.cACertificateSubjectAttributeName = paramBuilder.cACertificateSubjectAttributeName;
    this.crossCertificateSubjectAttributeName = paramBuilder.crossCertificateSubjectAttributeName;
    this.certificateRevocationListIssuerAttributeName = paramBuilder.certificateRevocationListIssuerAttributeName;
    this.deltaRevocationListIssuerAttributeName = paramBuilder.deltaRevocationListIssuerAttributeName;
    this.authorityRevocationListIssuerAttributeName = paramBuilder.authorityRevocationListIssuerAttributeName;
    this.attributeCertificateAttributeSubjectAttributeName = paramBuilder.attributeCertificateAttributeSubjectAttributeName;
    this.aACertificateSubjectAttributeName = paramBuilder.aACertificateSubjectAttributeName;
    this.attributeDescriptorCertificateSubjectAttributeName = paramBuilder.attributeDescriptorCertificateSubjectAttributeName;
    this.attributeCertificateRevocationListIssuerAttributeName = paramBuilder.attributeCertificateRevocationListIssuerAttributeName;
    this.attributeAuthorityRevocationListIssuerAttributeName = paramBuilder.attributeAuthorityRevocationListIssuerAttributeName;
    this.searchForSerialNumberIn = paramBuilder.searchForSerialNumberIn;
  }

  private int addHashCode(int paramInt, Object paramObject)
  {
    int i = paramInt * 29;
    if (paramObject == null);
    for (int j = 0; ; j = paramObject.hashCode())
      return j + i;
  }

  private boolean checkField(Object paramObject1, Object paramObject2)
  {
    if (paramObject1 == paramObject2)
      return true;
    if (paramObject1 == null)
      return false;
    return paramObject1.equals(paramObject2);
  }

  public static X509LDAPCertStoreParameters getInstance(LDAPCertStoreParameters paramLDAPCertStoreParameters)
  {
    return new Builder("ldap://" + paramLDAPCertStoreParameters.getServerName() + ":" + paramLDAPCertStoreParameters.getPort(), "").build();
  }

  public Object clone()
  {
    return this;
  }

  public boolean equal(Object paramObject)
  {
    if (paramObject == this);
    X509LDAPCertStoreParameters localX509LDAPCertStoreParameters;
    do
    {
      return true;
      if (!(paramObject instanceof X509LDAPCertStoreParameters))
        return false;
      localX509LDAPCertStoreParameters = (X509LDAPCertStoreParameters)paramObject;
    }
    while ((checkField(this.ldapURL, localX509LDAPCertStoreParameters.ldapURL)) && (checkField(this.baseDN, localX509LDAPCertStoreParameters.baseDN)) && (checkField(this.userCertificateAttribute, localX509LDAPCertStoreParameters.userCertificateAttribute)) && (checkField(this.cACertificateAttribute, localX509LDAPCertStoreParameters.cACertificateAttribute)) && (checkField(this.crossCertificateAttribute, localX509LDAPCertStoreParameters.crossCertificateAttribute)) && (checkField(this.certificateRevocationListAttribute, localX509LDAPCertStoreParameters.certificateRevocationListAttribute)) && (checkField(this.deltaRevocationListAttribute, localX509LDAPCertStoreParameters.deltaRevocationListAttribute)) && (checkField(this.authorityRevocationListAttribute, localX509LDAPCertStoreParameters.authorityRevocationListAttribute)) && (checkField(this.attributeCertificateAttributeAttribute, localX509LDAPCertStoreParameters.attributeCertificateAttributeAttribute)) && (checkField(this.aACertificateAttribute, localX509LDAPCertStoreParameters.aACertificateAttribute)) && (checkField(this.attributeDescriptorCertificateAttribute, localX509LDAPCertStoreParameters.attributeDescriptorCertificateAttribute)) && (checkField(this.attributeCertificateRevocationListAttribute, localX509LDAPCertStoreParameters.attributeCertificateRevocationListAttribute)) && (checkField(this.attributeAuthorityRevocationListAttribute, localX509LDAPCertStoreParameters.attributeAuthorityRevocationListAttribute)) && (checkField(this.ldapUserCertificateAttributeName, localX509LDAPCertStoreParameters.ldapUserCertificateAttributeName)) && (checkField(this.ldapCACertificateAttributeName, localX509LDAPCertStoreParameters.ldapCACertificateAttributeName)) && (checkField(this.ldapCrossCertificateAttributeName, localX509LDAPCertStoreParameters.ldapCrossCertificateAttributeName)) && (checkField(this.ldapCertificateRevocationListAttributeName, localX509LDAPCertStoreParameters.ldapCertificateRevocationListAttributeName)) && (checkField(this.ldapDeltaRevocationListAttributeName, localX509LDAPCertStoreParameters.ldapDeltaRevocationListAttributeName)) && (checkField(this.ldapAuthorityRevocationListAttributeName, localX509LDAPCertStoreParameters.ldapAuthorityRevocationListAttributeName)) && (checkField(this.ldapAttributeCertificateAttributeAttributeName, localX509LDAPCertStoreParameters.ldapAttributeCertificateAttributeAttributeName)) && (checkField(this.ldapAACertificateAttributeName, localX509LDAPCertStoreParameters.ldapAACertificateAttributeName)) && (checkField(this.ldapAttributeDescriptorCertificateAttributeName, localX509LDAPCertStoreParameters.ldapAttributeDescriptorCertificateAttributeName)) && (checkField(this.ldapAttributeCertificateRevocationListAttributeName, localX509LDAPCertStoreParameters.ldapAttributeCertificateRevocationListAttributeName)) && (checkField(this.ldapAttributeAuthorityRevocationListAttributeName, localX509LDAPCertStoreParameters.ldapAttributeAuthorityRevocationListAttributeName)) && (checkField(this.userCertificateSubjectAttributeName, localX509LDAPCertStoreParameters.userCertificateSubjectAttributeName)) && (checkField(this.cACertificateSubjectAttributeName, localX509LDAPCertStoreParameters.cACertificateSubjectAttributeName)) && (checkField(this.crossCertificateSubjectAttributeName, localX509LDAPCertStoreParameters.crossCertificateSubjectAttributeName)) && (checkField(this.certificateRevocationListIssuerAttributeName, localX509LDAPCertStoreParameters.certificateRevocationListIssuerAttributeName)) && (checkField(this.deltaRevocationListIssuerAttributeName, localX509LDAPCertStoreParameters.deltaRevocationListIssuerAttributeName)) && (checkField(this.authorityRevocationListIssuerAttributeName, localX509LDAPCertStoreParameters.authorityRevocationListIssuerAttributeName)) && (checkField(this.attributeCertificateAttributeSubjectAttributeName, localX509LDAPCertStoreParameters.attributeCertificateAttributeSubjectAttributeName)) && (checkField(this.aACertificateSubjectAttributeName, localX509LDAPCertStoreParameters.aACertificateSubjectAttributeName)) && (checkField(this.attributeDescriptorCertificateSubjectAttributeName, localX509LDAPCertStoreParameters.attributeDescriptorCertificateSubjectAttributeName)) && (checkField(this.attributeCertificateRevocationListIssuerAttributeName, localX509LDAPCertStoreParameters.attributeCertificateRevocationListIssuerAttributeName)) && (checkField(this.attributeAuthorityRevocationListIssuerAttributeName, localX509LDAPCertStoreParameters.attributeAuthorityRevocationListIssuerAttributeName)) && (checkField(this.searchForSerialNumberIn, localX509LDAPCertStoreParameters.searchForSerialNumberIn)));
    return false;
  }

  public String getAACertificateAttribute()
  {
    return this.aACertificateAttribute;
  }

  public String getAACertificateSubjectAttributeName()
  {
    return this.aACertificateSubjectAttributeName;
  }

  public String getAttributeAuthorityRevocationListAttribute()
  {
    return this.attributeAuthorityRevocationListAttribute;
  }

  public String getAttributeAuthorityRevocationListIssuerAttributeName()
  {
    return this.attributeAuthorityRevocationListIssuerAttributeName;
  }

  public String getAttributeCertificateAttributeAttribute()
  {
    return this.attributeCertificateAttributeAttribute;
  }

  public String getAttributeCertificateAttributeSubjectAttributeName()
  {
    return this.attributeCertificateAttributeSubjectAttributeName;
  }

  public String getAttributeCertificateRevocationListAttribute()
  {
    return this.attributeCertificateRevocationListAttribute;
  }

  public String getAttributeCertificateRevocationListIssuerAttributeName()
  {
    return this.attributeCertificateRevocationListIssuerAttributeName;
  }

  public String getAttributeDescriptorCertificateAttribute()
  {
    return this.attributeDescriptorCertificateAttribute;
  }

  public String getAttributeDescriptorCertificateSubjectAttributeName()
  {
    return this.attributeDescriptorCertificateSubjectAttributeName;
  }

  public String getAuthorityRevocationListAttribute()
  {
    return this.authorityRevocationListAttribute;
  }

  public String getAuthorityRevocationListIssuerAttributeName()
  {
    return this.authorityRevocationListIssuerAttributeName;
  }

  public String getBaseDN()
  {
    return this.baseDN;
  }

  public String getCACertificateAttribute()
  {
    return this.cACertificateAttribute;
  }

  public String getCACertificateSubjectAttributeName()
  {
    return this.cACertificateSubjectAttributeName;
  }

  public String getCertificateRevocationListAttribute()
  {
    return this.certificateRevocationListAttribute;
  }

  public String getCertificateRevocationListIssuerAttributeName()
  {
    return this.certificateRevocationListIssuerAttributeName;
  }

  public String getCrossCertificateAttribute()
  {
    return this.crossCertificateAttribute;
  }

  public String getCrossCertificateSubjectAttributeName()
  {
    return this.crossCertificateSubjectAttributeName;
  }

  public String getDeltaRevocationListAttribute()
  {
    return this.deltaRevocationListAttribute;
  }

  public String getDeltaRevocationListIssuerAttributeName()
  {
    return this.deltaRevocationListIssuerAttributeName;
  }

  public String getLdapAACertificateAttributeName()
  {
    return this.ldapAACertificateAttributeName;
  }

  public String getLdapAttributeAuthorityRevocationListAttributeName()
  {
    return this.ldapAttributeAuthorityRevocationListAttributeName;
  }

  public String getLdapAttributeCertificateAttributeAttributeName()
  {
    return this.ldapAttributeCertificateAttributeAttributeName;
  }

  public String getLdapAttributeCertificateRevocationListAttributeName()
  {
    return this.ldapAttributeCertificateRevocationListAttributeName;
  }

  public String getLdapAttributeDescriptorCertificateAttributeName()
  {
    return this.ldapAttributeDescriptorCertificateAttributeName;
  }

  public String getLdapAuthorityRevocationListAttributeName()
  {
    return this.ldapAuthorityRevocationListAttributeName;
  }

  public String getLdapCACertificateAttributeName()
  {
    return this.ldapCACertificateAttributeName;
  }

  public String getLdapCertificateRevocationListAttributeName()
  {
    return this.ldapCertificateRevocationListAttributeName;
  }

  public String getLdapCrossCertificateAttributeName()
  {
    return this.ldapCrossCertificateAttributeName;
  }

  public String getLdapDeltaRevocationListAttributeName()
  {
    return this.ldapDeltaRevocationListAttributeName;
  }

  public String getLdapURL()
  {
    return this.ldapURL;
  }

  public String getLdapUserCertificateAttributeName()
  {
    return this.ldapUserCertificateAttributeName;
  }

  public String getSearchForSerialNumberIn()
  {
    return this.searchForSerialNumberIn;
  }

  public String getUserCertificateAttribute()
  {
    return this.userCertificateAttribute;
  }

  public String getUserCertificateSubjectAttributeName()
  {
    return this.userCertificateSubjectAttributeName;
  }

  public int hashCode()
  {
    return addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(addHashCode(0, this.userCertificateAttribute), this.cACertificateAttribute), this.crossCertificateAttribute), this.certificateRevocationListAttribute), this.deltaRevocationListAttribute), this.authorityRevocationListAttribute), this.attributeCertificateAttributeAttribute), this.aACertificateAttribute), this.attributeDescriptorCertificateAttribute), this.attributeCertificateRevocationListAttribute), this.attributeAuthorityRevocationListAttribute), this.ldapUserCertificateAttributeName), this.ldapCACertificateAttributeName), this.ldapCrossCertificateAttributeName), this.ldapCertificateRevocationListAttributeName), this.ldapDeltaRevocationListAttributeName), this.ldapAuthorityRevocationListAttributeName), this.ldapAttributeCertificateAttributeAttributeName), this.ldapAACertificateAttributeName), this.ldapAttributeDescriptorCertificateAttributeName), this.ldapAttributeCertificateRevocationListAttributeName), this.ldapAttributeAuthorityRevocationListAttributeName), this.userCertificateSubjectAttributeName), this.cACertificateSubjectAttributeName), this.crossCertificateSubjectAttributeName), this.certificateRevocationListIssuerAttributeName), this.deltaRevocationListIssuerAttributeName), this.authorityRevocationListIssuerAttributeName), this.attributeCertificateAttributeSubjectAttributeName), this.aACertificateSubjectAttributeName), this.attributeDescriptorCertificateSubjectAttributeName), this.attributeCertificateRevocationListIssuerAttributeName), this.attributeAuthorityRevocationListIssuerAttributeName), this.searchForSerialNumberIn);
  }

  public static class Builder
  {
    private String aACertificateAttribute;
    private String aACertificateSubjectAttributeName;
    private String attributeAuthorityRevocationListAttribute;
    private String attributeAuthorityRevocationListIssuerAttributeName;
    private String attributeCertificateAttributeAttribute;
    private String attributeCertificateAttributeSubjectAttributeName;
    private String attributeCertificateRevocationListAttribute;
    private String attributeCertificateRevocationListIssuerAttributeName;
    private String attributeDescriptorCertificateAttribute;
    private String attributeDescriptorCertificateSubjectAttributeName;
    private String authorityRevocationListAttribute;
    private String authorityRevocationListIssuerAttributeName;
    private String baseDN;
    private String cACertificateAttribute;
    private String cACertificateSubjectAttributeName;
    private String certificateRevocationListAttribute;
    private String certificateRevocationListIssuerAttributeName;
    private String crossCertificateAttribute;
    private String crossCertificateSubjectAttributeName;
    private String deltaRevocationListAttribute;
    private String deltaRevocationListIssuerAttributeName;
    private String ldapAACertificateAttributeName;
    private String ldapAttributeAuthorityRevocationListAttributeName;
    private String ldapAttributeCertificateAttributeAttributeName;
    private String ldapAttributeCertificateRevocationListAttributeName;
    private String ldapAttributeDescriptorCertificateAttributeName;
    private String ldapAuthorityRevocationListAttributeName;
    private String ldapCACertificateAttributeName;
    private String ldapCertificateRevocationListAttributeName;
    private String ldapCrossCertificateAttributeName;
    private String ldapDeltaRevocationListAttributeName;
    private String ldapURL;
    private String ldapUserCertificateAttributeName;
    private String searchForSerialNumberIn;
    private String userCertificateAttribute;
    private String userCertificateSubjectAttributeName;

    public Builder()
    {
      this("ldap://localhost:389", "");
    }

    public Builder(String paramString1, String paramString2)
    {
      this.ldapURL = paramString1;
      if (paramString2 == null);
      for (this.baseDN = ""; ; this.baseDN = paramString2)
      {
        this.userCertificateAttribute = "userCertificate";
        this.cACertificateAttribute = "cACertificate";
        this.crossCertificateAttribute = "crossCertificatePair";
        this.certificateRevocationListAttribute = "certificateRevocationList";
        this.deltaRevocationListAttribute = "deltaRevocationList";
        this.authorityRevocationListAttribute = "authorityRevocationList";
        this.attributeCertificateAttributeAttribute = "attributeCertificateAttribute";
        this.aACertificateAttribute = "aACertificate";
        this.attributeDescriptorCertificateAttribute = "attributeDescriptorCertificate";
        this.attributeCertificateRevocationListAttribute = "attributeCertificateRevocationList";
        this.attributeAuthorityRevocationListAttribute = "attributeAuthorityRevocationList";
        this.ldapUserCertificateAttributeName = "cn";
        this.ldapCACertificateAttributeName = "cn ou o";
        this.ldapCrossCertificateAttributeName = "cn ou o";
        this.ldapCertificateRevocationListAttributeName = "cn ou o";
        this.ldapDeltaRevocationListAttributeName = "cn ou o";
        this.ldapAuthorityRevocationListAttributeName = "cn ou o";
        this.ldapAttributeCertificateAttributeAttributeName = "cn";
        this.ldapAACertificateAttributeName = "cn o ou";
        this.ldapAttributeDescriptorCertificateAttributeName = "cn o ou";
        this.ldapAttributeCertificateRevocationListAttributeName = "cn o ou";
        this.ldapAttributeAuthorityRevocationListAttributeName = "cn o ou";
        this.userCertificateSubjectAttributeName = "cn";
        this.cACertificateSubjectAttributeName = "o ou";
        this.crossCertificateSubjectAttributeName = "o ou";
        this.certificateRevocationListIssuerAttributeName = "o ou";
        this.deltaRevocationListIssuerAttributeName = "o ou";
        this.authorityRevocationListIssuerAttributeName = "o ou";
        this.attributeCertificateAttributeSubjectAttributeName = "cn";
        this.aACertificateSubjectAttributeName = "o ou";
        this.attributeDescriptorCertificateSubjectAttributeName = "o ou";
        this.attributeCertificateRevocationListIssuerAttributeName = "o ou";
        this.attributeAuthorityRevocationListIssuerAttributeName = "o ou";
        this.searchForSerialNumberIn = "uid serialNumber cn";
        return;
      }
    }

    public X509LDAPCertStoreParameters build()
    {
      if ((this.ldapUserCertificateAttributeName == null) || (this.ldapCACertificateAttributeName == null) || (this.ldapCrossCertificateAttributeName == null) || (this.ldapCertificateRevocationListAttributeName == null) || (this.ldapDeltaRevocationListAttributeName == null) || (this.ldapAuthorityRevocationListAttributeName == null) || (this.ldapAttributeCertificateAttributeAttributeName == null) || (this.ldapAACertificateAttributeName == null) || (this.ldapAttributeDescriptorCertificateAttributeName == null) || (this.ldapAttributeCertificateRevocationListAttributeName == null) || (this.ldapAttributeAuthorityRevocationListAttributeName == null) || (this.userCertificateSubjectAttributeName == null) || (this.cACertificateSubjectAttributeName == null) || (this.crossCertificateSubjectAttributeName == null) || (this.certificateRevocationListIssuerAttributeName == null) || (this.deltaRevocationListIssuerAttributeName == null) || (this.authorityRevocationListIssuerAttributeName == null) || (this.attributeCertificateAttributeSubjectAttributeName == null) || (this.aACertificateSubjectAttributeName == null) || (this.attributeDescriptorCertificateSubjectAttributeName == null) || (this.attributeCertificateRevocationListIssuerAttributeName == null) || (this.attributeAuthorityRevocationListIssuerAttributeName == null))
        throw new IllegalArgumentException("Necessary parameters not specified.");
      return new X509LDAPCertStoreParameters(this, null);
    }

    public Builder setAACertificateAttribute(String paramString)
    {
      this.aACertificateAttribute = paramString;
      return this;
    }

    public Builder setAACertificateSubjectAttributeName(String paramString)
    {
      this.aACertificateSubjectAttributeName = paramString;
      return this;
    }

    public Builder setAttributeAuthorityRevocationListAttribute(String paramString)
    {
      this.attributeAuthorityRevocationListAttribute = paramString;
      return this;
    }

    public Builder setAttributeAuthorityRevocationListIssuerAttributeName(String paramString)
    {
      this.attributeAuthorityRevocationListIssuerAttributeName = paramString;
      return this;
    }

    public Builder setAttributeCertificateAttributeAttribute(String paramString)
    {
      this.attributeCertificateAttributeAttribute = paramString;
      return this;
    }

    public Builder setAttributeCertificateAttributeSubjectAttributeName(String paramString)
    {
      this.attributeCertificateAttributeSubjectAttributeName = paramString;
      return this;
    }

    public Builder setAttributeCertificateRevocationListAttribute(String paramString)
    {
      this.attributeCertificateRevocationListAttribute = paramString;
      return this;
    }

    public Builder setAttributeCertificateRevocationListIssuerAttributeName(String paramString)
    {
      this.attributeCertificateRevocationListIssuerAttributeName = paramString;
      return this;
    }

    public Builder setAttributeDescriptorCertificateAttribute(String paramString)
    {
      this.attributeDescriptorCertificateAttribute = paramString;
      return this;
    }

    public Builder setAttributeDescriptorCertificateSubjectAttributeName(String paramString)
    {
      this.attributeDescriptorCertificateSubjectAttributeName = paramString;
      return this;
    }

    public Builder setAuthorityRevocationListAttribute(String paramString)
    {
      this.authorityRevocationListAttribute = paramString;
      return this;
    }

    public Builder setAuthorityRevocationListIssuerAttributeName(String paramString)
    {
      this.authorityRevocationListIssuerAttributeName = paramString;
      return this;
    }

    public Builder setCACertificateAttribute(String paramString)
    {
      this.cACertificateAttribute = paramString;
      return this;
    }

    public Builder setCACertificateSubjectAttributeName(String paramString)
    {
      this.cACertificateSubjectAttributeName = paramString;
      return this;
    }

    public Builder setCertificateRevocationListAttribute(String paramString)
    {
      this.certificateRevocationListAttribute = paramString;
      return this;
    }

    public Builder setCertificateRevocationListIssuerAttributeName(String paramString)
    {
      this.certificateRevocationListIssuerAttributeName = paramString;
      return this;
    }

    public Builder setCrossCertificateAttribute(String paramString)
    {
      this.crossCertificateAttribute = paramString;
      return this;
    }

    public Builder setCrossCertificateSubjectAttributeName(String paramString)
    {
      this.crossCertificateSubjectAttributeName = paramString;
      return this;
    }

    public Builder setDeltaRevocationListAttribute(String paramString)
    {
      this.deltaRevocationListAttribute = paramString;
      return this;
    }

    public Builder setDeltaRevocationListIssuerAttributeName(String paramString)
    {
      this.deltaRevocationListIssuerAttributeName = paramString;
      return this;
    }

    public Builder setLdapAACertificateAttributeName(String paramString)
    {
      this.ldapAACertificateAttributeName = paramString;
      return this;
    }

    public Builder setLdapAttributeAuthorityRevocationListAttributeName(String paramString)
    {
      this.ldapAttributeAuthorityRevocationListAttributeName = paramString;
      return this;
    }

    public Builder setLdapAttributeCertificateAttributeAttributeName(String paramString)
    {
      this.ldapAttributeCertificateAttributeAttributeName = paramString;
      return this;
    }

    public Builder setLdapAttributeCertificateRevocationListAttributeName(String paramString)
    {
      this.ldapAttributeCertificateRevocationListAttributeName = paramString;
      return this;
    }

    public Builder setLdapAttributeDescriptorCertificateAttributeName(String paramString)
    {
      this.ldapAttributeDescriptorCertificateAttributeName = paramString;
      return this;
    }

    public Builder setLdapAuthorityRevocationListAttributeName(String paramString)
    {
      this.ldapAuthorityRevocationListAttributeName = paramString;
      return this;
    }

    public Builder setLdapCACertificateAttributeName(String paramString)
    {
      this.ldapCACertificateAttributeName = paramString;
      return this;
    }

    public Builder setLdapCertificateRevocationListAttributeName(String paramString)
    {
      this.ldapCertificateRevocationListAttributeName = paramString;
      return this;
    }

    public Builder setLdapCrossCertificateAttributeName(String paramString)
    {
      this.ldapCrossCertificateAttributeName = paramString;
      return this;
    }

    public Builder setLdapDeltaRevocationListAttributeName(String paramString)
    {
      this.ldapDeltaRevocationListAttributeName = paramString;
      return this;
    }

    public Builder setLdapUserCertificateAttributeName(String paramString)
    {
      this.ldapUserCertificateAttributeName = paramString;
      return this;
    }

    public Builder setSearchForSerialNumberIn(String paramString)
    {
      this.searchForSerialNumberIn = paramString;
      return this;
    }

    public Builder setUserCertificateAttribute(String paramString)
    {
      this.userCertificateAttribute = paramString;
      return this;
    }

    public Builder setUserCertificateSubjectAttributeName(String paramString)
    {
      this.userCertificateSubjectAttributeName = paramString;
      return this;
    }
  }
}