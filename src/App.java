import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


public class App {

    /** Quantidade máxima de produtos que podem ser armazenados no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados */
    static Produto[] produtosCadastrados;

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos = 0;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }
    
    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * @return Um inteiro com a opção do usuário.
    */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e imprimir os dados de um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        try {
            return Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Lê os dados de um arquivo-texto e retorna um vetor de produtos. Arquivo-texto no formato
     * N (quantidade de produtos) <br/>
     * tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */

    public static Produto[] lerProdutos(String nomeArquivoDados) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nomeArquivoDados))) {
            String primeiraLinha = reader.readLine();
            if (primeiraLinha == null || primeiraLinha.trim().isEmpty()) {
                quantosProdutos = 0;
                return new Produto[MAX_NOVOS_PRODUTOS];
            }

            int tamanhoVetor = Integer.parseInt(primeiraLinha.trim());
            Produto[] produtos = new Produto[tamanhoVetor + MAX_NOVOS_PRODUTOS];
            int index = 0;

            String linha;
            while ((linha = reader.readLine()) != null && index < tamanhoVetor) {
                if (!linha.trim().isEmpty()) {
                    try {
                        Produto novo = Produto.criarDoTexto(linha.trim());
                        if (novo != null) {
                            produtos[index] = novo;
                            index++;
                        } else {
                            System.out.println("Tipo de produto inválido na linha: " + linha);
                        }
                    } catch (IllegalArgumentException | DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Produto ignorado (" + e.getMessage() + "): " + linha);
                    }
                }
            }

            quantosProdutos = index;
            return produtos;

        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar os dados dos produtos");
            quantosProdutos = 0;
            return new Produto[MAX_NOVOS_PRODUTOS];
        }
    }

    /** Localiza um produto no vetor de produtos cadastrados, a partir do nome de produto informado pelo usuário, e imprime seus dados. 
     *  A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime uma mensagem padrão */
    /** Localiza um produto no vetor de produtos cadastrados, a partir do nome de produto informado pelo usuário, e imprime seus dados. 
     *  A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime uma mensagem padrão */
    static void localizarProdutos() {
        System.out.print("Digite o nome do produto: ");
        String busca = teclado.nextLine();
        boolean encontrado = false;

        for (int i = 0; i < quantosProdutos; i++) {
            Produto produto = produtosCadastrados[i];
            if (produto != null && produto.getDesc().equalsIgnoreCase(busca.trim())) {
                System.out.println("\n--- Produto Encontrado ---");
                System.out.println(descreverProduto(produto));
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("\nProduto não encontrado.");
        }
    }

    
    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo) {
        if (produtosCadastrados == null || quantosProdutos == 0) {
            System.out.println("Nenhum produto para salvar.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write(String.valueOf(quantosProdutos));
            writer.newLine();

            for (int i = 0; i < quantosProdutos; i++) {
                writer.write(produtosCadastrados[i].gerarDadosTexto());
                writer.newLine();
            }
            System.out.println("Dados salvos com sucesso em: " + nomeArquivo);
            
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados no arquivo: " + e.getMessage());
        }
    }

    /**
     * Retorna a descrição do produto para exibição. Caso o produto esteja vencido (valor de venda
     * não pode ser solicitado), retorna uma mensagem indicando o vencimento.
     * @param produto Produto a ser descrito
     * @return String com os dados do produto
     */
    static String descreverProduto(Produto produto) {
        try {
            return produto.toString();
        } catch (IllegalArgumentException e) {
            return produto.getDesc() + ": PRODUTO VENCIDO";
        }
    }

    /** Lista todos os produtos cadastrados no vetor. Em caso de vetor vazio, imprime uma mensagem padrão */
    static void listarTodosOsProdutos() {
        if (quantosProdutos == 0) {
            System.out.println("\nNenhum produto cadastrado.");
            return;
        }

        System.out.println("\n--- Produtos Cadastrados ---");
        for (int i = 0; i < quantosProdutos; i++) {
            System.out.println((i + 1) + " - " + descreverProduto(produtosCadastrados[i]));
        }
    }

    /**
     * Rotina para cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui o produto no vetor.
     */
    static void cadastrarProduto() {
        if (quantosProdutos >= produtosCadastrados.length) {
            System.out.println("\nNão há espaço para cadastrar novos produtos.");
            return;
        }

        try {
            System.out.print("Tipo do produto (1 - Não perecível, 2 - Perecível): ");
            int tipo = Integer.parseInt(teclado.nextLine().trim());
            if (tipo != 1 && tipo != 2) {
                System.out.println("\nTipo de produto inválido.");
                return;
            }

            System.out.print("Descrição: ");
            String descricao = teclado.nextLine().trim();

            System.out.print("Preço de custo: ");
            double precoCusto = Double.parseDouble(teclado.nextLine().trim().replace(',', '.'));

            System.out.print("Margem de lucro (ex.: 0.3 para 30%; enter para padrão de 20%): ");
            String margemTexto = teclado.nextLine().trim();

            Produto novo;
            if (tipo == 1) {
                if (margemTexto.isEmpty()) {
                    novo = new ProdutoNaoPerecivel(descricao, precoCusto);
                } else {
                    novo = new ProdutoNaoPerecivel(descricao, precoCusto, Double.parseDouble(margemTexto.replace(',', '.')));
                }
            } else {
                System.out.print("Data de validade (dd/MM/aaaa): ");
                LocalDate validade = LocalDate.parse(teclado.nextLine().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (margemTexto.isEmpty()) {
                    novo = new ProdutoPerecivel(descricao, precoCusto, validade);
                } else {
                    novo = new ProdutoPerecivel(descricao, precoCusto, Double.parseDouble(margemTexto.replace(',', '.')), validade);
                }
            }

            for (int i = 0; i < quantosProdutos; i++) {
                if (produtosCadastrados[i].equals(novo)) {
                    System.out.println("\nJá existe um produto cadastrado com essa descrição.");
                    return;
                }
            }

            produtosCadastrados[quantosProdutos] = novo;
            quantosProdutos++;
            System.out.println("\nProduto cadastrado com sucesso:");
            System.out.println(descreverProduto(novo));

        } catch (NumberFormatException e) {
            System.out.println("\nValor numérico inválido. Cadastro cancelado.");
        } catch (DateTimeParseException e) {
            System.out.println("\nData inválida. Use o formato dd/MM/aaaa. Cadastro cancelado.");
        } catch (IllegalArgumentException e) {
            System.out.println("\nErro no cadastro: " + e.getMessage());
        }
    }
    
	public static void main(String[] args) {
		teclado = new Scanner(System.in, Charset.forName("UTF-8"));
        nomeArquivoDados = "C:\\Users\\1429288\\Downloads\\AEDs-II_Unidade0\\dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        
        int opcao = -1;
      
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
                case 0 -> { }
                default -> System.out.println("Opção inválida.");
            }
            pausa();
        }while(opcao != 0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();    
    }
}
