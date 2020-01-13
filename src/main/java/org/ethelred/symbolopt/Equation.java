package org.ethelred.symbolopt;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Set;
import java.util.function.ToDoubleFunction;

/**
 * Created by edward on 11/8/16.
 */
public class Equation implements Constraint
{
    private final double result;
    private final Set<Symbol> symbols;
    private static double weight = 1.0D;

    public Equation(double result, Iterable<Symbol> symbols)
    {
        this.result = result;
        this.symbols = ImmutableSet.copyOf(symbols);

    }

    public static void setWeight(double weight)
    {
        Equation.weight = weight;
    }

    public Collection<Symbol> distinctSymbols()
    {
        return symbols;
    }

    public double calculateScore(ToDoubleFunction<Symbol> symbolValueAccessor)
    {
        double sum = 0;
        for (Symbol symbol: symbols)
        {
            sum += symbolValueAccessor.applyAsDouble(symbol);
        }
        return Math.abs(result - sum);
    }

    @Override
    public double getWeight()
    {
        return weight;
    }
}
