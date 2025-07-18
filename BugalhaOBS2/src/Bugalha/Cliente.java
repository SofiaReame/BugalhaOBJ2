package Bugalha;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    private Socket socket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private Scanner scanner;

    private Jogo jogo;
    private boolean minhaVez;

    public Cliente() throws Exception {
        scanner = new Scanner(System.in);

        System.out.print("Digite seu nome: ");
        String meuNome = scanner.nextLine().trim();

        String ipServidor = "192.168.0.106"; // IP Vitoria
        int portaServidor = 12345;

        System.out.println("Conectando ao servidor...");
        socket = new Socket(ipServidor, portaServidor);

        saida = new ObjectOutputStream(socket.getOutputStream());
        saida.flush();
        entrada = new ObjectInputStream(socket.getInputStream());

        // Envia nome do jogador para o servidor
        saida.writeObject(meuNome);
        saida.flush();

        // Recebe resposta do servidor: id;minhaVez;meuNome;nomeAdversario
        String msgInicial = (String) entrada.readObject();
        String[] dados = msgInicial.split(";");

        String idJogador = dados[0];
        minhaVez = Boolean.parseBoolean(dados[1]);
        String nomeJogador = dados[2];
        String nomeAdversario = dados[3];

        System.out.println("Voce eh o jogador " + idJogador);
        System.out.println("Seu nome: " + nomeJogador);
        System.out.println("Adversario: " + nomeAdversario);

        // Cria jogo com nomes
        jogo = new Jogo(scanner, nomeJogador, nomeAdversario);

        jogar();
    }

    private void jogar() throws Exception {
        while (true) {
            if (jogo.fimDeJogo()) {
                System.out.println("\n*** FIM DE JOGO ***");
                jogo.mostrarTabuleiros();
                jogo.mostrarPontuacoesEFim();
                break;
            }

            
            if (minhaVez) {
                System.out.println("\nSua vez de jogar!");
                jogo.mostrarTabuleiros();

                int dado = jogo.rolarDadoDaVez();
                System.out.println("Voce rolou o dado e tirou: " + dado);

                int coluna;
                while (true) {
                    System.out.print("Escolha uma coluna (0, 1 ou 2): ");
                    if (!scanner.hasNextInt()) {
                        scanner.next();
                        System.out.println("Entrada invalida!");
                        continue;
                    }
                    coluna = scanner.nextInt();
                    scanner.nextLine();

                    if (jogo.jogarNaColuna(coluna, dado)) {
                        break;
                    }
                    System.out.println("Coluna cheia ou invalida! Tente outra.");
                }

                String jogada = coluna + ";" + dado;
                saida.writeObject(jogada);
                saida.flush();

                minhaVez = false;

            } else {
                System.out.println("Aguardando a jogada do adversario...");
                String jogadaAdversaria = (String) entrada.readObject();

                String[] partes = jogadaAdversaria.split(";");
                int colunaAdversaria = Integer.parseInt(partes[0]);
                int dadoAdversario = Integer.parseInt(partes[1]);

                jogo.jogarNoAdversario(colunaAdversaria, dadoAdversario);

                minhaVez = true;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Cliente();
    }
}