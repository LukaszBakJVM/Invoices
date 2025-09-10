package org.lukasz.faktury.utils;

import jakarta.validation.ConstraintViolation;
import org.lukasz.faktury.exceptions.CustomValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;
import java.util.stream.Collectors;
@Component
public class ValidationImpl implements Validation {
    private final LocalValidatorFactoryBean validatorFactoryBean;
    private final Logger logger = LoggerFactory.getLogger(ValidationImpl.class);

    public ValidationImpl(LocalValidatorFactoryBean validatorFactoryBean) {
        this.validatorFactoryBean = validatorFactoryBean;
    }

    @Override
    public <T> void validation(T t) {
        Set<ConstraintViolation<T>> violations = validatorFactoryBean.validate(t);


        if (!violations.isEmpty()) {
            String errorMessage = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" , "));

            logger.error("inside validation  errors: {}", errorMessage);

            throw new CustomValidationException(errorMessage);
        }
    }
}
