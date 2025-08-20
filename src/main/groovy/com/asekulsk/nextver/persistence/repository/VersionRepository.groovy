package com.asekulsk.nextver.persistence.repository

import com.asekulsk.nextver.persistence.model.Version
import org.springframework.data.jpa.repository.JpaRepository

interface VersionRepository extends JpaRepository<Version, Long> {

}
