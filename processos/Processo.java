package processos;

import Configuracoes.Parametros;

import java.net.*;

public abstract class Processo {

    protected int id;
    protected int porta;
    protected Parametros parametros;
    protected DatagramSocket socket;

    protected void inicializarSocket() throws SocketException {
        socket = new DatagramSocket(porta);
    }

    public abstract void iniciar();
}
