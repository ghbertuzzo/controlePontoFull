package pontoWeb.controller;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import pontoWeb.model.Dia;
import pontoWeb.model.Periodo;

public class PeriodoController {

	private final Integer SIZEVECTORDAY = 1440;

	public LocalTime numberToLocalTime(int number) {
		return LocalTime.of(number / 60, number % 60);
	}

	public Integer localTimeToNumber(LocalTime localTime) {
		return (localTime.getHour() * 60 + localTime.getMinute());
	}

	private Periodo mergePeriods(Periodo period, Periodo period2) {
		return new Periodo(period2.getEntrada(), period.getSaida());
	}

	private int newInterval(int initialIndex, Dia day) {
		int initial = initialIndex + 1;
		for (int i = initial; i < SIZEVECTORDAY; i++) {
			if (!day.getVectorDay()[i]) {
				return i;
			}
		}
		return SIZEVECTORDAY - 1;
	}

	public Dia setPeriods(List<Periodo> periods) {
		Dia day = new Dia();
		for (Periodo period : periods) {
			if (localTimeToNumber(period.getEntrada()) > localTimeToNumber(period.getSaida())) {
				for (int i = localTimeToNumber(period.getEntrada()); i < SIZEVECTORDAY; i++) {
					day.getVectorDay()[i] = true;
				}
				for (int i = 0; i < localTimeToNumber(period.getSaida()); i++) {
					day.getVectorDay()[i] = true;
				}

			} else {
				for (int i = localTimeToNumber(period.getEntrada()); i < localTimeToNumber(period.getSaida()); i++) {
					day.getVectorDay()[i] = true;
				}
			}
		}
		return day;
	}

	public List<Periodo> getPeriods(Dia day) {
		ArrayList<Periodo> listOfPeriods = new ArrayList<Periodo>();
		int initialIndex = -1;
		int finalIndex = -1;
		for (int i = 0; i < SIZEVECTORDAY; i++) {
			if (day.getVectorDay()[i]) {
				initialIndex = i;
				finalIndex = newInterval(initialIndex, day);
				Periodo period = new Periodo(numberToLocalTime(initialIndex), numberToLocalTime(finalIndex));
				listOfPeriods.add(period);
				i = finalIndex;
			}
		}
		listOfPeriods = needToMergePeriods(listOfPeriods);
		return listOfPeriods;
	}

	public ArrayList<Periodo> needToMergePeriods(ArrayList<Periodo> listOfPeriods) {
		for (Periodo period : listOfPeriods) {
			if (period.getEntrada().equals(LocalTime.of(0, 0))) {
				for (Periodo period2 : listOfPeriods) {
					if (period2.getSaida().equals(LocalTime.of(23, 59))) {
						Periodo newPeriod = mergePeriods(period, period2);
						listOfPeriods.remove(period);
						listOfPeriods.remove(period2);
						listOfPeriods.add(newPeriod);
						return listOfPeriods;
					}
				}
			}
		}
		return listOfPeriods;
	}

	public long sumBetweenPeriods(List<Periodo> listPeriods) {
		long minutesSum = 0;
		for(Periodo periodo: listPeriods) {
			minutesSum += ChronoUnit.MINUTES.between(periodo.getEntrada(),periodo.getSaida());
		}
		return minutesSum;
	}

}
