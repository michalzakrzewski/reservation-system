package com.zakrzewski.reservationsystem.validator;

import com.zakrzewski.reservationsystem.dto.request.EmployeeCreateAccountRequest;
import com.zakrzewski.reservationsystem.exceptions.InvalidInputException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeValidator.class);

    public EmployeeValidator() {
    }

    public void validateEmployeeCreateAccountRequest(final EmployeeCreateAccountRequest employeeCreateAccountRequest) {
        LOG.info("Validate employee create account request");

        if (StringUtils.isBlank(employeeCreateAccountRequest.email())) {
            LOG.error("Employee email is blank");
            throw new InvalidInputException("Employee email is blank");
        }

        if (StringUtils.isBlank(employeeCreateAccountRequest.password())) {
            LOG.error("Employee password is blank");
            throw new InvalidInputException("Employee password is blank");
        }

        if (StringUtils.isBlank(employeeCreateAccountRequest.firstName())) {
            LOG.error("Employee first name is blank");
            throw new InvalidInputException("Employee first name is blank");
        }

        if (StringUtils.isBlank(employeeCreateAccountRequest.lastName())) {
            LOG.error("Employee last name is blank");
            throw new InvalidInputException("Employee last name is blank");
        }

        if (employeeCreateAccountRequest.team() == null) {
            LOG.error("Employee team is null");
            throw new InvalidInputException("Employee team is null");
        }
    }
}
