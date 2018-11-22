/*
 * Copyright 2018 Intershop Communications AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.intershop.gradle.artifactory

import com.intershop.gradle.test.AbstractIntegrationSpec
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Unroll

class IntPromoteSpec extends AbstractIntegrationSpec {

    @Unroll
    def 'Simple test - #gradleVersion'(gradleVersion){
        given:
        buildFile << """
        plugins {
            id 'com.intershop.gradle.artifactory.rest'
        }
        
        import com.intershop.gradle.artifactory.tasks.docker.Promote
        
        artifactory {
        }
        
        task promote(type: Promote, group: 'Docker') {
            targetRepo = 'local-docker-dev'
            repoKey = 'local-docker-snapshots'
            imagePath = 'icm-test/iste-execution'
            tag = '1.0.0'
            targetTag = '1.0.0-SNAPSHOT'
        }
        
        """.stripIndent()

        when:
        List<String> args = ['promote', '-s', '-i']
        def result1 = getPreparedGradleRunner()
                .withArguments(args)
                .withGradleVersion(gradleVersion)
                .build()

        then:
        result1.task(':promote').outcome == TaskOutcome.SKIPPED

        where:
        gradleVersion << supportedGradleVersions
    }
}
