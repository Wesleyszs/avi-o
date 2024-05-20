import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Trabalho_java {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        Voo voo = new Voo();

        System.out.println("Bem-vindo ao sistema de compra de passagens.");

        while (true) {
            System.out.print("Digite o tipo de passagem (promocional, normal): ");
            String tipoPassagem = scanner.nextLine();

            if ("normal".equalsIgnoreCase(tipoPassagem)) {
                System.out.print("Digite a quantidade de poltronas: ");
                int quantidadePoltronas = scanner.nextInt();
                comprarPassagemNormal(voo, quantidadePoltronas, scanner);
            } else if ("promocional".equalsIgnoreCase(tipoPassagem)) {
                System.out.print("Digite a quantidade de poltronas promocionais: ");
                int quantidadePoltronasPromocionais = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("O vôo tem conexão? (s/n): ");
                String conexao = scanner.nextLine();
                boolean temConexao = conexao.equalsIgnoreCase("s");
                voo.setPrioridadeConexao(temConexao);
                comprarPassagemPromocional(voo, quantidadePoltronasPromocionais);
            } else {
                System.out.println("Tipo de passagem inválido.");
            }

            System.out.print("Deseja continuar comprando? (s/n): ");
            String opcao = scanner.next();
            scanner.nextLine(); // Consume newline
            if (!opcao.equalsIgnoreCase("s")) {
                break;
            }
        }

        System.out.println("\nPoltronas compradas:");
        voo.mostrarPoltronasCompradas();

        System.out.println("Boa viagem!");

        scanner.close();
    }

    private static void comprarPassagemNormal(Voo voo, int quantidadePoltronas, Scanner scanner) {
        voo.mostrarPoltronasDisponiveis();

        for (int i = 0; i < quantidadePoltronas; i++) {
            System.out.print("Digite o número da poltrona que deseja comprar: ");
            int numeroPoltrona = scanner.nextInt();
            voo.comprarPoltronaNormal(numeroPoltrona);
        }
    }

    private static void comprarPassagemPromocional(Voo voo, int quantidadePoltronasPromocionais) {
        Random random = new Random();
        List<Integer> poltronasCompradas = new ArrayList<>();

        for (int i = 0; i < quantidadePoltronasPromocionais; i++) {
            int numeroPoltrona;
            if (voo.isPrioridadeConexao()) {
                numeroPoltrona = random.nextInt(12) + 1; // Escolhe aleatoriamente uma das primeiras 12 poltronas
            } else {
                numeroPoltrona = random.nextInt(48) + 13; // Escolhe aleatoriamente uma poltrona entre 13 e 60
            }
            while (poltronasCompradas.contains(numeroPoltrona)) {
                if (voo.isPrioridadeConexao()) {
                    numeroPoltrona = random.nextInt(12) + 1; // Escolhe aleatoriamente uma das primeiras 12 poltronas
                } else {
                    numeroPoltrona = random.nextInt(48) + 13; // Escolhe aleatoriamente uma poltrona entre 13 e 60
                }
            }
            voo.comprarPoltronaPromocional(numeroPoltrona);
            poltronasCompradas.add(numeroPoltrona);
        }
    }
}

class Voo {
    private static final int FILAS = 10;
    private static final int POLTRONAS_POR_FILA = 6;
    private Poltrona[][] poltronas;
    private boolean prioridadeConexao;

    public Voo() {
        poltronas = new Poltrona[FILAS][POLTRONAS_POR_FILA];
        inicializarPoltronas();
    }

    private void inicializarPoltronas() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < POLTRONAS_POR_FILA; j++) {
                poltronas[i][j] = new Poltrona(i * POLTRONAS_POR_FILA + j + 1);
            }
        }
    }

    public void mostrarPoltronasDisponiveis() {
        System.out.println("Poltronas disponíveis:");
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < POLTRONAS_POR_FILA; j++) {
                if (!poltronas[i][j].isOcupada()) {
                    System.out.print(poltronas[i][j].getNumero() + " ");
                } else {
                    System.out.print("XX ");
                }
            }
            System.out.println();
        }
    }

    public void mostrarPoltronasCompradas() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < POLTRONAS_POR_FILA; j++) {
                if (poltronas[i][j].isOcupada()) {
                    System.out.println("Poltrona " + poltronas[i][j].getNumero() + " ocupada.");
                }
            }
        }
    }

    public void comprarPoltronaNormal(int numeroPoltrona) {
        Poltrona poltrona = getPoltronaPorNumero(numeroPoltrona);
        if (poltrona != null && !poltrona.isOcupada()) {
            poltrona.ocupar();
            System.out.println("Poltrona " + numeroPoltrona + " comprada com sucesso.");
        } else {
            System.out.println("Poltrona inválida ou já ocupada.");
        }
    }

    public void comprarPoltronaPromocional(int numeroPoltrona) {
        Poltrona poltrona = getPoltronaPorNumero(numeroPoltrona);
        if (poltrona != null && !poltrona.isOcupada()) {
            poltrona.ocupar();
            System.out.println("Poltrona " + numeroPoltrona + " comprada com sucesso (promocional).");
        } else {
            System.out.println("Poltrona inválida ou já ocupada.");
        }
    }

    public void setPrioridadeConexao(boolean prioridadeConexao) {
        this.prioridadeConexao = prioridadeConexao;
    }

    public boolean isPrioridadeConexao() {
        return prioridadeConexao;
    }

    private Poltrona getPoltronaPorNumero(int numero) {
        int fila = (numero - 1) / POLTRONAS_POR_FILA;
        int coluna = (numero - 1) % POLTRONAS_POR_FILA;
        if (fila >= 0 && fila < FILAS && coluna >= 0 && coluna < POLTRONAS_POR_FILA) {
            return poltronas[fila][coluna];
        }
        return null;
    }
}

class Poltrona {
    private int numero;
    private boolean ocupada;

    public Poltrona(int numero) {
        this.numero = numero;
        this.ocupada = false;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void ocupar() {
        this.ocupada = true;
    }
}
