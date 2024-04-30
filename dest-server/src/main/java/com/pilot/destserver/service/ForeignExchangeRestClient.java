package com.pilot.destserver.service;

import com.pilot.destserver.dto.LatestRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ForeignExchangeRestClient {

    private final String API_ACCESS_KEY = "eaaa177cc4f952bfd00c4347ca35be86";
//    private final String BASE = "USD";

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    public ForeignExchangeRestClient(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }


    public LatestRate latestRate() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://data.fixer.io/api/latest")
                .queryParam("access_key", API_ACCESS_KEY);
//                .queryParam("base", BASE);
//                .queryParam("symbols", symbols);

        return restTemplate.getForObject(builder.toUriString(), LatestRate.class);
    }

}
