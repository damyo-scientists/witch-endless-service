package witch.endless.service.service.firebase;


import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MockClassPathFirebaseService implements FirebaseService {

  @Override
  public FirebaseToken authenticate(String firebaseJwt) throws FirebaseAuthException {
    return null;
  }

  @Override
  public String sendFcm(String request) throws InterruptedException, ExecutionException {
    return null;
  }

  @Override
  public boolean verifyPurchaseCode(String code) throws IOException, GeneralSecurityException {
    return true;
  }
}
