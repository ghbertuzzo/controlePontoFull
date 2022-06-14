//BOTÃO EXPORT
$(document).ready(function () {

	$("#btn_export").on("click", function () {
		var xhttp = new XMLHttpRequest();
		xhttp.responseType = 'blob';
		xhttp.onreadystatechange = function () {
			if (this.readyState == 4 && this.status == 200) {
				var blob = new Blob([xhttp.response], { type: "application/pdf" });
				var link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob);
				var fileName = "report.pdf";
				link.download = fileName;
				link.click();
			}
		};
		xhttp.open("GET", "export", true);
		xhttp.send();
	});
});

//BOTÃO SALVAR
$(document).ready(function () {

	$("#btn_save").on("click", function () {
		var obj = new Object();
		var sizetableHt = document.getElementById("tab_logicHT").rows.length;
		var sizetableMf = document.getElementById("tab_logicMF").rows.length;
		let myvar;

		//DADOS DO HORARIO DE TRABALHO
		var periodos = [];
		for (let i = 1; i < sizetableHt - 1; i++) {
			myvar = "entrada_HT" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
			myvar = "saida_HT" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
		}

		//DIVISOR NA LISTA ENTRE HT E MF
		periodos.push("-");

		//DADOS DAS MARCACOES FEITAS
		for (let i = 1; i < sizetableMf - 1; i++) {
			myvar = "entrada_MF" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
			myvar = "saida_MF" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
		}
		obj.periodos = periodos;

		//VALIDA CAMPO DATA
		if ($("[name=dataform]")[0].value != "") {
			obj.data = $("[name=dataform]")[0].value;

			var data = JSON.stringify(obj);
			var xhr = new XMLHttpRequest();
			var url = "salvar";
			xhr.open("POST", url, true);
			xhr.setRequestHeader("Content-Type", "application/json");
			xhr.onreadystatechange = function () {
				if (xhr.readyState === 4 && xhr.status === 200) {
					addHistorico(xhr.responseText);
				}
			};
			xhr.send(data);
		} else {
			alert("Informe uma data válida!");
		}
	});
});

//BOTÃO CARREGAR
$(document).ready(function () {

	//CRIA BACKUP DAS LINHAS DAS 4 TABELAS INICIAIS
	var myTableHT = document.getElementById("tab_logicHT");
	myTableHT.oldHTML = myTableHT.innerHTML;
	var myTableMF = document.getElementById("tab_logicMF");
	myTableMF.oldHTML = myTableMF.innerHTML;
	var myTableHE = document.getElementById("tab_logicHE");
	myTableHE.oldHTML = myTableHE.innerHTML;
	var myTableAT = document.getElementById("tab_logicAT");
	myTableAT.oldHTML = myTableAT.innerHTML;

	$("#btn_load").on("click", function () {
		var obj = new Object();

		//VALIDA CAMPO DE DATA
		if ($("[name=dataform]")[0].value != "") {
			obj.data = $("[name=dataform]")[0].value;

			//RESTAURA TABELAS NO MODO INICIAL ANTES DE CARREGAR HISTÓRICO
			myTableHT.innerHTML = myTableHT.oldHTML;
			myTableMF.innerHTML = myTableMF.oldHTML;
			myTableAT.innerHTML = myTableAT.oldHTML;
			myTableHE.innerHTML = myTableHE.oldHTML;

			var data = JSON.stringify(obj);
			var xhr = new XMLHttpRequest();
			var url = "carregar";
			xhr.open("POST", url, true);
			xhr.setRequestHeader("Content-Type", "application/json");
			xhr.onreadystatechange = function () {
				if (xhr.readyState === 4 && xhr.status === 200) {
					loadHistorico(xhr.responseText);
				}
			};
			xhr.send(data);
		} else {
			alert("Informe uma data válida!");
		}
	});
});

//BOTÃO CALCULO ATRASOS 
$(document).ready(function () {

	//CRIA BACKUP DAS LINHAS DA TABELA DE ATRASO
	var myTableAT = document.getElementById("tab_logicAT");
	myTableAT.oldHTML = myTableAT.innerHTML;

	$("#btn_calc_at").on("click", function () {
		var obj = new Object();
		var sizetableHt = document.getElementById("tab_logicHT").rows.length;
		var sizetableMf = document.getElementById("tab_logicMF").rows.length;
		let myvar;
		obj = new Object();

		//DADOS DO HORARIO DE TRABALHO
		var periodos = [];
		for (let i = 1; i < sizetableHt - 1; i++) {
			myvar = "entrada_HT" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
			myvar = "saida_HT" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
		}

		//DIVISOR NA LISTA ENTRE HT E MF
		periodos.push("-");

		//DADOS DAS MARCACOES FEITAS
		for (let i = 1; i < sizetableMf - 1; i++) {
			myvar = "entrada_MF" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
			myvar = "saida_MF" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
		}

		//ADD LISTA DE PERIODOS NO OBJ
		obj.periodos = periodos;

		//RESTAURA ESTADO INICIAL DA TABELA ANTES DE POPULAR COM OS ATRASOS		
		myTableAT.innerHTML = myTableAT.oldHTML;

		var data = JSON.stringify(obj);
		console.log(JSON.stringify(obj));
		var xhr = new XMLHttpRequest();
		var url = "atrasos";
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.onreadystatechange = function () {
			if (xhr.readyState === 4 && xhr.status === 200) {
				addLinesAtrasos(JSON.parse(xhr.responseText));
			}
		}
		xhr.send(data);
	});
});

//BOTÃO CALCULO HORAEXTRA 
$(document).ready(function () {

	//CRIA BACKUP DAS LINHAS DA TABELA DE HORAEXTRA
	var myTableHE = document.getElementById("tab_logicHE");
	myTableHE.oldHTML = myTableHE.innerHTML;

	$("#btn_calc_he").on("click", function () {
		var obj = new Object();
		var sizetableHt = document.getElementById("tab_logicHT").rows.length;
		var sizetableMf = document.getElementById("tab_logicMF").rows.length;
		let myvar;

		//DADOS DO HORARIO DE TRABALHO
		var periodos = [];
		for (let i = 1; i < sizetableHt - 1; i++) {
			myvar = "entrada_HT" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
			myvar = "saida_HT" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
		}

		//DIVISOR NA LISTA ENTRE HT E MF
		periodos.push("-");

		//DADOS DAS MARCACOES FEITAS
		for (let i = 1; i < sizetableMf - 1; i++) {
			myvar = "entrada_MF" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
			myvar = "saida_MF" + i;
			if ($("[name=" + myvar + "]")[0].value != "") {
				periodos.push($("[name=" + myvar + "]")[0].value);
			}
		}

		//ADD LISTA DE PERIODOS NO OBJ
		obj.periodos = periodos;

		//RESTAURA ESTADO INICIAL DA TABELA ANTES DE POPULAR COM AS HORAS EXTRAS	
		myTableHE.innerHTML = myTableHE.oldHTML;

		var data = JSON.stringify(obj);
		var xhr = new XMLHttpRequest();
		var url = "horaextra";
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.onreadystatechange = function () {
			if (xhr.readyState === 4 && xhr.status === 200) {
				addLinesHoraExtra(JSON.parse(xhr.responseText));
			}
		};
		xhr.send(data);
	});
});

function addHistorico(jsonobj) {

	//CONVERTE STRINGJSON EM OBJ JSON
	var jsonPeriodos = JSON.parse(jsonobj);

	if (jsonPeriodos.periodos[0] != "vazio") {
		alert("Informações salvas com sucesso!");

		//APÓS SALVAR RESTAURA PÁGINA NO MODO INICIAL
		window.location = window.location.href;

	} else if (jsonPeriodos.periodos[0] == "vazio") {
		alert("Erro ao salvar informações!");
	} else {
		alert("Erro");
	}
}

function loadHistorico(jsonobj) {

	//CONVERTE STRINGJSON EM OBJ JSON
	var jsonPeriodos = JSON.parse(jsonobj);

	if (jsonPeriodos.periodos[0] != "vazio") {

		const periodosHT = [];
		const periodosMF = [];
		const periodosAT = [];
		const periodosHE = [];

		var delimiterMF = -1;
		for (let i = 0; i < jsonPeriodos.periodos.length; i++) {
			if (jsonPeriodos.periodos[i] === "-") {
				delimiterMF = i;
				break;
			} else {
				periodosHT.push(jsonPeriodos.periodos[i]);
			}
		}
		addLinesListHT(periodosHT);

		var delimiterAT = -1;
		for (let i = delimiterMF + 1; i < jsonPeriodos.periodos.length; i++) {
			if (jsonPeriodos.periodos[i] === "-") {
				delimiterAT = i;
				break;
			} else {
				periodosMF.push(jsonPeriodos.periodos[i]);
			}
		}
		addLinesListMF(periodosMF);

		var delimiterHE = -1;
		for (let i = delimiterAT + 1; i < jsonPeriodos.periodos.length; i++) {
			if (jsonPeriodos.periodos[i] === "-") {
				delimiterHE = i;
				break;
			} else {
				periodosAT.push(jsonPeriodos.periodos[i]);
			}
		}
		addLinesListAT(periodosAT);

		for (let i = delimiterHE + 1; i < jsonPeriodos.periodos.length; i++) {
			periodosHE.push(jsonPeriodos.periodos[i]);
		}
		addLinesListHE(periodosHE);
		alert("Informações carregadas com sucesso!");

	} else if (jsonPeriodos.periodos[0] == "vazio") {
		alert("Informações não encontradas nesta data!");
	} else {
		alert("bugou");
	}
}

function addLinesListHT(periodosHT) {

	//CALCULA QTAS LINHAS PRECISA ADC (QTDD DE PERIODOS DIV 2 (2 PERIODOS POR LINHA DA TABELA ENTRADA/SAIDA))
	let numrowtoadd = periodosHT.length / 2 - 1;

	//LAÇO QUE ADD A QTDD DE LINHAS NECESSÁRIAS	
	for (let i = 0; i < numrowtoadd; i++) {
		var newid = 0;
		$.each($("#tab_logicHT tr"), function () {
			if (parseInt($(this).data("id")) > newid) {
				newid = parseInt($(this).data("id"));
			}
		});
		newid++;
		var tr = $("<tr></tr>", {
			id: "addr" + newid,
			"data-id": newid
		});
		$.each($("#tab_logicHT tbody tr:nth(0) td"), function () {
			var cur_td = $(this);
			var children = cur_td.children();
			if ($(this).data("name") != undefined) {
				var td = $("<td></td>", {
					"data-name": $(cur_td).data("name")
				});
				var c = $(cur_td).find($(children[0]).prop('tagName')).clone().val("");
				c.attr("name", $(cur_td).data("name") + "_HT" + newid);
				c.appendTo($(td));
				td.appendTo($(tr));
			} else {
				var td = $("<td></td>", {
					'text': $('#tab_logicHT tr').length
				}).appendTo($(tr));
			}
		});

		// add delete button and td       
		$("<td></td>").append(
			$('<button class="btnic btn btn-danger row-remove"><i class="fa fa-close"></i></button>')
				.click(function () {
					$(this).closest("tr").remove();
				})
		).appendTo($(tr));

		// add the new row
		$(tr).appendTo($('#tab_logicHT'));

		$(tr).find("td button.row-remove").on("click", function () {
			$(this).closest("tr").remove();
		});
	}

	//PREENCHE VALORES DAS LINHAS ADCIONADAS
	let j = 0;
	for (let i = 0; i < periodosHT.length / 2; i++) {
		myvar = "entrada_HT" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = periodosHT[j];
		myvar = "saida_HT" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = periodosHT[j + 1];
		j += 2;
	}
}

function addLinesListMF(periodosMF) {

	//CALCULA QTAS LINHAS PRECISA ADC (QTDD DE PERIODOS DIV 2 (2 PERIODOS POR LINHA DA TABELA ENTRADA/SAIDA))
	let numrowtoadd = periodosMF.length / 2 - 1;

	//LAÇO QUE ADD A QTDD DE LINHAS NECESSÁRIAS	
	for (let i = 0; i < numrowtoadd; i++) {
		var newid = 0;
		$.each($("#tab_logicMF tr"), function () {
			if (parseInt($(this).data("id")) > newid) {
				newid = parseInt($(this).data("id"));
			}
		});
		newid++;
		var tr = $("<tr></tr>", {
			id: "addr" + newid,
			"data-id": newid
		});

		$.each($("#tab_logicMF tbody tr:nth(0) td"), function () {
			var cur_td = $(this);
			var children = cur_td.children();
			if ($(this).data("name") != undefined) {
				var td = $("<td></td>", {
					"data-name": $(cur_td).data("name")
				});
				var c = $(cur_td).find($(children[0]).prop('tagName')).clone().val("");
				c.attr("name", $(cur_td).data("name") + "_MF" + newid);
				c.appendTo($(td));
				td.appendTo($(tr));
			} else {
				var td = $("<td></td>", {
					'text': $('#tab_logicMF tr').length
				}).appendTo($(tr));
			}
		});

		// add delete button and td       
		$("<td></td>").append(
			$('<button class="btnic btn btn-danger row-remove"><i class="fa fa-close"></i></button>')
				.click(function () {
					$(this).closest("tr").remove();
				})
		).appendTo($(tr));

		// add the new row
		$(tr).appendTo($('#tab_logicMF'));

		$(tr).find("td button.row-remove").on("click", function () {
			$(this).closest("tr").remove();
		});
	}
	//PREENCHE VALORES DAS LINHAS ADCIONADAS
	let j = 0;
	for (let i = 0; i < periodosMF.length / 2; i++) {
		myvar = "entrada_MF" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = periodosMF[j];
		myvar = "saida_MF" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = periodosMF[j + 1];
		j += 2;
	}
}

function addLinesListAT(periodosAT) {

	//CALCULA QTAS LINHAS PRECISA ADC (QTDD DE PERIODOS DIV 2 (2 PERIODOS POR LINHA DA TABELA ENTRADA/SAIDA))
	let numrowtoadd = periodosAT.length / 2 - 1;

	//LAÇO QUE ADD A QTDD DE LINHAS NECESSÁRIAS	
	for (let i = 0; i < numrowtoadd; i++) {
		var newid = 0;
		$.each($("#tab_logicAT tr"), function () {
			if (parseInt($(this).data("id")) > newid) {
				newid = parseInt($(this).data("id"));
			}
		});
		newid++;
		var tr = $("<tr></tr>", {
			id: "addr" + newid,
			"data-id": newid
		});

		$.each($("#tab_logicAT tbody tr:nth(0) td"), function () {
			var cur_td = $(this);
			var children = cur_td.children();
			if ($(this).data("name") != undefined) {
				var td = $("<td></td>", {
					"data-name": $(cur_td).data("name")
				});
				var c = $(cur_td).find($(children[0]).prop('tagName')).clone().val("");
				c.attr("name", $(cur_td).data("name") + "_AT" + newid);
				c.appendTo($(td));
				td.appendTo($(tr));
			} else {
				var td = $("<td></td>", {
					'text': $('#tab_logicAT tr').length
				}).appendTo($(tr));
			}
		});

		// add delete button and td       
		$("<td></td>").append(
			$('<button class="btnic btn btn-danger row-remove"><i class="fa fa-close"></i></button>')
				.click(function () {
					$(this).closest("tr").remove();
				})
		).appendTo($(tr));

		// add the new row
		$(tr).appendTo($('#tab_logicAT'));

		$(tr).find("td button.row-remove").on("click", function () {
			$(this).closest("tr").remove();
		});
	}
	//PREENCHE VALORES DAS LINHAS ADCIONADAS
	let j = 0;
	for (let i = 0; i < periodosAT.length / 2; i++) {
		myvar = "entrada_AT" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = periodosAT[j];
		myvar = "saida_AT" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = periodosAT[j + 1];
		j += 2;
	}
}

function addLinesListHE(periodosHE) {

	//CALCULA QTAS LINHAS PRECISA ADC (QTDD DE PERIODOS DIV 2 (2 PERIODOS POR LINHA DA TABELA ENTRADA/SAIDA))
	let numrowtoadd = periodosHE.length / 2 - 1;

	//LAÇO QUE ADD A QTDD DE LINHAS NECESSÁRIAS	
	for (let i = 0; i < numrowtoadd; i++) {
		var newid = 0;
		$.each($("#tab_logicHE tr"), function () {
			if (parseInt($(this).data("id")) > newid) {
				newid = parseInt($(this).data("id"));
			}
		});
		newid++;
		var tr = $("<tr></tr>", {
			id: "addr" + newid,
			"data-id": newid
		});

		$.each($("#tab_logicHE tbody tr:nth(0) td"), function () {
			var cur_td = $(this);
			var children = cur_td.children();
			if ($(this).data("name") != undefined) {
				var td = $("<td></td>", {
					"data-name": $(cur_td).data("name")
				});
				var c = $(cur_td).find($(children[0]).prop('tagName')).clone().val("");
				c.attr("name", $(cur_td).data("name") + "_HE" + newid);
				c.appendTo($(td));
				td.appendTo($(tr));
			} else {
				var td = $("<td></td>", {
					'text': $('#tab_logicHE tr').length
				}).appendTo($(tr));
			}
		});

		// add delete button and td       
		$("<td></td>").append(
			$('<button class="btnic btn btn-danger row-remove"><i class="fa fa-close"></i></button>')
				.click(function () {
					$(this).closest("tr").remove();
				})
		).appendTo($(tr));

		// add the new row
		$(tr).appendTo($('#tab_logicHE'));

		$(tr).find("td button.row-remove").on("click", function () {
			$(this).closest("tr").remove();
		});
	}
	//PREENCHE VALORES DAS LINHAS ADCIONADAS
	let j = 0;
	for (let i = 0; i < periodosHE.length / 2; i++) {
		myvar = "entrada_HE" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = periodosHE[j];
		myvar = "saida_HE" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = periodosHE[j + 1];
		j += 2;
	}
}

function addLinesAtrasos(jsonobj) {

	//CALCULA QTAS LINHAS PRECISA ADC (QTDD DE PERIODOS DIV 2 (2 PERIODOS POR LINHA DA TABELA ENTRADA/SAIDA))
	let numrowtoadd = jsonobj.periodos.length / 2 - 1;

	//LAÇO QUE ADD A QTDD DE LINHAS NECESSÁRIAS	
	for (let i = 0; i < numrowtoadd; i++) {
		var newid = 0;
		$.each($("#tab_logicAT tr"), function () {
			if (parseInt($(this).data("id")) > newid) {
				newid = parseInt($(this).data("id"));
			}
		});
		newid++;
		var tr = $("<tr></tr>", {
			id: "addr" + newid,
			"data-id": newid
		});

		$.each($("#tab_logicAT tbody tr:nth(0) td"), function () {
			var cur_td = $(this);
			var children = cur_td.children();
			if ($(this).data("name") != undefined) {
				var td = $("<td></td>", {
					"data-name": $(cur_td).data("name")
				});
				var c = $(cur_td).find($(children[0]).prop('tagName')).clone().val("");
				c.attr("name", $(cur_td).data("name") + "_AT" + newid);
				c.appendTo($(td));
				td.appendTo($(tr));
			} else {
				var td = $("<td></td>", {
					'text': $('#tab_logicAT tr').length
				}).appendTo($(tr));
			}
		});

		// add delete button and td
		$("<td></td>").append(
			$('<button class="btnic btn btn-danger row-remove"><i class="fa fa-close"></i></button>')
				.click(function () {
					$(this).closest("tr").remove();
				})
		).appendTo($(tr));

		// add the new row
		$(tr).appendTo($('#tab_logicAT'));

		$(tr).find("td button.row-remove").on("click", function () {
			$(this).closest("tr").remove();
		});
	}

	//PREENCHE VALORES DAS LINHAS ADCIONADAS
	let j = 0;
	for (let i = 0; i < jsonobj.periodos.length / 2; i++) {
		myvar = "entrada_AT" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = jsonobj.periodos[j];
		myvar = "saida_AT" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = jsonobj.periodos[j + 1];
		j += 2;
	}
}

function addLinesHoraExtra(jsonobj) {

	//CALCULA QTAS LINHAS PRECISA ADC (QTDD DE PERIODOS DIV 2 (2 PERIODOS POR LINHA DA TABELA ENTRADA/SAIDA))
	let numrowtoadd = jsonobj.periodos.length / 2 - 1;

	//LAÇO QUE ADD A QTDD DE LINHAS NECESSÁRIAS	
	for (let i = 0; i < numrowtoadd; i++) {
		var newid = 0;
		$.each($("#tab_logicHE tr"), function () {
			if (parseInt($(this).data("id")) > newid) {
				newid = parseInt($(this).data("id"));
			}
		});
		newid++;
		var tr = $("<tr></tr>", {
			id: "addr" + newid,
			"data-id": newid
		});

		$.each($("#tab_logicHE tbody tr:nth(0) td"), function () {
			var cur_td = $(this);
			var children = cur_td.children();
			if ($(this).data("name") != undefined) {
				var td = $("<td></td>", {
					"data-name": $(cur_td).data("name")
				});
				var c = $(cur_td).find($(children[0]).prop('tagName')).clone().val("");
				c.attr("name", $(cur_td).data("name") + "_HE" + newid);
				c.appendTo($(td));
				td.appendTo($(tr));
			} else {
				var td = $("<td></td>", {
					'text': $('#tab_logicHE tr').length
				}).appendTo($(tr));
			}
		});

		// add delete button and td       
		$("<td></td>").append(
			$('<button class="btnic btn btn-danger row-remove"><i class="fa fa-close"></i></button>')
				.click(function () {
					$(this).closest("tr").remove();
				})
		).appendTo($(tr));

		// add the new row
		$(tr).appendTo($('#tab_logicHE'));

		$(tr).find("td button.row-remove").on("click", function () {
			$(this).closest("tr").remove();
		});
	}

	//PREENCHE VALORES DAS LINHAS ADCIONADAS
	let j = 0;
	for (let i = 0; i < jsonobj.periodos.length / 2; i++) {
		myvar = "entrada_HE" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = jsonobj.periodos[j];
		myvar = "saida_HE" + (i + 1);
		$("input[name=" + myvar + "]")[0].value = jsonobj.periodos[j + 1];
		j += 2;
	}
}

//TABELA HORARIO DE TRABALHO
$(document).ready(function () {

	//FUNÇÃO QUE ADD NOVA LINHA
	$("#add_rowHT").on("click", function () {
		var newid = 0;
		$.each($("#tab_logicHT tr"), function () {
			if (parseInt($(this).data("id")) > newid) {
				newid = parseInt($(this).data("id"));
			}
		});
		if (newid >= 3) {
			alert("Você só pode adicionar até 3 Horários de Trabalho");
		} else {
			newid++;
			var tr = $("<tr></tr>", {
				id: "addr" + newid,
				"data-id": newid
			});

			$.each($("#tab_logicHT tbody tr:nth(0) td"), function () {
				var cur_td = $(this);
				var children = cur_td.children();
				if ($(this).data("name") != undefined) {
					var td = $("<td></td>", {
						"data-name": $(cur_td).data("name")
					});
					var c = $(cur_td).find($(children[0]).prop('tagName')).clone().val("");
					c.attr("name", $(cur_td).data("name") + "_HT" + newid);
					c.appendTo($(td));
					td.appendTo($(tr));
				} else {
					var td = $("<td></td>", {
						'text': $('#tab_logicHT tr').length
					}).appendTo($(tr));
				}
			});

			// add delete button and td
			$("<td></td>").append(
				$('<button class="btnic btn btn-danger row-remove"><i class="fa fa-close"></i></button>')
					.click(function () {
						$(this).closest("tr").remove();
					})
			).appendTo($(tr));

			// add the new row
			$(tr).appendTo($('#tab_logicHT'));

			$(tr).find("td button.row-remove").on("click", function () {
				$(this).closest("tr").remove();
			});
		}
	});

});
//TABELA MARCAÇÕES FEITAS
$(document).ready(function () {

	//FUNÇÃO QUE ADD NOVA LINHA
	$("#add_rowMF").on("click", function () {

		var newid = 0;
		$.each($("#tab_logicMF tr"), function () {
			if (parseInt($(this).data("id")) > newid) {
				newid = parseInt($(this).data("id"));
			}
		});
		newid++;
		var tr = $("<tr></tr>", {
			id: "addr" + newid,
			"data-id": newid
		});

		$.each($("#tab_logicMF tbody tr:nth(0) td"), function () {
			var cur_td = $(this);
			var children = cur_td.children();
			if ($(this).data("name") != undefined) {
				var td = $("<td></td>", {
					"data-name": $(cur_td).data("name")
				});
				var c = $(cur_td).find($(children[0]).prop('tagName')).clone().val("");
				c.attr("name", $(cur_td).data("name") + "_MF" + newid);
				c.appendTo($(td));
				td.appendTo($(tr));
			} else {
				var td = $("<td></td>", {
					'text': $('#tab_logicMF tr').length
				}).appendTo($(tr));
			}
		});

		// add delete button and td  
		$("<td></td>").append(
			$('<button class="btnic btn btn-danger row-remove"><i class="fa fa-close"></i></button>')
				.click(function () {
					$(this).closest("tr").remove();
				})
		).appendTo($(tr));

		// add the new row
		$(tr).appendTo($('#tab_logicMF'));

		$(tr).find("td button.row-remove").on("click", function () {
			$(this).closest("tr").remove();
		});
	});

});