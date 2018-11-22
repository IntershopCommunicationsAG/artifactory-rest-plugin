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
package com.intershop.gradle.artifactory.tasks.helm

import com.intershop.gradle.artifactory.ArtifactoryRestPlugin
import com.intershop.gradle.artifactory.getValue
import com.intershop.gradle.artifactory.setValue
import com.intershop.gradle.artifactory.tasks.AbstractArtifactoryRestTask
import com.intershop.gradle.artifactory.tasks.AbstractArtifactoryTask
import org.gradle.api.GradleException
import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.*
import org.jfrog.artifactory.client.ArtifactoryRequest
import org.jfrog.artifactory.client.impl.ArtifactoryRequestImpl
import java.io.File

class HelmPublish : AbstractArtifactoryTask() {

    private val targetRepoProperty = project.objects.property(String::class.java)
    private val inputFileProperty = this.newInputFile()

    init {
        group = ArtifactoryRestPlugin.COMPONENT_GROUP_NAME
    }

    @Suppress("unused")
    @get:Input
    var targetRepo: String by targetRepoProperty

    @get:SkipWhenEmpty
    @get:InputFile
    var file: File?
        get() = inputFileProperty.get().asFile
        set(value) = inputFileProperty.set(value)

    @Suppress("unused")
    @Throws(InvalidUserDataException::class)
    @TaskAction
    fun start() {
        val result = client?.repository("")?.upload("", file)?.doUpload()
        if(result == null) {
            throw GradleException("Return value of upload is empty!")
        }
    }
}