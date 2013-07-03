dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            driverClassName="com.mysql.jdbc.Driver"
            dialect="org.hibernate.dialect.MySQL5InnoDBDialect"
            url="jdbc:mysql://localhost:3306/smartpaper?useUnicode=true&characterEncoding=utf8"
            username="root"
            password="root"
            dbCreate="update"
        }
    }
    test {
        dataSource {
            driverClassName="com.mysql.jdbc.Driver"
            dialect="org.hibernate.dialect.MySQL5InnoDBDialect"
            url="jdbc:mysql://localhost:3306/smartpaper?useUnicode=true&characterEncoding=utf8"
            username="root"
            password="root"
            dbCreate="update"
        }
    }
    production {
        dataSource {
            driverClassName="com.mysql.jdbc.Driver"
            dialect="org.hibernate.dialect.MySQL5InnoDBDialect"
            url="jdbc:mysql://localhost:3306/smartpaper2?useUnicode=true&characterEncoding=utf8"
            username="root"
            password="Asp1Bs2Ga3"
            dbCreate="update"
        }
    }
    awsproduction {
        dataSource {
            driverClassName="com.mysql.jdbc.Driver"
            dialect="org.hibernate.dialect.MySQL5InnoDBDialect"
            url="jdbc:mysql://localhost:3306/smartpaper?useUnicode=true&characterEncoding=utf8&autoReconnect=true"
            username="root"
            password="g93gR4hu"
            dbCreate="update"
            properties {
                 validationQuery="select 1"
                 testWhileIdle=true
                 timeBetweenEvictionRunsMillis=60000

                 maxActive=50
                 maxIdle=25
                 minIdle=1
                 initialSize=1
            }
        }
    }
}
