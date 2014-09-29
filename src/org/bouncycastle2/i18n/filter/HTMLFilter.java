package org.bouncycastle2.i18n.filter;

public class HTMLFilter
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
    case '$':
    case '*':
    case ',':
    case '.':
    case '/':
    case '0':
    case '1':
    case '2':
    case '3':
    case '4':
    case '5':
    case '6':
    case '7':
    case '8':
    case '9':
    case ':':
    case '=':
    default:
      i -= 3;
    case '<':
    case '>':
    case '(':
    case ')':
    case '#':
    case '&':
    case '"':
    case '\'':
    case '%':
    case ';':
    case '+':
    case '-':
    }
    while (true)
    {
      i += 4;
      break;
      localStringBuffer.replace(i, i + 1, "&#60");
      continue;
      localStringBuffer.replace(i, i + 1, "&#62");
      continue;
      localStringBuffer.replace(i, i + 1, "&#40");
      continue;
      localStringBuffer.replace(i, i + 1, "&#41");
      continue;
      localStringBuffer.replace(i, i + 1, "&#35");
      continue;
      localStringBuffer.replace(i, i + 1, "&#38");
      continue;
      localStringBuffer.replace(i, i + 1, "&#34");
      continue;
      localStringBuffer.replace(i, i + 1, "&#39");
      continue;
      localStringBuffer.replace(i, i + 1, "&#37");
      continue;
      localStringBuffer.replace(i, i + 1, "&#59");
      continue;
      localStringBuffer.replace(i, i + 1, "&#43");
      continue;
      localStringBuffer.replace(i, i + 1, "&#45");
    }
  }

  public String doFilterUrl(String paramString)
  {
    return doFilter(paramString);
  }
}