const fs = require("fs")
const path = require("path")

const rf = require("rimraf")
const { 
  each,
  kebabCase,
  partial,
  reduce,
  startsWith,
  isEmpty,
} = require("lodash")
const { getOptions } = require("loader-utils")
const {  validate } = require("schema-utils");


/**
 * Options passed in from webpack loader config
 */
const optionsSchema = {
  type: "object",
  properties: {
    
    // {String} Path to webfiles relative to the project root
    webfilesPath: {
      type: "string",
    },
    // {String} Target folder in which to created freemarker templates
    freemarkerTargetPath: {
      type: "string",
    },
    // {String} Full path including file name to the freemarker imports utility file
    importsPath: {
      type: "string",
    },
    // {String} DOM element selector on which to mount the Vue app
    appMountTarget: {
      type: "string"
    }
  },
}

const appModuleName = "VsApp"

function moduleIsStore(moduleName) {
  return startsWith(moduleName, "VsStore")
}

function moduleIsApp(moduleName) {
  return moduleName === appModuleName
}

function generateVueAppInclude() {
  return `<#include "../${moduleFileName(appModuleName)}">\n`
}

function generateImportsInclude(importsPath) {
  return `<#include "${importsPath}">\n`
}

function generateTemplateContent(moduleName, mod, configPaths, appMountTarget) {
  let content = ""
  const isStore = moduleIsStore(moduleName)
  const isApp = moduleIsApp(moduleName)

  if(!isApp) {
    content += generateVueAppInclude()
    content += generateImportsInclude(configPaths.imports)
    content += "\n"
  }

  if(!isEmpty(mod.styles)) {
    content += generateTemplateContentStyles(mod.styles, configPaths.webfiles)
  }

  if(!isEmpty(mod.scripts)) {
    content += generateTemplateContentScriptAssets(mod.scripts, isApp, configPaths.webfiles)
  }

  if(!isEmpty(mod.headingFonts)) {
    if (moduleName === 'VsHeading') {
       content += generateTemplateContentFontAssets(mod.headingFonts, isApp, configPaths.webfiles)
    }
  }

  if(!isEmpty(mod.bodyFonts)) {
    content += generateTemplateContentFontAssets(mod.bodyFonts, isApp, configPaths.webfiles)
  }
  
  if (isStore) {
    content += generateTemplateContentStoreScript(moduleName)
  } else if(isApp) {
    content += generateTemplateContentApp(appMountTarget)
  } else {
    content += generateTemplateContentRegisterScript(moduleName)
  }

  return content
}

// Creates custom main App scripts that:
// - Sets up the `vs` global
// - Adds the `initApp` method exported from the main VsApp module to the `vs` global
// - Globalises the Vue library exported from the main App module so it can be used to
//   register whichever components are needed
// - Call the `initApp` function to bootstrap the app 
function generateTemplateContentApp(appMountTarget) {
  const setupScriptContent = `
        // initialise global vs object
        vs = {
            stores: {},
            initApp: ${appModuleName}.initApp
        }
        
        Vue = ${appModuleName}.Vue
  `
  const initScriptContent = `
        vs.initApp({
          el: "[data-vue-app-init]"
        })
  `

  return generateTemplateContentScript(setupScriptContent, null, "htmlBodyEndScriptsFirst") +
    generateTemplateContentScript(initScriptContent, null, "htmlBodyEndAppInit")
}

function generateTemplateContentStoreScript(moduleName) {
  let scriptText = `vs.stores.${moduleName} = ${moduleName}.default`

  return generateTemplateContentScript(scriptText)
}

function generateTemplateContentRegisterScript(moduleName) {
  let scriptText = `Vue.component('${kebabCase(moduleName)}', ${moduleName}.default)`

  return generateTemplateContentScript(scriptText)
}

function generateTemplateContentScriptAssets(scripts, isApp, webfilesPath) {

  return reduce(
    scripts,
    function(content, assetPath) {
      const href = generateTemplateContentWebfileTag(assetPath, webfilesPath)

      let altHeadContributionName = null

      if(isApp) {
        altHeadContributionName = "htmlBodyEndScriptsFirst"
      }

      return content + generateTemplateContentPreload(href, "script") +
          generateTemplateContentScript(null, href, altHeadContributionName)
    },
    "",
  );
}

function generateTemplateContentFontAssets(fonts, isApp, webfilesPath) {
    return reduce(
      fonts,
      function(content, assetPath) {
        const href = generateTemplateContentWebfileTag(`fonts/${assetPath}`, webfilesPath)
        
        return content + generateTemplateContentPreload(href, "font");
        },
      "",
    );
  }

function generateTemplateContentPreload(href, linkType) {
  const linkString = generateTemplateContentLinkTag("preload", href, linkType)

  return generateTemplateContentHeadContribution(linkString, "htmlHeadPreload")
}

function generateTemplateContentLinkTag(rel, href, type) {
    let crossorigin = "";

    if (type === 'font') {
        crossorigin = "crossorigin='anonymous'";
    }

    if (rel === 'preload') {
        return `\t<link rel="${rel}" href="${href}" as="${type}" ${crossorigin} />`
    }

    return `\t<link rel="${rel}" href="${href}" type="${type}"/>`
}

function generateTemplateContentScript(scriptContent, href, headContributionName) {
  let nodeString = '\t<script type="text/javascript"'

  if (href) {
    nodeString += ' src="' + href + '"'
  }

  nodeString += ">"

  if (scriptContent) {
    nodeString += "\n\t\t" + scriptContent + "\n\t"
  }

  nodeString += "</script>"

  return generateTemplateContentHeadContribution(nodeString, headContributionName || "htmlBodyEndScripts")
}

function generateTemplateContentStyles(styles, webfilesPath) {
  return reduce(
    styles,
    function(content, assetPath) {
      const href = generateTemplateContentWebfileTag(assetPath, webfilesPath)

      return content +
        generateTemplateContentPreload(href, "style") +
        generateTemplateContentStyle(href)
    },
    "",
  ) + "\n"
}

function generateTemplateContentStyle(href) {
  const linkString = generateTemplateContentLinkTag("stylesheet", href, "text/css")

  return generateTemplateContentHeadContribution(linkString, "htmlHeadStyles")
}

function generateTemplateContentHeadContribution(content, category) {
  let node = ""

  node += '\n<@hst.headContribution category="' + category + '">\n'
  node += content + "\n"
  node += "</@hst.headContribution>\n"

  return node
}

function generateTemplateContentWebfileTag(assetPath, webfilesPath) {
  return `<@hst.webfile fullyQualified=fullyQualifiedURLs path='${webfilesPath}/${assetPath}' />`
}

function outputTemplate(mod, moduleName, configPaths) {
  let targetFilePath = path.join(
    configPaths.target,
    moduleSubPath(moduleName),
    moduleFileName(moduleName)
  )

  const template = generateTemplateContent(moduleName, mod, configPaths)

  fs.writeFileSync(targetFilePath, template)
}

function moduleSubPath(moduleName) {
  if (moduleIsStore(moduleName)) {
    return "/stores"
  } else if(moduleIsApp(moduleName)) {
    return "/"
  }

  return "/components"
}

function moduleFileName(moduleName) {
  return kebabCase(moduleName) + ".ftl"
}

function prepTargetDir(freemarkerTargetPath) {
  if (fs.existsSync(freemarkerTargetPath)) {
    rf.sync(freemarkerTargetPath)
  }

  fs.mkdirSync(freemarkerTargetPath, { recursive: true })
  fs.mkdirSync(path.join(freemarkerTargetPath, "components"))
  fs.mkdirSync(path.join(freemarkerTargetPath, "stores"))
}

/**
 * Outputs a freemarker template for each module listed in the provided manifest
 * 
 * manifest is a JSON string encoding an object with module names as keys and asset
 * maps as values. Each asset map has `scripts`, `styles` and `other` keys, which are 
 * arrays of paths to the asset chunks that encode the module and all its dependencies.
 * 
 * This module generates:
 * - A freemarker for each module consisting of a Vue component or Vuex store that contains:
 *    - htmlBodyEndScripts headContributions for each script asset chunk
 *    - htmlHeadStyle headContributions for each style asset chunk
 *    - htmlHeadPreload headContributions containing resource hints for each cript and style chunk
 *    - Includes for the main VsApp module and imports.ftl freemarker templates
 *    - htmlBodyEndScripts headContributions containing a script tag with either the component 
 *      registraion or global reference setup for each Vue component or Vuex store, respectively
 * - A freemarker for the main VsApp module with all of the above with the followig differences:
 *    - Each script asset chunk is contained in a htmlBodyEndScriptsFirst headContribution
 *    - Does not contain a reference to itself
 *    - Contains an additional script contained in a htmlBodyEndScriptsFirst headContribution that 
 *      globalises the Vue dependency exported from the VsApp module and sets up the `vs` global object
 *    - Contains an additional script contained in a htmlBodyEndAppInit headContribution tag that
 *      calls the initApp method exported from the VsApp module
 * 
 * For webpack loader options see the schema at the top of this file.
 * 
 * @param {String} manifest
 * 
 */
module.exports = function(manifest) {
  const modules = JSON.parse(manifest)
  const options = getOptions(this)

  validate(optionsSchema, options, "Generate Freemarker Template")

  prepTargetDir(options.freemarkerTargetPath)

  const configPaths = {
    target: options.freemarkerTargetPath,
    imports: options.importsPath,
    webfiles: options.webfilesPath,
  }

  each(modules, partial(
    outputTemplate,
    partial.placeholder,
    partial.placeholder,
    configPaths,
    options.appMountTarget
  ))

  return manifest
}
