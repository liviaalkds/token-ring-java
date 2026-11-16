import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class TesteTokenRing {
    private static final long TIMEOUT_LEITURA_MS = 10_000;

    private TesteTokenRing() {
    }

    public static void main(String[] args) throws InterruptedException {
        long[][] cenarios = {
                {0, 1, 6},
                {1, 1, 32},
                {0, 2, 10},
                {1, 2, 512},
                {0, 5, 22},
                {1, 3, 8192}
        };
        boolean tudoPassou = true;

        for (long[] cenario : cenarios) {
            boolean passou = executarCenario((int) cenario[0], (int) cenario[1],
                    (int) cenario[2]);
            System.out.printf("(%d, %d): %s%n", cenario[0], cenario[1],
                    passou ? "OK" : "FALHOU");
            tudoPassou &= passou;
            Thread.sleep(300);
        }

        System.exit(tudoPassou ? 0 : 1);
    }

    private static boolean executarCenario(int operacao, int voltas, long esperado)
            throws InterruptedException {
        List<Process> escravos = new ArrayList<>();
        Process mestre = null;
        final String[] resultado = {null};
        final boolean[] erroLeitura = {false};

        try {
            for (int id = 1; id <= 3; id++) {
                escravos.add(iniciar("Escravo", id, operacao, voltas));
            }
            Thread.sleep(1500);

            mestre = iniciar("Mestre", 0, operacao, voltas);
            Process processoMestre = mestre;
            Thread leitor = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        processoMestre.getInputStream()))) {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        if (linha.startsWith("resultado = ")) {
                            resultado[0] = linha.substring("resultado = ".length()).trim();
                        }
                    }
                } catch (IOException e) {
                    erroLeitura[0] = true;
                }
            });
            leitor.start();
            leitor.join(TIMEOUT_LEITURA_MS);

            if (leitor.isAlive()) {
                mestre.destroyForcibly();
                leitor.join(1000);
                return false;
            }
            if (mestre.isAlive()) {
                mestre.destroyForcibly();
            }
            if (erroLeitura[0] || resultado[0] == null) {
                return false;
            }
            try {
                return Long.parseLong(resultado[0]) == esperado;
            } catch (NumberFormatException e) {
                return false;
            }
        } finally {
            if (mestre != null && mestre.isAlive()) {
                mestre.destroyForcibly();
            }
            for (Process escravo : escravos) {
                if (escravo.isAlive()) {
                    escravo.destroyForcibly();
                }
            }
        }
    }

    private static Process iniciar(String classe, int id, int operacao, int voltas)
            throws InterruptedException {
        String executavel = System.getProperty("java.home") + java.io.File.separator
                + "bin" + java.io.File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        ProcessBuilder builder = new ProcessBuilder(
                executavel, "-cp", classpath, classe,
                Integer.toString(id), Integer.toString(operacao), Integer.toString(voltas));
        builder.redirectErrorStream(true);
        try {
            return builder.start();
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível iniciar " + classe + ".", e);
        }
    }
}
