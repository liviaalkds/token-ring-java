public final class Parametros {
    private final int id;
    private final int operacao;
    private final int voltas;

    public Parametros(String[] args, boolean ehMestre) {
        if (args == null || args.length < 3) {
            throw new IllegalArgumentException(
                    "Informe pelo menos 3 argumentos: <id> <operação> <voltas>."
            );
        }

        try {
            id = Integer.parseInt(args[0]);
            operacao = Integer.parseInt(args[1]);
            voltas = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "ID, operação e voltas devem ser números inteiros.", e
            );
        }

        if (ehMestre && id != 0) {
            throw new IllegalArgumentException("O ID do Mestre deve ser 0.");
        }
        if (!ehMestre && (id < 1 || id > 3)) {
            throw new IllegalArgumentException("O ID do Escravo deve estar entre 1 e 3.");
        }
        if (operacao != 0 && operacao != 1) {
            throw new IllegalArgumentException("A operação deve ser 0 ou 1.");
        }
        if (voltas < 1) {
            throw new IllegalArgumentException("O número de voltas deve ser maior ou igual a 1.");
        }
    }

    public int getId() {
        return id;
    }

    public int getOperacao() {
        return operacao;
    }

    public int getVoltas() {
        return voltas;
    }
}
