package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.caelum.financas.dao.exception.MovimentacaoValorNegativoException;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;

@Stateless
public class MovimentacaoDao {

	@PersistenceContext(unitName = "financas")
	EntityManager manager;

	public void adiciona(Movimentacao movimentacao) {
		this.manager.persist(movimentacao);
		if (movimentacao.getValor().compareTo(BigDecimal.ZERO) < 0) {
			throw new MovimentacaoValorNegativoException("Erro: movimentação com valor negativo.");
		}
	}

	public Movimentacao busca(Integer id) {
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return this.manager.createQuery("select m from Movimentacao m", Movimentacao.class).getResultList();
	}

	public List<Movimentacao> lista(String titular) {
		String jpql = "select m from Movimentacao m where m.conta.titular like :titular order by m.valor desc";
		TypedQuery<Movimentacao> query = this.manager.createQuery(jpql, Movimentacao.class);
		query.setParameter("titular", "%" + titular + "%");
		return query.getResultList();
	}

	public List<Movimentacao> lista(Conta conta) {
		String jpql = "select m from Movimentacao m where m.conta = :conta order by m.valor desc";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("conta", conta);
		return query.getResultList();
	}

	public List<Movimentacao> lista(TipoMovimentacao tipo, BigDecimal valorMax) {
		System.out.println(tipo.toString());
		String jpql = "select m from Movimentacao m where m.tipoMovimentacao = :tipo and m.valor <= :valorMax order by m.valor desc";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("tipo", tipo);
		query.setParameter("valorMax", valorMax);
		return query.getResultList();
	}

	public BigDecimal totalMovimentado(Conta conta, TipoMovimentacao tipoMovimentacao) {
		String jpql = "select sum(m.valor) from Movimentacao m where m.conta = :conta and m.tipoMovimentacao = :tipo";
		TypedQuery<BigDecimal> query = this.manager.createQuery(jpql, BigDecimal.class);
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipoMovimentacao);
		return query.getSingleResult();
	}

	public List<Object[]> totalMovimentadoMensal(Conta c, TipoMovimentacao t) {
		String jpql = "select month(m.data), year(m.data), sum(m.valor) from Movimentacao m "
				+ "where m.conta = :conta and m.tipoMovimentacao = :tipo group by year(m.data) || month(m.data)";
		Query q = this.manager.createQuery(jpql);
		q.setParameter("conta", c);
		q.setParameter("tipo", t);
		return q.getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		Movimentacao movimentacaoParaRemover = this.manager.find(Movimentacao.class, movimentacao.getId());
		this.manager.remove(movimentacaoParaRemover);
	}

}
