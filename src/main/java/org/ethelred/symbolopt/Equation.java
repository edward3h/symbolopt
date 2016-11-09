package org.ethelred.symbolopt;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Collection;
import java.util.Map;

/**
 * Created by edward on 11/8/16.
 */
public class Equation implements Constraint
{
    private final int result;
    private final Multiset<Symbol> symbols;

    public Equation(int result, Iterable<? extends Symbol> symbols)
    {
        this.result = result;
        this.symbols = HashMultiset.create(symbols);

    }

    public Collection<? extends Symbol> distinctSymbols()
    {
        return symbols.elementSet();
    }

    public int calculateScore(Map<Symbol, Integer> symbolIntegerMap)
    {
        int sum = 0;
        for (Symbol symbol: symbols)
        {
            sum += symbolIntegerMap.get(symbol);
        }
        return Math.abs(result - sum);
    }
}
