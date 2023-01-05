import { get } from "lodash"

/** PLACEHOLDER: COMPONENT IMPORTS */

import { initApp } from "@/main"

const defaultVueOptions = {
    components: {
        /** PLACEHOLDER: COMPONENT REGISTRATION */
    },
}

export default (context) => {

    const vueOptions = {
        ...get(context, "vueOptions", {}),
        ...defaultVueOptions,
    }
    
    return initApp(vueOptions, true)
}
