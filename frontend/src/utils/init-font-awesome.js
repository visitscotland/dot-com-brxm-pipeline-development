function initFontAwesome() {
    if (!window.fontAwesomeRequested) {
        window.fontAwesomeRequested = true;
        const script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = `https://kit.fontawesome.com/${process.env.ICON_KIT_TOKEN}.js`;
        script.crossOrigin = 'anonymous';
        document.head.appendChild(script);
    }
}

export default initFontAwesome;
