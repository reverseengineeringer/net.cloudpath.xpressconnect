package org.bouncycastle2.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionStore
  implements Store
{
  private Collection _local;

  public CollectionStore(Collection paramCollection)
  {
    this._local = new ArrayList(paramCollection);
  }

  public Collection getMatches(Selector paramSelector)
  {
    ArrayList localArrayList;
    if (paramSelector == null)
      localArrayList = new ArrayList(this._local);
    while (true)
    {
      return localArrayList;
      localArrayList = new ArrayList();
      Iterator localIterator = this._local.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        if (paramSelector.match(localObject))
          localArrayList.add(localObject);
      }
    }
  }
}