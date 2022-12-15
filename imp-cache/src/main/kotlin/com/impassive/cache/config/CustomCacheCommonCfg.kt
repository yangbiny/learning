package com.impassive.cache.config

import java.time.Duration

/**
 * @author impassive
 */
data class CustomCacheCommonCfg(
    val ttlAfterWrite: Duration
)
