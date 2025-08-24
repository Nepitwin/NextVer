package com.asekulsk.nextver.persistence.interfaces

import com.asekulsk.nextver.persistence.model.Project

interface IProjectService {
    boolean register(String name, String key)
    Project getProjectByName(String name)
}