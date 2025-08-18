package com.asekulsk.nextver.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "projects")
class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(unique = true, nullable = false)
    String name

    @Column(nullable = false)
    String versionFormat = "SEMVER"  // SEMVER, FOUR_PART, CUSTOM_PATTERN

    String customPattern
}
