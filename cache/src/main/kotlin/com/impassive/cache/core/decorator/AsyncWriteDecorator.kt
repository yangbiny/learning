package com.impassive.cache.core.decorator

import com.impassive.cache.core.CustomCache
import com.impassive.cache.core.tools.ApiParamValidateToos
import java.util.concurrent.ThreadPoolExecutor

/**
 * @author impassive
 */
class AsyncWriteDecorator<K, V>(
    cache: CustomCache<K, V>,
    private val asyncWriteExecutor: ThreadPoolExecutor
) : BaseDecorator<K, V>(cache) {

    init {
        ApiParamValidateToos.checkNotNull(asyncWriteExecutor, "asyncWriteExecutor can not be null")
    }

    override fun put(k: K, v: V) {
        asyncWriteExecutor.execute { super.put(k, v) }
    }

    override fun rm(key: K) {
        asyncWriteExecutor.execute { super.rm(key) }
    }

    override fun multiPut(map: Map<K, V>) {
        asyncWriteExecutor.execute { super.multiPut(map) }
    }

    override fun multiRm(keys: Collection<K>) {
        asyncWriteExecutor.execute { super.multiRm(keys) }
    }
}