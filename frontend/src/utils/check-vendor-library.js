/*
     * Check for the availability of the specified library
     * @param name {string} name - library to wait for
     * @param callback {function} - function to call if the library is loaded
     */
const checkVendorLibrary = (name, callback) => {
    const interval = 500;
    window.setTimeout(() => {
        if (window[name]) {
            callback(window[name]);
        } else {
            checkVendorLibrary(name, callback);
        }
    }, interval);
};

export default checkVendorLibrary;
