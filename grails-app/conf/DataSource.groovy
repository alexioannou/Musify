hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class='org.hibernate.cache.EhCacheProvider'
}

environments {

    development {
        pooled = true
        dbCreate = "none"
        driverClassName = "org.postgresql.Driver"
        url = "jdbc:postgresql://localhost:5432/musify"
        username = "postgres"
        password = "admin"
        dialect = "org.hibernate.dialect.PostgreSQLDialect"
    }

    test {
        pooled = true
        dbCreate = "none"
        driverClassName = "org.postgresql.Driver"
        url = "jdbc:postgresql://localhost:5432/musify"
        username = "postgres"
        password = "admin"
        dialect = "org.hibernate.dialect.PostgreSQLDialect"
    }

    production {
        pooled = true
        dbCreate = "none"
        driverClassName = "org.postgresql.Driver"
        url = "jdbc:postgresql://localhost:5432/musify"
        username = "postgres"
        password = "admin"
        dialect = "org.hibernate.dialect.PostgreSQLDialect"
    }
}
