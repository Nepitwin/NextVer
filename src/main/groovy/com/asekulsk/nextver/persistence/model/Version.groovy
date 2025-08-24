package com.asekulsk.nextver.persistence.model

import com.asekulsk.nextver.domain.enumeration.VersionType
import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.Canonical
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table
@Canonical
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
}
