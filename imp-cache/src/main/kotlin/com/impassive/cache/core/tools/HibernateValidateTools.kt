package com.impassive.cache.core.tools

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.hibernate.validator.HibernateValidator

/**
 * @author impassive
 */
object HibernateValidateTools {

    private val factory: ValidatorFactory = Validation.byProvider(HibernateValidator::class.java)
        .configure()
        .failFast(true)
        .buildValidatorFactory()

    private val validator: Validator = factory.validator

    @Throws(RuntimeException::class)
    fun checkAndThrow(obj: Any) {
        val validate = validator.validate(obj)
        validate.apply {
            if (this.isEmpty()) {
                return
            }
            val violations = this.take(1)[0]
            val message = violations?.run {
                violations.propertyPath.toString() + ":" + violations.message
            }
            throw RuntimeException(message)
        }
    }

}