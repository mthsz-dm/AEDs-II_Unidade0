import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public abstract class Produto {
	
	private static final double MARGEM_PADRAO = 0.2;
	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;
	
     /**
     * Inicializador privado. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	private void init(String desc, double precoCusto, double margemLucro) {
		
		if ((desc != null) && (desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			descricao = desc;
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
	protected Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}
	
     /**
     * Construtor sem margem de lucro - fica considerado o valor padrão de margem de lucro.
     * Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00 
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     */
	protected Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}
	
     /**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem de lucro.
     * @return Valor de venda do produto (double, positivo)
     */
	public abstract double valorDeVenda();
	
	/**
     * Descrição, em string, do produto, contendo sua descrição e o valor de venda.
     *  @return String com o formato:
     * [NOME]: R$ [VALOR DE VENDA]
     */
    @Override
	public String toString() {
    	
    	NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    	return "NOME: " + descricao + ": " + moeda.format(valorDeVenda());
	}
    
    /**
     * Igualdade de produtos: caso possuam o mesmo nome/descrição. 
     * @param obj Outro produto a ser comparado 
     * @return booleano true/false conforme o parâmetro possua a descrição igual ou não a este produto.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Produto)) {
            return false;
        }
        Produto outro = (Produto) obj;
        return this.descricao.equalsIgnoreCase(outro.descricao);
    }

    /**
     * Código hash coerente com equals: baseado na descrição, sem diferenciar maiúsculas/minúsculas.
     * @return Código hash do produto
     */
    @Override
    public int hashCode() {
        return descricao.toLowerCase().hashCode();
    }
    
    /**
     * Cria um produto a partir de uma linha de dados em formato texto. A linha de dados deve estar de acordo com a formatação
     * "tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
     * ou o funcionamento não será garantido. Os tipos são 1, para produto não perecível; e 2, para perecível.
     * @param linha Linha com os dados do produto a ser criado.
     * @return Um produto com os dados recebidos
     */
    static Produto criarDoTexto(String linha) {
    	String[] dados = linha.split(";");
     int tipo = Integer.parseInt(dados[0]);
     
     String descricao = dados[1];
     double precoDeCusto = Double.parseDouble(dados[2]);
     double margemDeLucro = Double.parseDouble(dados[3]);

     if (tipo == 1) {
          return new ProdutoNaoPerecivel(descricao, precoDeCusto, margemDeLucro);
     } else if (tipo == 2) {
          String dataStr = dados[4];  
          DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
          LocalDate dataDeValidade = LocalDate.parse(dataStr, formatador);
            
            return new ProdutoPerecivel(descricao, precoDeCusto, margemDeLucro, dataDeValidade);
     }
     
     return null;
    }
    	
    /**
     * Gera uma linha de texto a partir dos dados do produto.
     * @return Uma string no formato "tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
     */
    public abstract String gerarDadosTexto();

    public String getDesc(){
      return this.descricao;
    }
}