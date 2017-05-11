package br.com.caelum.financas.dao.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class MovimentacaoValorNegativoException extends RuntimeException {

//	private static final long serialVersionUID = 1L;

	public MovimentacaoValorNegativoException(String msg) {
		super(msg);
	}
}
