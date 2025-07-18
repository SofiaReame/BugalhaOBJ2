package Bugalha;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GerenciadorDeJogadas implements Runnable {

    private ObjectInputStream entrada;
    private ObjectOutputStream saida;

    public GerenciadorDeJogadas(ObjectInputStream entrada, ObjectOutputStream saida) {
        this.entrada = entrada;
        this.saida = saida;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String mensagem = (String) entrada.readObject();
                saida.writeObject(mensagem);
                saida.flush();
            }
        } catch (Exception e) {
            System.out.println("Jogador desconectou.");
        }
    }
}

