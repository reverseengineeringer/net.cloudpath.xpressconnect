package org.bouncycastle2.jce.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import org.bouncycastle2.jce.ProviderConfigurationPermission;
import org.bouncycastle2.jce.provider.asymmetric.ec.EC5Util;

public class ProviderUtil
{
  private static Permission BC_EC_LOCAL_PERMISSION = new ProviderConfigurationPermission(BouncyCastleProvider.PROVIDER_NAME, "threadLocalEcImplicitlyCa");
  private static Permission BC_EC_PERMISSION = new ProviderConfigurationPermission(BouncyCastleProvider.PROVIDER_NAME, "ecImplicitlyCa");
  private static final long MAX_MEMORY = Runtime.getRuntime().maxMemory();
  private static volatile org.bouncycastle2.jce.spec.ECParameterSpec ecImplicitCaParams;
  private static ThreadLocal threadSpec = new ThreadLocal();

  public static org.bouncycastle2.jce.spec.ECParameterSpec getEcImplicitlyCa()
  {
    org.bouncycastle2.jce.spec.ECParameterSpec localECParameterSpec = (org.bouncycastle2.jce.spec.ECParameterSpec)threadSpec.get();
    if (localECParameterSpec != null)
      return localECParameterSpec;
    return ecImplicitCaParams;
  }

  static int getReadLimit(InputStream paramInputStream)
    throws IOException
  {
    if ((paramInputStream instanceof ByteArrayInputStream))
      return paramInputStream.available();
    if (MAX_MEMORY > 2147483647L)
      return 2147483647;
    return (int)MAX_MEMORY;
  }

  static void setParameter(String paramString, Object paramObject)
  {
    SecurityManager localSecurityManager = System.getSecurityManager();
    if (paramString.equals("threadLocalEcImplicitlyCa"))
    {
      if (localSecurityManager != null)
        localSecurityManager.checkPermission(BC_EC_LOCAL_PERMISSION);
      if (((paramObject instanceof org.bouncycastle2.jce.spec.ECParameterSpec)) || (paramObject == null))
      {
        localECParameterSpec = (org.bouncycastle2.jce.spec.ECParameterSpec)paramObject;
        if (localECParameterSpec != null)
          break label63;
        threadSpec.remove();
      }
    }
    label63: 
    while (!paramString.equals("ecImplicitlyCa"))
    {
      org.bouncycastle2.jce.spec.ECParameterSpec localECParameterSpec;
      while (true)
      {
        return;
        localECParameterSpec = EC5Util.convertSpec((java.security.spec.ECParameterSpec)paramObject, false);
      }
      threadSpec.set(localECParameterSpec);
      return;
    }
    if (localSecurityManager != null)
      localSecurityManager.checkPermission(BC_EC_PERMISSION);
    if (((paramObject instanceof org.bouncycastle2.jce.spec.ECParameterSpec)) || (paramObject == null))
    {
      ecImplicitCaParams = (org.bouncycastle2.jce.spec.ECParameterSpec)paramObject;
      return;
    }
    ecImplicitCaParams = EC5Util.convertSpec((java.security.spec.ECParameterSpec)paramObject, false);
  }
}