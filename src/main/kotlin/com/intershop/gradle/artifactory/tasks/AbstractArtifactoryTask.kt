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

package com.intershop.gradle.artifactory.tasks

import com.intershop.gradle.artifactory.ArtifactoryExtension
import com.intershop.gradle.artifactory.ArtifactoryRestPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction
import org.jfrog.artifactory.client.Artifactory
import org.jfrog.artifactory.client.ArtifactoryClientBuilder
import org.jfrog.artifactory.client.ArtifactoryRequest

abstract class AbstractArtifactoryTask: DefaultTask() {

    init {
        this.setOnlyIf {
            client != null
        }
    }

    protected val client: Artifactory?
        get() {
            val extension = project.extensions.findByName(ArtifactoryRestPlugin.EXTENSION_NAME)
            if(extension != null && extension is ArtifactoryExtension) {
                return if(extension.artifactoryURL.isNotBlank()) {
                    val artifactory = ArtifactoryClientBuilder.create()
                            .setUrl(extension.artifactoryURL)
                    if (extension.username.isNotBlank() && extension.password.isNotBlank()) {
                        artifactory.username = extension.username
                        artifactory.password = extension.password
                    }
                    if (extension.accesstoken.isNotBlank()) {
                        artifactory.accessToken = extension.accesstoken
                    }
                    artifactory.build()
                } else {
                    project.logger.warn("There is no URL specified '{}'!", ArtifactoryRestPlugin.EXTENSION_NAME)
                    null
                }
            } else {
                project.logger.warn("No extension '{}' found!", ArtifactoryRestPlugin.EXTENSION_NAME)
                return null
            }
        }
}
