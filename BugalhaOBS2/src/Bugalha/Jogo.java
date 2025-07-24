package Bugalha;

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
    
    public void reiniciarTabuleiros() {
        jogador1.limparTabuleiro();
        jogador2.limparTabuleiro();
    }

    
    // Exibe as pontuações finais e quem venceu
    // Retorna o nome e pontuação do vencedor no fim
    // Se houver empate, retorna ambos jogadores 
    public String[] mostrarPontuacoesEFim() {
    int pontos1 = jogador1.calcularPontuacao();
    int pontos2 = jogador2.calcularPontuacao();

    System.out.println("\nPontuacao final:");
    System.out.println(jogador1.getNome() + ": " + pontos1 + " pontos");
    System.out.println(jogador2.getNome() + ": " + pontos2 + " pontos");

    try {
        if (pontos1 > pontos2) {
            System.out.println("Parabéns, " + jogador1.getNome() + " venceu!");
            return new String[]{jogador1.getNome(), String.valueOf(pontos1)};
        } else if (pontos2 > pontos1) {
            System.out.println("Parabéns, " + jogador2.getNome() + " venceu!");
            return new String[]{jogador2.getNome(), String.valueOf(pontos2)};
        } else {
            System.out.println("Empate!");
            return new String[]{
                jogador1.getNome(), String.valueOf(pontos1),
                jogador2.getNome(), String.valueOf(pontos2)
            };
        }
    } catch (Exception ex) {
        System.err.println("Erro ao calcular pontuação: " + ex.getMessage());
        return new String[0];
    }
}
}
