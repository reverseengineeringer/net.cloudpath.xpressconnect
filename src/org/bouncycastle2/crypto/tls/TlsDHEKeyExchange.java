package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle2.crypto.Signer;
import org.bouncycastle2.crypto.io.SignerInputStream;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPublicKeyParameters;

class TlsDHEKeyExchange extends TlsDHKeyExchange
{
  TlsDHEKeyExchange(TlsClientContext paramTlsClientContext, int paramInt)
  {
    super(paramTlsClientContext, paramInt);
  }

  protected Signer initSigner(TlsSigner paramTlsSigner, SecurityParameters paramSecurityParameters)
  {
    Signer localSigner = paramTlsSigner.createVerifyer(this.serverPublicKey);
    localSigner.update(paramSecurityParameters.clientRandom, 0, paramSecurityParameters.clientRandom.length);
    localSigner.update(paramSecurityParameters.serverRandom, 0, paramSecurityParameters.serverRandom.length);
    return localSigner;
  }

  public void processServerKeyExchange(InputStream paramInputStream)
    throws IOException
  {
    SecurityParameters localSecurityParameters = this.context.getSecurityParameters();
    Signer localSigner = initSigner(this.tlsSigner, localSecurityParameters);
    SignerInputStream localSignerInputStream = new SignerInputStream(paramInputStream, localSigner);
    byte[] arrayOfByte1 = TlsUtils.readOpaque16(localSignerInputStream);
    byte[] arrayOfByte2 = TlsUtils.readOpaque16(localSignerInputStream);
    byte[] arrayOfByte3 = TlsUtils.readOpaque16(localSignerInputStream);
    if (!localSigner.verifySignature(TlsUtils.readOpaque16(paramInputStream)))
      throw new TlsFatalAlert((short)42);
    BigInteger localBigInteger1 = new BigInteger(1, arrayOfByte1);
    BigInteger localBigInteger2 = new BigInteger(1, arrayOfByte2);
    this.dhAgreeServerPublicKey = validateDHPublicKey(new DHPublicKeyParameters(new BigInteger(1, arrayOfByte3), new DHParameters(localBigInteger1, localBigInteger2)));
  }

  public void skipServerKeyExchange()
    throws IOException
  {
    throw new TlsFatalAlert((short)10);
  }
}