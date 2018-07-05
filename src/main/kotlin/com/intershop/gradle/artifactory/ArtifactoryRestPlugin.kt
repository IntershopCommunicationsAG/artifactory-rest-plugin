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

import com.intershop.gradle.artifactory.tasks.docker.Promote
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin Class for 'com.interhsop.gradle.artifactory.rest'
 */
class ArtifactoryRestPlugin : Plugin<Project> {

    companion object {
        internal const val TASKDESCRIPTION = "Use Docker Promote rest api of Artifactory to handle Docker Images"
        const val COMPONENT_GROUP_NAME = "Artifactory Tasks"

        /**
         * Extension name of this plugin.
         */
        const val EXTENSION_NAME = "artifactory"

        // dublicated from artifactory-gradle-plugin
        const val REPO_BASEURL_ENV = "ARTIFACTORYBASEURL"
        const val REPO_BASEURL_PRJ = "artifactoryBaseURL"

        const val REPO_USER_NAME_ENV = "ARTIFACTORYUSERNAME"
        const val REPO_USER_NAME_PRJ = "artifactoryUserName"

        const val REPO_USER_PASSWORD_ENV = "ARTIFACTORYUSERPASSWD"
        const val REPO_USER_PASSWORD_PRJ = "artifactoryUserPasswd"

        const val REPO_ACCESSTOKEN_ENV = "ARTIFACTORYACCESSTOKEN"
        const val REPO_ACCESSTOKEN_PRJ = "artifactoryAccessToken"

        fun getVaribale(project: Project, envVar: String, projectVar: String, defaultValue: String): String {
            var returnValue = defaultValue
            if(System.getProperty(envVar) != null) {
                project.logger.debug("Specified from system property {}.", envVar)
                returnValue = System.getProperty(envVar).trim()
            } else if(System.getenv(envVar) != null) {
                project.logger.debug("Specified from system environment property {}.", envVar)
                returnValue = System.getenv(envVar).toString().trim()
            } else if(project.hasProperty(projectVar) && project.property(projectVar).toString().trim().isNotEmpty()) {
                project.logger.debug("Specified from project property {}.", projectVar)
                returnValue = project.property(projectVar).toString().trim()
            }

            return returnValue
        }
    }

    /**
     * Applies the plugin functionality to the configured project.
     *
     * @param project the current project
     */
    override fun apply(project: Project) {
        with(project) {
            logger.info("Artifactory rest plugin adds extension {} to {}", EXTENSION_NAME, name)

            val extension = extensions.findByType(ArtifactoryExtension::class.java)
                    ?: extensions.create(EXTENSION_NAME, ArtifactoryExtension::class.java, this)

            extension.artifactoryURL = getVaribale(project, REPO_BASEURL_ENV, REPO_BASEURL_PRJ, "")
            extension.username = getVaribale(project, REPO_USER_NAME_ENV, REPO_USER_NAME_PRJ, "")
            extension.password = getVaribale(project, REPO_USER_PASSWORD_ENV, REPO_USER_PASSWORD_PRJ, "")
            extension.accesstoken = getVaribale(project, REPO_ACCESSTOKEN_ENV, REPO_ACCESSTOKEN_PRJ, "")

            if(tasks.findByName("promote") == null) {
                val task = tasks.create("promote", Promote::class.java)
                task.description = TASKDESCRIPTION
            }
        }
    }
}
