var mylibrary = mylibrary || {};

mylibrary.MyComponent = function (element) {
    element.innerHTML =
        "<div class='caption' >Das ist eine GeoGebra Applet!</div>" +
        "<div id='applet_container1'></div>" +
        "<div class='textinput'>Senden sie ihre Lösung ab, bevor Sie zur nächsten Frage wechseln (kann mehrmals benutzt werden): " +
        "<input type='button' value='Lösung abschicken'/>" +
        "</div>";

    var ggbExercise = null;

    element.style.border = "thin solid black";
    element.style.display = "inline-block";

    this.getValue = function () {
        if (app1.isExercise()) {
            return app1.getExerciseFraction();
        } else {
            return -1.0;
        }
    };
    this.setValue = function (value) {
        if (value != null) {
            ggbExercise = new GGBApplet({
                "id": 'app1',
                "material_id": value,
                "width": 500,
                "height": 400,
                "borderColor": "#55FF00"
            }, true);
            ggbExercise.inject('applet_container1');
            alert("App with the material number: " + value + " loaded!");
        } else {
            alert("No correct material number specified!");
        }
    };

    this.click = function () {
        alert(app1.getExerciseFraction());
    };

    var button = element.getElementsByTagName("input")[0];
    var self = this;
    button.onclick = function () {
        self.click();
    };
};