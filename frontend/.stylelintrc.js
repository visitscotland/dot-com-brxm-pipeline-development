module.exports = {
    extends:[
        "stylelint-config-recommended-scss",
        "stylelint-config-airbnb"
    ],
    rules: {
        "selector-pseudo-element-no-unknown": [
            true,
            {
                ignorePseudoElements: ["v-deep"]
            }
        ],
        "indentation": "tab"
    }
}