package io.github.hvogel.clientes.service.validation.constraintvalidation;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import io.github.hvogel.clientes.service.validation.NotEmptyList;

public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List<?>> {

	@Override
	public boolean isValid(List<?> list, ConstraintValidatorContext context) {
		return list != null && !list.isEmpty();
	}

}
