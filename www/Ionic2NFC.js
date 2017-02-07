function Nfc() {}

Nfc.prototype.coolMethod = function (options, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Nfc", "coolMethod", []);
};

Nfc.install = function () {
    if (!window.plugins) {
        window.plugins = {};
    }

    window.plugins.Nfc = new Nfc();
    return window.plugins.Nfc;
};

cordova.addConstructor(Nfc.install);