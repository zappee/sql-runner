package com.remal.sqlrunner.util;

/**
 * Color in console using System.out.println.
 *
 * @author arnold.somogyi@gmail.com
 */
public class AnsiColor {

    /**
     * Default color of the terminal.
     */
    public static final String DEFAULT = "\033[0m";

    /**
     * Bold high intensity red color.
     */
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";

    /**
     * Bold high intensity green color.
     */
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m";

    /**
     * Yellow text.
     */
    public static final String YELLOW = "\033[0;33m";

    /**
     * Bright yellow text.
     */
    public static final String YELLOW_BRIGHT = "\033[0;93m";

    /**
     * Bright blue text.
     */
    public static final String BLUE_BRIGHT = "\033[0;94m";

    /**
     * Utility classes should not have a public or default constructor.
     */
    private AnsiColor() {
    }
}
