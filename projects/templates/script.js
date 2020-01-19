
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

    function zaladuj() {
        var myBackground;
        canvas : document.createElement('canvas');
        myBackground = new component(750, 900, "https://i.ibb.co/hCVYxTT/road.jpg", 0, 0, "background");
    }

    var postPoint = function () {
        var pathString = $('#imageInput').val();
        var exampleNumber = null;
        var exampleUnit = null;
        var exampleJson = JSON.stringify(
            {
                pathString: pathString,
                exampleNumber: exampleNumber,
                exampleUnit:exampleUnit
            });
        $.ajax({
            url: "/upload", //wymagane, gdzie się łączymy, zw na hostowanie w tej samej aplikacji mozna uzyc sciezki wzglednej
            method: "post", // metoda HTTP połączenia, domyślnie get
            contentType: 'application/json', //gdy wysyłamy dane czasami chcemy ustawić ich typ
            data: exampleJson
        })

            .done(function (e) {
                console.log("wyslane");
            })
            //akcja na blad odpowiedz
            .fail(function (e) {
                //zobaczcie jak wyglada response w consoli javascript
                //wyswietlcie poprawny komunikat bledu (czemu wystapil)
                //sprobujcie przechodzic przez atrybuty obiektu: np. e.responseJSON.errors ...
                alert("Error encountered.");
                console.log(e);
            });

    };

    var refreshContent = function () {
        console.log("Czekam");
        //inna skladnia

        $.ajax({
            url: "/upload",
            success: function (response) {
                clearContent();
                response.forEach(addExampleToContainer);
            },
            error: function () {
            },
            complete: function () {
            }
        });
    };


    var clearContent = function () {
        console.log('Clearing content!');
    };

    var addExampleToContainer = function (example) {
        var content_container = document.getElementById("myCanvas");
        var x = example.x;
        var y = example.y;
        draw(x, y);
    };

    function draw(x, y) {
        var canvas = document.getElementById("myCanvas");
        var context = canvas.getContext("2d");
        context.arc(x, y, 1, 0, 2 * Math.PI, false);
        context.fillStyle = 'green';
        context.fill();
        context.lineWidth = 5;
        context.strokeStyle = '#003300';
        context.stroke();
    }


    window.onload = function () {
        document.getElementById('createExampleSubmit').onclick = postPoint;
    };


