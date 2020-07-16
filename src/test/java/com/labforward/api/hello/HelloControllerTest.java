package com.labforward.api.hello;

import com.labforward.api.common.MVCIntegrationTest;
import com.labforward.api.core.exception.EntityValidationException;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;
import static java.util.Optional.empty;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HelloControllerTest extends MVCIntegrationTest {

    private static final String HELLO_LUKE = "Hello Luke";

    @Test
    public void A_getHelloIsOKAndReturnsValidJSON() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id", is(HelloWorldService.DEFAULT_ID)))
                .andExpect(jsonPath("$.result.message", is(HelloWorldService.DEFAULT_MESSAGE)));
    }

    @Test
    public void B_getAllHelloIsOKAndReturnsValidJSON() throws Exception {
        mockMvc.perform(get("/hellos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(not(empty()))));
    }

    @Test
    public void C_returnsBadRequestWhenMessageMissing() throws Exception {
        String body = "{}";
        mockMvc.perform(post("/hello").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
    }

    @Test
    public void D_returnsBadRequestWhenUnexpectedAttributeProvided() throws Exception {
        String body = "{ \"tacos\":\"value\" }";
        mockMvc.perform(post("/hello").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", containsString(EntityValidationException.MESSAGE)));
    }

    @Test
    public void E_returnsBadRequestWhenMessageEmptyString() throws Exception {
        Greeting emptyMessage = new Greeting("");
        final String body = getGreetingBody(emptyMessage);

        mockMvc.perform(post("/hello").content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
    }

    @Test
    public void F_createOKWhenRequiredGreetingProvided() throws Exception {
        Greeting hello = new Greeting(HELLO_LUKE);
        final String body = getGreetingBody(hello);

        mockMvc.perform(post("/hello").contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message", is(hello.getMessage())));
    }

    @Test
    public void G_returnsBadRequestWhenUnexpectedAttributeProvidedInUpdateRequest() throws Exception {
        String body = "{ \"tacos\":\"value\" }";
        mockMvc.perform(put("/hello/default").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", containsString(EntityValidationException.MESSAGE)));
    }

    @Test
    public void H_updateOKWhenRequiredGreetingProvided() throws Exception {
        Greeting updatedHello = new Greeting("NEW UPDATED VALUE");
        final String body = getGreetingBody(updatedHello);

        mockMvc.perform(put("/hello/default").contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message", is(updatedHello.getMessage())));
    }

    private String getGreetingBody(Greeting greeting) throws JSONException {
        JSONObject json = new JSONObject().put("message", greeting.getMessage());

        if (greeting.getId() != null) {
            json.put("id", greeting.getId());
        }

        return json.toString();
    }

}
