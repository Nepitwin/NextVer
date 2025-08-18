package com.asekulsk.nextver.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "versions")
class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(nullable = false)
    String project

    @Column(nullable = false)
    String versionString

    @Column(nullable = false)
    boolean released = true
}
