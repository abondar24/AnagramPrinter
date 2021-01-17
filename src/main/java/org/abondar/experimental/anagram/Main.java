package org.abondar.experimental.anagram;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length!=2){
            System.err.println("Missing dictionary or phrase");
            System.exit(1);
        }

        try {

            var ap = new AnagramPrinter();

            long startTime = System.currentTimeMillis();
            var res = ap.getAnagrams(args[0],args[1]);
            long endTime = (System.currentTimeMillis() - startTime);

            if (res.isEmpty()){
                System.out.printf("No anagrams found of \"%s\"\n",args[1]);
            } else {
                System.out.printf("Anagrams of \"%s\"\n",args[1]);
                res.forEach(System.out::println);
            }
            System.out.printf("Execution time: %d ms\n",endTime);

        } catch (IOException ex){
            System.err.println(ex.getMessage());
            System.exit(1);
        }

    }
}
