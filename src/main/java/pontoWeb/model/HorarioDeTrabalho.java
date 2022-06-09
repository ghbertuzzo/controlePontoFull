package pontoWeb.model;

import java.util.ArrayList;
import java.util.List;

import pontoWeb.view.HorarioDeTrabalhoView;

public class HorarioDeTrabalho {
	
	List<Periodo> listOfPeriods;

	public HorarioDeTrabalho(HorarioDeTrabalhoView horarioDeTrabalho) {
		super();
		this.listOfPeriods = new ArrayList<Periodo>();
		for(int i = 0; i<horarioDeTrabalho.getListEntries().size();i++) {
			this.listOfPeriods.add(new Periodo(horarioDeTrabalho.getListEntries().get(i), horarioDeTrabalho.getListExits().get(i)));
		}
	}	
	public HorarioDeTrabalho() {
		this.listOfPeriods = new ArrayList<Periodo>();
	}

}
