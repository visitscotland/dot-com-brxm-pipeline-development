import { get, isObject } from "lodash"

import { Vue, initApp as initAppMain } from "@/main"

const isServerRendered = (appMountTargetSelector) => {
    const appContainer = document.querySelector(appMountTargetSelector)

    return appContainer && appContainer.hasAttribute("data-server-rendered")
}

export { Vue }

export const initApp = (options) => {

    const appMountTargetSelector = get(options, "el")
    const hasBeenSSRed = isServerRendered(appMountTargetSelector)

    if (appMountTargetSelector && hasBeenSSRed) {
        if(!isObject(options)) {
            options = {}
        }
        options.template = "#app-template"
    }

    if(hasBeenSSRed) {
        // This fixes hydration mismatching because SSR does not preserve comments
        options.comments = false
    }

    return initAppMain(options)
}
