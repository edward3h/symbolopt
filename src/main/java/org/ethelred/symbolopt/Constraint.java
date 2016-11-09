package org.ethelred.symbolopt;

import java.util.Collection;
import java.util.Map;

/**
 * Created by edward on 11/9/16.
 */
public interface Constraint
{
    Collection<? extends Symbol> distinctSymbols();

    int calculateScore(Map<Symbol, Integer> symbolIntegerMap);
}
