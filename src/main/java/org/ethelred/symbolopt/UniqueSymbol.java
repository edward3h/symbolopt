package org.ethelred.symbolopt;

import java.util.Collection;
import java.util.Map;

/**
 * Created by edward on 11/9/16.
 */
public class UniqueSymbol implements Constraint
{
    private final Symbol value;
    private final int divisor;

    public UniqueSymbol(Symbol symbol, int count)
    {
        this.value = symbol;
        this.divisor = count * count;
    }

    @Override
    public Collection<? extends Symbol> distinctSymbols()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public double calculateScore(Map<Symbol, Integer> symbolIntegerMap)
    {
        return Math.abs(((double) symbolIntegerMap.get(value)) / divisor);
    }

    @Override
    public double getWeight()
    {
        return 1;
    }
}
