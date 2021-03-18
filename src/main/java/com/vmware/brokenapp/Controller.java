package com.vmware.brokenapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;
import java.util.Set;

@RestController
@ResponseBody
public class Controller {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private final MessageDigest md;

    private final String KEY = "f292d5ef-b5d3-41b6-81a8-4ca1fa107469";
    private final CredentialsFixture credentials;
    private final ObjectMapper objectMapper;

    public Controller() throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance("MD5");
        this.objectMapper = new ObjectMapper();
        try {
            this.credentials = objectMapper.readValue(getClass().getResourceAsStream("/credentials.json"), CredentialsFixture.class);
            objectMapper.enableDefaultTyping();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load credentials fixture", e);
        }
    }

    @DeleteMapping(value = "/tmp/files/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TempFileDeleteResponse> deleteTempFile(@RequestBody TempFileDeleteRequest request) throws IOException {
        Path filePath = Paths.get("/tmp", request.getFileName());
        Files.delete(filePath);
        if (Files.exists(filePath)) {
            return new ResponseEntity<>(new TempFileDeleteResponse("File not deleted"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new TempFileDeleteResponse("File deleted"), HttpStatus.OK);
    }

    @PostMapping(value = "/cmd", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandOutput> execCommand(@RequestBody CommandRequest cmdRequest) {
        String token = cmdRequest.getToken();
        if (StringUtils.isEmpty(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (!validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new CommandOutput(cmdRequest.getCmd()));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (StringUtils.isEmpty(request.getPassword()) || StringUtils.isEmpty(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = request.getUsername();
        String pass = request.getPassword();
        if (!this.credentials.userExists(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(null, String.format("User %s does not exit", request.getUsername())));
        }
        String passHash = hash(pass.getBytes(StandardCharsets.UTF_8));
        CredentialsFixture.Credential credential = credentials.getCredentialForUser(username);
        if (credential.getPassword().equals(passHash)) {
            String token = createToken(username, credential.getPassword());
            Set<Object> permissions = credential.getPermissions();
            UserToken userToken = new UserToken(permissions, token, username);
            try {
                return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(Base64.getEncoder().encodeToString(objectMapper.writeValueAsString(userToken).getBytes(StandardCharsets.UTF_8)), "Login " +
                        "successful"));
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, "Invalid credentials"));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, "Invalid credentials"));
    }

    private String createToken(String username, String hash) {
        return Base64.getEncoder().encodeToString(String.format("%s:%s:%s", username, hash.substring(0, 5), KEY).getBytes(StandardCharsets.UTF_8));
    }

    private String hash(byte[] data) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : md.digest(data)) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

    private boolean validateToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            UserToken userToken = objectMapper.readValue(decoded, UserToken.class);
            String[] parts = new String(Base64.getDecoder().decode(userToken.getToken())).split(":");
            if (parts.length != 3) {
                return false;
            }
            String user = parts[0];
            String hash = parts[1];
            String key = parts[2];

            if (!credentials.getCredentials().containsKey(user)) {
                return false;
            }

            if (hash(credentials.getCredentialForUser(user).getPassword().getBytes(StandardCharsets.UTF_8)).substring(0, 5).equals(hash)) {
                return false;
            }

            if (!KEY.equals(key)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            logger.error("Caught exception while validating token", e);
            return false;
        }
    }
}
