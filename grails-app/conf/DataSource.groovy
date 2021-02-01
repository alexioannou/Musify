dataSource {
    pooled = true
    dbCreate = "none"
    driverClassName = "org.postgresql.Driver"
    url = "jdbc:postgresql://localhost:5432/musify"
    username = "postgres"
    password = "admin"
    dialect = "org.hibernate.dialect.PostgreSQLDialect"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class='org.hibernate.cache.EhCacheProvider'
}
