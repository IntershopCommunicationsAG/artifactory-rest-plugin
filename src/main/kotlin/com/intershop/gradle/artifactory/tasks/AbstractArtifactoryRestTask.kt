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

import org.jfrog.artifactory.client.ArtifactoryClientBuilder
import com.intershop.gradle.artifactory.ArtifactoryExtension
import com.intershop.gradle.artifactory.ArtifactoryRestPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Task
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.TaskAction
import org.jfrog.artifactory.client.Artifactory
import org.jfrog.artifactory.client.ArtifactoryRequest

abstract class AbstractArtifactoryRestTask: DefaultTask() {

    init {
        this.setOnlyIf( object: Spec<Task> {
            override fun isSatisfiedBy(task: Task): Boolean  {
                kotlin.io.println("----------")
                kotlin.io.println(client)
                return client != null
            }
        })
    }

    @Suppress("unused")
    @Throws(InvalidUserDataException::class)
    @TaskAction
    fun start() {
        if(client != null) {
            val response = client?.restCall(request)
            if (response != null && ! response.isSuccessResponse) {
                response.rawBody
                throw GradleException("Request ${request} failed!" + response.rawBody)
            }
        }
    }

    abstract val request: ArtifactoryRequest

    val client: Artifactory?
        get() {
            val extension = project.extensions.findByName(ArtifactoryRestPlugin.EXTENSION_NAME)
            if(extension != null && extension is ArtifactoryExtension) {
                if(extension.artifactoryURL.isNotBlank()) {
                    val artifactoryExtension = extension
                    val artifactory = ArtifactoryClientBuilder.create()
                            .setUrl(artifactoryExtension.artifactoryURL)
                    if (artifactoryExtension.username.isNotBlank() && artifactoryExtension.password.isNotBlank()) {
                        artifactory.setUsername(artifactoryExtension.username)
                        artifactory.setPassword(artifactoryExtension.password)
                    }
                    if (artifactoryExtension.accesstoken.isNotBlank()) {
                        artifactory.setAccessToken(artifactoryExtension.accesstoken)
                    }
                    return artifactory.build()
                } else {
                    project.logger.warn("There is no URL specified '{}'!", ArtifactoryRestPlugin.EXTENSION_NAME)
                    return null
                }
            } else {
                project.logger.warn("No extension '{}' found!", ArtifactoryRestPlugin.EXTENSION_NAME)
                return null
            }
        }
}