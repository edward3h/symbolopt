package org.ethelred.symbolopt;

import com.google.common.base.MoreObjects;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;

/**
 * Created by edward on 11/8/16.
 */
public class Candidate
{
    private final SortedMap<Symbol, Double> values;
    private final ToDoubleFunction<ToDoubleFunction<Symbol>> scoreCallback;
    private boolean scoreCalculated = false;
    private double scoreMemo;

    public Candidate(Set<Symbol> symbols, DoubleSupplier doubleSupplier, ToDoubleFunction<ToDoubleFunction<Symbol>> scoreCallback)
    {
        values = new TreeMap<>();
        for(Symbol symbol: symbols)
        {
            values.put(symbol, doubleSupplier.getAsDouble());
        }
        this.scoreCallback = scoreCallback;
    }

    private Candidate(SortedMap<Symbol, Double> values, ToDoubleFunction<ToDoubleFunction<Symbol>> scoreCallback)
    {
        this.values = values;
        this.scoreCallback = scoreCallback;
    }

    public double score()
    {
        if(!scoreCalculated)
        {
            scoreMemo = scoreCallback.applyAsDouble(values::get);
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

    public Candidate cross(Candidate other, BooleanSupplier booleanSupplier)
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
        return new Candidate(mixedMap, scoreCallback);
    }

    public Candidate mutate(DoubleSupplier doubleSupplier)
    {
        SortedMap<Symbol, Double> mixedMap = new TreeMap<>();
        for(Map.Entry<Symbol, Double> entry: values.entrySet())
        {
            mixedMap.put(entry.getKey(), entry.getValue() + doubleSupplier.getAsDouble());
        }
        return new Candidate(mixedMap, scoreCallback);
    }
}
