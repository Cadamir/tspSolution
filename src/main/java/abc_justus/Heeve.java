package abc_justus;

import java.lang.module.Configuration;

public class Heeve {
    private final EmployedBee[] empBees = new EmployedBee[Configuration.INSTANCE.maximalEmpBees];

    private final double[][] foodMatrix = new double[Configuration.INSTANCE.maximumCountFoodSources][Configuration.INSTANCE.dimension];
    private final double[] f = new double[Configuration.INSTANCE.maximumCountFoodSources];
    private final double[] fitness = new double[Configuration.INSTANCE.maximumCountFoodSources];
    private final double[] trial = new double[Configuration.INSTANCE.maximumCountFoodSources];
    private final double[] probabilities = new double[Configuration.INSTANCE.maximumCountFoodSources];
    private final double[] solution = new double[Configuration.INSTANCE.dimension];
    private final double[] globalMinimumArray = new double[Configuration.INSTANCE.maximumCountSimulation];
    private double objectiveValue;
    private double fitnessValue;
    private int neighbour, parameterDelta;
    private double globalMinimum;

    public Heeve() {
        setup();
        memorizeBestSource();
    }

    public void execute() {
        sendEmployedBees();
        calculateProbabilities();
        sendOnlookerBees();
        memorizeBestSource();
        sendScoutBees();
    }

    private void sendEmployedBees() {
        int i, j;

        for (i = 0; i < Configuration.INSTANCE.maximumCountFoodSources; i++) {
            parameterDelta = (int) (Utility.generateRandomValue() * Configuration.INSTANCE.dimension);
            neighbour = (int) (Utility.generateRandomValue() * Configuration.INSTANCE.maximumCountFoodSources);

            for (j = 0; j < Configuration.INSTANCE.dimension; j++) {
                solution[j] = foodMatrix[i][j];
            }

            solution[parameterDelta] = foodMatrix[i][parameterDelta] + (foodMatrix[i][parameterDelta] - foodMatrix[neighbour][parameterDelta])
                    * (Utility.generateRandomValue() - 0.5) * 2;

            if (solution[parameterDelta] < Configuration.INSTANCE.lowerBound) {
                solution[parameterDelta] = Configuration.INSTANCE.lowerBound;
            }

            if (solution[parameterDelta] > Configuration.INSTANCE.upperBound) {
                solution[parameterDelta] = Configuration.INSTANCE.upperBound;
            }

            objectiveValue = Configuration.INSTANCE.calculateFunction(solution);
            fitnessValue = calculateFitness(objectiveValue);

            if (fitnessValue > fitness[i]) {
                trial[i] = 0;
                for (j = 0; j < Configuration.INSTANCE.dimension; j++) {
                    foodMatrix[i][j] = solution[j];
                }

                f[i] = objectiveValue;
                fitness[i] = fitnessValue;
            } else {
                trial[i] = trial[i] + 1;
            }
        }
    }

    private void sendOnlookerBees() {
        int i, j, t;
        i = 0;
        t = 0;

        while (t < Configuration.INSTANCE.maximumCountFoodSources) {
            if (Utility.generateRandomValue() < probabilities[i]) {
                t++;

                parameterDelta = (int) (Utility.generateRandomValue() * Configuration.INSTANCE.dimension);
                neighbour = (int) (Utility.generateRandomValue() * Configuration.INSTANCE.maximumCountFoodSources);

                while (neighbour == i) {
                    neighbour = (int) (Utility.generateRandomValue() * Configuration.INSTANCE.maximumCountFoodSources);
                }

                for (j = 0; j < Configuration.INSTANCE.dimension; j++) {
                    solution[j] = foodMatrix[i][j];
                }

                solution[parameterDelta] = foodMatrix[i][parameterDelta] + (foodMatrix[i][parameterDelta] - foodMatrix[neighbour][parameterDelta]) *
                        (Utility.generateRandomValue() - 0.5) * 2;

                if (solution[parameterDelta] < Configuration.INSTANCE.lowerBound) {
                    solution[parameterDelta] = Configuration.INSTANCE.lowerBound;
                }

                if (solution[parameterDelta] > Configuration.INSTANCE.upperBound) {
                    solution[parameterDelta] = Configuration.INSTANCE.upperBound;
                }

                objectiveValue = Configuration.INSTANCE.calculateFunction(solution);
                fitnessValue = calculateFitness(objectiveValue);

                if (fitnessValue > fitness[i]) {
                    trial[i] = 0;
                    for (j = 0; j < Configuration.INSTANCE.dimension; j++) {
                        foodMatrix[i][j] = solution[j];
                    }
                    f[i] = objectiveValue;
                    fitness[i] = fitnessValue;
                } else {
                    trial[i] = trial[i] + 1;
                }
            }

            i++;

            if (i == Configuration.INSTANCE.maximumCountFoodSources) {
                i = 0;
            }
        }
    }

    private void sendScoutBees() {
        for (EmployedBee bee: empBees) {
            if (bee.isScouting()) {
                bee.scout();
            }
        }
    }

    private void calculateProbabilities() {
        int i;
        double bestFitness;
        bestFitness = fitness[0];

        for (i = 1; i < Configuration.INSTANCE.maximumCountFoodSources; i++) {
            if (fitness[i] > bestFitness) {
                bestFitness = fitness[i];
            }
        }

        for (i = 0; i < Configuration.INSTANCE.maximumCountFoodSources; i++) {
            probabilities[i] = (0.9 * (fitness[i] / bestFitness)) + 0.1;
        }
    }

    private void memorizeBestSource() {
        int i;

        for (i = 0; i < Configuration.INSTANCE.maximumCountFoodSources; i++) {
            if (f[i] < globalMinimum) {
                globalMinimum = f[i];
            }
        }
    }

    private void init(int index) {
        int j;

        for (j = 0; j < Configuration.INSTANCE.dimension; j++) {
            foodMatrix[index][j] = Utility.generateRandomValue() * (Configuration.INSTANCE.upperBound - Configuration.INSTANCE.lowerBound) +
                    Configuration.INSTANCE.lowerBound;
            solution[j] = foodMatrix[index][j];
        }

        f[index] = Configuration.INSTANCE.calculateFunction(solution);
        fitness[index] = calculateFitness(f[index]);
        trial[index] = 0;
    }

    private void setup() {
        int i;

        for (i = 0; i < Configuration.INSTANCE.maximumCountFoodSources; i++) {
            init(i);
        }

        globalMinimum = f[0];
    }

    private double calculateFitness(double value) {
        double result;

        if (value >= 0) {
            result = 1 / (value + 1);
        } else {
            result = 1 + Math.abs(value);
        }

        return result;
    }}
