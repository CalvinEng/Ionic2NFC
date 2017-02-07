function Nfc() {}

Nfc.prototype.coolMethod = function (arg0, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Ionic2NFC", "coolMethod", [arg0]);
};

Nfc.prototype.startNfc = function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Ionic2NFC", "startNfc", []);
};

Nfc.install = function () {
    if (!window.plugins) {
        window.plugins = {};
    }

    window.plugins.Nfc = new Nfc();
    return window.plugins.Nfc;
};

cordova.addConstructor(Nfc.install);