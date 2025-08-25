package com.asekulsk.nextver.persistence.model

import com.asekulsk.nextver.security.crypto.KeyCrypto
import groovy.transform.Canonical
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table
@Canonical(includeFields = true)
class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(unique = true, nullable = false)
    String name

    @Column(name = "public_ssh_key", columnDefinition = "TEXT", nullable = false)
    private String key

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    List<Version> versions = []

    String getKey() {
        return key
    }

    void setKey(String key) {
        if (!KeyCrypto.IsValidSshKey(key)) {
            throw new IllegalArgumentException("Invalid SSH public key format")
        }
        this.key = key
    }

    void setName(String name) {
        if (!name) {
            throw new IllegalArgumentException("Project name must not be empty")
        }
        this.name = name
    }

    Project addVersion(Version version) {
        if (version == null) {
            return this
        }

        if (!versions.contains(version)) {
            versions << version
        }

        return this
    }

    Project removeVersion(Version version) {
        if (version == null) {
            return this
        }

        versions.remove(version)
        return this
    }

    void clearVersions() {
        versions.clear()
    }
}