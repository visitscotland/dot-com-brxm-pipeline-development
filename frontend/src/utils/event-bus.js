/* eslint-disable */
export default class EventBus {
    /**
     * Initialize a new event bus instance.
     */
    constructor() {
        this.bus = document.createElement('fakeelement');
    }

    /**
     * Add an event listener.
     */
    addEventListener(event, callback) {
        this.bus.addEventListener(event, callback);
    }

    /**
     * Remove an event listener.
     */
    removeEventListener(event, callback) {
        this.bus.removeEventListener(event, callback);
    }

    /**
     * Dispatch an event.
     */
    dispatchEvent(
        event,
        detail = {
        }
    ) {
        /**
         * CustomEvent() polyfill
         * https://developer.mozilla.org/en-US/docs/Web/API/CustomEvent/CustomEvent#Polyfill
         */
        (function () {

            if (typeof window.CustomEvent === 'function') return false;

            function CustomEvent(event, params) {
                params = params || { bubbles: false, cancelable: false, detail: undefined };
                var evt = document.createEvent('CustomEvent');
                evt.initCustomEvent(event, params.bubbles, params.cancelable, params.detail);
                return evt;
            }

            CustomEvent.prototype = window.Event.prototype;

            window.CustomEvent = CustomEvent;

            return null;
        })();

        this.bus.dispatchEvent(new CustomEvent(event, { detail }));
    }
}
