# Token Ring

Implementação do algoritmo de exclusão mútua Token Ring em Java, com processos que se comunicam em anel e um token que circula entre eles para controlar o acesso à seção crítica.

## Estrutura do projeto

```
.
├── Configuracoes/
├── processos/
├── Mestre.java
├── Escravo.java
├── TesteTokenRing.java
└── out/            (gerada na compilação)
```

A pasta `Configuracoes` guarda as classes de configuração do anel, a pasta `processos` guarda as classes dos processos que participam dele, `Mestre` e `Escravo` representam os dois papéis na execução, e `TesteTokenRing` é a classe de entrada que dispara o teste.

## Requisitos

É necessário ter o JDK instalado (versão 8 ou superior). Para conferir, rode `javac -version` e `java -version` no terminal.

## Compilação

Na raiz do projeto, execute:

```bash
javac -d out Configuracoes/*.java processos/*.java Mestre.java Escravo.java TesteTokenRing.java
```

O parâmetro `-d out` manda os arquivos `.class` para a pasta `out`, que o próprio `javac` cria se ela não existir. Assim o código-fonte fica separado dos arquivos compilados.

## Execução

Depois de compilar, rode a classe de teste a partir da raiz do projeto:

```bash
java -cp out
```

Se as classes de `Configuracoes` e `processos` estiverem declaradas em pacotes, o `-cp out` já resolve a busca por elas, desde que os nomes dos pacotes batam com os das pastas.

## Problemas comuns

Se aparecer `javac: command not found`, o JDK não está instalado ou não está no PATH. Se aparecer `Error: Could not find or load main class TesteTokenRing`, confira se a compilação terminou sem erros e se o comando `java` está sendo executado na raiz do projeto, e não dentro de `out`. No Windows, se o `*.java` não for expandido pelo terminal, use o PowerShell ou o Git Bash, ou liste os arquivos um a um.

## Autores

Daniel Oliveira, Jeovana Miranda, Lívia Alkimim e Thaylon Brenner.

---

Feito com ❤️
