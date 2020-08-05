var geogebraLibrary = geogebraLibrary || {};

// This integration script uses the deprecated function getExerciseFractio()
// to assess exercises created through custom tools in GeoGebra.
// This method will most likely not be supported in future releases of GeoGebra.
// This script is only included for the sake of thoroughness,
// should the function continue to be a valid option for integration.
geogebraLibrary.GeoGebraComponent = function (element) {
    element.innerHTML =
        "<div id='applet_container'></div>" +
        "<div class='textinput'>Senden sie ihre Lösung ab, bevor Sie zur nächsten Frage wechseln (kann mehrmals benutzt werden): " +
        "<input type='button' value='Lösung abschicken'/>" +
        "</div>";

    // Variable for the GeoGebra-Exercise
    var ggbExercise = null;

    // Returns the exercise fraction, if the GeoGebra-Applet is an exercise
    this.getValue = function () {
        if (geoApp.isExercise()) {
            return geoApp.getExerciseFraction();
        } else {
            return -1.0;
        }
    };

    // Injects the Div "applet_container" with an GeoGebra-Applet with the given material number
    this.setValue = function (materialNr) {
        if (materialNr != null) {
            ggbExercise = new GGBApplet({
                "id": 'geoApp',
                "material_id": materialNr,
                "borderColor": "#55FF00"
            }, true);
            ggbExercise.inject('applet_container');
            //alert("App with the material number: " + value + " loaded!");
        } else {
            alert("No correct material number specified!");
        }
    };

    // Default implementation of the click function; Is overwritten in the JavaScript-Connector
    this.click = function () {
        alert(geoApp.getExerciseFraction());
    };

    var button = element.getElementsByTagName("input")[0];
    var self = this;

    // Function click is assigned to the button
    button.onclick = function () {
        self.click();
    };
};