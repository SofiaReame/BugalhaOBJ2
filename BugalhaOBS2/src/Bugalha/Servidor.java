package Bugalha;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Servidor {

    public static void main(String[] args) throws Exception {
        Podio podioXML = new Podio();
        int porta = 12345;
        ServerSocket servidor = new ServerSocket(porta);
        System.out.println("Servidor iniciado na porta " + porta + ". Aguardando jogadores...");

        // Aceita jogador 1
        Socket jogador1 = servidor.accept();
        System.out.println("Jogador 1 conectado: " + jogador1.getInetAddress());
        ObjectOutputStream saidaJogador1 = new ObjectOutputStream(jogador1.getOutputStream());
        saidaJogador1.flush();
        ObjectInputStream entradaJogador1 = new ObjectInputStream(jogador1.getInputStream());

        // Recebe o nome do jogador 1
        String nomeJogador1 = (String) entradaJogador1.readObject();
        System.out.println("Nome do Jogador 1: " + nomeJogador1);

        // Aceita jogador 2
        Socket jogador2 = servidor.accept();
        System.out.println("Jogador 2 conectado: " + jogador2.getInetAddress());
        ObjectOutputStream saidaJogador2 = new ObjectOutputStream(jogador2.getOutputStream());
        saidaJogador2.flush();
        ObjectInputStream entradaJogador2 = new ObjectInputStream(jogador2.getInputStream());

        // Recebe o nome do jogador 2
        String nomeJogador2 = (String) entradaJogador2.readObject();
        System.out.println("Nome do Jogador 2: " + nomeJogador2);

        // Envia para os jogadores: id;vez;meuNome;nomeAdversario
        saidaJogador1.writeObject("1;true;" + nomeJogador1 + ";" + nomeJogador2);
        saidaJogador1.flush();

        saidaJogador2.writeObject("2;false;" + nomeJogador2 + ";" + nomeJogador1);
        saidaJogador2.flush();

        // JOGADOR 1 MASTER
        Thread t1 = new Thread(() -> {
            try {
                while (true) {
                    String msg = (String) entradaJogador1.readObject();

                    if (msg.startsWith("FIM;")) {
                        // Parseia dados do vencedor
                        String[] partes = msg.split(";");
                        // partes[1]=nome1, partes[2]=pontos1 talvez 3 e 4 em empate

                        // Salva no Podio
                        if (partes.length == 3) {
                            podioXML.salvarPontuacao(partes[1], Integer.parseInt(partes[2]));
                        } else if (partes.length == 5) {
                            podioXML.salvarPontuacao(partes[1], Integer.parseInt(partes[2]));
                            podioXML.salvarPontuacao(partes[3], Integer.parseInt(partes[4]));
                        }

                        // Lê o XML inteiro como string
                        String xmlAtualizado = Files.readString(
                                Paths.get("podio.xml"), StandardCharsets.UTF_8);

                        // Envia para ambos os clientes, prefixando para identificar
                        String payload = "PODIO;" + xmlAtualizado;
                        saidaJogador1.writeObject(payload);
                        saidaJogador1.flush();
                        saidaJogador2.writeObject(payload);
                        saidaJogador2.flush();

                    } else {
                        // encaminha jogada normal para o adversário
                        saidaJogador2.writeObject(msg);
                        saidaJogador2.flush();
                    }
                }
            } catch (Exception e) {
                System.out.println("Jogador 1 desconectou.");
            }
        });

        //JOGADOR 2 NORMAL
        Thread t2 = new Thread(() -> {
            try {
                while (true) {
                    String msg = (String) entradaJogador2.readObject();
                    // não processa FIM - encaminha tudo para o 1
                    saidaJogador1.writeObject(msg);
                    saidaJogador1.flush();
                }
            } catch (Exception e) {
                System.out.println("Jogador 2 desconectou.");
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        servidor.close();
    }
}