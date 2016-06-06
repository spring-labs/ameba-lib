/*
 * Copyright 2014-2015 the original author or authors.
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

import static org.assertj.core.api.Assertions.assertThat;

import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * A AbstractMongoDBIntegrationTests.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AbstractMongoDBIntegrationTests.TestConfig.class)
public abstract class AbstractMongoDBIntegrationTests {

        @Configuration
        @EnableMongoRepositories(basePackageClasses = AbstractMongoDBIntegrationTests.class, considerNestedRepositories = true)
        static class TestConfig extends AbstractMongoConfiguration {

            @Override
            protected String getDatabaseName() {
                return "database";
            }

            @Override
            public Mongo mongo() throws Exception {
                return new Fongo(getDatabaseName()).getMongo();
            }
        }

        @Autowired MongoOperations operations;

        @Before
        @After
        public void cleanUp() {
            operations
                    .getCollectionNames()
                    .stream()
                    .filter(collectionName ->
                            !collectionName.startsWith("system"))
                    .forEach(collectionName ->
                            operations.execute(collectionName, collection -> {
                                collection.remove(new BasicDBObject());
                                assertThat(collection.find().hasNext()).isFalse();
                                return null;
                            })
                    );
        }
    }