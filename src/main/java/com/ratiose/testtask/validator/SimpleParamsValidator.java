package com.ratiose.testtask.validator;

import java.util.List;

public interface SimpleParamsValidator
{
    List<String> validate(String name);
    List<String> validate(String year, String month);
}
