package com.cloudpath.common.util;

public class Encode
{
  private static final String cls_Key1 = BinaryUtil.doXor(Md5.md5("mxpmca(#KS)Al2ls01lk3,dkwla)x?"), Md5.md5("IQ*XmpD2d823msk2109`lam`0`(@Mk1292&")) + "K920@(!<Mc";
  private static final String cls_Key1Frag1 = "mxpmca(#KS)Al2ls01lk3,dkwla)x?";
  private static final String cls_Key1Frag2 = "IQ*XmpD2d823msk2109`lam`0`(@Mk1292&";
  private static final String cls_Key2 = BinaryUtil.doXor(Md5.md5("hx.2/1=3Re~(*#<KDld02LO"), Md5.md5("J92*1,.x~!dk20edli3e099310dck20K)(#")) + '\000' + '\002' + '\004' + '\005';
  private static final String cls_Key2Frag1 = "hx.2/1=3Re~(*#<KDld02LO";
  private static final String cls_Key2Frag2 = "J92*1,.x~!dk20edli3e099310dck20K)(#";

  public static final String encode(String paramString)
  {
    if (paramString == null)
      return null;
    String str1 = paramString.replaceAll(" ", "").replace("\r", "").replace("\n", "");
    String str2 = BinaryUtil.doXor(cls_Key1, cls_Key2);
    return Md5.md5(BinaryUtil.doXor(Md5.md5(str1), Md5.md5(str2)));
  }

  public static final String xpcDeobsfucate(String paramString)
  {
    String str1 = BinaryUtil.convertFromSpacelessHex(paramString);
    for (String str2 = BinaryUtil.doXor(cls_Key1, cls_Key2); str2.length() < str1.length(); str2 = str2 + str2);
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < str1.length(); i++)
      localStringBuffer.append((char)(str2.charAt(i) ^ str1.charAt(i)));
    String str3 = "";
    for (int j = -1 + localStringBuffer.length(); j > localStringBuffer.length() / 2; j -= 2)
      str3 = str3 + localStringBuffer.charAt(j);
    return str3;
  }
}