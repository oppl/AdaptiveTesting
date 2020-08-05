var geogebraLibrary = geogebraLibrary || {};

// Integration script for geometric exercises
geogebraLibrary.GeoGebraComponent = function (element) {
    element.innerHTML =
        "<div id='applet_container'></div>"; /* +
        "<div class='textinput'>Senden sie ihre Lösung ab, bevor Sie zur nächsten Frage wechseln (kann mehrmals benutzt werden): " +
        "<input type='button' value='Lösung abschicken'/>" +
        "</div>"; */

    // Variable for the GeoGebra-Exercise
    var ggbExercise = null;

    var noOfStartPoints;
    var noOfTargets;
    var completedTargets;
    var checkApi;
    var points = 0.0;

    var self = this;

    // Called whenever the user creates an object in the GeoGebra-App
    function ggbObjectAdded(name) {

        unregisterListeners(checkApi);

        try {

            console.log(name + " added");

            for (var i = 1; i <= noOfTargets; i++) {
                console.log("checking against Target_" + i);

                checkApi.evalCommand("check = Target_" + i + " == " + name);
                var correct = !!checkApi.getValue('check');

                // Perturb the starting objects randomly to make sure the construction is correct
                var count = 5;
                while (correct && count-- > 0) {
                    perturb();
                    correct = correct && !!checkApi.getValue('check');
                }
                console.log("result: " + correct);

                if (correct) {
                    console.log("Target_" + i + " reached!\n");
                    // Check if the part of the exercise wasn't already answered correctly
                    if(completedTargets[i-1] === false){
                        // Add points for the correctly answered part
                        points = points + (1.0 / noOfTargets);
                        // Save that it was already answered, so that points can't be awarded twice
                        completedTargets[i-1] = true;
                    }
                }

            }
            self.click();
            registerListeners(checkApi);
        } catch (e) {
            console.log(e);
        }

    }

    function registerListeners(api) {
        api.registerAddListener(ggbObjectAdded);
    }

    function unregisterListeners(api) {
        api.unregisterAddListener(ggbObjectAdded);
    }

    function perturb() {

        try {

            for (var i = 1; i <= noOfStartPoints; i++) {

                var pointName = "SP_" + i;
                var x = checkApi.getXcoord(pointName);
                var y = checkApi.getYcoord(pointName);

                x = x * (1 + Math.random() / 1000);
                y = y * (1 + Math.random() / 1000);
                console.log("setting coords of " + pointName + " to (" + x + ", " + y + ")");
                checkApi.setCoords(pointName, x, y);

            }

        } catch (e) {
            console.log(e);
        }
    }

    // Returns the achieved points
    this.getValue = function () {
        return points;
    };

    // Injects the Div "applet_container" with an GeoGebra-Applet with the given material number
    this.setValue = function (materialNr) {
        if (materialNr != null) {
            var parameters = {
                "id": "ggbApplet",
                "width": 715,
                "height": 500,
                "autoHeight" : true,
                "showMenuBar": false,
                "showAlgebraInput": false,
                "showToolBar": true,
                //"customToolBar": "0 39 73 62 | 1 501 67 , 5 19 , 72 75 76 | 2 15 45 , 18 65 , 7 37 | 4 3 8 9 , 13 44 , 58 , 47 | 16 51 64 , 70 | 10 34 53 11 , 24  20 22 , 21 23 | 55 56 57 , 12 | 36 46 , 38 49  50 , 71  14  68 | 30 29 54 32 31 33 | 25 17 26 60 52 61 | 40 41 42 , 27 28 35 , 6",
                "showToolBarHelp": false,
                "showResetIcon": false,
                "enableLabelDrags": false,
                "enableShiftDragZoom": true,
                "enableRightClick": false,
                "errorDialogsActive": false,
                "useBrowserForJS": true,
                "allowStyleBar": false,
                "preventFocus": false,
                "showZoomButtons": true,
                "capturingThreshold": 3,
                // Add code here to run when the applet starts
                "appletOnLoad": function(api) {

                    checkApi = api;

                    // Count the target objects to be constructed (named like Target_1, Target_2 ... in the GeoGebra-App)
                    var i = 1;
                    while (ggbApplet.getObjectType("Target_" + i)) {
                        console.log("Target_" + i + " exists");
                        i++;
                    }

                    noOfTargets = i - 1;
                    // Create an array that keeps track of parts of the exercise that where already answered
                    completedTargets = new Array(noOfTargets);
                    for (var z = 0; z < completedTargets.length; z++) {
                        completedTargets[z] = false;
                    }
                    console.log("noOfTargets = " + noOfTargets);

                    // Count the start points for the construction (named like SP_1, SP_2 ... in the GeoGebra-App)
                    var y = 1;
                    while (ggbApplet.getObjectType("SP_" + y)) {
                        console.log("SP_" + y + " exists");
                        y++;
                    }

                    noOfStartPoints = y - 1;
                    console.log("noOfStartPoints = " + noOfStartPoints);

                    registerListeners(checkApi);

                },
                "showFullscreenButton": true,
                "scale": 1,
                "disableAutoScale": false,
                "allowUpscale": false,
                "clickToLoad": false,
                "appName": "classic",
                "language": "en",
                "material_id": materialNr,
            };
            var views = {
                'is3D': 0,
                'AV': 0,
                'SV': 0,
                'CV': 0,
                'EV2': 0,
                'CP': 0,
                'PC': 0,
                'DA': 0,
                'FI': 0,
                'macro': 0
            };
            ggbExercise = new GGBApplet(parameters, '5.0', views);
            ggbExercise.inject('applet_container');
            //alert("App with the material number: " + value + " loaded!");
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