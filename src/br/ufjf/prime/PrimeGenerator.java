package br.ufjf.prime;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ltoscano
 */
public class PrimeGenerator 
{

    public static void generate(int start, int end, List<List<Integer>> arrayList, int index){

        boolean isPrime;

        List<Integer> primes = new LinkedList<>();

        for(int i = start; i <= end; i++)
        {
            if((i == 1) || (i == 0))
            {
                continue;
            }

            isPrime = true;

            for(int j = 2; j <= (i / 2); ++j)
            {
                if((i % j) == 0)
                {
                    isPrime = false;
                    break;
                }
            }

            if(isPrime)
            {
                primes.add(i);
            }
        }

        arrayList.set(index, primes);
    }


}
