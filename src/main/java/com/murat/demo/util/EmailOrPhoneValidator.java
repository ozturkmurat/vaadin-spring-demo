package com.murat.demo.util;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;

@SuppressWarnings("serial")
public
class EmailOrPhoneValidator extends AbstractValidator<String> {

    private final EmailValidator emailValidator;

    public EmailOrPhoneValidator() {
        super("");
        emailValidator = new EmailValidator(
                "The string '{0}' is not a valid email address");
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        String val = value;
        // remove all spaces
        val = val.replace(" ", "");
        // if string starts from +0-9 ignoring spaces
        if (!startsWithCountryCode(val)) {
            return emailValidator.apply(value, context);
        }
        String digits = val.substring(1);
        // if string contains only + and digits (ignoring spaces)
        if (!hasOnlyDigits(digits)) {
            return ValidationResult.error(String.format(
                    "The string '%s' is not a valid phone number. "
                            + "Phone numbers should start with a plus sign followed by digits.",
                    value));
        }
        // now there should be at least 10 digits
        if (digits.length() >= 10) {
            return ValidationResult.ok();
        }
        return ValidationResult.error(String.format(
                "The string '%s' is not a valid phone number. "
                        + "Phone should start with a plus sign and contain at least 10 digits",
                value));
    }

    private boolean startsWithCountryCode(String phone) {
        return phone.length() >= 2 && phone.charAt(0) == '+'
                && Character.isDigit(phone.charAt(1));
    }

    private boolean hasOnlyDigits(String phone) {
        return phone.chars().allMatch(Character::isDigit);
    }

}
