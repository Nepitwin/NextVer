package com.asekulsk.nextver.unit.domain.versioning

import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.versioning.FourPartStrategy
import spock.lang.Specification
import spock.lang.Unroll

class FourPartStrategySpec extends Specification {

    def strategy = new FourPartStrategy()

    @Unroll
    def "should increment #type version correctly"() {
        expect:
        strategy.getNextVersion(current, type) == expected

        where:
        current     | type                         || expected
        "1.2.3.4"   | VersionIncrementType.MAJOR   || "2.0.0.0"
        "1.2.3.4"   | VersionIncrementType.MINOR   || "1.3.0.0"
        "1.2.3.4"   | VersionIncrementType.PATCH   || "1.2.4.0"
        "1.2.3.4"   | VersionIncrementType.BUILD   || "1.2.3.5"
        "0.9.9.9"   | VersionIncrementType.MAJOR   || "1.0.0.0"
        "2.5.9.7"   | VersionIncrementType.MINOR   || "2.6.0.0"
        "3.4.5.99"  | VersionIncrementType.PATCH   || "3.4.6.0"
    }

    def "should throw exception for invalid version format"() {
        when:
        strategy.getNextVersion("1.2.3", VersionIncrementType.PATCH)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Invalid four-part version: 1.2.3"
    }

    @Unroll
    def "validate should return #expected for version '#version'"() {
        expect:
        strategy.validate(version) == expected

        where:
        version        || expected
        "1.2.3.4"      || true
        "0.0.0.1"      || true
        "10.20.30.40"  || true
        "1.2.3"        || false
        "1.2.3.4.5"    || false
        "abc.1.2.3"    || false
        ""             || false
    }
}
