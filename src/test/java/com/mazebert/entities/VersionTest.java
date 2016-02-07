package com.mazebert.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class VersionTest {
    private Version version;

    @Test
    public void parse_nullString() {
        givenVersion(null);
        thenVersionIs(0, 0, 0);
    }

    @Test
    public void parse_emptyString() {
        givenVersion("");
        thenVersionIs(0, 0, 0);
    }

    @Test
    public void parse_major() {
        givenVersion("1.0.0");
        thenVersionIs(1, 0, 0);
    }

    @Test
    public void parse_minor() {
        givenVersion("0.1.0");
        thenVersionIs(0, 1, 0);
    }

    @Test
    public void parse_bugfix() {
        givenVersion("0.0.1");
        thenVersionIs(0, 0, 1);
    }

    @Test
    public void parse_integration() {
        givenVersion("10.9.1024");
        thenVersionIs(10, 9, 1024);
    }

    @Test
    public void toStringFormatting() {
        givenVersion("1.3.14");
        assertEquals("1.3.14", version.toString());
    }

    @Test
    public void compareTo_equals() {
        givenVersion("1.2.3");
        assertEquals(0, version.compareTo(new Version("1.2.3")));
    }

    @Test
    public void compareTo_greaterMajor() {
        givenVersion("2.2.3");
        assertEquals(1, version.compareTo(new Version("1.2.3")));
    }

    @Test
    public void compareTo_greaterMinor() {
        givenVersion("1.3.3");
        assertEquals(1, version.compareTo(new Version("1.2.3")));
    }

    @Test
    public void compareTo_greaterBugfix() {
        givenVersion("1.2.4");
        assertEquals(1, version.compareTo(new Version("1.2.3")));
    }

    @Test
    public void compareTo_lesserMajor() {
        givenVersion("0.2.3");
        assertEquals(-1, version.compareTo(new Version("1.2.3")));
    }

    @Test
    public void compareTo_lesserMinor() {
        givenVersion("1.1.3");
        assertEquals(-1, version.compareTo(new Version("1.2.3")));
    }

    @Test
    public void compareTo_lesserBugfix() {
        givenVersion("1.2.2");
        assertEquals(-1, version.compareTo(new Version("1.2.3")));
    }

    private void givenVersion(String version) {
        this.version = new Version(version);
    }

    private void thenVersionIs(int major, int minor, int bugfix) {
        assertEquals(major, version.getMajor());
        assertEquals(minor, version.getMinor());
        assertEquals(bugfix, version.getBugfix());
    }
}