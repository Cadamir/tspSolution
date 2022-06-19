package opti;

import configuration.Configuration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

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
        jo.put("q", "chicago");
        jo.put("randomFactor", "chicago");

        FileWriter myWriter = new FileWriter("../best_config.json");
        myWriter.write(jo.toString());
        myWriter.close();
    }
}
