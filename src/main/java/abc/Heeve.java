package abc;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Heeve {

    Bee [] employedBees = new Bee[25];
    Bee [] onlookBees = new Bee[25];
    JSONObject confi;
    FootSource best;

    public Heeve() throws Exception {
        File file = new File("C:\\Users\\Programmierer\\Documents\\unfinished\\bee\\src\\main\\java\\Configuration\\Configuration");
        if(!file.exists()) {
            throw new Exception("File not found");
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder test = new StringBuilder();
        while ((line = br.readLine()) != null) {
            test.append(line);
        }

        confi = new JSONObject(test);
        int azEB = confi.getJSONObject("Configuration").getJSONObject("Heeve").getInt("AnzEmployed");
        int azOB = confi.getJSONObject("Configuration").getJSONObject("Heeve").getInt("AnzOnlook");

        int az = 0;

        for(int i = 0; i < azEB; i++) {
            employedBees[i] = new Bee(az, BeeState.scout);
        }
        for(int i = 0; i < azOB; i++) {
            onlookBees[i] = new Bee(az, BeeState.onlook);
        }

        start();
    }

    public Bee[] getEmployedBees() {
        return employedBees;
    }

    public Bee[] getOnlookBees() {
        return onlookBees;
    }

    public FootSource start() {
        //Scout
        for(Bee bee:employedBees){
            bee.findFS();
            if (best.compare() < bee.best.compare()) {
                best = bee.best;
            }
        }

        for(int i = 0; i < confi.getJSONObject("Configuration").getInt("Iteration"); i++){
            //Employed
            for (Bee bee:employedBees) {
                bee.findFS();
                if (best.compare() < bee.best.compare()) {
                    best = bee.best;
                }
            }

            //Onlook
            for (Bee bee:onlookBees) {
                bee.findFS();
                if (best.compare() < bee.best.compare()) {
                    best = bee.best;
                }
            }

            //Scout
            for (Bee bee:onlookBees) {
                if (bee.state == BeeState.scout)
                    bee.findFS();
                if (best.compare() < bee.best.compare()) {
                    best = bee.best;
                }
            }
        }
        return best;
    }
}

