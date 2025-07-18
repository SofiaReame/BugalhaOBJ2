package Bugalha;

import java.util.Random;

public class Dado {
    private Random random;

    public Dado() {
        random = new Random();
    }

    // Rola o dado e retorna um número de 1 a 6
    public int rolar() {
        return random.nextInt(6) + 1; // (0 a 5) + 1 = (1 a 6)
    }
}
