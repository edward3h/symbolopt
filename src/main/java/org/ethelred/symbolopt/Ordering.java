package org.ethelred.symbolopt;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.ToDoubleFunction;

/**
 * Created by edward on 11/9/16.
 */
public class Ordering implements Constraint
{
    private final ImmutableList<Symbol> valuesInOrder;
    private static double weight = 1.0D;

    public Ordering(Iterable<? extends Symbol> symbols)
    {
        valuesInOrder = ImmutableList.copyOf(symbols);
    }

    public static void setWeight(double weight)
    {
        Ordering.weight = weight;
    }

    @Override
    public Collection<Symbol> distinctSymbols()
    {
        return new HashSet<>(valuesInOrder);
    }

    public double calculateScore(ToDoubleFunction<Symbol> symbolValueAccessor)
    {
        double sum = 0;
        double min = 0;
        for (int i = 1; i < valuesInOrder.size(); i++)
        {
            min = Math.min(symbolValueAccessor.applyAsDouble(valuesInOrder.get(i-1)), min);
            double current = symbolValueAccessor.applyAsDouble(valuesInOrder.get(i));
            if(current > min)
            {
                sum += current - min;
            }
        }
        return sum;
    }

    @Override
    public double getWeight()
    {
        return weight;
    }
}
