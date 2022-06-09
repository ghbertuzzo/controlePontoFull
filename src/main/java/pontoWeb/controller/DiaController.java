package pontoWeb.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import pontoWeb.model.Dia;
import pontoWeb.model.Periodo;
import pontoWeb.view.TabelaView;

public class DiaController {

	public PeriodoController periodoController;
	private final Integer SIZEVECTORDAY = 1440;

	public DiaController() {
		this.periodoController = new PeriodoController();
	}

	public LocalTime numberToLocalTime(int number) {
		return LocalTime.of(number / 60, number % 60);
	}

	public Integer localTimeToNumber(LocalTime localTime) {
		return (localTime.getHour() * 60 + localTime.getMinute());
	}

	public Dia getConvertedDay(TabelaView tableView) {
		List<Periodo> listOfPeriods = new ArrayList<Periodo>();
		for (int i = 0; i < tableView.getListEntries().size(); i++) {
			Periodo period = new Periodo(tableView.getListEntries().get(i), tableView.getListExits().get(i));
			listOfPeriods.add(period);
		}
		Dia day = this.periodoController.setPeriods(listOfPeriods);
		return day;
	}

	public Dia getAtrasos(Dia horarioTrabalho, Dia marcacoesFeitas) {
		Dia atraso = new Dia();
		for (int i = 0; i < SIZEVECTORDAY; i++) {
			if (horarioTrabalho.getVectorDay()[i] && !marcacoesFeitas.getVectorDay()[i]) {
				atraso.getVectorDay()[i] = true;
			}
		}
		return atraso;
	}

	public Dia getHoraExtra(Dia marcacoesFeitas, Dia horarioTrabalho) {
		Dia horaextra = new Dia();
		for (int i = 0; i < SIZEVECTORDAY; i++) {
			if (marcacoesFeitas.getVectorDay()[i] && !horarioTrabalho.getVectorDay()[i]) {
				horaextra.getVectorDay()[i] = true;
			}
		}
		return horaextra;
	}

	public Dia getConvertedDayHT(String json) {
		//{"horario_trabalho":{"entradas":["08:00","13:00"],"saidas":["12:00","17:30"]},"marcacoes_feitas":{"entradas":["08:59","01:04","04:00"],"saidas":["05:09","07:40","09:05"]}}
		
		ArrayList<Periodo> periodArray = new ArrayList<Periodo>(); 
		PeriodoController periodoController = new PeriodoController();
		Dia diaAtraso = periodoController.setPeriods(periodArray);
		return diaAtraso;
	}

	public Dia getConvertedDayMF(String json) {
		// TODO Auto-generated method stub
		return null;
	}
}
