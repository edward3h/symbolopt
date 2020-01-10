package org.ethelred.symbolopt;

import com.google.common.base.MoreObjects;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.ToDoubleFunction;

/**
 * Created by edward on 11/8/16.
 */
public class Individual
{
    private final SortedMap<Symbol, Double> values;
    private final ToDoubleFunction<Map<Symbol, Double>> scoreCallback;
    private boolean scoreCalculated = false;
    private double scoreMemo;

    public Individual(Set<Symbol> symbols, DoubleSupplier doubleSupplier, ToDoubleFunction<Map<Symbol, Double>> scoreCallback)
    {
        values = new TreeMap<>();
        for(Symbol symbol: symbols)
        {
            values.put(symbol, doubleSupplier.getAsDouble());
        }
        this.scoreCallback = scoreCallback;
    }

    private Individual(SortedMap<Symbol, Double> values, ToDoubleFunction<Map<Symbol, Double>> scoreCallback)
    {
        this.values = values;
        this.scoreCallback = scoreCallback;
    }

    public double score()
    {
        if(!scoreCalculated)
        {
            scoreMemo = scoreCallback.applyAsDouble(values);
            scoreCalculated = true;
        }
        return scoreMemo;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("values", values)
                .toString();
    }

    public Individual cross(Individual other, BooleanSupplier booleanSupplier)
    {
        SortedMap<Symbol, Double> mixedMap = new TreeMap<>();
        for(Symbol k: values.keySet())
        {
            if(booleanSupplier.getAsBoolean())
            {
                mixedMap.put(k, values.get(k));
            }
            else
            {
                mixedMap.put(k, other.values.get(k));
            }
        }
        return new Individual(mixedMap, scoreCallback);
    }

    public Individual mutate(DoubleSupplier doubleSupplier)
    {
        SortedMap<Symbol, Double> mixedMap = new TreeMap<>();
        for(Map.Entry<Symbol, Double> entry: values.entrySet())
        {
            mixedMap.put(entry.getKey(), entry.getValue() + doubleSupplier.getAsDouble());
        }
        return new Individual(mixedMap, scoreCallback);
    }
}
