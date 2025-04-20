package dev.rndmorris.utils.versioning;

import java.util.Objects;

public final class SemVer implements Comparable<SemVer> {

    public final int major;
    public final int minor;
    public final int patch;
    public final String tag;

    SemVer(int major, int minor, int patch, String tag) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        if (tag != null) {
            tag = tag.trim();
            if (tag.isEmpty()) {
                tag = null;
            }
        }
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

    @Override
    public int compareTo(SemVer o) {
        var diff = major - o.major;
        if (diff != 0) {
            return Math.min(Math.max(diff, -1), 1);
        }
        diff = minor - o.minor;
        if (diff != 0) {
            return Math.min(Math.max(diff, -1), 1);
        }
        diff = patch - o.patch;
        if (diff != 0) {
            return Math.min(Math.max(diff, -1), 1);
        }
        if (tag == null && o.tag == null) {
            return 0;
        }
        if (tag == null) {
            return 1;
        }
        if (o.tag == null) {
            return -1;
        }
        return Math.min(Math.max(tag.compareTo(o.tag), -1), 1);
    }
}
