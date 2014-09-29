package org.bouncycastle2.jce;

import java.security.BasicPermission;
import java.security.Permission;
import java.util.StringTokenizer;
import org.bouncycastle2.util.Strings;

public class ProviderConfigurationPermission extends BasicPermission
{
  private static final int ALL = 3;
  private static final String ALL_STR = "all";
  private static final int EC_IMPLICITLY_CA = 2;
  private static final String EC_IMPLICITLY_CA_STR = "ecimplicitlyca";
  private static final int THREAD_LOCAL_EC_IMPLICITLY_CA = 1;
  private static final String THREAD_LOCAL_EC_IMPLICITLY_CA_STR = "threadlocalecimplicitlyca";
  private final String actions;
  private final int permissionMask;

  public ProviderConfigurationPermission(String paramString)
  {
    super(paramString);
    this.actions = "all";
    this.permissionMask = 3;
  }

  public ProviderConfigurationPermission(String paramString1, String paramString2)
  {
    super(paramString1, paramString2);
    this.actions = paramString2;
    this.permissionMask = calculateMask(paramString2);
  }

  private int calculateMask(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(Strings.toLowerCase(paramString), " ,");
    int i = 0;
    while (true)
    {
      if (!localStringTokenizer.hasMoreTokens())
      {
        if (i != 0)
          break;
        throw new IllegalArgumentException("unknown permissions passed to mask");
      }
      String str = localStringTokenizer.nextToken();
      if (str.equals("threadlocalecimplicitlyca"))
        i |= 1;
      else if (str.equals("ecimplicitlyca"))
        i |= 2;
      else if (str.equals("all"))
        i |= 3;
    }
    return i;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    ProviderConfigurationPermission localProviderConfigurationPermission;
    do
    {
      return true;
      if (!(paramObject instanceof ProviderConfigurationPermission))
        break;
      localProviderConfigurationPermission = (ProviderConfigurationPermission)paramObject;
    }
    while ((this.permissionMask == localProviderConfigurationPermission.permissionMask) && (getName().equals(localProviderConfigurationPermission.getName())));
    return false;
    return false;
  }

  public String getActions()
  {
    return this.actions;
  }

  public int hashCode()
  {
    return getName().hashCode() + this.permissionMask;
  }

  public boolean implies(Permission paramPermission)
  {
    if (!(paramPermission instanceof ProviderConfigurationPermission));
    ProviderConfigurationPermission localProviderConfigurationPermission;
    do
    {
      do
        return false;
      while (!getName().equals(paramPermission.getName()));
      localProviderConfigurationPermission = (ProviderConfigurationPermission)paramPermission;
    }
    while ((this.permissionMask & localProviderConfigurationPermission.permissionMask) != localProviderConfigurationPermission.permissionMask);
    return true;
  }
}