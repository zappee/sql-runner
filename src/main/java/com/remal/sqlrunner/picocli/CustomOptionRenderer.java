package com.remal.sqlrunner.picocli;

import picocli.CommandLine.Help;
import picocli.CommandLine.Help.Ansi.Text;
import picocli.CommandLine.Help.ColorScheme;
import picocli.CommandLine.IHelpFactory;
import picocli.CommandLine.INegatableOptionTransformer;
import picocli.CommandLine.Model.CommandSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This is a fix for picocli framework.
 * This hides the Java variable name from the command line help.</p>
 *
 * <p>Copyright 2021 Arnold Somogyi</p>
 *
 * @author arnold.somogyi@gmail.com
 */
public class CustomOptionRenderer implements IHelpFactory {

    /**
     * Returns a {@code Help} instance to assist in rendering the usage help message
     *
     * @param commandSpec the command to create usage help for
     * @param colorScheme the color scheme to use when rendering usage help
     * @return a {@code Help} instance
     */
    @Override
    public Help create(CommandSpec commandSpec, ColorScheme colorScheme) {
        return new Help(commandSpec, colorScheme) {
            @Override
            public IOptionRenderer createDefaultOptionRenderer() {
                return (option, ignored, scheme) -> {
                    String shortOption = option.shortestName(); // assumes every option has a short option
                    String longOption = option.longestName(); // assumes every option has a short and a long option

                    if (option.negatable()) { // ok to omit if you don't have negatable options
                        INegatableOptionTransformer transformer = option.command().negatableOptionTransformer();
                        shortOption = transformer.makeSynopsis(shortOption, option.command());
                        longOption = transformer.makeSynopsis(longOption, option.command());
                    }

                    // assume one line of description text (may contain embedded %n line separators)
                    String[] description = option.description();
                    Text[] descriptionFirstLines = scheme.text(description[0]).splitLines();

                    Text empty = Ansi.OFF.text("");
                    List<Text[]> result = new ArrayList<>();
                    result.add(new Text[] {
                            scheme.optionText(
                                    String.valueOf(option.command().usageMessage().requiredOptionMarker())
                            ),
                            scheme.optionText(shortOption),
                            scheme.text(","), // we assume every option has a sho
                            scheme.optionText(longOption), // just the option name without parameter
                            descriptionFirstLines[0]
                    });

                    for (int i = 1; i < descriptionFirstLines.length; i++) {
                        result.add(new Text[]{empty, empty, empty, empty, descriptionFirstLines[i]});
                    }

                    if (option.command().usageMessage().showDefaultValues()) {
                        Text defaultValue = scheme.text("  Default: " + option.defaultValueString(true));
                        result.add(new Text[]{empty, empty, empty, empty, defaultValue});
                    }

                    return result.toArray(new Text[result.size()][]);
                };
            }
        };
    }
}
