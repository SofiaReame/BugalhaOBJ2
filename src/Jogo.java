import java.util.Scanner;

public class Jogo {

    private Jogador jogador1;
    private Jogador jogador2;
    private Scanner scanner;

    public Jogo(Scanner scanner, String nomeJogador1, String nomeJogador2) {
        this.scanner = scanner;
        this.jogador1 = new Jogador(nomeJogador1);
        this.jogador2 = new Jogador(nomeJogador2);
    }

    // Rola o dado para o jogador 1
    public int rolarDadoDaVez() {
        return jogador1.rolarDado();
    }

    // Jogada do jogador 1
    public boolean jogarNaColuna(int coluna, int valor) {
        return jogador1.adicionarDado(coluna, valor, jogador2);
    }

    // Jogada do adversario 
    public void jogarNoAdversario(int coluna, int valor) {
        jogador2.adicionarDado(coluna, valor, jogador1);
    }

    // Mostra os tabuleiros
    public void mostrarTabuleiros() {
        jogador1.mostrarTabuleiroBonito();
        jogador2.mostrarTabuleiroBonito();
    }

    // Verifica se o tabuleiro de ambos os jogadores está cheio
    public boolean fimDeJogo() {
        return tabuleiroCheio(jogador1) || tabuleiroCheio(jogador2);
    }

    // Método auxiliar para verificar se todas as colunas estão cheias
    private boolean tabuleiroCheio(Jogador jogador) {
        for (int i = 0; i < 3; i++) {
            if (jogador.getColunas()[i].size() < 3) {
                return false;
            }
        }
        return true;
    }

    // Exibe as pontuações finais e quem venceu
    public void mostrarPontuacoesEFim() {
        int pontos1 = jogador1.calcularPontuacao();
        int pontos2 = jogador2.calcularPontuacao();

        System.out.println("\nPontuação final:");
        System.out.println(jogador1.getNome() + ": " + pontos1 + " pontos");
        System.out.println(jogador2.getNome() + ": " + pontos2 + " pontos");

        if (pontos1 > pontos2) {
            System.out.println("Paraebens, " + jogador1.getNome() + " venceu!");
        } else if (pontos2 > pontos1) {
            System.out.println("Parabens, " + jogador2.getNome() + " venceu!");
        } else {
            System.out.println("Empate!");
        }
    }
}
