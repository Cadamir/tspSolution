import BruteF.RootKoord;
import abc.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;


public class MainTest {
    @Test
    public void mainTest() throws Exception {
        Heeve heeve = new Heeve();

        Bee [] eB = heeve.getEmployedBees();
        for (Bee bee : eB) {
            System.out.println(bee);
        }

        Bee [] oB = heeve.getOnlookBees();
        for (Bee bee : oB) {
            System.out.println(bee);
        }
    }

    @Test
    public void test(){
        int j = 0;
        int [] koord = new int[280];
        HashMap<Integer, Integer> test = new HashMap<Integer, Integer>();
        for(int i = 0; i < koord.length; i++) {
            j++;
            int f = (int) (Math.random() * 280);
//            System.out.println(f);
            if(test.containsValue(f))
                i--;
            else {
                test.put(i, f);
                koord[i] = f+1;
            }

        }
        System.out.println("Stuff " + j);
        for(int a = 0; a <= 280; a++){
            if(!test.containsValue(a)){
                System.out.println("Test doesn't contains "+a);
            }
        }
    }

    @Test
    public void bfTest(){
        RootKoord rk = new RootKoord();
    }
}
