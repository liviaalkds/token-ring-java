package processos;

import Configuracoes.Operacao;
import Configuracoes.Parametros;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

public class Escravo extends Processo {

    private static final int PORTA_BASE = 5000;
    private static final int TOTAL_PROCESSOS = 4;
    private static final int TIMEOUT_RECEBER_MS = 30_000;

    private final String hostProximo;

    public Escravo(int id, int porta, Parametros parametros) {
        this(id, porta, parametros, "localhost");
    }

    public Escravo(int id, int porta, Parametros parametros, String hostProximo) {
        this.id = id;
        this.porta = porta;
        this.parametros = parametros;
        this.hostProximo = hostProximo;
    }

    @Override
    public void iniciar() {
        try {
            inicializarSocket();
            socket.setSoTimeout(TIMEOUT_RECEBER_MS);

            int proximo = (id + 1) % TOTAL_PROCESSOS;
            int portaProximo = PORTA_BASE + proximo;
            InetAddress enderecoProximo = InetAddress.getByName(hostProximo);

            for (int volta = 1; volta <= parametros.getVoltas(); volta++) {
                long token = receberToken();
                token = Operacao.aplicar(token, parametros.getOperacao());
                enviarToken(token, enderecoProximo, portaProximo);
                System.out.println("[Escravo " + id + "] volta " + volta
                        + ": token enviado = " + token);
            }
        } catch (SocketTimeoutException e) {
            System.err.println("[Escravo " + id + "] Timeout: nenhum token recebido em "
                    + (TIMEOUT_RECEBER_MS / 1000) + " s.");
        } catch (ArithmeticException e) {
            System.err.println("[Escravo " + id + "] Overflow ao aplicar a operação: "
                    + e.getMessage());
        } catch (SocketException e) {
            System.err.println("[Escravo " + id + "] Erro de socket (porta " + porta
                    + " em uso?): " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[Escravo " + id + "] Erro de I/O: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private long receberToken() throws IOException {
        byte[] buffer = new byte[Long.BYTES];
        DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
        socket.receive(pacote);
        return ByteBuffer.wrap(pacote.getData(), 0, Long.BYTES).getLong();
    }

    private void enviarToken(long token, InetAddress destino, int portaDestino)
            throws IOException {
        byte[] dados = ByteBuffer.allocate(Long.BYTES).putLong(token).array();
        socket.send(new DatagramPacket(dados, dados.length, destino, portaDestino));
    }
}