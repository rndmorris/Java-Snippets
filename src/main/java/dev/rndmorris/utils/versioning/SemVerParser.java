package dev.rndmorris.utils.versioning;

import java.util.Objects;

public class SemVerParser {

    private int major = -1;
    private int minor = -1;
    private int patch = -1;

    /**
     * Attempt to parse a semver string.
     * @return An object that contains either the parsed semver data or an error message.
     */
    public ParseResult tryParseSemVer(String input) {
        if (input == null) {
            return new ParseResult(ErrorReason.InputNull);
        }
        input = input.trim();
        if (input.isEmpty()) {
            return new ParseResult(ErrorReason.InputBlank);
        }

        // reset outputs
        major = -1;
        minor = -1;
        patch = -1;
        String tag = null;

        int value = 0;
        boolean parsedDigit = false;
        for (var index = 0; index < input.length(); ++index) {
            var c = input.charAt(index);

            if (index == 0 && c == 'v') {
                // allow a single 'v' prefix
                continue;
            }

            if ('0' <= c && c <= '9') {
                // continue parsing the version
                value = (value * 10) + (c - '0');
                parsedDigit = true;
                continue;
            }

            if ('.' == c && (major < 0 || minor < 0)) {
                setNext(value);
                value = 0;
                parsedDigit = false;
                continue;
            }

            if ('-' == c) {
                if (major < 0 || minor < 0 || !parsedDigit) {
                    return new ParseResult(ErrorReason.MisplacedTag);
                }
                // everything after this is tag suffix, so save the last value
                setNext(value);

                // everything (if anything) after this goes in the tag
                if (index + 1 < input.length()) {
                    tag = input.substring(index + 1).trim();
                    // blank tags should be treated as not existing
                    if (tag.isEmpty()) {
                        tag = null;
                    }
                }
                break;
            }
            return new ParseResult(c);
        }

        // at the end of the string, set whatever value is left
        if (parsedDigit) {
            setNext(value);
        }

        return new ParseResult(
                new SemVer(
                        major, // major will always be at least 0 at this point
                        Math.max(0, minor),
                        Math.max(0, patch),
                        tag));
    }

    private void setNext(int value) {
        if (major < 0) {
            major = value;
        } else if (minor < 0) {
            minor = value;
        } else if (patch < 0) {
            patch = value;
        }
    }

    public static class ParseResult {

        public final SemVer result;
        public final ErrorReason error;
        public final char invalidCharacter;

        public ParseResult(SemVer result) {
            this.result = Objects.requireNonNull(result);
            this.error = null;
            this.invalidCharacter = (char) 0;
        }

        public ParseResult(ErrorReason error) {
            this.result = null;
            this.error = Objects.requireNonNull(error);
            this.invalidCharacter = (char) 0;
        }

        public ParseResult(char invalidCharacter) {
            this.result = null;
            this.error = ErrorReason.InvalidCharacter;
            this.invalidCharacter = invalidCharacter;
        }
    }

    public enum ErrorReason {

        InputNull("Argument `input` is null."),
        InputBlank("Argument `input` is blank."),
        InvalidCharacter("Encountered invalid character."),
        MisplacedTag("The version tag indicator character `-` was discovered before major, minor, and patch versions."),
        ;

        public final String errorMessage;

        ErrorReason(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
