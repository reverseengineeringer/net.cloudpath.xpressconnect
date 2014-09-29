package org.bouncycastle2.i18n.filter;

public class SQLFilter
  implements Filter
{
  public String doFilter(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramString);
    int i = 0;
    if (i >= localStringBuffer.length())
      return localStringBuffer.toString();
    switch (localStringBuffer.charAt(i))
    {
    default:
    case '\'':
    case '"':
    case '=':
    case '-':
    case '/':
    case '\\':
    case ';':
    case '\r':
    case '\n':
    }
    while (true)
    {
      i++;
      break;
      localStringBuffer.replace(i, i + 1, "\\'");
      i++;
      continue;
      localStringBuffer.replace(i, i + 1, "\\\"");
      i++;
      continue;
      localStringBuffer.replace(i, i + 1, "\\=");
      i++;
      continue;
      localStringBuffer.replace(i, i + 1, "\\-");
      i++;
      continue;
      localStringBuffer.replace(i, i + 1, "\\/");
      i++;
      continue;
      localStringBuffer.replace(i, i + 1, "\\\\");
      i++;
      continue;
      localStringBuffer.replace(i, i + 1, "\\;");
      i++;
      continue;
      localStringBuffer.replace(i, i + 1, "\\r");
      i++;
      continue;
      localStringBuffer.replace(i, i + 1, "\\n");
      i++;
    }
  }

  public String doFilterUrl(String paramString)
  {
    return doFilter(paramString);
  }
}