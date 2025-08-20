package com.asekulsk.nextver.persistence.interfaces

import com.asekulsk.nextver.persistence.model.Project

interface IProjectService {
    boolean register(String name)
    Project getProjectByName(String name)
}