import java.util.List;

public class Venda {
    private int id;
    private Usuario usuario;
    private List<ItemVenda> itens;
    private double total;
    private FormaPagamento formaPagamento;

    public Venda(int id, Usuario usuario, List<ItemVenda> itens, FormaPagamento formaPagamento) {
        this.id = id;
        this.usuario = usuario;
        this.itens = itens;
        this.formaPagamento = formaPagamento;
        this.total = calcularTotal();
    }

    public Venda(Usuario usuario, List<ItemVenda> itens, FormaPagamento formaPagamento) {
        this.usuario = usuario;
        this.itens = itens;
        this.formaPagamento = formaPagamento;
        this.total = calcularTotal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    private double calcularTotal() {
        return itens.stream().mapToDouble(ItemVenda::calcularTotal).sum();
    }

}
