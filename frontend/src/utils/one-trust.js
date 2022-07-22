/* eslint-disable */

/*
 ** OneTrust tracking
 * Module that can be called from UI modules to enable correct handling of cookies
 * August 2020 Gallery Module Updates
 **/
import hashCode from './hash-code';

export default class OneTrustVS {
    constructor() {
        this.fallbacks = [];
    }

    /*
     * Initialize setup of the VS side of OneTrust integration
     * Listens for cookie related popups or instagram embeds
     * The config object sent to each listener is slightly different hence the different
     * eventlistener functions
     */
    init = () => {
        // foreach polyfill
        if (window.NodeList && !NodeList.prototype.forEach) {
            NodeList.prototype.forEach = Array.prototype.forEach;
        }
    }

    instaOT = (otCategory, module, fallback, container, position = null) => {
        console.log('insta')
        let that = this;
        //wait until OneTrust library is available
        this.checkThirdPartyLibrary('OneTrust', () => {
            this.onetrustCookieCheck(otCategory, result => {
                if (!result) {
                    // if cookie category check doesn't pass, show the fallback
                    // pass in the name of the module, the fallback element,
                    // the list of Instagram posts or single container and the position to show the fallback
                    if (container) {
                        console.log('fallback', fallback);
                        console.log('container', container);

                        let fallbackClass = Array.from(fallback.classList)[0];
                        let fallbackExists;
                        if (container.length > 1) {
                            container.forEach((item, index) => {
                                fallbackExists = container[index].querySelector(`.${fallbackClass}`);
                                if (!fallbackExists) {
                                    that.showOneTrustFallback(otCategory, module, fallback, container[index], position);
                                }
                            })
                        } else {
                            fallbackExists = container.querySelector(`.${fallbackClass}`);
                            if (!fallbackExists) {
                                that.showOneTrustFallback(otCategory, module, fallback, container, position);
                            }
                        }
                    }
                } else {
                    that.setIframeSrc();
                }
            });

            // event listener for hiding fallback if cookie category string has been updated
            window.OneTrust.OnConsentChanged(function () {
                that.onetrustCookieCheck(otCategory, result => {
                    if (result) {
                        window.OneTrustEventBus.dispatchEvent('otCategory-approved');
                        that.hideOneTrustFallback(otCategory);
                        that.setIframeSrc();
                    }
                });
            });
        });
    };

    showOneTrustFallback = (otCategory, module, fallback, container, position = null) => {
        console.log('showfallback')
        let fallbackState = {
            visible: false,
            fallback,
        };

        // don't do anything if the target fallback is already displayed where it's supposed to
        if (fallbackState.visible) {
            return false;
        }
        // if a HTMLNodeList is passed, iterate through each item and
        // show fallback alongside each element (used in instagram scenario)
        if (container.length > 1 && fallbackState.fallback) {
            console.log('aqui')
            container.forEach(item => {
                let node = fallbackState.fallback.cloneNode(true);
                node.id = hashCode();
                item.insertAdjacentHTML('afterend', node.outerHTML);
                //pull this new node from the dom to get the correct fallback
                let domFallback = document.getElementById(node.id);
                domFallback.style.display = 'block';
                fallbackState.visible = true;
                node.classList.add(`vscookies-${otCategory}`);

                this.fallbacks.push({
                    id: domFallback.id,
                    cat: otCategory,
                    visible: true,
                });

                this.setFBButtonListener(node);
            });
            // if one container HTMLElement is passed and the fallback is available in the DOM,
            // clone it and show in the correct place
        } else {
            console.log('aca')
            let node = fallbackState.fallback.cloneNode(true);
            node.id = hashCode();
            node.style.display = 'block';
            node.classList.add(`vscookies-${otCategory}`);

            if (position) {
                container.insertBefore(node, container.childNodes[position]);
            } else {
                container.appendChild(node);
            }

            this.fallbacks.push({
                id: node.id,
                cat: otCategory,
            });

            fallbackState.visible = true;

            this.setFBButtonListener(node);
        }
    };

    /*
     * Return true or false to a match of consented cookie category
     * @param category {string} the category to look for e.g. C0003
     * @param callback {function} - callback function to run
     */
    onetrustCookieCheck = (category, callback) => {
        return callback(window.OnetrustActiveGroups.includes(category));
    };

    /*
     * Hide specified fallback category
     * @param otCategory {string} the class a fallback is within
     */
    hideOneTrustFallback = otCategory => {
        this.fallbacks.map(item => {
            if (item.cat === otCategory) {
                const cookieElement = document.getElementById(item.id);

                if (cookieElement) {
                    document.getElementById(item.id).style.display = 'none';
                    return;
                }
            }
        });

        const allCookies = document.querySelectorAll(`.vscookies-${otCategory}`);
        if (allCookies.size > 0) {
            allCookies.map(item => {
                item.style.display = 'none';
            });
        }
    };

    /*
     * Check for the availability of the specified library
     * @param name {string} name - library to wait for
     * @param callback {function} - function to call if the library is loaded
     */
    checkThirdPartyLibrary = (name, callback) => {
        let interval = 500;
        let that = this;
        window.setTimeout(function () {
            if (window[name]) {
                callback(window[name]);
            } else {
                that.checkThirdPartyLibrary(name, callback);
            }
        }, interval);
    };

    setFBButtonListener = fbElement => {
        fbElement.querySelector('#ot-sdk-btn').addEventListener('click', function () {
            window.OneTrust.ToggleInfoDisplay();
        });
    };

    setIframeSrc = () => {
        let iframe = document.querySelectorAll('iframe[data-src]:not([data-src=""])');

        if (iframe) {
            iframe.forEach(item => {
                item.src = item.dataset.src;
                item.removeAttribute('data-src');
            });
        }
    };
}
