package io.github.hvogel.clientes.service.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;

@Email(regexp = ".+@.+\\..+|")
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ExtendedEmail {
	  @OverridesAttribute(constraint = Email.class, name = "message")
	  String message() default "Favor preencher o email corretamente.";

	  @OverridesAttribute(constraint = Email.class, name = "groups")
	  Class<?>[] groups() default {};

	  @OverridesAttribute(constraint = Email.class, name = "payload")
	  Class<? extends Payload>[] payload() default {};

}
