package abc;

import java.util.HashMap;

public class Bee {
    public BeeState state;
    public int nr;
    public FootSource best;

    public Bee(int nr, BeeState state) {
        this.state = state;
        this.nr = nr;
        best = findFS();
    }

    public FootSource findFS() {
        FootSource fs = new FootSource();
        switch (this.state) {
            case scout: {
                fs = findScout();
            }break;
            case employed: {
                fs = findEmployed();
            }break;
            case onlook: {
                fs = findOnlook();
            }break;
        }
        return fs;
    }

    private FootSource findScout() {
        int [] koord = new int[280];
        HashMap test = new HashMap();
        for(int i = 0; i < koord.length; i++) {
            int f = (int) (Math.random() * 280);
            if(test.containsValue(f))
                i--;
            else
                test.put(i, f);

        }
        System.out.println(koord);
        return FootSource.getNewFS(koord);
    }

    private FootSource findEmployed() {
        return new FootSource();
    }

    private FootSource findOnlook() {
        return new FootSource();
    }
}
