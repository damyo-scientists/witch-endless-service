package witch.endless.service.controller.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import witch.endless.service.behavior.account.AccountBehavior;
import witch.endless.service.controller.AuthorizedControllerTest;
import witch.endless.service.request.account.AccountUpdateRequest;
import witch.endless.service.request.account.AccountUpdateRequest.AccountUpdate;


@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountControllerTest extends AuthorizedControllerTest {

  @Autowired private AccountBehavior behavior;
  @Autowired private ObjectMapper mapper;
  @Autowired private MockMvc mvc;

  @Test
  void testGetById() throws Exception {
    mvc.perform(get("/accounts/1")
        .header(AUTHORIZATION_HEADER_KEY, BEARER_AUTHORIZATION_HEADER_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.account", notNullValue()))
        .andExpect(jsonPath("$.account.email", is("alex@gmail.com")));

    mvc.perform(get("/accounts/999999")
        .header(AUTHORIZATION_HEADER_KEY, BEARER_AUTHORIZATION_HEADER_VALUE)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetByEmail() throws Exception {
    mvc.perform(get("/accounts/@alex@gmail.com")
        .header(AUTHORIZATION_HEADER_KEY, BEARER_AUTHORIZATION_HEADER_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.account", notNullValue()))
        .andExpect(jsonPath("$.account.email", is("alex@gmail.com")));

    mvc.perform(get("/accounts/999999")
        .header(AUTHORIZATION_HEADER_KEY, BEARER_AUTHORIZATION_HEADER_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetAll() throws Exception {
    mvc.perform(get("/accounts")
        .header(AUTHORIZATION_HEADER_KEY, BEARER_AUTHORIZATION_HEADER_VALUE)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accounts", hasSize(greaterThanOrEqualTo(1))))
        .andExpect(jsonPath("$.accounts.[0].email", is("alex@gmail.com")));
  }

  @Test
  void testUpdate() throws Exception {
    AccountUpdateRequest request = AccountUpdateRequest.builder()
        .account(AccountUpdate.builder().nickname("test-name").build())
        .build();

    mvc.perform(patch("/accounts/1")
        .header(AUTHORIZATION_HEADER_KEY, BEARER_AUTHORIZATION_HEADER_VALUE)
        .contentType("application/merge-patch+json")
        .content(mapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.account.nickname", is("test-name")));
  }

  @Test
  void testDelete() throws Exception {
    mvc.perform(delete("/accounts/1")
        .header(AUTHORIZATION_HEADER_KEY, BEARER_AUTHORIZATION_HEADER_VALUE))
        .andExpect(status().isNoContent());

    assertThat(behavior.findById(1L)).isEmpty();
  }
}
