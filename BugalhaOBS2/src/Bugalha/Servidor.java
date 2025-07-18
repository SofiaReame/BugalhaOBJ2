package Bugalha;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    
    public static void main(String[] args) throws Exception {
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

        Thread t1 = new Thread(() -> {
            try {
                while (true) {
                    String jogada = (String) entradaJogador1.readObject();
                    saidaJogador2.writeObject(jogada);
                    saidaJogador2.flush();
                }
            } catch (Exception e) {
                System.out.println("Jogador 1 desconectou.");
            }
        });
        
        Thread t2 = new Thread(() -> {
            try {
                while (true) {
                    String jogada = (String) entradaJogador2.readObject();
                    saidaJogador1.writeObject(jogada);
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