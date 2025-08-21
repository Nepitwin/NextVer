package com.asekulsk.nextver.util

import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.model.Version

class PersistenceGenerator {

    private static Long project_id = 1
    private static Long version_id = 1

    static String GenerateRandomString(int length) {
        def letters = ('A'..'Z') + ('a'..'z')
        def name = (1..length).collect { letters[new Random().nextInt(letters.size())] }.join()
        name.capitalize()
    }

    static Project GenerateProject(String name = GenerateRandomString(20), List<Version> versions = [])
    {
        var project = new Project()
        project.id = project_id
        project.name = name
        project.versions = versions

        project_id++

        project
    }

    static Version GenerateVersion(String name = GenerateRandomString(20), Project project = null, String versionString, VersionType type) {
        var version = new Version()
        version.id = version_id
        version.name = name
        version.project = project
        version.version = versionString
        version.type = type

        version_id++

        version
    }
}
