package br.com.caelum.financas.mb;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.caelum.financas.dao.ContaDao;
import br.com.caelum.financas.dao.MovimentacaoDao;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;

@ManagedBean
@ViewScoped
//@Named
//@SessionScoped
public class MovimentacoesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Movimentacao> movimentacoes;
	private Movimentacao movimentacao = new Movimentacao();
	private Integer contaId;
	private Integer categoriaId;

	@Inject
	private MovimentacaoDao movimentacaoDao;
	
	@Inject
	private ContaDao contaDao;

	public void grava() {
		Conta contaDaMovimentacao = contaDao.busca(this.contaId);
		if (contaDaMovimentacao != null) { // Pode ser substituído por uma excepition
			System.out.println("Fazendo a gravacao da movimentacao");
			this.movimentacao.setConta(contaDaMovimentacao);
			this.movimentacaoDao.adiciona(this.movimentacao);
			this.movimentacoes = movimentacaoDao.lista();
			limpaFormularioDoJSF();
		}
	}

	public void remove() {
		System.out.println("Removendo a movimentacao");
		this.movimentacaoDao.remove(this.movimentacao);
		this.movimentacoes = movimentacaoDao.lista();
		limpaFormularioDoJSF();
	}

	public List<Movimentacao> getMovimentacoes() {
		return this.movimentacoes == null ? this.movimentacoes = movimentacaoDao.lista() : this.movimentacoes;
	}

	public Movimentacao getMovimentacao() {
		if (movimentacao.getData() == null) {
			movimentacao.setData(Calendar.getInstance());
		}
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public Integer getContaId() {
		return contaId;
	}

	public void setContaId(Integer contaId) {
		this.contaId = contaId;
	}

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

	/**
	 * Esse metodo apenas limpa o formulario da forma com que o JSF espera.
	 * Invoque-o no momento manager que precisar do formulario vazio.
	 */
	private void limpaFormularioDoJSF() {
		this.movimentacao = new Movimentacao();
	}

	public TipoMovimentacao[] getTiposDeMovimentacao() {
		return TipoMovimentacao.values();
	}
}
