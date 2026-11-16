import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Escravo {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Uso: java Escravo <id (1-3)> <operacao (0 ou 1)> <voltas> [ipProximo]");
            return;
        }

        // Leitura dos parâmetros
        int id = Integer.parseInt(args[0]);
        int operacao = Integer.parseInt(args[1]);
        int voltas = Integer.parseInt(args[2]);

        // Parâmetro opcional para IP do próximo processo (padrão: localhost)
        String ipProximo = "localhost";
        if (args.length >= 4) {
            ipProximo = args[3];
        }

        if (id < 1 || id > 3) {
            System.out.println("Erro: O ID do escravo deve ser 1, 2 ou 3.");
            return;
        }

        if (operacao != 0 && operacao != 1) {
            System.out.println("Erro: A operacao deve ser 0 (incrementar) ou 1 (multiplicar por 2).");
            return;
        }

        // Definição das portas no sentido horário:
        // Mestre = 5000 | Escravo 1 = 5001 | Escravo 2 = 5002 | Escravo 3 = 5003
        int portaLocal = 5000 + id;
        int portaProximo;
        if (id == 3) {
            portaProximo = 5000; // Escravo 3 envia de volta para o Mestre
        } else {
            portaProximo = 5000 + id + 1; // Escravo 1 -> 2, Escravo 2 -> 3
        }

        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(portaLocal);
            InetAddress enderecoProximo = InetAddress.getByName(ipProximo);

            System.out.println("Escravo " + id + " iniciado na porta " + portaLocal + ".");
            System.out.println("Aguardando token por " + voltas + " volta(s)...");

            for (int i = 1; i <= voltas; i++) {
                // Recebe o pacote UDP contendo o token
                byte[] bufferRecebimento = new byte[1024];
                DatagramPacket pacoteRecebido = new DatagramPacket(bufferRecebimento, bufferRecebimento.length);
                socket.receive(pacoteRecebido);

                String mensagemRecebida = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength()).trim();
                int token = Integer.parseInt(mensagemRecebida);

                System.out.println("[Volta " + i + "] Escravo " + id + " recebeu token = " + token);

                // Aplica a operação escolhida
                if (operacao == 0) {
                    token = token + 1;
                } else {
                    token = token * 2;
                }

                System.out.println("[Volta " + i + "] Escravo " + id + " atualizou token para = " + token);

                // Envia o token atualizado para o próximo nó no sentido horário
                String mensagemEnvio = String.valueOf(token);
                byte[] bufferEnvio = mensagemEnvio.getBytes();
                DatagramPacket pacoteEnvio = new DatagramPacket(
                        bufferEnvio,
                        bufferEnvio.length,
                        enderecoProximo,
                        portaProximo
                );
                socket.send(pacoteEnvio);
            }

            System.out.println("Escravo " + id + " finalizou todas as voltas.");

        } catch (Exception e) {
            System.out.println("Erro no Escravo " + id + ": " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
