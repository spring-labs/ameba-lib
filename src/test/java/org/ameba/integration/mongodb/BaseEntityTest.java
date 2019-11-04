/*
 * Copyright 2014-2018 the original author or authors.
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

import org.ameba.app.BaseConfiguration;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A BaseEntityTest.
 *
 * @author Heiko Scherrer
 * @version 1.0
 * @since 1.6
 */
@ComponentScan(basePackageClasses = {BaseEntityTest.class, BaseConfiguration.class})
public class BaseEntityTest extends AbstractMongoDBIntegrationTests {

    @Test
    public void testPk() {
        TestDocument td = new TestDocument();
        template.insert(td);

        List<TestDocument> all = template.findAll(TestDocument.class);
        assertThat(all).hasSize(1);
        assertThat(all.get(0).isNew()).isFalse();
        assertThat(all.get(0).getPk()).isNotNull();
        assertThat(all.get(0).getCreateDt()).isNotNull();
        assertThat(all.get(0).getLastModifiedDt()).isNotNull();
        assertThat(all.get(0)).extracting("ol").contains(0L);
    }
}
