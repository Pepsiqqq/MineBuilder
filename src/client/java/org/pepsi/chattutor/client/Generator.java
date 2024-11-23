package org.pepsi.chattutor.client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Generator {
    public Generator() {

    }

    public static List<List<List<String>>> getCoords(String prompt){
    List<String> charX = new ArrayList<String>();
    List<List<String>> charY = new ArrayList<List<String>>();
    List<List<List<String>>> charZ = new ArrayList< List<List<String>>>();
    int line = -1;
    Stream<String> stirngs = prompt.lines();
    List<String> rawStrings  = new ArrayList<String>();
    stirngs.forEach(rawStrings::add);
    //StringBuilder temp = new StringBuilder();
    for(int i = 0;i<rawStrings.size();i++){
        if(Character.isDigit(accessCharByIndex(rawStrings.get(i), 0))) {
            if(!charY.isEmpty()){
                charZ.add(List.copyOf(charY));
                charY.clear();
            }
        }
        else{
            List<String> temp = List.of(rawStrings.get(i).split(" "));
            charX.addAll(temp);
        }
        if(!charX.isEmpty()){
            charY.add(List.copyOf(charX));
            charX.clear();
        }
    }
    charZ.add(List.copyOf(charY));
    charY.clear();
    System.out.println(charZ);
    return charZ;
}
    public static char accessCharByIndex(String s, int k) {
        return s.charAt(k);
    }
}
