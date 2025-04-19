package dev.rndmorris.utils.versioning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SemVerParserTests {

    private final SemVerParser instance;

    public SemVerParserTests() {
        instance = new SemVerParser();
    }

    @ParameterizedTest
    @MethodSource("validInputs")
    void testValidInputs(SemVer expected, String input) {
        // Arrange
        // nothing to do

        // Act
        var parseResult = instance.tryParseSemVer(input);

        // Assert
        assertNull(parseResult.error);
        assertEquals(expected, parseResult.result);
    }

    static Stream<Arguments> validInputs() {
        return Stream.of(
            Arguments.of(new SemVer(1, 0, 0, null), "1.0.0"),
            Arguments.of(new SemVer(0, 1, 0, null), "0.1.0"),
            Arguments.of(new SemVer(0, 0, 1, null), "0.0.1"),

            Arguments.of(new SemVer(1, 0, 0, "prerelease-1"), "1.0.0-prerelease-1"),
            Arguments.of(new SemVer(0, 1, 0, "prerelease-1"), "0.1.0-prerelease-1"),
            Arguments.of(new SemVer(0, 0, 1, "prerelease-1"), "0.0.1-prerelease-1"),

            // Blank/empty tags should be treated as not existing
            Arguments.of(new SemVer(0, 0, 0, null), "0.0.0-"),

            Arguments.of(new SemVer(1, 0, 0, null), "1"),
            Arguments.of(new SemVer(1, 0, 0, null), "1.0"),
            Arguments.of(new SemVer(1, 1, 0, null), "1.1"),
            Arguments.of(new SemVer(0, 1, 0, null), "0.1"),

            // We allow an optional "v" prefix, even if it's not necessarily part of semver
            Arguments.of(new SemVer(1, 0, 0, null), "v1.0.0"),
            Arguments.of(new SemVer(0, 1, 0, null), "v0.1.0"),
            Arguments.of(new SemVer(0, 0, 1, null), "v0.0.1"),

            Arguments.of(new SemVer(1, 0, 0, "prerelease-1"), "v1.0.0-prerelease-1"),
            Arguments.of(new SemVer(0, 1, 0, "prerelease-1"), "v0.1.0-prerelease-1"),
            Arguments.of(new SemVer(0, 0, 1, "prerelease-1"), "v0.0.1-prerelease-1"),

            Arguments.of(new SemVer(0, 0, 0, null), "v0.0.0-")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInputs")
    void testInvalidInputs(String input) {
        // Arrange
        // nothing to do

        // Act
        var parseResult = instance.tryParseSemVer(input);

        // Assert
        assertNull(parseResult.result);
        assertNotNull(parseResult.error);
    }

    static Stream<Arguments> invalidInputs() {
        return Stream.of(
                Arguments.of("abcd"),
                Arguments.of("-1.0.0"),
                Arguments.of("0.-1.0"),
                Arguments.of("0.0.-1"),
                Arguments.of("this is totally a semver string"));
    }

}
