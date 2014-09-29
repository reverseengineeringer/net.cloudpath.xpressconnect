package org.bouncycastle2.i18n;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;
import org.bouncycastle2.i18n.filter.Filter;
import org.bouncycastle2.i18n.filter.TrustedInput;
import org.bouncycastle2.i18n.filter.UntrustedInput;
import org.bouncycastle2.i18n.filter.UntrustedUrlInput;

public class LocalizedMessage
{
  public static final String DEFAULT_ENCODING = "ISO-8859-1";
  protected FilteredArguments arguments;
  protected String encoding = "ISO-8859-1";
  protected FilteredArguments extraArgs = null;
  protected Filter filter = null;
  protected final String id;
  protected ClassLoader loader = null;
  protected final String resource;

  public LocalizedMessage(String paramString1, String paramString2)
    throws NullPointerException
  {
    if ((paramString1 == null) || (paramString2 == null))
      throw new NullPointerException();
    this.id = paramString2;
    this.resource = paramString1;
    this.arguments = new FilteredArguments();
  }

  public LocalizedMessage(String paramString1, String paramString2, String paramString3)
    throws NullPointerException, UnsupportedEncodingException
  {
    if ((paramString1 == null) || (paramString2 == null))
      throw new NullPointerException();
    this.id = paramString2;
    this.resource = paramString1;
    this.arguments = new FilteredArguments();
    if (!Charset.isSupported(paramString3))
      throw new UnsupportedEncodingException("The encoding \"" + paramString3 + "\" is not supported.");
    this.encoding = paramString3;
  }

  public LocalizedMessage(String paramString1, String paramString2, String paramString3, Object[] paramArrayOfObject)
    throws NullPointerException, UnsupportedEncodingException
  {
    if ((paramString1 == null) || (paramString2 == null) || (paramArrayOfObject == null))
      throw new NullPointerException();
    this.id = paramString2;
    this.resource = paramString1;
    this.arguments = new FilteredArguments(paramArrayOfObject);
    if (!Charset.isSupported(paramString3))
      throw new UnsupportedEncodingException("The encoding \"" + paramString3 + "\" is not supported.");
    this.encoding = paramString3;
  }

  public LocalizedMessage(String paramString1, String paramString2, Object[] paramArrayOfObject)
    throws NullPointerException
  {
    if ((paramString1 == null) || (paramString2 == null) || (paramArrayOfObject == null))
      throw new NullPointerException();
    this.id = paramString2;
    this.resource = paramString1;
    this.arguments = new FilteredArguments(paramArrayOfObject);
  }

  protected String addExtraArgs(String paramString, Locale paramLocale)
  {
    StringBuffer localStringBuffer;
    Object[] arrayOfObject;
    if (this.extraArgs != null)
    {
      localStringBuffer = new StringBuffer(paramString);
      arrayOfObject = this.extraArgs.getFilteredArgs(paramLocale);
    }
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfObject.length)
      {
        paramString = localStringBuffer.toString();
        return paramString;
      }
      localStringBuffer.append(arrayOfObject[i]);
    }
  }

  protected String formatWithTimeZone(String paramString, Object[] paramArrayOfObject, Locale paramLocale, TimeZone paramTimeZone)
  {
    MessageFormat localMessageFormat = new MessageFormat(" ");
    localMessageFormat.setLocale(paramLocale);
    localMessageFormat.applyPattern(paramString);
    Format[] arrayOfFormat;
    if (!paramTimeZone.equals(TimeZone.getDefault()))
      arrayOfFormat = localMessageFormat.getFormats();
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfFormat.length)
        return localMessageFormat.format(paramArrayOfObject);
      if ((arrayOfFormat[i] instanceof DateFormat))
      {
        DateFormat localDateFormat = (DateFormat)arrayOfFormat[i];
        localDateFormat.setTimeZone(paramTimeZone);
        localMessageFormat.setFormat(i, localDateFormat);
      }
    }
  }

  public Object[] getArguments()
  {
    return this.arguments.getArguments();
  }

  public ClassLoader getClassLoader()
  {
    return this.loader;
  }

  public String getEntry(String paramString, Locale paramLocale, TimeZone paramTimeZone)
    throws MissingEntryException
  {
    String str1 = this.id;
    if (paramString != null)
      str1 = str1 + "." + paramString;
    try
    {
      if (this.loader == null);
      ResourceBundle localResourceBundle;
      for (Object localObject = ResourceBundle.getBundle(this.resource, paramLocale); ; localObject = localResourceBundle)
      {
        String str4 = ((ResourceBundle)localObject).getString(str1);
        if (!this.encoding.equals("ISO-8859-1"))
          str4 = new String(str4.getBytes("ISO-8859-1"), this.encoding);
        if (!this.arguments.isEmpty())
          str4 = formatWithTimeZone(str4, this.arguments.getFilteredArgs(paramLocale), paramLocale, paramTimeZone);
        return addExtraArgs(str4, paramLocale);
        localResourceBundle = ResourceBundle.getBundle(this.resource, paramLocale, this.loader);
      }
    }
    catch (MissingResourceException localMissingResourceException)
    {
      String str2 = "Can't find entry " + str1 + " in resource file " + this.resource + ".";
      String str3 = this.resource;
      if (this.loader != null);
      for (ClassLoader localClassLoader = this.loader; ; localClassLoader = getClassLoader())
        throw new MissingEntryException(str2, str3, str1, paramLocale, localClassLoader);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new RuntimeException(localUnsupportedEncodingException);
    }
  }

  public Object[] getExtraArgs()
  {
    if (this.extraArgs == null)
      return null;
    return this.extraArgs.getArguments();
  }

  public Filter getFilter()
  {
    return this.filter;
  }

  public String getId()
  {
    return this.id;
  }

  public String getResource()
  {
    return this.resource;
  }

  public void setClassLoader(ClassLoader paramClassLoader)
  {
    this.loader = paramClassLoader;
  }

  public void setExtraArgument(Object paramObject)
  {
    setExtraArguments(new Object[] { paramObject });
  }

  public void setExtraArguments(Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject != null)
    {
      this.extraArgs = new FilteredArguments(paramArrayOfObject);
      this.extraArgs.setFilter(this.filter);
      return;
    }
    this.extraArgs = null;
  }

  public void setFilter(Filter paramFilter)
  {
    this.arguments.setFilter(paramFilter);
    if (this.extraArgs != null)
      this.extraArgs.setFilter(paramFilter);
    this.filter = paramFilter;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("Resource: \"").append(this.resource);
    localStringBuffer.append("\" Id: \"").append(this.id).append("\"");
    localStringBuffer.append(" Arguments: ").append(this.arguments.getArguments().length).append(" normal");
    if ((this.extraArgs != null) && (this.extraArgs.getArguments().length > 0))
      localStringBuffer.append(", ").append(this.extraArgs.getArguments().length).append(" extra");
    localStringBuffer.append(" Encoding: ").append(this.encoding);
    localStringBuffer.append(" ClassLoader: ").append(this.loader);
    return localStringBuffer.toString();
  }

  protected class FilteredArguments
  {
    protected static final int FILTER = 1;
    protected static final int FILTER_URL = 2;
    protected static final int NO_FILTER;
    protected int[] argFilterType;
    protected Object[] arguments;
    protected Filter filter = null;
    protected Object[] filteredArgs;
    protected boolean[] isLocaleSpecific;
    protected Object[] unpackedArgs;

    FilteredArguments()
    {
      this(new Object[0]);
    }

    FilteredArguments(Object[] arg2)
    {
      Object localObject;
      this.arguments = localObject;
      this.unpackedArgs = new Object[localObject.length];
      this.filteredArgs = new Object[localObject.length];
      this.isLocaleSpecific = new boolean[localObject.length];
      this.argFilterType = new int[localObject.length];
      int i = 0;
      if (i >= localObject.length)
        return;
      if ((localObject[i] instanceof TrustedInput))
      {
        this.unpackedArgs[i] = ((TrustedInput)localObject[i]).getInput();
        this.argFilterType[i] = 0;
      }
      while (true)
      {
        this.isLocaleSpecific[i] = (this.unpackedArgs[i] instanceof LocaleString);
        i++;
        break;
        if ((localObject[i] instanceof UntrustedInput))
        {
          this.unpackedArgs[i] = ((UntrustedInput)localObject[i]).getInput();
          if ((localObject[i] instanceof UntrustedUrlInput))
            this.argFilterType[i] = 2;
          else
            this.argFilterType[i] = 1;
        }
        else
        {
          this.unpackedArgs[i] = localObject[i];
          this.argFilterType[i] = 1;
        }
      }
    }

    private Object filter(int paramInt, Object paramObject)
    {
      if (this.filter != null)
      {
        if (paramObject == null);
        for (Object localObject = "null"; ; localObject = paramObject)
          switch (paramInt)
          {
          default:
            localObject = null;
          case 0:
            return localObject;
          case 1:
          case 2:
          }
        return this.filter.doFilter(localObject.toString());
        return this.filter.doFilterUrl(localObject.toString());
      }
      return paramObject;
    }

    public Object[] getArguments()
    {
      return this.arguments;
    }

    public Filter getFilter()
    {
      return this.filter;
    }

    public Object[] getFilteredArgs(Locale paramLocale)
    {
      Object[] arrayOfObject = new Object[this.unpackedArgs.length];
      int i = 0;
      if (i >= this.unpackedArgs.length)
        return arrayOfObject;
      Object localObject2;
      if (this.filteredArgs[i] != null)
        localObject2 = this.filteredArgs[i];
      while (true)
      {
        arrayOfObject[i] = localObject2;
        i++;
        break;
        Object localObject1 = this.unpackedArgs[i];
        if (this.isLocaleSpecific[i] != 0)
        {
          String str = ((LocaleString)localObject1).getLocaleString(paramLocale);
          localObject2 = filter(this.argFilterType[i], str);
        }
        else
        {
          localObject2 = filter(this.argFilterType[i], localObject1);
          this.filteredArgs[i] = localObject2;
        }
      }
    }

    public boolean isEmpty()
    {
      return this.unpackedArgs.length == 0;
    }

    public void setFilter(Filter paramFilter)
    {
      if (paramFilter != this.filter);
      for (int i = 0; ; i++)
      {
        if (i >= this.unpackedArgs.length)
        {
          this.filter = paramFilter;
          return;
        }
        this.filteredArgs[i] = null;
      }
    }
  }
}