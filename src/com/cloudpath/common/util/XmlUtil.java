package com.cloudpath.common.util;

public class XmlUtil
{
  public static final String createElement(String paramString1, String paramString2)
  {
    return createElement(paramString1, paramString2, false);
  }

  public static final String createElement(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, String paramString4)
  {
    if (paramString2.length() > 0)
      paramString2 = " " + paramString2;
    String str = delimitXml(paramString3);
    if ((paramBoolean2) || ((str != null) && (str.length() > 0)))
    {
      if ((paramString4.length() > 0) && (paramString4.equals(str)))
        return "";
      return "<" + paramString1 + paramString2 + "><![CDATA[" + str + "]]></" + paramString1 + ">\n";
    }
    return "";
  }

  public static final String createElement(String paramString1, String paramString2, boolean paramBoolean)
  {
    return createElement(paramString1, "", paramString2, true, paramBoolean, "");
  }

  public static final String createElementClose(String paramString)
  {
    return "</" + paramString + ">\n";
  }

  public static final String createElementOpen(String paramString)
  {
    return "<" + paramString + ">\n";
  }

  public static String delimitXml(String paramString)
  {
    if (paramString == null)
      paramString = "";
    while (paramString.length() <= 0)
      return paramString;
    return paramString.replace("&amp;", "&").replace("&", "&amp;");
  }
}