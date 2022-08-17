import noJsClass from '#utils/no-js-class.js';

const jsIsDisabled = () => {
    const elements = document.getElementsByClassName(noJsClass);

    return elements.length > 0;
};

export default jsIsDisabled;
