package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {
    private static final Logger logger = LoggerFactory.getLogger(IncentiveService.class);

    @Value("${incentive.api.url:http://localhost:8080/incentive}")
    private String incentiveApiUrl;

    private final RestTemplate restTemplate;

    public IncentiveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Incentive getIncentive(Transaction transaction) {
        if (transaction == null) {
            logger.warn("Null transaction provided to incentive service");
            return new Incentive(0.0f);
        }

        try {
            logger.debug("Requesting incentive for transaction: {}", transaction);
            Incentive incentive = restTemplate.postForObject(incentiveApiUrl, transaction, Incentive.class);

            if (incentive == null) {
                logger.warn("Received null incentive response for transaction: {}", transaction);
                return new Incentive(0.0f);
            }

            logger.info("Received incentive amount: {} for transaction: {}", incentive.getAmount(), transaction);
            return incentive;

        } catch (RestClientException e) {
            logger.error("Failed to communicate with incentive API for transaction: {}", transaction, e);
            return new Incentive(0.0f);
        } catch (Exception e) {
            logger.error("Unexpected error while getting incentive for transaction: {}", transaction, e);
            return new Incentive(0.0f);
        }
    }
}