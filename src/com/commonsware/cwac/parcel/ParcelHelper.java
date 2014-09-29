package com.commonsware.cwac.parcel;

import android.content.Context;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ParcelHelper
{
  private static final int CACHE_SIZE = 101;
  private Class arrr = null;
  private Map<String, Integer> cache = null;
  private String id = null;

  public ParcelHelper(String paramString, Context paramContext)
  {
    this.id = paramString.replace('.', '_').replace('-', '_');
    try
    {
      this.arrr = Class.forName(paramContext.getPackageName() + ".R");
      this.cache = Collections.synchronizedMap(new Cache());
      return;
    }
    catch (Throwable localThrowable)
    {
      throw new RuntimeException("Exception finding R class", localThrowable);
    }
  }

  private Class getResourceClass(String paramString)
  {
    for (Class localClass : this.arrr.getClasses())
      if (paramString.equals(localClass.getSimpleName()))
        return localClass;
    return null;
  }

  public int getDrawableId(String paramString)
  {
    return getIdentifier(paramString, "drawable", true);
  }

  public int getIdentifier(String paramString1, String paramString2)
  {
    return getIdentifier(paramString1, paramString2, true);
  }

  public int getIdentifier(String paramString1, String paramString2, boolean paramBoolean)
  {
    int i = -1;
    StringBuilder localStringBuilder1 = new StringBuilder(paramString1);
    localStringBuilder1.append('|');
    localStringBuilder1.append(paramString2);
    Integer localInteger = (Integer)this.cache.get(localStringBuilder1.toString());
    if (localInteger != null)
      return localInteger.intValue();
    if ((!paramString1.startsWith(this.id)) && (paramBoolean))
    {
      StringBuilder localStringBuilder2 = new StringBuilder(this.id);
      localStringBuilder2.append('_');
      localStringBuilder2.append(paramString1);
      paramString1 = localStringBuilder2.toString();
    }
    try
    {
      Class localClass = getResourceClass(paramString2);
      if (localClass != null)
      {
        Field localField = localClass.getDeclaredField(paramString1);
        if (localField != null)
        {
          i = localField.getInt(localClass);
          this.cache.put(localStringBuilder1.toString(), Integer.valueOf(i));
        }
      }
      return i;
    }
    catch (Throwable localThrowable)
    {
      throw new RuntimeException("Exception finding resource identifier", localThrowable);
    }
  }

  public int getItemId(String paramString)
  {
    return getIdentifier(paramString, "id", false);
  }

  public int getLayoutId(String paramString)
  {
    return getIdentifier(paramString, "layout", true);
  }

  public int getMenuId(String paramString)
  {
    return getIdentifier(paramString, "menu", true);
  }

  public int[] getStyleableArray(String paramString)
  {
    try
    {
      Class localClass = getResourceClass("styleable");
      if (localClass != null)
      {
        Field localField = localClass.getDeclaredField(paramString);
        if (localField != null)
        {
          Object localObject = localField.get(localClass);
          if ((localObject instanceof int[]))
          {
            arrayOfInt = new int[Array.getLength(localObject)];
            for (int i = 0; i < Array.getLength(localObject); i++)
              arrayOfInt[i] = Array.getInt(localObject, i);
          }
        }
      }
    }
    catch (Throwable localThrowable)
    {
      throw new RuntimeException("Exception finding styleable", localThrowable);
    }
    int[] arrayOfInt = new int[0];
    return arrayOfInt;
  }

  public int getStyleableId(String paramString1, String paramString2)
  {
    return getIdentifier(paramString1 + "_" + paramString2, "styleable", false);
  }

  public class Cache extends LinkedHashMap<String, Integer>
  {
    public Cache()
    {
      super(1.1F, true);
    }

    protected boolean removeEldestEntry(Map.Entry paramEntry)
    {
      return size() > 101;
    }
  }
}