import Configuracoes.Parametros;

public class Mestre {
    public static void main(String[] args) {
        try {
            Parametros parametros = new Parametros(args, true);
            String host = args.length > 3 ? args[3] : "localhost";
            new processos.Mestre(parametros.getId(), 5000 + parametros.getId(),
                    parametros, host).iniciar();
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
            System.err.println("Uso: java Mestre <id> <operacao> <voltas> [host]");
            System.exit(1);
        }
    }
}