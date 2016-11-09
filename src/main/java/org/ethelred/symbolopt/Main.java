package org.ethelred.symbolopt;

/**
 * Created by edward on 11/8/16.
 */
public class Main
{
    public static void main(String[] args)
    {
        SymbolOpt app = new SymbolOpt();
        app.parseArgs(args);
        app.run();
    }
}
