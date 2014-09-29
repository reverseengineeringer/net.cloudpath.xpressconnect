package com.cloudpath.common.pojo;

import com.cloudpath.common.parse.XmlContainer;
import com.cloudpath.common.util.Encode;
import com.cloudpath.common.util.XmlUtil;
import com.cloudpath.common.util.Xmlable;
import java.io.PrintStream;

public class Content
  implements XmlContainer
{
  public static final String ELEMENT_CHECKSUM = "checksum";
  public static final String ELEMENT_CONTENT = "content";
  public static final String ELEMENT_CONTENTS = "contents";
  private String cls_Checksum = "";
  private Xmlable cls_Content = null;
  private boolean cls_IsValid = false;
  private String cls_Xml;

  public Content()
  {
  }

  public Content(Xmlable paramXmlable)
  {
    setContent(paramXmlable);
    this.cls_IsValid = true;
  }

  public String getChecksum()
  {
    return this.cls_Checksum;
  }

  public Xmlable getContent()
  {
    return this.cls_Content;
  }

  public String getEndElement()
  {
    return "</content>";
  }

  public String getStartElement()
  {
    return "<content>";
  }

  public String getXml()
  {
    if (isValid());
    for (String str = Encode.encode(getContent().getXml()); ; str = getChecksum())
      return XmlUtil.createElementOpen("contents") + XmlUtil.createElement("checksum", str) + XmlUtil.createElementOpen("content") + getContent().getXml() + XmlUtil.createElementClose("content") + XmlUtil.createElementClose("contents");
  }

  public boolean isValid()
  {
    return this.cls_IsValid;
  }

  public void setChecksum(String paramString)
  {
    this.cls_Checksum = paramString;
    verify();
  }

  public void setContent(Xmlable paramXmlable)
  {
    this.cls_Content = paramXmlable;
    if ((this.cls_Xml == null) || (this.cls_Xml.length() == 0))
    {
      this.cls_Xml = paramXmlable.getXml();
      verify();
    }
  }

  public void setIsValid(boolean paramBoolean)
  {
    this.cls_IsValid = paramBoolean;
  }

  public void setXml(String paramString)
  {
    this.cls_Xml = paramString;
    verify();
  }

  public String toString()
  {
    return "[Content]";
  }

  public void verify()
  {
    if ((this.cls_Xml == null) || (this.cls_Xml.length() == 0))
    {
      this.cls_IsValid = true;
      return;
    }
    String str = Encode.encode(this.cls_Xml);
    System.out.println("'good' checksum = " + str);
    this.cls_IsValid = str.equals(getChecksum());
  }
}