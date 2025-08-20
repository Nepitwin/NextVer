package com.asekulsk.nextver.persistence.model

import jakarta.persistence.*

@Entity
@Table
class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(unique = true, nullable = false)
    String name

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Version> versions = []
}
