package org.ethelred.symbolopt;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Created by edward on 11/8/16.
 */
public class Symbol implements Comparable<Symbol>
{
    private final String value;

    public Symbol(String s)
    {
        this.value = s;
    }

    @Override
    public int compareTo(Symbol o)
    {
        return value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(value, symbol.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(value);
    }

    @Override
    public String toString()
    {
        return value;
    }
}
