package org.ethelred.symbolopt;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by edward on 11/9/16.
 */
public class Ordering implements Constraint
{
    private final ImmutableList<Symbol> valuesInOrder;

    public Ordering(Iterable<? extends Symbol> symbols)
    {
        valuesInOrder = ImmutableList.copyOf(symbols);
    }

    @Override
    public Collection<? extends Symbol> distinctSymbols()
    {
        return new HashSet<>(valuesInOrder);
    }

    public double calculateScore(Map<Symbol, Integer> symbolIntegerMap)
    {
        int sum = 0;
        int min = 0;
        for (int i = 1; i < valuesInOrder.size(); i++)
        {
            min = Math.min(symbolIntegerMap.get(valuesInOrder.get(i-1)), min);
            int current = symbolIntegerMap.get(valuesInOrder.get(i));
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
        return 1;
    }
}
