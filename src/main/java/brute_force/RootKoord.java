package brute_force;

import java.util.ArrayList;
import java.util.HashMap;

public class RootKoord extends Koord{
    private int [] koords;
    private final static int LEN = 10;
    public RootKoord(){
         coolerFuck(new HashMap<Integer, Integer>(), new ArrayList<Integer>());


    }

    public static void coolerFuck(HashMap<Integer, Integer> map, ArrayList<Integer> list){
        if (map.size() == LEN + 1) {
            for(Integer i : list){
                System.out.print(i + " - ");
            }
            System.out.println();
            return;
        }
        for(int i = 0; i <= LEN; i++ ) {
            if (!map.containsValue(i)) {
                map.put(i, i);
                list.add(i);
                coolerFuck(map, list);
                map.remove(i);
                list.remove(list.size()-1);
            }
        }
    }
}
