package re1kur.ums.jwt.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import re1kur.ums.entity.User;
import re1kur.ums.jwt.Credentials;
import re1kur.ums.jwt.JwtProvider;
import re1kur.ums.jwt.JwtToken;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
@Slf4j
public class JwtProviderImpl implements JwtProvider {
    @Value("${custom.jwt.privateKeyPath}")
    private String privateKeyPath;

    @Value("${custom.jwt.publicKeyPath}")
    private String publicKeyPath;

    @Value("${custom.jwt.keySize}")
    private int keySize;

    @Value("${custom.jwt.ttl-hours}")
    private int accessTtl;

    @Value("${custom.jwt.refresh-ttl-days}")
    private int refreshTtl;

    @Override
    @SneakyThrows
    public JwtToken getToken(User user) {
        Credentials credentials = new Credentials(user);
        return generate(credentials);
    }

    public JwtToken generate(Credentials cred) throws JOSEException {
        if (!Files.exists(Paths.get(publicKeyPath))) {
            generateKeyPair();
        }

        RSAPrivateKey privateKey = readPrivateKeyFromFile(privateKeyPath);
        RSAPublicKey publicKey = readPublicKeyFromFile(publicKeyPath);
        RSASSASigner signer = new RSASSASigner(privateKey);

        JWSHeader accessHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();

        JWTClaimsSet accessPayload = new JWTClaimsSet.Builder()
                .claim("sub", cred.getClaims().get("sub"))
                .claim("email", cred.getClaims().get("email"))
                .claim("enabled", cred.getClaims().get("enabled"))
                .claim("scope", cred.getClaims().get("scope"))
                .claim("token_type", "access")
                .issueTime(new Date())
                .expirationTime(Date.from(LocalDateTime.now().plusHours(accessTtl).toInstant(ZoneOffset.UTC)))
                .build();

        SignedJWT accessToken = new SignedJWT(accessHeader, accessPayload);
        accessToken.sign(signer);

        JWSHeader refreshHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();

        JWTClaimsSet refreshPayload = new JWTClaimsSet.Builder()
                .claim("sub", cred.getClaims().get("sub"))
                .claim("token_type", "refresh")
                .issueTime(new Date())
                .expirationTime(Date.from(LocalDateTime.now().plusDays(refreshTtl).toInstant(ZoneOffset.UTC)))
                .build();

        SignedJWT refreshToken = new SignedJWT(refreshHeader, refreshPayload);
        refreshToken.sign(signer);

        verifyJWTSign(publicKey, accessToken);
        verifyJWTSign(publicKey, refreshToken);

        return JwtToken.builder()
                .body(accessToken.serialize())
                .refreshToken(refreshToken.serialize())
                .expiresAt(Instant.ofEpochSecond((long) accessTtl * 60 * 60))
                .refreshExpiresAt(Instant.ofEpochSecond((long) refreshTtl * 24 * 60 * 60))
                .build();
    }

    @SneakyThrows
    private void generateKeyPair() {
        KeyPairGenerator pairGenerator = KeyPairGenerator.getInstance("RSA");
        pairGenerator.initialize(keySize);
        KeyPair keyPair = pairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Files.write(Paths.get(publicKeyPath), publicKey.getEncoded(), StandardOpenOption.CREATE_NEW);
        Files.write(Paths.get(privateKeyPath), privateKey.getEncoded(), StandardOpenOption.CREATE_NEW);
    }

    @SneakyThrows
    private RSAPrivateKey readPrivateKeyFromFile(String path) {
        byte[] keyContent = Files.readAllBytes(Paths.get(path));
        return (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(keyContent));
    }

    @SneakyThrows
    private RSAPublicKey readPublicKeyFromFile(String path) {
        byte[] keyContent = Files.readAllBytes(Paths.get(path));
        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(keyContent));
    }

    @SneakyThrows
    private void verifyJWTSign(RSAPublicKey publicKey, SignedJWT jwt) {
        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        boolean verify = verifier.verify(jwt.getHeader(), jwt.getSigningInput(), jwt.getSignature());
        log.info("Test verification with public key: {}", verify ? "DONE" : "FAILED");
    }
}
