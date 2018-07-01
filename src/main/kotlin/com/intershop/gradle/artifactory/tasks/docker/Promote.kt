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

package com.intershop.gradle.artifactory.tasks.docker

import com.intershop.gradle.artifactory.getValue
import com.intershop.gradle.artifactory.setValue
import com.intershop.gradle.artifactory.tasks.AbstractArtifactoryRestTask
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.jfrog.artifactory.client.ArtifactoryRequest
import org.jfrog.artifactory.client.impl.ArtifactoryRequestImpl

open class Promote: AbstractArtifactoryRestTask() {

    private val targetRepoProperty = project.objects.property(String::class.java)
    private val repoKeyProperty = project.objects.property(String::class.java)
    private val imagePathProperty = project.objects.property(String::class.java)
    private val tagProperty = project.objects.property(String::class.java)
    private val targetTagProperty = project.objects.property(String::class.java)
    private val operationProperty = project.objects.property(String::class.java)

    init {
        operationProperty.set("")
    }

    @Suppress("unused")
    @get:Input
    var targetRepo: String by targetRepoProperty

    @Suppress( "unused")
    fun provideTargetRepo(targetRepo: Provider<String>)
            = targetRepoProperty.set(targetRepo)

    @Suppress("unused")
    @get:Input
    var repoKey: String by repoKeyProperty

    @Suppress( "unused")
    fun provideRepoKey(repoKey: Provider<String>)
            = repoKeyProperty.set(repoKey)

    @Suppress("unused")
    @get:Input
    var imagePath: String by imagePathProperty

    @Suppress( "unused")
    fun provideImagePath(imagePath: Provider<String>)
            = imagePathProperty.set(imagePath)

    @Suppress("unused")
    @get:Input
    var tag: String by tagProperty

    @Suppress( "unused")
    fun provideTag(tag: Provider<String>)
            = tagProperty.set(tag)

    @Suppress("unused")
    @get:Input
    var targetTag: String by targetTagProperty

    @Suppress( "unused")
    fun provideTargetTag(targetTag: Provider<String>)
            = targetTagProperty.set(targetTag)

    @Suppress("unused")
    @get:Optional
    @get:Input
    var operation: String by operationProperty

    @Suppress( "unused")
    fun provideOperation(operation: Provider<String>)
            = operationProperty.set(operation)

    override val request: ArtifactoryRequest
        get() {
            val ops = if(operation.equals("move", true))  "false" else "true"

            val requestObj = hashMapOf(
                    "targetRepo" to targetRepo,
                    "repoKey" to repoKey,
                    "dockerRepository" to imagePath,
                    "tag" to tag,
                    "targetTag" to targetTag,
                    "copy" to ops
            )
            return ArtifactoryRequestImpl().apiUrl("api/docker/${repoKey}/v2/promote")
                    .method(ArtifactoryRequest.Method.POST)
                    .requestType(ArtifactoryRequest.ContentType.JSON)
                    .responseType(ArtifactoryRequest.ContentType.JSON)
                    .requestBody(requestObj)
        }
}