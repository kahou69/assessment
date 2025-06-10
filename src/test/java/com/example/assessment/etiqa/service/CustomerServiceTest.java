package com.example.assessment.etiqa.service;

import com.example.assessment.etiqa.exception.InvalidEmailException;
import com.example.assessment.etiqa.model.EmailType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {

    private final CustomerService service = new CustomerService(null);

    @Test
    void shouldPassWhenOfficeEmailIsPresentAndValid() {
        Map<EmailType, String> emails = new HashMap<>();
        emails.put(EmailType.OFFICE, "test@company.com");
        emails.put(EmailType.PERSONAL, "test@personal.com");

        assertDoesNotThrow(() -> {
            service.validateEmails(emails);
        });

    }

    @Test
    void shouldThrowIfNoRequiredEmailIsPresent() {
        Map<EmailType, String> emails = new HashMap<>();

        InvalidEmailException exception = assertThrows(
                InvalidEmailException.class,
                () -> {
                    service.validateEmails(emails);
                }
        );

        assertTrue(exception.getMessage().contains("At least one"));
    }

    @Test
    void shouldThrowIfEmailFormatsIsInvalid() {
        Map<EmailType, String> emails = new HashMap<>();
        emails.put(EmailType.OFFICE, "bad-email");

        InvalidEmailException exception = assertThrows(
                InvalidEmailException.class,
                () -> {
                    service.validateEmails(emails);
                }
        );

        assertTrue(exception.getMessage().contains("Invalid format"));
    }

    @Test
    void shouldThrowIfUnknownEmailTypePresent() {
        Map<EmailType, String> emails = new HashMap<>();
        emails.put(EmailType.OFFICE, "someone@office.com");
        emails.put(null, "someone@office.com");

        InvalidEmailException exception = assertThrows(
                InvalidEmailException.class,
                () -> {
                    service.validateEmails(emails);
                }
        );

        assertTrue(exception.getMessage().contains("Invalid email type"));
    }
}
