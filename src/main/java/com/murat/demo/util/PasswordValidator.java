
package com.murat.demo.util;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.StringLengthValidator;

@SuppressWarnings("serial")
public class PasswordValidator extends StringLengthValidator {

    public PasswordValidator() {
        super("", 6, Integer.MAX_VALUE);
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        ValidationResult result = super.apply(value, context);
        if (result.isError()) {
            return ValidationResult
                    .error("Password should contain at least 6 characters");
        } else if (!hasDigit(value) || !hasLetter(value)) {
            return ValidationResult
                    .error("Password must contain a letter and a number");
        }
        return result;
    }

    private boolean hasDigit(String pwd) {
        return pwd.chars().anyMatch(Character::isDigit);
    }

    private boolean hasLetter(String pwd) {
        return pwd.chars().anyMatch(Character::isLetter);
    }
}
