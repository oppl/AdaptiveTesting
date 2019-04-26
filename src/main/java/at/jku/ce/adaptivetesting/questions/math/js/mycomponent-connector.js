window.at_jku_ce_adaptivetesting_questions_math_js_MyComponent =
    function() {
        var mycomponent =
            new mylibrary.MyComponent(this.getElement());

        this.onStateChange = function() {
            if(this.getState().value != 0.0 && this.getState().value != 1.0) {
                mycomponent.setValue(this.getState().value);
            }
        };

        var self = this;
        mycomponent.click = function() {
            self.onClick(mycomponent.getValue());
        };
    };