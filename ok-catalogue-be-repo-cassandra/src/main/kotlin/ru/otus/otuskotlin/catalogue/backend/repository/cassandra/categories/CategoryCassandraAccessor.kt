package ru.otus.otuskotlin.catalogue.backend.repository.cassandra.categories

import com.datastax.driver.core.ResultSet
import com.datastax.driver.mapping.annotations.Accessor
import com.datastax.driver.mapping.annotations.Param
import com.datastax.driver.mapping.annotations.Query
import com.google.common.util.concurrent.ListenableFuture

@Accessor
interface CategoryCassandraAccessor {

    @Query(QUERY_RENAME)
    fun rename(
            @Param("id") id: String,
            @Param("new_label") label: String,
            @Param("lock_key") optLockKey: String,
            @Param("new_lock") nextLockKey: String
    ): ListenableFuture<ResultSet>

    companion object{
        const val QUERY_RENAME = "UPDATE categories SET label = :new_label, lock_version = :new_lock " +
                "WHERE id = :id IF lock_version = :lock_key;"
    }
}