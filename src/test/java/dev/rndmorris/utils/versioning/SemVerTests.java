package dev.rndmorris.utils.versioning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class SemVerTests {

    @ParameterizedTest
    @MethodSource("compareToInputs")
    void testCompareTo(SemVer input1, SemVer input2, int expected) {
        // Arrange
        // nothing to do

        // Act
        var result = input1.compareTo(input2);

        // Assert
        Assertions.assertEquals(expected, result);
    }

    static Stream<Arguments> compareToInputs() {
        return Stream.of(
            Arguments.of(p("0.0.0"), p("0.0.0"), 0),
            Arguments.of(p("0.0.0"), p("0.0.1"), -1),
            Arguments.of(p("0.0.3"), p("0.0.2"), 1),

            Arguments.of(p("0.4.0"), p("0.4.0"), 0),
            Arguments.of(p("0.4.0"), p("0.5.0"), -1),
            Arguments.of(p("0.7.0"), p("0.6.0"), 1),

            Arguments.of(p("7.0.0"), p("7.0.0"), 0),
            Arguments.of(p("7.0.0"), p("8.0.0"), -1),
            Arguments.of(p("9.0.0"), p("8.0.0"), 1),

            Arguments.of(p("1.0.0"), p("1.0.4"), -1),
            Arguments.of(p("1.0.0"), p("1.7.0"), -1),
            Arguments.of(p("1.0.0"), p("1.2.9"), -1),

            // a tag should always put the version behind that same version without the tag
            Arguments.of(p("1.0.0"), p("1.0.0-beta-1"), 1),
            Arguments.of(p("1.0.1"), p("1.0.1-beta-1"), 1),
            Arguments.of(p("1.1.1"), p("1.1.1-beta-1"), 1),

            // tags should break ties alphanumerically
            Arguments.of(p("1.0.0-beta-2"), p("1.0.0-beta-1"), 1),
            Arguments.of(p("1.0.1-beta-3"), p("1.0.1-beta-4"), -1),
            Arguments.of(p("1.1.1-beta-1"), p("1.1.1-beta-1"), 0)
        );
    }

    static SemVer p(String version) {
        return new SemVerParser().tryParseSemVer(version).result;
    }
}
