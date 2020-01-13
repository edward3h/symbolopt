package org.ethelred.symbolopt;

import java.util.Collection;
import java.util.function.ToDoubleFunction;

/**
 * Created by edward on 11/9/16.
 */
public class UniqueSymbol implements Constraint
{
    private final Symbol symbol;
    private final double divisor;
    private static double weight = 1.0D;

    public UniqueSymbol(Symbol symbol, int count)
    {
        this.symbol = symbol;
        this.divisor = count * count;
    }

    public static void setWeight(double weight)
    {
        UniqueSymbol.weight = weight;
    }

    @Override
    public Collection<Symbol> distinctSymbols()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public double calculateScore(ToDoubleFunction<Symbol> symbolValueAccessor)
    {
        return Math.abs(symbolValueAccessor.applyAsDouble(symbol) / divisor);
    }

    @Override
    public double getWeight()
    {
        return weight;
    }
}
