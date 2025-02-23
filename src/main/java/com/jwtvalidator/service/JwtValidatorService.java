package com.jwtvalidator.service;

import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtvalidator.exception.JwtValidationException;
import com.jwtvalidator.util.PrimeUtils;

@Service
public class JwtValidatorService {

    private static final Logger logger = LoggerFactory.getLogger(JwtValidatorService.class);

    private static final Set<String> VALID_ROLES = Set.of("Admin", "Member", "External");
    private static final int MAX_NAME_LENGTH = 256;

    public boolean validate(String jwt) {
        if (jwt == null || jwt.isEmpty()) {
            logger.warn("JWT is null or empty");
            throw new JwtValidationException("JWT is null or empty");
        }

        // Dividir o token em 3 partes (header, payload, signature)
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            logger.warn("Invalid JWT format. Expected 3 parts, found {}", parts.length);
            throw new JwtValidationException("Invalid JWT format. Expected 3 parts, found " + parts.length);
        }

        try {
            // Decodificar o payload usando Base64 URL
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode payload = mapper.readTree(payloadJson);

            // Verificar se há exatamente 3 claims
            Iterator<String> fieldNamesIterator = payload.fieldNames();
            Set<String> fieldNames = new HashSet<>();
            while (fieldNamesIterator.hasNext()) {
                fieldNames.add(fieldNamesIterator.next());
            }
            if (fieldNames.size() != 3 || 
                !fieldNames.contains("Name") || 
                !fieldNames.contains("Role") || 
                !fieldNames.contains("Seed")) {
                logger.warn("Invalid claims. Expected claims: [Name, Role, Seed]. Found claims: {}", fieldNames);
                throw new JwtValidationException("Invalid claims. Expected claims: [Name, Role, Seed]. Found: " + fieldNames);
            }

            // Validar a claim "Name": tamanho máximo e sem dígitos numéricos
            String name = payload.get("Name").asText();
            if (name.length() > MAX_NAME_LENGTH) {
                logger.warn("Name length exceeds maximum allowed ({}): {}", MAX_NAME_LENGTH, name);
                throw new JwtValidationException("Name length exceeds maximum allowed (" + MAX_NAME_LENGTH + "): " + name);
            }
            if (name.matches(".*\\d.*")) {
                logger.warn("Name contains numeric characters: {}", name);
                throw new JwtValidationException("Name contains numeric characters: " + name);
            }

            // Validar a claim "Role": deve ser um dos valores válidos
            String role = payload.get("Role").asText();
            if (!VALID_ROLES.contains(role)) {
                logger.warn("Invalid role: {}. Valid roles: {}", role, VALID_ROLES);
                throw new JwtValidationException("Invalid role: " + role + ". Valid roles: " + VALID_ROLES);
            }

            // Validar a claim "Seed": deve ser um número primo
            String seedStr = payload.get("Seed").asText();
            int seed;
            try {
                seed = Integer.parseInt(seedStr);
            } catch (NumberFormatException e) {
                logger.warn("Seed is not a valid integer: {}", seedStr);
                throw new JwtValidationException("Seed is not a valid integer: " + seedStr, e);
            }
            if (!PrimeUtils.isPrime(seed)) {
                logger.warn("Seed is not a prime number: {}", seed);
                throw new JwtValidationException("Seed is not a prime number: " + seed);
            }

            logger.info("JWT validated successfully");
            return true;

        } catch (Exception e) {
            logger.error("Exception occurred during JWT validation", e);
            throw new JwtValidationException("Exception occurred during JWT validation", e);
        }
    }
}
