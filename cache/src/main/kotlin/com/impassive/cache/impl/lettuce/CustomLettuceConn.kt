package com.impassive.cache.impl.lettuce

import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.masterreplica.MasterReplica
import java.io.Closeable

/**
 * @author impassive
 */
class CustomLettuceConn<K, V>(
    cfg: CustomLettuceConnConfig<K, V>
) : Closeable {

    val conn: StatefulRedisConnection<K, V> =
        if (cfg.onlySingleNode()) {
            cfg.client.connect(cfg.codec, cfg.masterUri)
        } else {
            val uris = mutableListOf(cfg.masterUri)
            uris.addAll(cfg.replicaUris)
            MasterReplica.connect(cfg.client, cfg.codec, uris)
        }


    override fun close() {
        conn.close()
    }

}