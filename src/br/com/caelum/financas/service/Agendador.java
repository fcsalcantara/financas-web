package br.com.caelum.financas.service;

import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

@Singleton
public class Agendador {

	// Serviço de temporizador oferecido pelo EJB container
	@Resource
	private TimerService timerService;

	// @PostConstruct
	// public void iniciarTimer() {
	// this.timerService.createTimer(10000L, "timeout");
	// }

	// Método executado no mb.AgendadorFormBean
	public void agenda(String exprMinutos, String exprSegundos) {

		// Instancia um objeto de expressão de agendamento
		ScheduleExpression schExpr = new ScheduleExpression();

		// Define o ciclo do agendamento
		schExpr.hour("*");
		schExpr.minute(exprMinutos);
		schExpr.second(exprSegundos);

		// Instancia um objeto de configuração do Timer
		TimerConfig tmrConf = new TimerConfig();

		// Configura o ciclo de agendamento do Timer
		tmrConf.setInfo(schExpr.toString());
		tmrConf.setPersistent(false); // Configura o agendamento para não ser
										// persistido pelo container

		// Inicia o agendamento no TimerService
		this.timerService.createCalendarTimer(schExpr, tmrConf);
		System.out.println("Agendamento para " + schExpr);
	}

	// Ação executada quando o temporizador fecha o ciclo
	@Timeout
	public void agendado(Timer timer) {
		System.out.println(timer.getInfo());
		// ...
	}

//	@Schedule(hour = "*", minute = "*", second = "*/15", persistent = false)
	public void enviaEmail() {
		System.out.println("Enviando email a cada 15 segundos...");
	}

	// private static int totalCriado;

	// @PostConstruct
	// public void incrementarTotalCriado() {
	// System.out.println("Agendador criado.");
	// ++totalCriado;
	// }

	// @PreDestroy
	// public void antesDeEncerrar() {
	// System.out.println("Agendador destruído.");
	// }

	// public void executa() {
	// System.out.printf("%d instancias criadas %n", totalCriado);

	// simulando demora de 4s na execucao
	// try {
	// System.out.printf("Executando %s %n", this);
	// Thread.sleep(40000);
	// } catch (InterruptedException e) {
	// }
	// }

}
