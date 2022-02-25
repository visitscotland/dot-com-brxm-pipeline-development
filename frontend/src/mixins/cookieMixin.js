const cookieMixin = {
    computed: {
        selectedLanguage() {
            return this.language.substr(0, 2).toUpperCase();
        },
        localeCookie() {
            return this.language.replace('_', '-').toLowerCase();
        },
        translationCookie() {
            return `/en/${this.language.substr(0, 2).toLowerCase()}`;
        },
    },
    methods: {
        cookieExists(cookie) {
            return (document.cookie.indexOf(`${cookie}=`) >= 0);
        },
        setCookie(name, value, newCookie, sessionCookie = false) {
            let cookieString = '';
            cookieString = `${name}=${value};`;
            cookieString += 'path=/;';

            if (newCookie && !sessionCookie) {
                const expiryDate = new Date();
                expiryDate.setFullYear(expiryDate.getFullYear() + 1);

                cookieString += `expires=${expiryDate};`;
            };

            document.cookie = cookieString;
        },
    },
};

export default cookieMixin;
