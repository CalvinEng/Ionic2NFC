function Nfc() {}

Nfc.prototype.coolMethod = function (arg0, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Ionic2NFC", "coolMethod", [arg0]);
};

Nfc.install = function () {
    if (!window.plugins) {
        window.plugins = {};
    }

    window.plugins.Nfc = new Nfc();
    return window.plugins.Nfc;
};

cordova.addConstructor(Nfc.install);