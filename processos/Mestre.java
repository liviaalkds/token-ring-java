package processos;

import Configuracoes.Parametros;

import java.net.SocketException;

public class Mestre extends Processo {

    public Mestre(int id, int porta, Parametros parametros){
        this.id = id;
        this.porta = porta;
        this.parametros = parametros;
    }

    @Override
    public void iniciar() {
        try {
            inicializarSocket();
            //colocar logica
        } catch (SocketException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
