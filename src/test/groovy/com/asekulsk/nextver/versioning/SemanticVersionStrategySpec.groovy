package com.asekulsk.nextver.versioning

import com.asekulsk.nextver.enumeration.VersionIncrementType
import spock.lang.Specification
import spock.lang.Unroll

class SemanticVersionStrategySpec extends Specification {

    def strategy = new SemanticVersionStrategy()

    @Unroll
    def "should increment #type version correctly"() {
        expect:
        strategy.getNextVersion(current, type) == expected

        where:
        current   | type                         || expected
        "1.2.3"   | VersionIncrementType.MAJOR   || "2.0.0"
        "1.2.3"   | VersionIncrementType.MINOR   || "1.3.0"
        "1.2.3"   | VersionIncrementType.PATCH   || "1.2.4"
        "0.9.9"   | VersionIncrementType.MAJOR   || "1.0.0"
        "2.5.9"   | VersionIncrementType.MINOR   || "2.6.0"
    }

    def "should throw exception for BUILD increment"() {
        when:
        strategy.getNextVersion("1.2.3", VersionIncrementType.BUILD)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "BUILD not supported for SEMVER"
    }

    def "should throw exception for invalid version format"() {
        when:
        strategy.getNextVersion("1.2", VersionIncrementType.PATCH)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Invalid semver: 1.2"
    }

    @Unroll
    def "validate should return #expected for version '#version'"() {
        expect:
        strategy.validate(version) == expected

        where:
        version     || expected
        "1.2.3"     || true
        "0.0.1"     || true
        "10.20.30"  || true
        "1.2"       || false
        "1.2.3.4"   || false
        "abc.def.1" || false
        ""          || false
    }
}
