package com.impassive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.impassive.rpc.utils.JsonTools;
import com.impassive.rpc.utils.StringTools;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import lombok.Data;
import org.junit.Test;

/**
 * @author impassive
 */

public class CertTest {

  @Test
  public void name() throws CertificateException {
    OriginTransactionIdResponse originTransactionIdResponse = JsonTools.fromJson(text,
        OriginTransactionIdResponse.class);
    List<String> signedTransactions = originTransactionIdResponse.getSignedTransactions();
    for (String signedTransaction : signedTransactions) {
      String[] split = signedTransaction.split("\\.");

      byte[] decode = Base64.getUrlDecoder().decode(split[0]);
      String headerStr = new String(decode);
      IapSignHeader iapSignHeader = JsonTools.fromJson(headerStr, IapSignHeader.class);

      List<String> x5c = iapSignHeader.getX5c();
      String s = x5c.get(x5c.size() - 1);
      boolean equal = StringTools.isEqual(s, last);

      for (int i = 1; i < x5c.size(); i++) {

        X509Certificate before = build(iapSignHeader.x5c.get(i - 1));
        X509Certificate after = build(iapSignHeader.x5c.get(i));
        boolean verify = verify(before, after);
        if (!verify) {
          return;
        }

      }
    }
  }

  private static X509Certificate build(String certContent) throws CertificateException {
    String format = String.format("-----BEGIN CERTIFICATE-----\n"
        + "%s"
        + "-----END CERTIFICATE-----", certContent);
    CertificateFactory x509 = CertificateFactory.getInstance("x509");
    ByteArrayInputStream inStream = new ByteArrayInputStream(
        format.getBytes(StandardCharsets.UTF_8));
    return (X509Certificate) x509.generateCertificate(inStream);
  }

  private boolean verify(X509Certificate before, X509Certificate after) {
    String issuerX500Principal = before.getIssuerX500Principal().getName();
    String name = after.getSubjectX500Principal().getName();
    if (!StringTools.isEqual(issuerX500Principal, name)) {
      return false;
    }
    try {
      before.verify(after.getPublicKey());
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @Data
  public static class IapSignHeader {

    private String alg;

    private List<String> x5c;

  }


  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class OriginTransactionIdResponse {

    private String revision;

    private String bundleId;

    private String appAppleId;

    private String environment;

    private Boolean hasMore;

    private List<String> signedTransactions;

  }

  private static final String last = "MIICQzCCAcmgAwIBAgIILcX8iNLFS5UwCgYIKoZIzj0EAwMwZzEbMBkGA1UEAwwSQXBwbGUgUm9vdCBDQSAtIEczMSYwJAYDVQQLDB1BcHBsZSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTETMBEGA1UECgwKQXBwbGUgSW5jLjELMAkGA1UEBhMCVVMwHhcNMTQwNDMwMTgxOTA2WhcNMzkwNDMwMTgxOTA2WjBnMRswGQYDVQQDDBJBcHBsZSBSb290IENBIC0gRzMxJjAkBgNVBAsMHUFwcGxlIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MRMwEQYDVQQKDApBcHBsZSBJbmMuMQswCQYDVQQGEwJVUzB2MBAGByqGSM49AgEGBSuBBAAiA2IABJjpLz1AcqTtkyJygRMc3RCV8cWjTnHcFBbZDuWmBSp3ZHtfTjjTuxxEtX/1H7YyYl3J6YRbTzBPEVoA/VhYDKX1DyxNB0cTddqXl5dvMVztK517IDvYuVTZXpmkOlEKMaNCMEAwHQYDVR0OBBYEFLuw3qFYM4iapIqZ3r6966/ayySrMA8GA1UdEwEB/wQFMAMBAf8wDgYDVR0PAQH/BAQDAgEGMAoGCCqGSM49BAMDA2gAMGUCMQCD6cHEFl4aXTQY2e3v9GwOAEZLuN+yRhHFD/3meoyhpmvOwgPUnPWTxnS4at+qIxUCMG1mihDK1A3UT82NQz60imOlM27jbdoXt2QfyFMm+YhidDkLF1vLUagM6BgD56KyKA==";

  private static final String text = "";
}
