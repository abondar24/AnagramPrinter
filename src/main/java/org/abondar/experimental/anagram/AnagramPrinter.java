package org.abondar.experimental.anagram;

import com.sun.source.tree.Tree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class AnagramPrinter {

    private static final int MIN_LEN = 3;



    private static final Map<Character,Integer> charMap = new HashMap<>() {
        {
            put('a', 2);
            put('b', 3);
            put('c', 5);
            put('d', 7);
            put('e', 11);
            put('f', 13);
            put('g', 17);
            put('h', 19);
            put('i', 23);
            put('j', 29);
            put('k', 31);
            put('l', 37);
            put('m', 41);
            put('n', 43);
            put('o', 47);
            put('p', 53);
            put('q', 59);
            put('r', 61);
            put('s', 67);
            put('t', 71);
            put('u', 73);
            put('v', 79);
            put('w', 83);
            put('x', 89);
            put('y', 97);
            put('z', 101);
        }
    };


    public List<String> getAnagrams(String dictName, String phrase) throws IOException {
        phrase = normalizePhrase(phrase);
        var dict = readDict(dictName);

        return getAnagrams(dict, phrase);
    }

    private String normalizePhrase(String phrase) {
        return phrase.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-zA-Z]", "")
                .replaceAll(" ", "");
    }

    private Map<String, String> readDict(String dictName) throws IOException {
        var is = new FileInputStream(dictName);
        var br = new BufferedReader(new InputStreamReader(is));
        Map<String, String> dictionary = new HashMap<>();

        br.lines().forEach(l -> dictionary.put(l, sortChars(l)));

        return dictionary;
    }

    private String sortChars(String word) {
        char[] wc = word.toCharArray();
        Arrays.sort(wc);
        return new String(wc);
    }

    public boolean phraseContainsWord(String phrase, String word) {

        for (int i = 0; i < word.length(); i++) {
            var ch = word.charAt(i);
            if (phrase.indexOf(ch) == -1) {
                return false;
            }
        }

        return true;
    }

    private List<String> getAnagrams(Map<String, String> dict, String phrase) {

        List<String> res = new ArrayList<>();

        var matchingWords = getMatchingWords(phrase, dict.keySet());

        var sortedPhrase = sortChars(phrase);
        var equalLenWords = getEqualLenWords(matchingWords, sortedPhrase);

        if (!equalLenWords.isEmpty()) {
            res.addAll(equalLenWords);
        }

        matchingWords = filterShortKeys(matchingWords);
       var  sortedMatching = new TreeSet<>(matchingWords);
        var leftAnagrams = getLeftAnagrams(sortedMatching, dict, sortedPhrase);
        res.addAll(leftAnagrams);

        return res;
    }



    private Set<String> getMatchingWords(String phrase, Set<String> words) {
        return words.stream()
                .filter(word -> phraseContainsWord(phrase, word))
                .collect(Collectors.toSet());
    }

    private Set<String> getEqualLenWords(Set<String> words, String sortedPhrase) {
        return words.stream()
                .filter(w -> w.length() == sortedPhrase.length())
                .filter(w->sortChars(w).equals(sortedPhrase))
                .collect(Collectors.toSet());
    }

    private List<String> getLeftAnagrams(TreeSet<String> anagramWords, Map<String, String> dict, String sortedPhrase) {
        List<TreeSet<String>> rawAnagrams = new ArrayList<>();

        anagramWords.forEach(aw->{
            var additionalWords = getAdditionalWords(anagramWords,aw,sortedPhrase.length());

            additionalWords.forEach(adw->{
                TreeSet<String> rawAnagram = new TreeSet<>();

                rawAnagram.add(aw);
                rawAnagram.add(adw);
                var sortedAnagram =  sortChars(dict.get(aw) + dict.get(adw));
                if (sortedAnagram.equals(sortedPhrase)) {
                    rawAnagrams.add(rawAnagram);
                    }
            });


        });

      return getAnagrams(rawAnagrams);
    }

    private List<String> getAnagrams(List<TreeSet<String>> rawAnagrams){
        List<String> anagrams = new ArrayList<>();

        rawAnagrams.forEach(ra-> ra.forEach(r->{
            var anagram = new StringBuilder();
            anagram.append(ra.first());
            if (!r.equals(ra.first())){
                anagram.append(" ");
                anagram.append(r);
                anagrams.add(anagram.toString());
            }
        }));
        return anagrams;
    }

    private List<String> getAdditionalWords(Set<String> words, String word, int phraseLen) {
        return words.stream()
                .filter(w-> charMap.get(w.charAt(0))>charMap.get(word.charAt(0)))
                .filter(w -> word.length() + w.length() == phraseLen)
                .collect(Collectors.toList());
    }


    private Set<String> filterShortKeys(Set<String> keys) {
        return keys.stream()
                .filter(k -> k.length() >= MIN_LEN)
                .collect(Collectors.toSet());
    }


}
