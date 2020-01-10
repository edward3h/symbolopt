package org.ethelred.symbolopt;

import java.util.Collection;
import java.util.Map;

/**
 * Created by edward on 11/9/16.
 */
public class UniqueSymbol implements Constraint
{
    private final Symbol value;
    private final double divisor;
    private static double weight = 1.0D;

    public UniqueSymbol(Symbol symbol, int count)
    {
        this.value = symbol;
        this.divisor = count * count;
    }

    public static void setWeight(double weight)
    {
        UniqueSymbol.weight = weight;
    }

    @Override
    public Collection<? extends Symbol> distinctSymbols()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public double calculateScore(Map<Symbol, Double> symbolMap)
    {
        return Math.abs(symbolMap.get(value) / divisor);
    }

    @Override
    public double getWeight()
    {
        return weight;
    }
}
