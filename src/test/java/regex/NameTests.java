package regex;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for regex pattern used for validating employee names in Main addEmployee().
 */
class NameTests {

    private final String pattern = "(" + // Everything in a group for the last quantify
            "[A-Z]" + // 1 uppercase letter
            "[a-z]{1,50}" + // 1-50 lowercase letters
            "[ ]?)" + // 0-1 space for the next word
            "+"; // All this 1-many times


    @Test()
    @DisplayName("Valid first name")
    public void ValidFirstName() {
        assertTrue(Pattern.matches(pattern, "Bob"));
    }

    @Test()
    @DisplayName("Valid full name")
    public void ValidFull() {
        assertTrue(Pattern.matches(pattern, "Bob The Builder"));
    }


    @Test()
    @DisplayName("Empty is invalid")
    public void Empty() {
        assertTrue(!Pattern.matches(pattern, ""));
    }

    @Test()
    @DisplayName("Single character in word is invalid")
    public void TooShort() {
        assertTrue(!Pattern.matches(pattern, "B"));
    }

    @Test()
    @DisplayName("51+ characters in word is invalid")
    public void TooLong() {
        assertTrue(!Pattern.matches(pattern, "Boooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooob"));
    }

    @Test()
    @DisplayName("Double spaces are invalid")
    public void DoubleSpace() {
        assertTrue(!Pattern.matches(pattern, "bob  the builder"));
    }

    @Test()
    @DisplayName("Non capital words are invalid")
    public void NoUppercase() {
        assertTrue(!Pattern.matches(pattern, "bob the builder"));
    }

    @Test()
    @DisplayName("Any non capital words are invalid")
    public void OnlyFirstUppercase() {
        assertTrue(!Pattern.matches(pattern, "Bob the builder"));
    }

    @Test()
    @DisplayName("Characters other than letters and spaces are invalid")
    public void InvalidChar() {
        assertTrue(!Pattern.matches(pattern, "B0b"));
    }

}
