import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


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
        return Integer.parseInt(teclado.nextLine());
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
                return new Produto[0];
            }
            
            int tamanhoVetor = Integer.parseInt(primeiraLinha.trim());
            Produto[] produtos = new Produto[tamanhoVetor];
            int index = 0;
            
            String linha;
            while ((linha = reader.readLine()) != null && index < tamanhoVetor) {
                if (!linha.trim().isEmpty()) {
                    produtos[index] = Produto.criarDoTexto(linha);
                    index++;
                }
            }
            
            quantosProdutos = index;
            return produtos;
            
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar os dados dos produtos");
            return new Produto[0];
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

        for (Produto produto : produtosCadastrados) {
            if (produto != null && produto.getDesc().equalsIgnoreCase(busca)) {
                System.out.println("\n--- Produto Encontrado ---");
                System.out.println(produto.toString());
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
        if (produtosCadastrados == null || produtosCadastrados.length == 0) {
            System.out.println("Nenhum produto para salvar.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write(String.valueOf(produtosCadastrados.length));
            writer.newLine();

            for (Produto produto : produtosCadastrados) {
                if (produto != null) {
                    writer.write(produto.gerarDadosTexto());
                    writer.newLine();
                }
            }
            System.out.println("Dados salvos com sucesso em: " + nomeArquivo);
            
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados no arquivo: " + e.getMessage());
        }
    }

    static void listarTodosOsProdutos() {
    	
    }
    
    /**
     * Rotina para cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui o produto no vetor.
     */
    static void cadastrarProduto() {
    	
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
            }
            pausa();
        }while(opcao != 0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();    
    }
}
