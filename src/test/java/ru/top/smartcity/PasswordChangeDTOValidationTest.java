package ru.top.smartcity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.top.smartcity.DTOs.PasswordChangeDTO;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PasswordChangeDTOValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        PasswordChangeDTO dto = new PasswordChangeDTO("validOld", "validNew", "validNew");
        Set<ConstraintViolation<PasswordChangeDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

}
