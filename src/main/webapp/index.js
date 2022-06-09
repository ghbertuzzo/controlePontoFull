$(document).ready(function () {
	$("#btn_export").on("click", function () {
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState == 4 && this.status == 200) {
				document.getElementById("demo").innerHTML = xhttp.responseText;
			}
		};
		xhttp.open("GET", "export", true);
		xhttp.send();
		console.log("Get enviado!");
	});
});

//BOTÃO CALCULO ATRASOS
$(document).ready(function () {
	$("#btn_calc_at").on("click", function () {
		//CRIA JSON COM HT E MF
		var obj = new Object();
		var sizetableHt = document.getElementById("tab_logicHT").rows.length;
		var sizetableMf = document.getElementById("tab_logicMF").rows.length;
		let myvar;

		//DADOS DO HORARIO DE TRABALHO
		var horario_trabalho = new Object();
		var ht_entradas = [];
		var ht_saidas = [];
		for (let i = 1; i < sizetableHt - 1; i++) {
			myvar = "entrada" + i + "_HT";
			ht_entradas.push($("[name=" + myvar + "]")[0].value);

			myvar = "saida" + i + "_HT";
			ht_saidas.push($("[name=" + myvar + "]")[0].value);
		}
		horario_trabalho.entradas = ht_entradas;
		horario_trabalho.saidas = ht_saidas;
		obj.horario_trabalho = horario_trabalho;

		//DADOS DAS MARCACOES FEITAS
		var marcacoes_feitas = new Object();
		var mf_entradas = [];
		var mf_saidas = [];
		for (let i = 1; i < sizetableMf - 1; i++) {
			myvar = "entrada" + i + "_MF";
			mf_entradas.push($("[name=" + myvar + "]")[0].value);

			myvar = "saida" + i + "_MF";
			mf_saidas.push($("[name=" + myvar + "]")[0].value);
		}
		marcacoes_feitas.entradas = mf_entradas;
		marcacoes_feitas.saidas = mf_saidas;
		obj.marcacoes_feitas = marcacoes_feitas;

		console.log(obj);
		//convert object to json string
		var data = JSON.stringify(obj);

		//send request post		
		var xhr = new XMLHttpRequest();
		var url = "atrasos";
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.onreadystatechange = function () {
			if (xhr.readyState === 4 && xhr.status === 200) {
				console.log("Parsed:")
				console.log(JSON.parse(xhr.responseText));
				console.log("Texts:")
				console.log(xhr.responseText);
			}
		};
		xhr.send(data);
	});
});

$(document).ready(function () {
	$("#btn_save").on("click", function () {

		//GERA JSON COM INFORMAÇÕES DE: DATA, HORARIOS DE TRABALHO E MARCACOES FEITAS
		var obj = new Object();
		obj.data = $("[name=dataform]")[0].value;
		var sizetableHt = document.getElementById("tab_logicHT").rows.length;
		var sizetableMf = document.getElementById("tab_logicMF").rows.length;

		let myvar;

		//DADOS DO HORARIO DE TRABALHO
		var horario_trabalho = new Object();
		var ht_entradas = [];
		var ht_saidas = [];
		for (let i = 1; i < sizetableHt - 1; i++) {
			myvar = "entrada" + i + "_HT";
			ht_entradas.push($("[name=" + myvar + "]")[0].value);

			myvar = "saida" + i + "_HT";
			ht_saidas.push($("[name=" + myvar + "]")[0].value);
		}
		horario_trabalho.entradas = ht_entradas;
		horario_trabalho.saidas = ht_saidas;
		obj.horario_trabalho = horario_trabalho;

		//DADOS DAS MARCACOES FEITAS
		var marcacoes_feitas = new Object();
		var mf_entradas = [];
		var mf_saidas = [];
		for (let i = 1; i < sizetableMf - 1; i++) {
			myvar = "entrada" + i + "_MF";
			mf_entradas.push($("[name=" + myvar + "]")[0].value);

			myvar = "saida" + i + "_MF";
			mf_saidas.push($("[name=" + myvar + "]")[0].value);
		}
		marcacoes_feitas.entradas = mf_entradas;
		marcacoes_feitas.saidas = mf_saidas;
		obj.marcacoes_feitas = marcacoes_feitas;

		console.log(obj);
		//convert object to json string
		var data = JSON.stringify(obj);

		//convert string to Json Object
		console.log(JSON.parse(data)); // this is your requirement.

		//send request post		
		var xhr = new XMLHttpRequest();
		var url = "export";
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.onreadystatechange = function () {
			if (xhr.readyState === 4 && xhr.status === 200) {
				console.log(JSON.parse(xhr.responseText));
			}
		};
		xhr.send(data);
		console.log("Enviado!");
	});
});

//TABELA HORARIO DE TRABALHO
$(document).ready(function () {
	$("#add_rowHT").on("click", function () {
		// Dynamic Rows Code

		// Get max row id and set new id
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

			// loop through each td and create new elements with name of newid
			$.each($("#tab_logicHT tbody tr:nth(0) td"), function () {
				var cur_td = $(this);
				var children = cur_td.children();
				// add new td and element if it has a nane
				if ($(this).data("name") != undefined) {
					var td = $("<td></td>", {
						"data-name": $(cur_td).data("name")
					});
					var c = $(cur_td).find($(children[0]).prop('tagName')).clone().val("");
					c.attr("name", $(cur_td).data("name") + newid + "_HT");
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
	$("#add_rowMF").on("click", function () {
		// Dynamic Rows Code

		// Get max row id and set new id
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

		// loop through each td and create new elements with name of newid
		$.each($("#tab_logicMF tbody tr:nth(0) td"), function () {
			var cur_td = $(this);
			var children = cur_td.children();
			// add new td and element if it has a nane
			if ($(this).data("name") != undefined) {
				var td = $("<td></td>", {
					"data-name": $(cur_td).data("name")
				});
				var c = $(cur_td).find($(children[0]).prop('tagName')).clone().val("");
				c.attr("name", $(cur_td).data("name") + newid + "_MF");
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