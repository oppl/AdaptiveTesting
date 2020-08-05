var geogebraLibrary = geogebraLibrary || {};

// Integration script for other mathematical exercises
geogebraLibrary.GeoGebraMathComponent = function (element) {
    element.innerHTML =
        "<div id='applet_container'></div>"; /* +
        "<div class='textinput'>Senden sie ihre Lösung ab, bevor Sie zur nächsten Frage wechseln (kann mehrmals benutzt werden): " +
        "<input type='button' value='Lösung abschicken'/>" +
        "</div>"; */

    // Variable for the GeoGebra-Exercise
    var ggbExercise = null;
    // Variable for the GeoGebra Apps API
    var checkApi;
    // Number of result objects in the GeoGebra-Applet
    //
    var noOfResults = 0;

    // Returns the amount of points achieved
    this.getValue = function () {
        var points = 0.0;
        for (var i = 1; i <= noOfResults; i++) {
            console.log(checkApi.getValue("Result_" + i));
            // Check for each result boolean if it was true
            if(checkApi.getValue("Result_" + i) == true){
                // If it was, award points for this part of the exercise
                points = points + 1.0 / noOfResults;
            }
        }
        return points;
    };

    var self = this;
    function updateListener(objName) {
        console.log("Value changed of: " + objName);
        self.click();
    }

    // Injects the Div "applet_container" with an GeoGebra-Applet with the given material number
    this.setValue = function (materialNr) {
        if (materialNr != null) {
            ggbExercise = new GGBApplet({
                "id": 'ggbApplet',
                "ggbBase64": materialNr,
                "showAlgebraInput": false,
                "width": 715,
                "height": 150,
                "appletOnLoad": function(api) {
                    // Initialize GeoGebra API
                    checkApi = api;
                    // Get the number of result objects
                    var i = 1;
                    while (ggbApplet.getObjectType("Result_" + i)) {
                        console.log("Result_" + i + " exists");
                        i++;
                    }
                    noOfResults = i - 1;
                    checkApi.registerUpdateListener(updateListener);
                }
            }, true);
            ggbExercise.inject('applet_container');
        } else {
            alert("No correct material number specified!");
        }
    };

    // Default implementation of the click function; Is overwritten in the JavaScript-Connector
    this.click = function () {
        alert("Click!");
    };

    /*
    var button = element.getElementsByTagName("input")[0];
    var self = this;

    // Function click is assigned to the button
    button.onclick = function () {
        self.click();
    };
    */
};