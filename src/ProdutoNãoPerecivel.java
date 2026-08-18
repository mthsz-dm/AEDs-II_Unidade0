public class ProdutoNãoPerecivel extends Produto {
    public ProdutoNãoPerecivel(String desc, double precoCusto) {
        super(desc, precoCusto);
    }

    /**
     * Gera uma linha de texto a partir dos dados do produto.
     * Preço e margem de lucro são formatados com 2 casas decimais.
     * 
     * @return Uma
     *         string no formato "1;descrição;preçoDeCusto;margemDeLucro"
     */
    @Override
    public String gerarDadosTexto() {
    }
}
