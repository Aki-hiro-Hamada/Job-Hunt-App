package com.example.jobapp.config;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.mongodb.ConnectionString;

/**
 * Render / Atlas の「どこに接続しているか」切り分け用。
 * 機密情報（ユーザー名/パスワード）は出さず、ホスト名とDB名のみログ出力する。
 */
@Component
public class MongoConnectionInfoLogger implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MongoConnectionInfoLogger.class);

    @Value("${spring.data.mongodb.uri:}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database:}")
    private String mongoDatabase;

    @Override
    public void run(ApplicationArguments args) {
        if (mongoUri == null || mongoUri.isBlank()) {
            log.warn("MongoDB: spring.data.mongodb.uri is empty. (Embedded Mongo may be used depending on runtime)");
            return;
        }

        try {
            ConnectionString cs = new ConnectionString(mongoUri);
            String hosts = cs.getHosts() == null ? "" : cs.getHosts().stream().collect(Collectors.joining(","));
            String dbFromUri = cs.getDatabase();
            log.info("MongoDB connection target: hosts=[{}], database(property)=[{}], database(uri)=[{}]",
                    hosts,
                    (mongoDatabase == null ? "" : mongoDatabase),
                    (dbFromUri == null ? "" : dbFromUri));
        } catch (Exception e) {
            log.warn("MongoDB: failed to parse spring.data.mongodb.uri for diagnostics (will not log uri).", e);
        }
    }
}

