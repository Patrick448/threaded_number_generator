package br.ufjf.prime;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParallelPrimeGenerator {

    private static final Object mutex = new Object();

    public static class Task implements Runnable
    {
        private final int index;
        private final int start;
        private final int end;
        private final List<List<Integer>> resultList;

        public Task(int index, int start, int end, List<List<Integer>> resultList) {
            this.index = index;
            this.start = start;
            this.end = end;
            this.resultList = resultList;
        }

        @Override
        public void run()
        {
            synchronized(mutex)
            {   //System.out.print("Hello from thread " + index + ". Start: "+start+ ". End: "+end);
                PrimeGenerator.generate(start, end, resultList, index);

               // System.out.println();
            }
        }
    }

    public static void main(String[] args)
    {
        int start = 1;
        int end = 10000;
        int intervalSize= 1000;
        int threadCount = (int) Math.ceil((double)(end-start)/intervalSize);
        Thread[] threads = new Thread[threadCount];
        List<List<Integer>> resultList = Collections.synchronizedList(new ArrayList<List<Integer>>(Collections.nCopies(threadCount, (List<Integer>)null)));

        for(int i=start; i<end; i+=intervalSize){
            int taskRangeEnd = i+intervalSize;
            int index = i/intervalSize;

            if(i+intervalSize> end){
                taskRangeEnd = end;
            }
            Task task = new Task(index, i, taskRangeEnd, resultList );
            threads[index] = new Thread(task);
            threads[index].start();
        }


        for(int i = 0; i < threadCount; i++)
        {
            try
            {
                threads[i].join();
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(ParallelPrimeGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        try(FileWriter fileWriter = new FileWriter("primes.txt"))
        {
            for(List<Integer> list : resultList)
            {
                for(Integer prime: list){
                    fileWriter.write(prime + " ");
                }
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(ParallelPrimeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
