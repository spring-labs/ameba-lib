/*
 * Copyright 2015-2024 the original author or authors.
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
import org.ameba.app.BaseConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A AbstractMongoDBIntegrationTests.
 *
 * @author Heiko Scherrer
 */
@DataMongoTest
@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = AbstractMongoDBIntegrationTests.class, considerNestedRepositories = true)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BaseConfiguration.class})
public abstract class AbstractMongoDBIntegrationTests {

    static final String HOST = "localhost";
    private static final int PORT = 27017;
    static final String DATABASE = "database";
    @Autowired
    MongoTemplate template;
    @MockBean
    private Tracer tracer;
    /*
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
*/
    @AfterEach
    @BeforeEach
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