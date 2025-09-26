package com.example.HW2.credit_processing.service;

import com.example.HW2.credit_processing.dto.ClientInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ClientInfoService {

    private final RestTemplate restTemplate;

    @Value("${client-processing.url}")
    private String clientProcessingUrl;

    public ClientInfoDto getClientInfo(Long clientId) {
        String url = clientProcessingUrl + "/api/clients/" + clientId;
        return restTemplate.getForObject(url, ClientInfoDto.class);
    }
}
