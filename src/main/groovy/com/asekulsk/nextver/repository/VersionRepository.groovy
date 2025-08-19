package com.asekulsk.nextver.repository

import com.asekulsk.nextver.persistence.Version
import org.springframework.data.jpa.repository.JpaRepository

interface VersionRepository extends JpaRepository<Version, Long> {

}
