package opti;

import configuration.Configuration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ConfigSave {
    public double alpha, beta, evaporation, q, randomFactor;

    public void save(){
        alpha = Configuration.INSTANCE.alpha;
        beta = Configuration.INSTANCE.beta;
        evaporation = Configuration.INSTANCE.evaporation;
        q = Configuration.INSTANCE.q;
        randomFactor = Configuration.INSTANCE.randomFactor;
    }

    @Override
    public String toString() {
        return "Alpha: " + alpha +
                "\nBeta: " + beta +
                "\nEvaporation: " + evaporation +
                "\nQ: " + q +
                "\nrandomFactor: " + randomFactor;
    }

    public void saveToFile() throws JSONException, IOException {
        JSONObject jo = new JSONObject();
        jo.put("alpha", alpha);
        jo.put("beta", beta);
        jo.put("evaporation", evaporation);
        jo.put("q", q);
        jo.put("randomFactor", randomFactor);

        try {
            FileWriter fr = new FileWriter("/home/cadamir/IdeaProjects/tspSolution/src/main/resources/best_config.json", StandardCharsets.UTF_8);
            System.out.println("resources" + File.separator +"best_config.json");
            fr.write(jo.toString());
            System.out.println(jo.toString());
            fr.close();
        }catch (Exception e){
            try{
                FileWriter fr = new FileWriter("/home/cadamir/IdeaProjects/tspSolution/src/main/resources/best_config.json", StandardCharsets.UTF_8);
                System.out.println("resources" + File.separator +"best_config.json");
                fr.write(jo.toString());
                System.out.println(jo.toString());
                fr.close();
            }catch (Exception f){
                f.printStackTrace();
            }
        }
    }
}
