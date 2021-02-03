dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"
    username = "postgres"
    password = "admin"
    dialect = "org.hibernate.dialect.PostgreSQLDialect"
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class='org.hibernate.cache.EhCacheProvider'
}

environments {
    development {
        dataSource {
            dbCreate = "none"
            url = "jdbc:postgresql://localhost:5432/musify"
        }
    }
    test {
        dataSource {
            dbCreate = "none"
            url = "jdbc:postgresql://localhost:5432/musify"
        }
    }
    production {
        dataSource {
            dbCreate = "none"
            url = "jdbc:postgresql://localhost:5432/musify"
        }
    }
}