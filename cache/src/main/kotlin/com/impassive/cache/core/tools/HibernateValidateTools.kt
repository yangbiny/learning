package com.impassive.cache.core.tools

import org.hibernate.validator.HibernateValidator
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

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