package com.ratiose.testtask.validator.impl;

import com.ratiose.testtask.validator.SimpleParamsValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleParamsValidatorImpl implements SimpleParamsValidator
{
    private static final String BLANK_YEAR_AND_MONTH_ERROR = "Year and month should not be blank";
    private static final String DATE_VALIDATION_ERROR =
            "Month should contain 2 digits, year - 4 digits and not be in future";
    private static final String BlANK_NAME_ERROR = "Input parameter should not be blank";
    private static final String INVALID_NAME_ERROR = "Input parameter should be at least 3 characters";

    private LocalDate now = LocalDate.now();

    @Override
    public List<String> validate(String name)
    {
        List<String> validationErrors = new ArrayList<>();
        if (isBlankParam(name))
        {
            validationErrors.add(BlANK_NAME_ERROR);
            return validationErrors;
        }
        if (!isValidName(name))
        {
            validationErrors.add(INVALID_NAME_ERROR);
        }
        return validationErrors;
    }

    @Override
    public List<String> validate (String year, String month)
    {
        List<String> validationErrors = new ArrayList<>();
        if (isBlankParam(year) || isBlankParam(month))
        {
            validationErrors.add(BLANK_YEAR_AND_MONTH_ERROR);
            return validationErrors;
        }

        if (isNotValidDate(year, month))
        {
            validationErrors.add(DATE_VALIDATION_ERROR);
        }
        return validationErrors;
    }

    private boolean isBlankParam(String param)
    {
        return StringUtils.isBlank(param);
    }

    private boolean isNotValidDate(String year, String month)
    {
        if (!NumberUtils.isDigits(year) || year.length() != 4)
        {
            return true;
        }
        if(!NumberUtils.isDigits(month) || month.length() != 2)
        {
            return true;
        }
        LocalDate date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
        return date.isAfter(now.withDayOfMonth(1));
    }

    private boolean isValidName (String name)
    {
        return name.length() > 2;
    }
}
