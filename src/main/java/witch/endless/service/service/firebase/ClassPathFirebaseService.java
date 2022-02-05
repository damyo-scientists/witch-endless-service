package witch.endless.service.service.firebase;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import witch.endless.service.config.AppConfig.FcmConfig;
import witch.endless.service.config.AppConfig.PlayStoreConfig;

@RequiredArgsConstructor
@Slf4j
public class ClassPathFirebaseService implements FirebaseService {

  protected static final String PACKAGE_NAME = "com.SepiaGames.WDS";

  protected final FcmConfig fcmConfig;
  protected final PlayStoreConfig playStoreConfig;

  @PostConstruct
  public void initialize() throws IOException {
    ClassPathResource serviceAccountResource = new ClassPathResource(
        fcmConfig.getServiceAccountJson());

    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccountResource.getInputStream()))
        .setDatabaseUrl(fcmConfig.getDatabaseUrl())
        .build();

    if (FirebaseApp.getApps() == null) {
      FirebaseApp.initializeApp(options);
    }
  }

  public FirebaseToken authenticate(String firebaseJwt) throws FirebaseAuthException {
    return FirebaseAuth.getInstance().verifyIdToken(firebaseJwt);
  }

  public String sendFcm(final String request) throws InterruptedException, ExecutionException {
    Message message = Message.builder()
        // 임시 토큰
        .setToken(
            "cGBzwn-gQymu5UJShPzU1X:APA91bHBrVIA0_0g0cwvHBT1Veoh06Y7Gv71y8aFuB6vy6i_SQwUrRnirDBQiZ23rodhrPHWmDKhxC0pPzGktQOKywSfY_5C5Ye_i18cX2H_sEBb9r3215cXWza1asCJVPIR1qzqkt9P")
        .setNotification(Notification.builder().setTitle("title").setBody("test").build())
        .build();

    String response = FirebaseMessaging.getInstance().sendAsync(message).get();
    log.info("Sent message: " + response);
    return response;
  }

  @Override
  public boolean verifyPurchaseCode(final String code) throws IOException, GeneralSecurityException {

    GoogleCredentials credential = GoogleCredentials
        .fromStream(new ClassPathResource(playStoreConfig.getServiceAccountJson()).getInputStream())
        .createScoped(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));

    AndroidPublisher publisher = new AndroidPublisher
        .Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
        new HttpCredentialsAdapter(credential))
        .setApplicationName("com.SepiaGames.WDS").build();

    AndroidPublisher.Purchases purchases = publisher.purchases();
    ProductPurchase purchase = purchases.products().get(
        PACKAGE_NAME,
        "test",
        "jpdpgbdoadogjffmmicjhjjk.AO-J1OzR7yaQns-OFHK9wplD4_qiJ8x85Wbi0Ya3YsL3BLe79zgIHXewQHE2h9DHquX8NO646wjA7L-BZwGz_w72Y6Lb27hqsw"
    ).execute();
    return true;
  }
}
