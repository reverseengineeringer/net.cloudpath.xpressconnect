package org.bouncycastle2.asn1.cryptopro;

import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Hashtable;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.math.ec.ECCurve.Fp;
import org.bouncycastle2.math.ec.ECFieldElement.Fp;
import org.bouncycastle2.math.ec.ECPoint.Fp;

public class ECGOST3410NamedCurves
{
  static final Hashtable names;
  static final Hashtable objIds = new Hashtable();
  static final Hashtable params = new Hashtable();

  static
  {
    names = new Hashtable();
    BigInteger localBigInteger1 = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639319");
    BigInteger localBigInteger2 = new BigInteger("115792089237316195423570985008687907853073762908499243225378155805079068850323");
    ECCurve.Fp localFp1 = new ECCurve.Fp(localBigInteger1, new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639316"), new BigInteger("166"));
    ECDomainParameters localECDomainParameters1 = new ECDomainParameters(localFp1, new ECPoint.Fp(localFp1, new ECFieldElement.Fp(localFp1.getQ(), new BigInteger("1")), new ECFieldElement.Fp(localFp1.getQ(), new BigInteger("64033881142927202683649881450433473985931760268884941288852745803908878638612"))), localBigInteger2);
    params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A, localECDomainParameters1);
    BigInteger localBigInteger3 = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639319");
    BigInteger localBigInteger4 = new BigInteger("115792089237316195423570985008687907853073762908499243225378155805079068850323");
    ECCurve.Fp localFp2 = new ECCurve.Fp(localBigInteger3, new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639316"), new BigInteger("166"));
    ECDomainParameters localECDomainParameters2 = new ECDomainParameters(localFp2, new ECPoint.Fp(localFp2, new ECFieldElement.Fp(localFp2.getQ(), new BigInteger("1")), new ECFieldElement.Fp(localFp2.getQ(), new BigInteger("64033881142927202683649881450433473985931760268884941288852745803908878638612"))), localBigInteger4);
    params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA, localECDomainParameters2);
    BigInteger localBigInteger5 = new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564823193");
    BigInteger localBigInteger6 = new BigInteger("57896044618658097711785492504343953927102133160255826820068844496087732066703");
    ECCurve.Fp localFp3 = new ECCurve.Fp(localBigInteger5, new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564823190"), new BigInteger("28091019353058090096996979000309560759124368558014865957655842872397301267595"));
    ECDomainParameters localECDomainParameters3 = new ECDomainParameters(localFp3, new ECPoint.Fp(localFp3, new ECFieldElement.Fp(localBigInteger5, new BigInteger("1")), new ECFieldElement.Fp(localBigInteger5, new BigInteger("28792665814854611296992347458380284135028636778229113005756334730996303888124"))), localBigInteger6);
    params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B, localECDomainParameters3);
    BigInteger localBigInteger7 = new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502619");
    BigInteger localBigInteger8 = new BigInteger("70390085352083305199547718019018437840920882647164081035322601458352298396601");
    ECCurve.Fp localFp4 = new ECCurve.Fp(localBigInteger7, new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502616"), new BigInteger("32858"));
    ECDomainParameters localECDomainParameters4 = new ECDomainParameters(localFp4, new ECPoint.Fp(localFp4, new ECFieldElement.Fp(localBigInteger7, new BigInteger("0")), new ECFieldElement.Fp(localBigInteger7, new BigInteger("29818893917731240733471273240314769927240550812383695689146495261604565990247"))), localBigInteger8);
    params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB, localECDomainParameters4);
    BigInteger localBigInteger9 = new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502619");
    BigInteger localBigInteger10 = new BigInteger("70390085352083305199547718019018437840920882647164081035322601458352298396601");
    ECCurve.Fp localFp5 = new ECCurve.Fp(localBigInteger9, new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502616"), new BigInteger("32858"));
    ECDomainParameters localECDomainParameters5 = new ECDomainParameters(localFp5, new ECPoint.Fp(localFp5, new ECFieldElement.Fp(localBigInteger9, new BigInteger("0")), new ECFieldElement.Fp(localBigInteger9, new BigInteger("29818893917731240733471273240314769927240550812383695689146495261604565990247"))), localBigInteger10);
    params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C, localECDomainParameters5);
    objIds.put("GostR3410-2001-CryptoPro-A", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A);
    objIds.put("GostR3410-2001-CryptoPro-B", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B);
    objIds.put("GostR3410-2001-CryptoPro-C", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C);
    objIds.put("GostR3410-2001-CryptoPro-XchA", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA);
    objIds.put("GostR3410-2001-CryptoPro-XchB", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB);
    names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A, "GostR3410-2001-CryptoPro-A");
    names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B, "GostR3410-2001-CryptoPro-B");
    names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C, "GostR3410-2001-CryptoPro-C");
    names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA, "GostR3410-2001-CryptoPro-XchA");
    names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB, "GostR3410-2001-CryptoPro-XchB");
  }

  public static ECDomainParameters getByName(String paramString)
  {
    DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)objIds.get(paramString);
    if (localDERObjectIdentifier != null)
      return (ECDomainParameters)params.get(localDERObjectIdentifier);
    return null;
  }

  public static ECDomainParameters getByOID(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return (ECDomainParameters)params.get(paramDERObjectIdentifier);
  }

  public static String getName(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return (String)names.get(paramDERObjectIdentifier);
  }

  public static Enumeration getNames()
  {
    return objIds.keys();
  }

  public static DERObjectIdentifier getOID(String paramString)
  {
    return (DERObjectIdentifier)objIds.get(paramString);
  }
}