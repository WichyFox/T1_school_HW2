package com.example.HW_2.client_processing.controller;

import com.example.HW_2.client_processing.dto.ClientDto;
import com.example.HW_2.client_processing.dto.ClientRegistrationRequest;
import com.example.HW_2.client_processing.dto.UserDto;
import com.example.HW_2.client_processing.entity.Client;
import com.example.HW_2.client_processing.entity.User;
import com.example.HW_2.client_processing.service.ClientService;
import com.example.HW_2.client_processing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerClient(@RequestBody ClientRegistrationRequest request) {
        boolean blacklisted = clientService.isBlacklisted(request.getDocumentType(), request.getDocumentId());
        if (blacklisted) {
            return ResponseEntity.status(403).build();
        }
        Client client = clientService.createClient(request);
        User user = userService.createUser(client.getUserId(), request.getLogin(), request.getPassword(), request.getEmail());

        UserDto response = UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDto> getClient(@PathVariable Long clientId) {
        return clientService.findById(clientId)
                .map(client -> ResponseEntity.ok(ClientDto.fromEntity(client)))
                .orElse(ResponseEntity.notFound().build());
    }
}
