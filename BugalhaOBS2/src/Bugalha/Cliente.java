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

    private Podio podio;
    private Jogo jogo;
    private boolean minhaVez;

    public Cliente(Scanner scanner) throws Exception {
        this.scanner = scanner;
        this.podio = new Podio();

        System.out.print("Digite seu nome: ");
        String meuNome = scanner.nextLine().trim();

         //======================LEMBRAR DE TROCAR O IP DE ACORDO COM OS TESTES ======================

        // String ipServidor = "192.168.0.245"; // IP V
        String ipServidor = "192.168.0.222"; // IP S
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

        boolean continuar = true;

        do {
            // inicia jogo e gerencia reinício
            jogar();

            // Após fim da partida, verifica reinício
            System.out.print("\nDeseja jogar novamente? (s/n): ");
            String resposta = scanner.nextLine().trim();
            saida.writeObject(resposta);
            saida.flush();

            String respostaAdversario = (String) entrada.readObject();

            if (resposta.equalsIgnoreCase("s") && respostaAdversario.equalsIgnoreCase("s")) {
                jogo.reiniciarTabuleiros();
                minhaVez = !minhaVez;
                System.out.println("\nNovo jogo iniciado!\n");
            } else {
                continuar = false;
                System.out.println("\nUm dos jogadores optou por sair. Encerrando partida.");
            }
        } while (continuar);

        socket.close(); // encerra conexão ao final
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Podio podio = new Podio();
        boolean executando = true;
        do {
            System.out.println("=== BUGALHA ===");
            System.out.println("1. Ver pódio");
            System.out.println("2. Jogar partida");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao) {
                case 1:
                    podio.exibirPodio();
                    break;
                case 2:
                    try {
                        new Cliente(scanner);
                    } catch (Exception e) {
                        System.out.println("Erro no cliente: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.println("Saindo...");
                    executando = false;
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        } while (executando);
        scanner.close();
    }
}
