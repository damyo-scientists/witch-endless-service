package witch.endless.service.controller.account;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import witch.endless.service.factory.model.account.DeviceTokenFactory;
import witch.endless.service.model.account.Account;
import witch.endless.service.model.account.deviceToken.DeviceToken;
import witch.endless.service.request.auth.SignInRequest;
import witch.endless.service.request.auth.SignUpRequest;
import witch.endless.service.service.firebase.FirebaseService;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthControllerTest {

  @MockBean
  private FirebaseService firebaseService;

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testSignUp() throws Exception {
    Account account = Account.builder()
        .email("alex2@gmail.com")
        .firebaseUid("abcd")
        .build();

    DeviceToken deviceToken = DeviceTokenFactory.create("test");

    mvc.perform(post("/auth/sign-up")
        .content(objectMapper.writeValueAsString(new SignUpRequest(account, deviceToken)))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    account = Account.builder()
        .email("alex3@gmail.com")
        .build();

    mvc.perform(post("/auth/sign-up")
        .content(objectMapper.writeValueAsString(new SignUpRequest(account, deviceToken)))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity());

    deviceToken.setValue(null);

    mvc.perform(post("/auth/sign-up")
        .content(objectMapper.writeValueAsString(new SignUpRequest(account, deviceToken)))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  void testSignIn() throws Exception {
    var mockFirebaseToken = mock(FirebaseToken.class);
    when(mockFirebaseToken.getUid()).thenReturn("vTtfz7zpU1bIR9kIDc8gOpUK8q92");
    when(mockFirebaseToken.getEmail()).thenReturn("alex@gmail.com");
    when(firebaseService.authenticate(eq("testJwt"))).thenReturn(mockFirebaseToken);

    var request = new SignInRequest( "testJwt");
    mvc.perform(post("/auth/sign-in")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
