package com.thiagoarend.parking_spot_management_api;

import com.thiagoarend.parking_spot_management_api.web.dto.UserCreateDto;
import com.thiagoarend.parking_spot_management_api.web.dto.UserPasswordDto;
import com.thiagoarend.parking_spot_management_api.web.dto.UserResponseDto;
import com.thiagoarend.parking_spot_management_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // tomcat em ambiente de teste é executado em porta randomica
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIntegrationTest {

    @Autowired
    WebTestClient testClient;

    @Test()
    public void createWithValidUsernameAndPasswordShouldReturnUserWithStatus200() {
        // generates post request and captures response
        UserResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tod@email.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getUsername()).isEqualTo("tod@email.com");
        assertThat(responseBody.getRole()).isEqualTo("CLIENT");
    }

    @Test()
    public void createWithInvalidUsernameShouldReturnErrorMessageWithStatus422() {
        // generates post request and captures response (empty e-mail)
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().size()).isEqualTo(1);
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        // generates post request and captures response (wrong e-mail format)
        responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tod@email", "123456"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().size()).isEqualTo(1);
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        // generates post request and captures response (wrong e-mail format)
        responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tod@", "123456"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().size()).isEqualTo(1);
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test()
    public void createWithInvalidPasswordShouldReturnErrorMessageWithStatus422() {
        // generates post request and captures response (empty password)
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tod@email.com", ""))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().size()).isEqualTo(1);
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        // generates post request and captures response (more than 6 characters)
        responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tod@email.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().size()).isEqualTo(1);
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        // generates post request and captures response (less than 6 characters)
        responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tod@email.com", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().size()).isEqualTo(1);
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test()
    public void createWithNonUniqueUsernameAndPasswordShouldReturnErrorMessageWithStatus409() {
        // generates post request and captures response
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("ana@email.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test()
    public void getByIdWithExistentIdShouldReturnUserWithStatus200() {
        // generates post request and captures response
        UserResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/users/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(100);
        assertThat(responseBody.getUsername()).isEqualTo("ana@email.com");
        assertThat(responseBody.getRole()).isEqualTo("ADMIN");
    }

    @Test()
    public void getByIdWithNonExistentIdShouldReturnErrorMessageWithStatus404() {
        // generates post request and captures response
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/users/0")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test()
    public void updatePasswordWithValidDataShouldReturnStatus204() {
        // generates post request and captures response
        testClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test()
    public void updatePasswordWithNonExistentIdShouldReturnErrorMessageWithStatus404() {
        // generates post request and captures response
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/users/0")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test()
    public void updatePasswordWithInvalidInputValuesShouldReturnErrorMessageWithStatus422() {
        // generates post request and captures response
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("", "", ""))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().size()).isGreaterThanOrEqualTo(1);
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        // generates post request and captures response
        responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("12345", "12345", "12345"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().size()).isGreaterThanOrEqualTo(1);
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        // generates post request and captures response
        responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("1234567", "1234567", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getErrors().size()).isGreaterThanOrEqualTo(1);
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test()
    public void updatePasswordWithNonMatchingValuesShouldReturnErrorMessageWithStatus400() {
        // generates post request and captures response
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("123456", "123456", "000000"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("000000", "123456", "123456"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test()
    public void getAllReturnUserListWithStatus200() {
        // generates post request and captures response
        List<UserResponseDto> responseBody = testClient
                .get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDto.class)
                .returnResult().getResponseBody();

        // assertions
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.size()).isEqualTo(3);
    }
}
