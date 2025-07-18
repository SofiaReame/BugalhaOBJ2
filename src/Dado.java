import java.util.Random;

/**
 * Representa um dado de 1 a 6 usado no jogo Bugalha.
 */
public class Dado {
    private int valor;

    /**
     * Construtor padrão que sorteia o valor do dado automaticamente ao criar.
     */
    public Dado() {
        rolar();
    }

    /**
     * Retorna o valor atual do dado.
     * @return valor do dado entre 1 e 6.
     */
    public int getValor() {
        return valor;
    }

    /**
     * Sorteia um novo valor aleatório entre 1 e 6.
     */
    public void rolar() {
        Random random = new Random();
        this.valor = random.nextInt(6) + 1; // Gera número entre 1 e 6
    }

    /**
     * Retorna a representação textual do dado.
     * @return valor como String.
     */
    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}
