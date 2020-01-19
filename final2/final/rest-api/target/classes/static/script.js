
var postPoint = function () {
	var loaderWait = document.getElementById("loaderWait");
	toggle(loaderWait);
	var pathString=document.getElementById("pliczek").value;
	pathString = pathString.substring(pathString.lastIndexOf('\\') + 1);
	var pointJson = JSON.stringify(
		{
			pathString:pathString,
			xsCoord:null,
			ysCoord:null
		});
	$.ajax({
		url         : "/api/v1/point", //wymagane, gdzie się łączymy, zw na hostowanie w tej samej aplikacji mozna uzyc sciezki wzglednej
		method      : "post", // metoda HTTP połączenia, domyślnie get
		contentType : 'application/json', //gdy wysyłamy dane czasami chcemy ustawić ich typ
		data        : pointJson
	})
		.done(function(e){
			clearContent();

			refreshContent();
		})
		.fail(function(e){
			alert("Error encountered.");
			console.log(e);
		});
};



window.onload = function () {
    document.getElementById('dzialaj').onclick = wysylaj;
    refreshContent();
};

var refreshContent = function () {
    $.ajax({
        url : "/api/v1/point",
        success : function(response) {
            clearContent();
            response.forEach(addPointToContainer);

        },
        error : function() {},
        complete : function() {
            var loaderWait = document.getElementById("loaderWait");
            toggle(loaderWait);
        }
    });
};


var clearContent = function () {
    // document.getElementById('table-content-container').innerHTML = "";
    var tableHeaderObj = document.createElement('tr');
    var table = document.getElementById('table-content-container');
    var tableHeaderText = table.getElementsByTagName('tr')[0].innerHTML;
    tableHeaderObj.innerHTML = tableHeaderText;

    table.innerHTML = '';
    table.appendChild(tableHeaderObj);

    var canvas = document.getElementById("myCanvas");
    var context = canvas.getContext("2d");
    context.clearRect(0,0,canvas.width, canvas.height);


};

var clearMap = function () {
    console.log('Clearing map!');

    var canvas = document.getElementById("myCanvas");
    var context = canvas.getContext("2d");
    context.clearRect(0,0,canvas.width, canvas.height);
};

var deleteLink = function (element) {
    console.log('DELETING: ' + element.getAttribute('id'));

    $.ajax({

        url : "/api/v1/point/deleteFromArray/" + element.getAttribute('id'),
        method: 'delete',
        success : refreshContent,
        error : function() {},
        complete : function() {}
    });
};

var drawThisPoint = function(point){
    if(point.pathString == $('#imageInput').val()){
        paint(0.6*point.xsCoord,0.6*point.ysCoord);
    }
    var d= new Date();
}

var addPointToContainer = function(point){
    var content_container = document.getElementById("table-content-container");
    var numberOfDivs = content_container.getElementsByTagName('tr').length -1;
    var singleRow=document.createElement('tr');

    singleRow.innerHTML = '<td>' + numberOfDivs + '</td>';
    singleRow.innerHTML += '<td id=path' + numberOfDivs + '>' + point.pathString + '</td>';
    singleRow.innerHTML += '<td id=x' + numberOfDivs + '>' + point.xsCoord + '</td>';
    singleRow.innerHTML += '<td id=y' + numberOfDivs + '>' + point.ysCoord + '</td>';
    singleRow.innerHTML += '<button id=' + numberOfDivs.toString() + ' onclick="analyzePoint(this)">Map</button>';
    content_container.appendChild(singleRow);
};



function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#blah')
                .attr('src', e.target.result)
                .width(562)
                .height(402.5);
        };

        reader.readAsDataURL(input.files[0]);
    }
}
function paint(x, y){
    var canvas = document.getElementById("myCanvas");
    var context = canvas.getContext("2d");
    var radius = 15;

    context.beginPath();
    context.arc(x,y, radius, 0, 2 * Math.PI, false);
    context.fillStyle = 'green';
    context.fill();
    context.lineWidth = 5;
    context.strokeStyle = '#003300';
    context.stroke();

}

function analyzePoint(elem){
    var numberID = elem.id;
    console.log(numberID);
    var nameString = document.getElementById("path" + numberID).innerHTML;
    var filename = nameString.split("/").pop();
    $("#imageToMap").attr("src", filename + 'newimage.jpg');
    var x = document.getElementById("x" + numberID).innerHTML;
    var y = document.getElementById("y" + numberID).innerHTML;
    paint(0.6*x,0.6*y);
}

var show = function (elem) {
    elem.style.display = 'block';
};

var hide = function (elem) {
    elem.style.display = 'none';
};

var toggle = function (elem) {
    if (window.getComputedStyle(elem).display === 'block') {
        hide(elem);
        return;
    }
    show(elem);
};



var wysylaj = function(){
	var lista = [];
    var pathString=document.getElementById("pliczek").value;
	pathString = pathString.substring(pathString.lastIndexOf('\\') + 1);
    var i;
    var flaga = 1;
	//var table = document.getElementById('table-content-container');
	$("table tr td:nth-child(2)").each(function () {
		lista.push($(this).html());
	});
    if(lista[lista.length - 1] == pathString & lista.length > 1){
        console.log("Ten pathString juz byl");
        flaga = 0;
    }
    if (lista.length == 1 & lista[0] != pathString){
        lista.push(pathString);
        postPoint();
    }
    if (lista.length == 1 & lista[0] == pathString){
        console.log("Ten pathString juz byl");
    }
    if (lista.length == 0){
        lista.push(pathString);
        postPoint();
    }
    if(lista.length > 1){
        for(i = 0; i < lista.length - 1; i++){
            if(lista[i] == pathString){
                flaga = 0;
                console.log("Ten pathString juz byl");
            }
        }
    }
    if(flaga == 1 & lista.length > 2){
        lista.push(pathString);
        postPoint();
    }
    if(lista[0] != pathString & lista[1] != pathString & lista.length == 2){
        lista.push(pathString);
        postPoint();
    }
};

var deletePoint = function (idi) {
    console.log('DELETING ALL');

    $.ajax({
        url : "/api/v1/point/" + idi,
        method: 'delete',
        success : refreshContent,
        error : function() {},
        complete : function() {}
    });
};

var restartAll = function () {
    var content_container = document.getElementById("table-content-container");
    var numberOfDivs = content_container.getElementsByTagName('tr').length -1;
    console.log('DELETING ALL');
    for (i = 1; i < numberOfDivs+1; i++) {
        deletePoint(i);
    }

    lista=[];
    clearContent();
};
