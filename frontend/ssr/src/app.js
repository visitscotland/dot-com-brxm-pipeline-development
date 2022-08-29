
import Vue from "vue"
import { get } from "lodash"

/** PLACEHOLDER: IMPORTS */

export default async (context) => {
    const defaultVueOptions = {
        components: {
            /** PLACEHOLDER: COMPONENT REGISTRATION */
        },
        comments: true,
    }

    const vueOptions = {
        ...defaultVueOptions,
        ...get(context, "vueOptions", {}),
    }

    return new Vue(vueOptions)
}
