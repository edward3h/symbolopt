package org.ethelred.symbolopt;

import java.util.Collection;
import java.util.function.ToDoubleFunction;

/**
 * Created by edward on 11/9/16.
 */
public interface Constraint
{
    Collection<Symbol> distinctSymbols();

    double calculateScore(ToDoubleFunction<Symbol> symbolValueAccessor);

    double getWeight();
}
