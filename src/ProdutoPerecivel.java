public class ProdutoPerecivel extends Produto {
    public ProdutoPerecivel(String desc, double precoCusto) {
        super(desc, precoCusto);

    }

    /**
     * Gera uma linha de texto a partir dos dados do produto.
     * Preço e margem de lucro são formatados com 2 casas decimais.
     * Data de validade é formatada no formato dd/mm/aaaa
     * 
     * @return Uma
     *         string no formato
     *         "2;descrição;preçoDeCusto;margemDeLucro;dataDeValidade"
     */
    @Override
    public String gerarDadosTexto() {
    }
}
