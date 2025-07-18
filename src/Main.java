import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o nome do Jogador 1:");
        String nome1 = scanner.nextLine();

        System.out.println("Digite o nome do Jogador 2:");
        String nome2 = scanner.nextLine();

        Jogo jogo = new Jogo(nome1, nome2);
        jogo.iniciar();

        scanner.close();
    }
}
