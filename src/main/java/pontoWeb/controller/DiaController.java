package pontoWeb.controller;

import java.time.LocalTime;
import java.util.ArrayList;

import com.google.gson.Gson;

import pontoWeb.model.Dia;
import pontoWeb.model.JSON_Periodos;
import pontoWeb.model.Periodo;

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
		Gson gson = new Gson();
		JSON_Periodos listPeriods = gson.fromJson(json, JSON_Periodos.class);
		ArrayList<Periodo> periodArray = new ArrayList<Periodo>();
		for (int i = 0; i < listPeriods.getPeriodos().size(); i++) {
			if (listPeriods.getPeriodos().get(i).equals("-")) {
				break;
			} else {
				Periodo p = new Periodo(LocalTime.parse(listPeriods.getPeriodos().get(i)),
						LocalTime.parse(listPeriods.getPeriodos().get(i + 1)));
				periodArray.add(p);
				i++;
			}
		}
		PeriodoController periodoController = new PeriodoController();
		Dia diaHT = periodoController.setPeriods(periodArray);
		return diaHT;
	}

	public Dia getConvertedDayMF(String json) {
		Gson gson = new Gson();
		JSON_Periodos listPeriods = gson.fromJson(json, JSON_Periodos.class);
		ArrayList<Periodo> periodArray = new ArrayList<Periodo>();
		int index = -1;
		for (int i = 0; i < listPeriods.getPeriodos().size(); i++) {
			if (listPeriods.getPeriodos().get(i).equals("-")) {
				index = i + 1;
			}
		}
		for (int j = index; j < listPeriods.getPeriodos().size(); j++) {
			Periodo p = new Periodo(LocalTime.parse(listPeriods.getPeriodos().get(j)),
					LocalTime.parse(listPeriods.getPeriodos().get(j + 1)));
			periodArray.add(p);
			j++;
		}
		PeriodoController periodoController = new PeriodoController();
		Dia diaMF = periodoController.setPeriods(periodArray);
		return diaMF;
	}
}
