import java.net.*;
import java.io.*;


public class Comunicacao {

    private static final int TOTAL_NOS = 4;
    private static final int PORTA_BASE = 5000;

    private int id;
    private String hostProximo;
    private DatagramSocket aSocket = null;   // socket "servidor" (recebe)

    /** Abre o socket de recebimento na porta 5000 + id. */
    public Comunicacao(int id) {
        this.id = id;
        this.hostProximo = System.getProperty("proximo", "localhost");
        int porta = PORTA_BASE + id;
        try {
            aSocket = new DatagramSocket(porta);
            System.out.println("[" + nome() + "] ouvindo na porta " + porta
                    + " | proximo no em " + hostProximo);
        } catch (SocketException e) {
            throw new RuntimeException("Socket " + e.getMessage()
                    + " (a porta " + porta + " ja esta ocupada?)", e);
        }
    }

    /** Espera o token do no anterior (igual ao receive do UDPServerSimple). */
    public long receber() {
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            System.out.println("[" + nome() + "] esperando mensagem chegar no receive.");
            aSocket.receive(request);
            String data = new String(request.getData(), 0, request.getLength());
            long token = Long.parseLong(data.trim());
            System.out.println("[" + nome() + "] recebeu o token " + token
                    + " de " + request.getAddress().getHostAddress());
            return token;
        } catch (IOException e) {
            throw new RuntimeException("IO " + e.getMessage(), e);
        }
    }

    /** Envia o token ao no (id + 1) % 4 (igual ao send do UDPClientSimple). */
    public void enviar(long token) {
        DatagramSocket envioSocket = null;
        int proximo = (id + 1) % TOTAL_NOS;
        int serverPort = PORTA_BASE + proximo;
        try {
            envioSocket = new DatagramSocket();
            byte[] msg = Long.toString(token).getBytes();
            InetAddress aHost = InetAddress.getByName(hostProximo);
            DatagramPacket request = new DatagramPacket(msg, msg.length, aHost, serverPort);
            envioSocket.send(request);
            System.out.println("[" + nome() + "] enviou o token " + token
                    + " para o no " + proximo + " (" + hostProximo + ":" + serverPort + ")");
        } catch (SocketException e) {
            throw new RuntimeException("Socket " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO " + e.getMessage(), e);
        } finally {
            if (envioSocket != null)
                envioSocket.close();
        }
    }

    /** Fecha o socket de recebimento. */
    public void fechar() {
        if (aSocket != null)
            aSocket.close();
        System.out.println("[" + nome() + "] comunicacao encerrada");
    }

    private String nome() {
        return (id == 0) ? "Mestre" : "Escravo " + id;
    }
}