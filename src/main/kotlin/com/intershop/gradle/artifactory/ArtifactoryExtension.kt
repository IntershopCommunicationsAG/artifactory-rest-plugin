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

import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * This class provides the main extension for the
 * artifactory rest plugin.
 *
 * @param project provides the current Gradle project instance.
 * @constructor Creates a pre configured extension
 */
open class ArtifactoryExtension @Inject constructor(project: Project) {

    // artifactory URL property
    private val artifactoryURLProperty: Property<String> = project.objects.property(String::class.java)
    // artifactory username property
    private val usernameProperty: Property<String> = project.objects.property(String::class.java)
    // artifactory password property
    private val passwordProperty: Property<String> = project.objects.property(String::class.java)
    // artifactory accesstoken property
    private val accesstokenProperty: Property<String> = project.objects.property(String::class.java)

    /**
     * This is the artifactory URL provider (read only)
     *
     * @property artifactoryURLProvider the provider of the artifactory URL
     */
    val artifactoryURLProvider: Provider<String?>
        get() = artifactoryURLProperty

    /**
     * The artifactory URL with the root context if necessary.
     *
     * @property artifactoryURL the artifactory url
     */
    var artifactoryURL by artifactoryURLProperty

    /**
     * This is the artifactory username provider (read only)
     *
     * @property usernameProvider the provider of the artifactory username
     */
    val usernameProvider: Provider<String?>
        get() = usernameProperty

    /**
     * The artifactory username.
     *
     * @property username the artifactory username
     */
    var username by usernameProperty

    /**
     * This is the artifactory user password (read only)
     *
     * @property passwordProvider the provider of the artifactory user password
     */
    val passwordProvider: Provider<String?>
        get() = passwordProperty

    /**
     * The artifactory user password.
     *
     * @property password the artifactory username
     */
    var password by passwordProperty

    /**
     * This is the artifactory accesstoken (read only)
     *
     * @property accesstokenProvider the provider of the artifactory accesstoken
     */
    val accesstokenProvider: Provider<String?>
        get() = accesstokenProperty

    /**
     * The artifactory access token.
     *
     * @property accesstoken the artifactory accesstoken string
     */
    var accesstoken by accesstokenProperty
}