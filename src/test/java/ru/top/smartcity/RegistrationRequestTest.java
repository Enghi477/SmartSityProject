package ru.top.smartcity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import ru.top.smartcity.DTOs.RegistrationRequest;

import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationRequestTest {

    private final Validator validator;

    public RegistrationRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrect_thenNoViolations() {
        RegistrationRequest request = new RegistrationRequest(
                "Иван",
                "Иванов",
                "ivan@example.com",
                "password123",
                "+79123456789",
                25
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }


    @Test
    void whenFirstNameTooShort_thenOneViolation() {
        RegistrationRequest request = new RegistrationRequest(
                "И",
                "Иванов",
                "ivan@example.com",
                "password123",
                "+79123456789",
                25
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Имя должно быть от 2 до 50 символов", violations.iterator().next().getMessage());
    }


    @Test
    void whenEmailInvalid_thenOneViolation() {
        RegistrationRequest request = new RegistrationRequest(
                "Иван",
                "Иванов",
                "invalid-email",
                "password123",
                "+79123456789",
                25
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Некорректный email", violations.iterator().next().getMessage());
    }

    @Test
    void whenPasswordTooShort_thenOneViolation() {
        RegistrationRequest request = new RegistrationRequest(
                "Иван",
                "Иванов",
                "ivan@example.com",
                "pass",
                "+79123456789",
                25
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Пароль должен содержать минимум 6 символов", violations.iterator().next().getMessage());
    }

    @Test
    void whenPhoneInvalid_thenOneViolation() {
        RegistrationRequest request = new RegistrationRequest(
                "Иван",
                "Иванов",
                "ivan@example.com",
                "password123",
                "123",
                25
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Некорректный номер телефона", violations.iterator().next().getMessage());
    }

    @Test
    void whenAgeTooLow_thenOneViolation() {
        RegistrationRequest request = new RegistrationRequest(
                "Иван",
                "Иванов",
                "ivan@example.com",
                "password123",
                "+79123456789",
                17
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Возраст не должен быть меньше 18", violations.iterator().next().getMessage());
    }

    @Test
    void whenAgeTooHigh_thenOneViolation() {
        RegistrationRequest request = new RegistrationRequest(
                "Иван",
                "Иванов",
                "ivan@example.com",
                "password123",
                "+79123456789",
                101
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Возраст не должен быть больше 100", violations.iterator().next().getMessage());
    }

    @Test
    void whenAgeNull_thenOneViolation() {
        RegistrationRequest request = new RegistrationRequest(
                "Иван",
                "Иванов",
                "ivan@example.com",
                "password123",
                "+79123456789",
                null
        );

        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Возраст обязателен", violations.iterator().next().getMessage());
    }

}
