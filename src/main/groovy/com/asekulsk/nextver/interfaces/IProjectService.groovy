package com.asekulsk.nextver.interfaces

import com.asekulsk.nextver.persistence.Project

interface IProjectService {
    boolean register(String name)
    Project getProjectByName(String name)
}