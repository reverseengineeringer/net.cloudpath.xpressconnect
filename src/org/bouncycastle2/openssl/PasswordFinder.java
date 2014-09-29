package org.bouncycastle2.openssl;

public abstract interface PasswordFinder
{
  public abstract char[] getPassword();
}