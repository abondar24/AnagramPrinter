package org.abondar.experimental.anagram;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AnagramPrinterTest {

    private AnagramPrinter ag = new AnagramPrinter();
    private static final String DICTIONARY_FILE ="wordlist.txt";


    @Test
    public void phraseContainsWordTest(){
        var phrase = "aschheim";
        var key = "asches";

        assertTrue(ag.phraseContainsWord(phrase,key));
        assertFalse(ag.phraseContainsWord(phrase,"hiz"));
    }

    @Test
    public void testAnagramSingleWord() throws Exception{
        var bs = "Aschheim";

        var res =ag.getAnagrams(DICTIONARY_FILE,bs);
        assertEquals(7,res.size());
    }


    @Test
    public void testAnagramSpecial() throws Exception{
        var bs = "IT-Crowd";


        var res =ag.getAnagrams(DICTIONARY_FILE,bs);
        assertEquals(5,res.size());
    }

    @Test
    public void testAnagramTwoWords() throws Exception{
        var bs = "best secret";


        long startTime = System.currentTimeMillis();
        var res =ag.getAnagrams(DICTIONARY_FILE,bs);
        long endTime = (System.currentTimeMillis() - startTime);
        System.out.println(endTime);
        System.out.println(res);
        assertEquals(14,res.size());
    }

    @Test
    public void testAnagramPerformance() throws Exception{
        var bs = "best secret";


        long startTime = System.currentTimeMillis();
        ag.getAnagrams(DICTIONARY_FILE,bs);
        long endTime = (System.currentTimeMillis() - startTime);
        assertTrue(endTime<160);
    }

    @Test
    public void testAnagramEqualLen() throws Exception{
        var bs = "salo";


        var res =ag.getAnagrams(DICTIONARY_FILE,bs);
        assertEquals(2,res.size());
    }

}
