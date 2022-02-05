package witch.endless.service.service.firebase;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

public interface FirebaseService {

  FirebaseToken authenticate(String firebaseJwt) throws FirebaseAuthException;

  String sendFcm(final String request) throws InterruptedException, ExecutionException;

  boolean verifyPurchaseCode(String code) throws IOException, GeneralSecurityException;
}
