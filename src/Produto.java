import java.text.NumberFormat;
import java.util.Objects;

public abstract class Produto {
	
	private static final double MARGEM_PADRAO = 0.2;
	private String descricao;
	private double precoCusto;
	private double margemLucro;
	
	/**
     * Inicializador privado. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	private void init(String desc, double precoCusto, double margemLucro) {
		
		if ((desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			this.descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}
	
	/**
     * Construtor completo. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	public Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}
	
	/**
     * Construtor sem margem de lucro - fica considerado o valor padrão de margem de lucro.
     * Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00 
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     */
	public Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}
	
	 /**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem de lucro.
     * @return Valor de venda do produto (double, positivo)
     */
	public double valorDeVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}
	
	/**
     * Descrição, em string, do produto, contendo sua descrição e o valor de venda.
     *  @return String com o formato:
     * [NOME]: R$ [VALOR DE VENDA]
     */
    @Override
	public String toString() {
    	
    	NumberFormat moeda = NumberFormat.getCurrencyInstance();
    	
		return String.format("NOME: " + descricao + ": " + moeda.format(valorDeVenda()));
	}

    @Override
    public boolean equals(Object o) {
        Produto produto = (Produto) o;
        
        if (this.descricao == null && produto.descricao == null) return true;
        if (this.descricao == null || produto.descricao == null) return false;
        
        return this.descricao.equalsIgnoreCase(produto.descricao);
    }

	/**
	* Gera uma linha de texto a partir dos dados do produto.
	* @return Uma
	string no formato "tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
	*/
	public abstract String gerarDadosTexto();
	
	/**
	* Cria um produto a partir de uma linha de dados em formato texto.
	* A linha de dados deve estar de acordo com a formatação
	* "tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
	* ou o funcionamento não será garantido. Os tipos são 1, para produto não perecível; e 2, para perecível.
	* @param linha Linha com os dados do produto a ser criado.
	* @return Um produto com os dados recebidos
	*/
	static Produto criarDoTexto(String linha) {
	/* A implementação deste método deve separar os atributos existentes na
	String linha, verificar se o produto
	é do tipo 1 ou 2, e instanciar o objeto adequado, com os dados fornecidos e de acordo com seu tipo. O objeto
	instanciado é retornado pelo método. */
	}

	/**
	* Lê os dados de um arquivo-texto e retorna um vetor de produtos. Arquivo-texto no formato:
	* N (quantidade de produtos)
	* tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]
	* Deve haver uma linha para cada um dos produtos.
	* Retorna um vetor vazio em caso de problemas com a leitura do arquivo.
	* @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
	* @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
	*/
	static Produto[] lerProdutos(String nomeArquivoDados) {
	}
	/** Localiza um produto no vetor de produtos cadastrados, a partir do nome de produto informado pelo usuário,
	* e imprime seus dados.
	* A busca não é sensível a letras maiúsculas e minúsculas. No caso de não encontrar o produto, imprime uma
	mensagem padrão */
	static void localizarProdutos() {
	}
	/**
	* Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
	* @param nomeArquivo Nome do arquivo a ser gravado.
	*/
	static void salvarProdutos(String nomeArquivo) {
	}
}