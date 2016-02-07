package com.mazebert.entities;

public class Version implements Comparable<Version> {
    private int major;
    private int minor;
    private int bugfix;

    public Version(String version) {
        parseFromString(version);
    }

    private void parseFromString(String version) {
        if (version != null) {
            String[] components = version.split("\\.");
            major = parseComponent(components, 0);
            minor = parseComponent(components, 1);
            bugfix = parseComponent(components, 2);
        }
    }

    private int parseComponent(String[] components, int index) {
        try {
            return Integer.parseInt(components[index]);
        } catch (Throwable e) {
            return 0;
        }
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getBugfix() {
        return bugfix;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + bugfix;
    }

    @Override
    public int compareTo(Version other) {
        if (major > other.major) {
            return 1;
        } else if (major < other.major) {
            return -1;
        }

        if (minor > other.minor) {
            return 1;
        } else if (minor < other.minor) {
            return -1;
        }

        if (bugfix > other.bugfix) {
            return 1;
        } else if (bugfix < other.bugfix) {
            return -1;
        }

        return 0;
    }
}
