package org.bouncycastle2.util;

import java.util.Collection;

public abstract interface Store
{
  public abstract Collection getMatches(Selector paramSelector)
    throws StoreException;
}