/*
Copyright 2023 the original author, Lam Tong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package io.github.lamtong.maria.init.migrate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库迁移代理, 聚合所有数据库迁移操作.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @see SchemaMigrator
 * @since 0.0.1
 */
@Component
public class SchemaMigratorProxy implements SchemaMigrator {

    private static final Logger logger = LoggerFactory.getLogger(SchemaMigratorProxy.class);

    private static final List<SchemaMigrator> MIGRATORS = new LinkedList<>();

    static {
        MIGRATORS.add(new PreSchemaMigrator());
        MIGRATORS.add(new MariaWebSchemaMigrator());
        MIGRATORS.add(new MariaNacosSchemaMigrator());
        MIGRATORS.add(new MariaZipkinSchemaMigrator());
        MIGRATORS.add(new MariaJobSchemaMigrator());
        MIGRATORS.add(new PostSchemaMigrator());
    }

    public static List<String> getSchemaNames() {
        return MIGRATORS.stream()
                .filter(migrator -> migrator instanceof SchemaNameProvider)
                .map(migrator -> ((SchemaNameProvider) migrator))
                .map(SchemaNameProvider::schemaName)
                .collect(Collectors.toList());
    }

    @PostConstruct
    @Override
    public boolean migrate() {
        if (logger.isInfoEnabled()) {
            logger.info("Prepare to start data migration.");
        }
        for (SchemaMigrator migrator : MIGRATORS) {
            boolean success = migrator.migrate();
            if (migrator instanceof SchemaNameProvider) {
                SchemaNameProvider nameProvider = (SchemaNameProvider) migrator;
                if (!success) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Error while migrating schema [{}], please check scripts.", nameProvider.schemaName());
                    }
                    return false;
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("Schema [{}] has been migrated successfully.", nameProvider.schemaName());
                    }
                }
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("Finish data migration.");
        }
        return true;
    }

}
