package com.impassive.cache.core

import com.impassive.cache.core.tools.HibernateValidateTools

/**
 * @author impassive
 */
interface Validatable {

    fun validate() {
        HibernateValidateTools.checkAndThrow(this)
    }

}