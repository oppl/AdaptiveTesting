// Define the namespace
var mylibrary = mylibrary || {};

mylibrary.MyComponent = function (element) {
    element.innerHTML =
        "<div class='caption' >Das ist eine GeoGebra Applet!</div>" +
        "<div id='applet_container1'></div>";

    var ggbApp = new GGBApplet({"appName": "graphing", "width": 800, "height": 600, "showToolBar": true, "showAlgebraInput": true, "showMenuBar": true }, true);
    ggbApp.inject('applet_container1');

    element.style.border = "thin solid black";
    element.style.display = "inline-block";

    // Getter and setter for the value property
    this.getValue = function () {

    };
    this.setValue = function (value) {

    };
};