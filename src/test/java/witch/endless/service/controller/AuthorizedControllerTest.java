package witch.endless.service.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import witch.endless.service.request.auth.SignInRequest;
import witch.endless.service.response.auth.JwtResponse;
import witch.endless.service.service.firebase.FirebaseService;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthorizedControllerTest {

  @MockBean
  private FirebaseService firebaseService;

  protected static String AUTHORIZATION_HEADER_KEY = "Authorization";
  protected static String JWT_TOKEN;
  protected static String BEARER_AUTHORIZATION_HEADER_VALUE;

  @BeforeEach
  void signInProcess(@Autowired ObjectMapper mapper, @Autowired MockMvc mvc)
      throws Exception {
    var mockFirebaseToken = mock(FirebaseToken.class);
    when(mockFirebaseToken.getUid()).thenReturn("vTtfz7zpU1bIR9kIDc8gOpUK8q92");
    when(mockFirebaseToken.getEmail()).thenReturn("alex@gmail.com");
    when(firebaseService.authenticate(eq("abcd"))).thenReturn(mockFirebaseToken);

    var request = new SignInRequest("abcd");
    MvcResult mvcResult = mvc.perform(post("/auth/sign-in")
        .content(mapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    JwtResponse jwtResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(),
        JwtResponse.class);
    JWT_TOKEN = jwtResponse.getToken();
    BEARER_AUTHORIZATION_HEADER_VALUE = "Bearer " + JWT_TOKEN;
  }
}
