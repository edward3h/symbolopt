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
    private final double result;
    private final Multiset<Symbol> symbols;
    private static double weight = 1.0D;

    public Equation(double result, Iterable<? extends Symbol> symbols)
    {
        this.result = result;
        this.symbols = HashMultiset.create(symbols);

    }

    public static void setWeight(double weight)
    {
        Equation.weight = weight;
    }

    public Collection<? extends Symbol> distinctSymbols()
    {
        return symbols.elementSet();
    }

    public double calculateScore(Map<Symbol, Double> symbolMap)
    {
        double sum = 0;
        for (Symbol symbol: symbols)
        {
            sum += symbolMap.get(symbol);
        }
        return Math.abs(result - sum);
    }

    @Override
    public double getWeight()
    {
        return weight;
    }
}
