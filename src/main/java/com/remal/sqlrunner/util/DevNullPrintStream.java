package com.remal.sqlrunner.util;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * This is a replacement of the standard Java System.out stream, will print
 * everything to dev/null. Used in quiet mode.
 *
 * <p>Copyright 2021 Arnold Somogyi</p>
 *
 * @author arnold.somogyi@gmail.com
 */
public final class DevNullPrintStream {

    /**
     * Print stream that  prints to dev/null.
     *
     * @return the dev/null print stream
     */
    public static PrintStream getPrintStream() {
        return new PrintStream(new OutputStream() {
            public void write(int b) {
                // do nothing
            }
        });
    }

    /**
     * Utility classes should not have a public or default constructor.
     */
    private DevNullPrintStream() {
    }
}
