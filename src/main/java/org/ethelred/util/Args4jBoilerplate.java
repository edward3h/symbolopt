package org.ethelred.util;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.spi.OptionHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by edward on 11/8/16.
 */
public abstract class Args4jBoilerplate
{
    @Option(name = "--help", usage = "Show option help", aliases={"-?", "-h"})
    private boolean m_showHelp = false;
    @Option(name = "--debug", usage = "Enable logging", aliases = {"--verbose", "-v"})
    private boolean m_enableLogger = false;

    // parser is temporarily retained so that exceptions can be thrown correctly
    private CmdLineParser parser;

    public void parseArgs(String[] args)
    {
        parser = new CmdLineParser(this);
        try
        {
            // parse the arguments.
            parser.parseArgument(args);
            afterParse(parser);
        }
        catch (CmdLineException e)
        {
            // if there's a problem in the command line,
            // you'll get this exception. this will report
            // an error message.
            System.err.println(e.getMessage());
            _printUsage(parser);


            System.exit(1);
        }

        if (m_showHelp)
        {
            _printUsage(parser);
            System.exit(0);
        }
        parser = null;
    }

    protected CmdLineParser getParser()
    {
        return parser;
    }

    /**
     * extension hook to validate arguments after parsing. e.g. if there is validation which depends on multiple fields
     * @throws CmdLineException
     */
    protected void afterParse(CmdLineParser parser) throws CmdLineException
    {

    }

    private void _printUsage(CmdLineParser parser)
    {
        parser.setUsageWidth(getConsoleWidth(80));
        System.err.print("java " + getClass().getSimpleName() + " [options...]");
        for(OptionHandler oh: parser.getArguments())
        {
            System.err.print(' ');
            if(!oh.option.required())
            {
                System.err.print('[');
            }
            System.err.print(oh.getNameAndMeta(null));
            if (oh.option.isMultiValued()) {
                System.err.print(" ...");
            }

            if(!oh.option.required())
            {
                System.err.print(']');
            }
        }
        System.err.println();
        // print the list of available options
        parser.printUsage(new OutputStreamWriter(System.err), null, OptionHandlerFilter.ALL);
        System.err.println();
    }

    protected boolean isVerbose()
    {
        return m_enableLogger;
    }

    /**
     * Attempt to get the width (number of columns) of the console the app is running in. I tested this on Mac and Linux
     * - I don't expect it to work on Windows.
     * @param defaultWidth default to use if it couldn't get the actual width
     * @return defaultWidth if it couldn't get the value, width otherwise
     */
    public static int getConsoleWidth(int defaultWidth)
    {
        try
        {
            // from http://stackoverflow.com/questions/1286461/can-i-find-the-console-width-with-java
            Process p = Runtime.getRuntime().exec(new String[] {
                    "bash", "-c", "tput cols 2> /dev/tty" });
            // I'm not sure how the redirection in the command makes it work, but I tested it out and it does
            // see http://svn.ordoacerbus.com/scrapbook/scrapbook/org/ethelred/test/ConsoleTest.java
            byte[] bytes = ByteStreams.toByteArray(p.getInputStream());
            String processOutput = new String(bytes, Charsets.UTF_8).trim();
            int result = parseInt(processOutput, defaultWidth);
            return result;
        }
        catch (IOException e)
        {
            // TODO
        }
        return defaultWidth;
    }

    private static int parseInt(String s, int defaultValue)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            // ignore
        }
        return defaultValue;
    }
}

