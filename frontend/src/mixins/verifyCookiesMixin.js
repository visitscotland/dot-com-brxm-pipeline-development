import cookieStore from '../stores/cookies.store';

const cookieCheckerMixin = {
    data() {
        return {
            timesRun: 0,
            cookiesInitStatus: null,
        };
    },
    computed: {
        cookiesSet() {
            return cookieStore.getters.getCookieValues;
        },
        onetrustActiveGroups() {
            return window.OnetrustActiveGroups;
        },
        requiredCookiesExist() {
            let cookiesExist = true;

            this.requiredCookies.forEach((cookieVal) => {
                if (this.cookiesSet.indexOf(cookieVal) === -1) {
                    cookiesExist = false;
                }
            });

            return cookiesExist;
        },
    },
    mounted() {
        // check for cookies being set by OneTrust
        const setCookieStatus = () => {
            // if the interval has been run 50 times (5 seconds) without
            // cookies being set declare an error
            if (this.timesRun > 50) {
                clearStatusInterval(); // eslint-disable-line no-use-before-define
                if (typeof this.onetrustActiveGroups === 'undefined') {
                    this.cookiesInitStatus = 'error';
                }
            } else if (this.cookiesSet.length > 0) {
                clearStatusInterval(); // eslint-disable-line no-use-before-define
                this.cookiesInitStatus = true;
            }
        };
        const cookieStatus = setInterval(() => {
            this.timesRun += 1;
            setCookieStatus(this.timesRun);
        }, 100);
        const clearStatusInterval = () => {
            clearInterval(cookieStatus);
        };
    },
};

export default cookieCheckerMixin;
