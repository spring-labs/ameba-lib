/*
 * Copyright 2015-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ameba.integration.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.ameba.app.BaseConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A AbstractMongoDBIntegrationTests.
 *
 * @author Heiko Scherrer
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AbstractMongoDBIntegrationTests.TestConfig.class, BaseConfiguration.class})
public abstract class AbstractMongoDBIntegrationTests {

    static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static MongodExecutable mongodExecutable;
    static final String DATABASE = "database";
    @Autowired
    MongoOperations template;

    @EnableMongoAuditing
    @Configuration
    @EnableMongoRepositories(basePackageClasses = AbstractMongoDBIntegrationTests.class, considerNestedRepositories = true)
    static class TestConfig extends AbstractMongoConfiguration {

        @Value("#{systemProperties['MONGO.URI'] ?: 'mongodb://" + HOST + "'}")
        private String uri;
        @Value("#{systemProperties['MONGO.DB'] ?: '" + DATABASE + "'}")
        private String database;

        @Override
        protected String getDatabaseName() {
            return database;
        }

        @Override
        public MongoClient mongoClient() {
            return new MongoClient(new MongoClientURI(uri));
        }
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(HOST, PORT, Network.localhostIsIPv6()))
                .build();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
    }

    @AfterClass
    public static void afterClass() {
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }

    @After
    @Before
    public void aroundTests() throws Exception {
        cleanUp();
    }

    private void cleanUp() {
        template
                .getCollectionNames()
                .stream()
                .filter(collectionName ->
                        !collectionName.startsWith("system"))
                .forEach(collectionName ->
                        template.execute(collectionName, collection -> {
                            collection.deleteOne(new BasicDBObject());
                            assertThat(collection.find().iterator().hasNext()).isFalse();
                            return null;
                        })
                );
    }
}