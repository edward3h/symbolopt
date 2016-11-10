package org.ethelred.symbolopt;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.ethelred.util.Args4jBoilerplate;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by edward on 11/8/16.
 */
public class SymbolOpt extends Args4jBoilerplate
{
    @Option(name = "--population", usage = "population size")
    private int populationSize = 100;
    @Option(name = "--generations", usage = "generations to run")
    private int generations = 100;

    @Option(name = "--data", required = true, usage = "Path to data file")
    private Path datafile;

    private Set<Constraint> constraints;
    private Set<Symbol> symbols;
    private List<Individual> population;
    private Random random = new Random();

    void run()
    {
        try
        {
            _readData();

            _generateInitialPopulation();

            Comparator<Individual> comparing = Comparator.comparing(Individual::score);
            for (int i = 0; i < generations; i++)
            {
                Collections.sort(population, comparing);
                if(isVerbose())
                {
                    System.err.println(population.get(0).score());
                }
                _repopulate();
            }

            Collections.sort(population, comparing);
            _showResults();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void _showResults()
    {
        System.out.println(
            population.get(0)
        );
    }

    private void _repopulate()
    {
        if(population.size() > populationSize)
        {
            population.subList(populationSize - 1, population.size()).clear();
        }
        int halfIndex = (population.size() / 2);
        population.subList(halfIndex, population.size()).clear();
        for (int i = 0; i < (halfIndex - 1); i += 2)
        {
            population.add(
                    population.get(i).cross(population.get(i+1), random::nextBoolean)
            );
        }
        for (int i = 0; i < halfIndex; i++)
        {
            population.add(population.get(i).mutate(()->random.nextInt(5) - 2));
        }
        for( int i = 0; i < halfIndex; i++)
        {
            population.add(new Individual(symbols, ()->random.nextInt(20), this::_calculateScore));
        }
    }

    private void _generateInitialPopulation()
    {
        population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++)
        {
            population.add(new Individual(symbols, ()->random.nextInt(20), this::_calculateScore));
        }
    }

    private double _calculateScore(Map<Symbol, Integer> symbolIntegerMap)
    {
        double sum = 0;
        for (Constraint e:
                constraints)
        {
            sum += e.calculateScore(symbolIntegerMap) * e.getWeight();
        }
        return sum;
    }

    private void _readData() throws IOException
    {
        constraints = new HashSet<>();
        symbols = new TreeSet<>();

        Multiset<Symbol> checkSymbolUniques = HashMultiset.create();
        Files.lines(datafile, StandardCharsets.UTF_8)
                .filter(Objects::nonNull)
                .map(this::_removeComments)
                .filter(s -> !s.trim().isEmpty())
                .forEach(line -> {
                    Constraint e = _constraintFromLine(line);
                    if(e != null)
                    {
                        constraints.add(e);
                        symbols.addAll(e.distinctSymbols());
                        checkSymbolUniques.addAll(e.distinctSymbols());
                    }
                });

        for (Multiset.Entry<Symbol> e:
                checkSymbolUniques.entrySet())
        {
            constraints.add(new UniqueSymbol(e.getElement(), e.getCount()));
        }
    }

    private Constraint _constraintFromLine(String line)
    {
        String[] parts = line.split("\\s+");
        int lastIndex = parts.length - 1;
        if(_isInteger(parts[0]))
        {
            return new Equation(Integer.parseInt(parts[0]), Arrays.stream(parts, 1, lastIndex).map(Symbol::new).collect(Collectors.toList()));
        }
        else if(_isInteger(parts[lastIndex]))
        {
            return new Equation(Integer.parseInt(parts[lastIndex]), Arrays.stream(parts, 0, lastIndex - 1).map(Symbol::new).collect(Collectors.toList()));
        }
        else if(">".equals(parts[0]))
        {
            return new Ordering(Arrays.stream(parts, 1, lastIndex).map(Symbol::new).collect(Collectors.toList()));
        }
        else
        {
            System.err.println(
                    "Invalid line:\n\t" + line
            );
        }
        return null;
    }

    private boolean _isInteger(String s)
    {
        try
        {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    private String _removeComments(String line)
    {
        String[] parts = line.split("[#;]");
        return parts[0];
    }
}
