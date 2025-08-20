package com.asekulsk.nextver.persistence.model

import com.asekulsk.nextver.domain.enumeration.VersionType
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table
class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(nullable = false)
    String name

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    VersionType type

    @Column(nullable = false)
    String version

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    Project project
}
