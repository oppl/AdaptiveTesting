window.at_jku_ce_adaptivetesting_questions_math_js_GeoGebraComponent =
    function() {
        var geocomponent =
            new geogebraLibrary.GeoGebraComponent(this.getElement());

        //Listen for change of the shared state in the server side
        this.onStateChange = function() {
            //Only initializes the component (with the material number)
            //if the value isn't 0 or 1.
            //This check is used, because the callback (to get the exercise fraction)
            //also leads to a state change.
            if(this.getState().value != 0.0 && this.getState().value != 1.0) {
                geocomponent.setValue(this.getState().value);
            }
        };

        var self = this;
        //Overrides the click function inside the GeoGebra Component
        geocomponent.click = function() {
            //Calls a server-side JS-Function (onClick) that gets handed
            //the exercise fraction of the GeoGebra component
            //through the method getValue()
            self.onClick(geocomponent.getValue());
        };
    };