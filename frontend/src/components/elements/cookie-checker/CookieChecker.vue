<template>
    <span class="d-none" />
</template>

<script>
// import Vue from 'vue/dist/vue.js';
// import CookieFallback from './CookiesFallback.vue';
// import EventBus from '../../../utils/event-bus';
import OneTrustVS from '../../../utils/one-trust';
import cookieStore from '../../../stores/cookies.store';

/**
 *  C0001 - STRICTLY NECESSARY
 *  C0002 - ANALYTICAL COOKIES
 *  C0003 - FUNCTIONAL COOKIES
 *  C0004 - MARKETING COOKIES
 */

/**
 * Component used to check which cookies preferences
 * the user is using and provide access to certain content
 * depending on those preferences
 *
 * @displayName Cookies Checker
 */

export default {
    data() {
        return {
            oneTrust: new OneTrustVS(),
            activeGroups: '',
        };
    },
    watch: {
        activeGroups() {
            const activeCookieArray = this.activeGroups.split(',');

            const filteredArray = activeCookieArray.filter((el) => el !== '');

            cookieStore.dispatch('setCookieArray', filteredArray);
            cookieStore.dispatch('setTestRun', true);
        },
    },
    created() {
        this.checkOneTrust();
        this.checkActiveGroups();
        // KEEP UNTIL INSTAGRAM FUNCTIONALITY IS FIXED
        //   Add eventbus to check for events related to OneTrust and cookies
        // if (!(typeof window.OneTrustEventBus === 'function')) {
        //     window.OneTrustEventBus = new EventBus();

        //     window.OneTrustEventBus.addEventListener('instagram-visible', (event) => {
        //         // get category
        //         const OT_CATEGORY = event.detail.cookie_category;

        //         // get fallback from module
        //         const vsModule = document.getElementsByClassName(event.detail.module)[0];
        //         let container = document.querySelectorAll(`.${event.detail.container}`);
        //         const fallback = document.querySelector(`.${event.detail.fallback}`);

        //         container = container.length > 1 ? container : container[0];
        //         this.oneTrust.instaOT(OT_CATEGORY, vsModule, fallback, container);
        //     });
        // }
    },
    methods: {
        /**
         * initialises listener to ensure accepted cookie values
         * are updated when OneTrust preferences are changed
         */
        checkOneTrust() {
            if (typeof window !== 'undefined') {
                this.oneTrust.checkThirdPartyLibrary('OneTrust', () => {
                    window.OneTrust.OnConsentChanged(() => {
                        this.oneTrust.checkThirdPartyLibrary('OnetrustActiveGroups', () => {
                            // Check what cookies were accepted
                            this.activeGroups = window.OnetrustActiveGroups;
                        });
                    });
                });
            }
        },
        /**
         * checks cookies have been accepted via OneTrust and
         * adds them to activeGroups
         */
        checkActiveGroups() {
            if (typeof window !== 'undefined') {
                this.oneTrust.checkThirdPartyLibrary('OnetrustActiveGroups', () => {
                    // Check what cookies were accepted
                    this.activeGroups = window.OnetrustActiveGroups;
                });
            }
        },
    },
};
</script>
