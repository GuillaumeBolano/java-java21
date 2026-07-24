package java17.ex07;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

import org.junit.Test;

import java17.data.Data;
import java17.data.domain.Pizza;

/**
 * Exercice 08 - Stream Parallel - Collections
 */
public class Stream_07_Test {

    // Voici une méthode qui exécute 10 fois un traitement et retourne le meilleur temps (le plus court)
    private long monitor(Runnable fn) {

        long fastest = Long.MAX_VALUE;

        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            fn.run();
            long end = System.nanoTime();
            long duration = (end - start) / 1_000_000;
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }

    // TODO créer une fonction List<Pizza> -> List<Pizza>
    // TODO seules les pizzas ayant un prix >= 1000 sont conservées
    Function<List<Pizza>, List<Pizza>> filterPizza = p1 -> {
        return p1.stream().filter(p -> p.getPrice() >= 1000).toList();
    };

    // TODO créer une fonction List<Pizza> -> List<Pizza>
    // TODO seules les pizzas ayant un prix >= 1000 sont conservées
    // TODO .parallel()
    Function<List<Pizza>, List<Pizza>> parallelFilterPizza = p1 -> {
        return p1.stream().filter(p -> p.getPrice() >= 1000).parallel().toList();
    };

    // TODO exécuter le test pour visualiser le temps d'exécution
    @Test
    public void test_arraylist_vs_linkedlist() throws Exception {
        arraylist_vs_linkedlist(filterPizza);
    }

    // Que constatez-vous ?
    // Les temps d'ésécution sont identiques
    // De mon côté :
    // INFO: arrayList=6 ms
    // INFO: linkedList=6 ms


    // TODO exécuter le test pour visualiser le temps d'exécution
    @Test
    public void test_parallel_arraylist_vs_linkedlist() throws Exception {
        arraylist_vs_linkedlist(parallelFilterPizza);
    }

    // Que constatez-vous ?
    // Les temps d'éxécution sont plus rapides avec le parallel()
    // Les temps d'éxécution sont plus rapides pour une arrayList
    // INFO: arrayList=2 ms
    // INFO: linkedList=4 ms

    public void arraylist_vs_linkedlist(Function<List<Pizza>, List<Pizza>> fn) throws Exception {

        int nbPizzas = 1000000;

        List<Pizza> pizzaArrayList = new ArrayList<>(new Data().getPizzas(nbPizzas));
        List<Pizza> pizzaLinkedList = new LinkedList<>(new Data().getPizzas(nbPizzas));

        long arrayList = monitor(() -> fn.apply(pizzaArrayList));
        long linkedList = monitor(() -> fn.apply(pizzaLinkedList));

        Logger.getGlobal().info("arrayList=" + arrayList + " ms");
        Logger.getGlobal().info("linkedList=" + linkedList + " ms");

    }

}
