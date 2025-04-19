package dev.rndmorris.utils.versioning;

import java.util.Objects;

public final class SemVer {

    public final int major;
    public final int minor;
    public final int patch;
    public final String tag;

    SemVer(int major, int minor, int patch, String tag) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.tag = tag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, tag);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SemVer other && major == other.major
                && minor == other.minor
                && patch == other.patch
                && Objects.equals(tag, other.tag);
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d%s", major, minor, patch, tag != null ? "-" + tag : "");
    }
}
