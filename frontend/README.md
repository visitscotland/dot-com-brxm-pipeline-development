![VisitScotland](https://sttc.visitscotland.com/static/img/logos/scotland-alba-logo-500.png)

# VisitScotland.com Frontend Package

This is the repository for the **VisitScotland.com Frontend**, which encodes frontend Vue components and styles for use on the visitscotland.com site.

The package utilises the [Vue Styleguidist](https://vue-styleguidist.github.io/) package for its design system builds. The starting point for the package was [Vue Design System](https://vueds.com) and has since been enhanced and modified significantly compared to that library in several areas, including:

- The included components have been changed to suit VisitScotland's needs and have been moved into subdirectories in the `src/components` folder.
- The design system build has been enhanced so it can include content from a remote API (Bloomreach Hippo CMS or Contentful).
- The Color component has been enhanced with categories and accessibility checks.
- The main production build has been converted to a library build for more efficient asset loading on the client.
- NPM has been replaced with Yarn package manager.
- A Bloomreach Hippo CMS app has been added in the folder `hippo`, which acts as the source code for the CMS site to provide content to the design system build.
- An SSR app and set of builds has been added to enable the frontend assets to be server-side rendered.

## Getting started

Install the package:

```sh
# Clone the repo
git clone https://username@bitbucket.visitscotland.com/scm/vscom/design-system.git

# Navigate to frontend dir
cd frontend

# Install the package
yarn install
```

Start up the development environment (a hot-reloading design system instance)

```sh
# Create a `.env` file containing the necessary environment variables (see below)
touch .env

# Build and run the design system
yarn system
```

The design system instance will be running at [localhost:6060](http://localhost:6060) and is independent of the parent Hippo instance.

## The design system

This package can be used to generate a design system using one of the `system` build scripts. The design system documents the components and styles contained in the `src` folder, as well as arbitrary design information. Additionally, the design system provides a contained development environment that hot-reloads when source files are changed.

To generate the design system using either of the `system` build scriots, the relevent environment variables must be set either in a `.env` file or made available as system variables on the system **building** the site.

```sh
# Generates and runs the local instance of the deisgn system at localhost:6060
yarn system

# Generates assets encoding the design system for hosting on a production server
yarn system:build
``` 

### Design system config

The layout and content of the design system is generated from a combination of the source files and configuration in `build/system.config.js`. Details of the available config settings can be found in the [vue-styleguidist documentation](https://vue-styleguidist.github.io/Configuration.htm).

There is one custom config key `remoteProfiles`, which can be used to specify config for including content from remote sources in the generated design system. Details for the remote configuration [can be found below](#remote-config).

### <a name="remote-config"></a> Configuring the remote build

When included, the `remoteProfiles` key in `build/system.config.js` specifies config profiles for merging content from a remote API into the static local config in that file.

These builds require valid configuration, as follows:

- The `remoteProfiles` key in `build/system.config.js` is a map of objects - each key being the profile name and the value being an object with the profile's config. The project currently includes profiles for Hippo and Contentful, which are specified in `config/build/system.remote-profiles.config.js`. See that file for details on how to properly configure remote profiles.
- Set the `VS_DS_REMOTE_CONFIG_URL` and any other environment variables (e.g. `VS_DS_REMOTE_CONFIG_HIPPO_PROJECT_NAME`) by specifying them in a `.env` file in the package root, or some other way.

Common environment variables for remote config:
`VS_DS_REMOTE_CONFIG_URL` - sets the URL for the API call
`VS_DS_REMOTE_CONFIG_STRICT_SSL` - used to set the `strictSSL` value passed to `request-promise-native`, which will be true unless this variable has the value "false"
`VS_DS_REMOTE_PROFILE` - used to specify which named profile to run

Environment variables for Hippo and Contentful config:
`VS_DS_REMOTE_CONFIG_HIPPO_SECTIONS_FIELD_TITLE`
`VS_DS_REMOTE_CONFIG_HIPPO_SECTIONS_CONTENT_FIELD_TITLE`
`VS_DS_REMOTE_CONFIG_HIPPO_PROJECT_NAME`
`VS_DS_REMOTE_CONFIG_CONTENTFUL_TOKEN`

Only the `VS_DS_REMOTE_CONFIG_URL` variable is truly needed. However, each profile generally requires other variables to be set.

By default, the build selects the first profile defined on the `remoteProfiles` key. A different profile can be selected in one of the following ways:

1. Set the `VS_DS_REMOTE_PROFILE` environment variable to the desired profile name
2. Pass the profile name as the --remote-profile arg when running the package.json script, e.g `yarn system --remote-profile contentful`

<a name="developing-in-the-design-system-instance"></a>### Developing in the design system instance

```sh
yarn system
```

This build script generates and serves the design system at [localhost:6060](http://localhost:6060).

Modifications to source files will trigger a rebuild and refresh the browser. 

NOTE: Tokens will **not** be recompiled automatically on file change but this can be triggered manually if needed by running:

```sh
yarn theo
```

### Building static design system assets for production

The following script generates a flat site version of the design system site, according to the config in `build/system.config.js`, with all assets being output into `dist/system`:

```sh
yarn system:build
```

The assets can be served as a static site using a standard http server.

## Using the components, stores and styles in downstream projects

The `library` and `library:dev` build scripts compile asset bundles containing all the JS, CSS and other assets required to include the Vue components, Vuex stores and core app styles/scripts in the `src` folder in a downstream project.

### The entry modules and associated assets

The bundles created by `library` and `library:dev` are collections of entry modules with all supporting assets:

- One entry module for each Vue SFC (`.vue`) in the `src/components` subfolders called `Vs{FileName}`
- One entry module for each Vuex store (`.store.js`) in the `src/components` subfolders called `VsStore{FileName}`
- An additional core app module created from the `src/main.js` file and called `VsApp`

The build generates JS and CSS asset chunks for all entry modules and dependencies into the `dist/library/scripts` and `dist/library/styles` folders. Any other assets are output into the `dist/library/assets` folder. Each JS and CSS chunk is required by one or more of the entry modules.

The `library` build generates production assets, with hashed asset names. The `library:dev` build generates development assets, with meaningful names based on which entry module they are needed by.

### The manifest.json

The build also generates a `manifest.json` file in the `dist/library` folder. The `manifest.json` is a map of entry modules and a list of assets, separated by asset type, required for each entry module to function. All the listed assets for an entry module need to be loaded for that module to function correctly.

An example manifest [can be seen here](#example-manifest).

### The core app module

The core app module contains styles and scripts, such as the Bootstrap reboot styles, that are needed globally by all components. Global JS should be imported directly in `src/main.js` while any global styles should be imported into `src/styles/core.styles.scss`, which itself is imported into `src/main.js`.

### Using the modules

1. Load the core app module assets
2. Load the component and store module assets
3. Register any components
4. Initialise the Vue app
5. Use the components in the HTML or the stores in scripts

Each entry module is exposed by webpack as a UMD module that will be accessible once all the assets (as listed in the `manifest.json`) are loaded. The module's exports can then be accessed globally using the module's name.

The core app module assets should be included before any component and store assets to ensure that any global scripts have been loaded. It has 2 named exports `window.VsApp.Vue` and `window.VsApp.initApp`. The `Vue` named export is the Vue library that's included in the package. It is the [Runtime and Compiler version](https://vuejs.org/v2/guide/installation.html#Runtime-Compiler-vs-Runtime-only) of Vue so it can initialise an app from an arbitrary app template. The `initApp` export can be used to initialise the Vue app and accepts an options argument that will be passed to the `new Vue()` call with some default options added. 

Load the assets for component and store modules after the core app assets. The component and stores modules should be default exports so should be accessible like `window.{moduleName}.default`, respectively. Register the components

### Library usage example

<a name="example-manifest"></a>Given a `manifest.json` that looks like this:

```json
{
  "VsItineraryStop": {
    "scripts": [
      "scripts/runtime.js",
      "scripts/vendors~VsAccordion~VsItineraryStop~~d5a3aa75.js",
      "scripts/VsItineraryStop.js"
    ],
    "styles": [
      "styles/VsContentSection~VsExample~VsHeading~VsItineraryDay~VsItineraryStop~VsListicleItem.css",
      "styles/VsItineraryStop.css"
    ]
  },
  "VsStoreItinerariesStore": {
    "scripts": [
      "scripts/runtime.js",
      "scripts/vendors~VsDrawer~VsStoreItinerariesStore~afa580f0.js",
      "scripts/VsItineraryMap~VsItineraryMapMarker~VsStoreItinerariesStore.js",
      "scripts/VsStoreItinerariesStore.js"
    ]
  },
  "VsApp": {
    "scripts": [
      "scripts/runtime.js",
      "scripts/vendors~VsApp~VsButtonSquareSocial~VsDesktopN~f5f162b1.js",
      "scripts/VsApp.js"
    ],
    "styles": [
      "styles/VsApp.css"
    ]
  },
  //...
}
```

<a name="example-downstream-usage"></a>The ItineraryStop component and Itineraries store can then be used like this:

```html
<!-- Include the style assets -->
<head>
  <link rel="stylesheet" href="{publicPath}/styles/VsContentSection~VsExample~VsHeading~VsItineraryDay~VsItineraryStop~VsListicleItem.css" type="text/css">
  <link rel="stylesheet" href="{publicPath}/styles/VsItineraryStop.css" type="text/css">
  <link rel="stylesheet" href="{publicPath}/styles/VsApp.css" type="text/css">
</head>
<body>
  
  <!-- Include a mount point and no-js class -->
  <div data-vs-vue-app class="no-js">
    <!-- Use the ItineraryStop component -->
    <vs-itinerary-stop :stop-number="1" stop-title="Edinburgh"></vs-itinerary-stop>
  </div>

  <!-- Include the core app module script assets -->
  <script src="{publicPath}/scripts/runtime.js" type="text/javascript"></script>
  <script src="{publicPath}/vendors~VsApp~VsButtonSquareSocial~VsDesktopN~f5f162b1.js" type="text/javascript"></script>
  <script src="{publicPath}/VsApp.js" type="text/javascript"></script>

  <script type="text/javascript">    

    // Globalise the Vue library (optional)
    Vue = VsApp.Vue

  </script>

  <!-- Include the script assets for the VsItineraryStop and VsStoreItinerariesStore modules -->
  <script src="{publicPath}/scripts/vendors~VsAccordion~VsItineraryStop~~d5a3aa75.js" type="text/javascript"></script>
  <script src="{publicPath}/scripts/VsItineraryStop.js" type="text/javascript"></script>

  <script src="{publicPath}/scripts/vendors~VsDrawer~VsStoreItinerariesStore~afa580f0.js" type="text/javascript"></script>
  <script src="{publicPath}/scripts/VsItineraryMap~VsItineraryMapMarker~VsStoreItinerariesStore.js" type="text/javascript"></script>
  <script src="{publicPath}/scripts/VsStoreItinerariesStore.js" type="text/javascript"></script>

  <script type="text/javascript">
    
    // Register the component
    VsApp.Vue.component("vs-itinerary", VsItinerary.default)
    
    // Initialise the Vue app, passing the mount point selector on the "el" option
    VsApp.initApp({
      el: "[data-vs-vue-app]"
    })
  
    // Use the store
    VsStoreItinerariesStore.default.subscribe(function(mutation, state) {
        console.log("The new state is...")
        console.log(state)
    })

    VsStoreItinerariesStore.default.dispatch("changeStop", { stop: 8 })
      .then(function() {
        console.log("Action complete")
      })

	</script>
</body>
```

## VueX

To include VueX functionality in one or more components:

- [Write a VueX store](#vuex-store-authoring)
- [Import the store into the Vue SFC file and reference it directly in the code](#vuex-store-component-usage-example) (do not add it to the `store` key of the component).
- _To make the store functionality work in the production (system components) build_ - VueX must be available globally before the components are registered with Vue.

Multiple components can use the same store and the store's state will be shared among them. The store can also be accessed directly independently of a component, and the common state will be shared in all contexts.

<a name="vuex-store-authoring"></a>### Writing VueX Stores

Define each store in its own file and export as the default export. This ensures the store is exposed properly by the webpack builds.

Each store file must be given a file name ending in `store.js`otherwise it won't be recognised as a Vuex store.

```js
import Vuex from "vuex"
import Vue from "vue"

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    beans: {
      namespaced: true,
      state: {
        beans: [],
      },
      getters: {
        getBeanCount(state) {
          return state.beans.length
        },
        isBeanAdded: state => beanId => {
          return includes(state.beans, { id: beanId })
        },
      },
      mutations: {
        addBean(state, newBean) {
          state.beans.push(newBean)
        },
      },
      actions: {
        addBean({ context }, newBean) {
          context.commit("addBean", newBean)
        },
      },
    },
  },
})
```

<a name="vuex-store-component-usage-example"></a>### Accessing VueX stores in components

Example use inside components:

```js
import beanStore from "./bean-counter.store.js"

export default {
  template: `
    <div>
      <span>{{ totalBeans }}</span>
      <vs-button @click="addBean">Add new bean</vs-button>
    </div>
  `,
  computed: {
    totalBeans() {
      return beanStore.getters["beans/getBeanCount"]
    },
    methods: {
      addBean(arg) {
        return beanStore.dispatch("beans/addBean", this.newBean)
      },
    },
  },
}
```

## Component usage examples

Examples of component usage can be included in the SFC in a `docs` block. The `docs` block examples are recognised by the `vue-styleguidist` build and added as component examples to the generated design system site. [The `vue-styleguidist` documentation](https://vue-styleguidist.github.io/docs/Documenting.html#usage-examples-and-readme-files) has examples of how to do this.

If `docs` examples get very large, [they can be moved to an external file and imported instead](https://vue-styleguidist.github.io/docs/Documenting.html#importing-examples).

### Addition of fixture data/assets for documentation examples

To make fixtures data available for easy inclusion in `docs` block examples, create `.json` files in `src/assets/fixtures/`, in subfolders if desired. The documentation site build automatically exposes the contained data in a key relative to the camel-cased folder and file names. For example:

`/src/assets/fixtures/header/main-nav.json`

```json

[
  {
    title: "home",
    href: "/"
  },
  ...
]
```

`/src/components/your/Component.vue`

```html
<docs>
  <main-nav>
    <nav-item v-href="header.mainNav[0].href">
      {{ header.mainNav[0].title }}
    </nav-item>
  </main-nav>
</docs>
```

## Server-side rendering

This branch includes an SSR sub-package that can be used to spin up a NodeJS/Express app that performs SSR on a target site. When a client request comes into the app, a proxy request to the target site is made at the same path. The page HTML that is returned is processed by Vue SSR and returned to the client.

### Get started

To install and start the local Hippo and NodeJS SSR instances:

```sh
# Install and run the parent Hippo site (this also installs and builds the frontend assets in this package required for SSR)
mvn clean verify && mvn -Pcargo.run

# Add the relevant environment variables to the `frontend/.env` file, creating if necessary
touch frontend/.env

# Start the SSR app on http://localhost:8082
cd frontend
yarn ssr:serve
```

This will start:
- the Hippo instance at [http://localhost:8080](http://localhost:8080)
- the SSR app at [http://localhost:8082](http://localhost:8082)

The Hippo instance is basically unchanged from the existing Hippo instance. To browse the SSR'ed Hippo site through the SSR app, browse to the same URL except at the SSR app host.

For example, to view the SSR version of the Hippo page [http://localhost:8080/site/see-do/victorian-heritage-trail](http://localhost:8080/site/see-do/victorian-heritage-trail), browse to [http://localhost:8082/site/see-do/victorian-heritage-trail](http://localhost:8082/site/see-do/victorian-heritage-trail).

The site is browsable just like the regular Hippo site. Note that it's not possible to access the CMS pages via the SSR site.

**NOTE: do not use the ssr:serve:dev npm script. It is broken and will be fixed soon** 

### SSR environment variable

The NodeJS app requires an environment variable called `VS_SSR_PROXY_TARGET_HOST`, which is the host address of the target site. If the target is the local Hippo site the value should be `http://localhost:8080`. The easiest way to provide this is to add the following line to the `frontend/.env` file:

```sh
VS_SSR_PROXY_TARGET_HOST = http://localhost:8080
```


## Styling

The package's core styles and Sass members are located in the `src/styles/` directory. All global Sass members are imported into `src/styles/resources.scss` and are available for use in any Vue component without explicit import - these are resolved during the builds using [Sass resources loader](https://github.com/shakacode/sass-resources-loader).

Any styles intended to be applied gloabally should be imported into `src/styles/core.styles.scss` - this Sass file is included in the core App module and will manifest as global styles in the library and design system builds.

### Bootstrap / Bootstrap Vue

The package uses Bootstrap 4 styles as its base styles. Bootstrap members are made available in all Vue components along with all the other Sass members. Bootstrap core styles (reboot and utilities) are included in `src/styles/core.styles.scss` so are included in the core app module so they are applied globally.

The Bootstrap variable default values are overriden in `src/styles/_bootstrap-overrides.scss`.

### Tokens

### Targetting styles for users with JS enabled

A No-js class is defined for targetting styles at users who have Javascript disabled. To apply this no-js class, use the `no-js` Sass mixin to wrap the styles that need to be targetted.

```scss
@include no-js {
  .thing-to-be-hidden-for-no-js-users {
    display: none;
  }
}
```

The class itself is defined as the `no_javascript` token in `src/tokens/utility.yaml`, which is converted into a Sass variable with the name `$no-javascript`. The class itself should not be used anywhere as a static string - all use should be achieved via the Sass mixin or the Sass variable if the mixin can't be used for some reason.

For the no-js styles to take effect, the class must be placed on the Vue app mount point or a parent of it. Nothing else needs to be done other than initialising the Vue app with the core App module's `initApp` function, which should be being done already. On initial page load, the no-js class is present and any styles wrapped in the `no-js` mixin will take effect. Just after the Vue app is initialised in the core App module, a JS function is called automatically that removes the class from any elements that have it, and so the no-js styles no longer take effect.

## Tests

## Linting

## Logging

This package includes the `vuejs-logger` NPM package, which provides logging capabilities. To use it import the `src/utilities/logger.js` utility and use it as follows.

```js
import logger from "@/utils/logger"

logger.debug("test", this.a, 123)
logger.info("test", this.b)
logger.warn("test")
logger.error("test")
logger.fatal("test")
```

Check the [`vuejs-logger` documentation](https://www.npmjs.com/package/vuejs-logger) for further examples and options. Note that the syntax used in the examples shown here differs from the syntax used in the documentation. Use the syntax shown here to make it more obvious you're using an import.

To change the `vuejs-logger` configuration alter the options object passed to it in `src/utilities/logger.js`.

## TODO

- Alter docs to allow inclusion of dynamic full width view in examples
- Add info to readme (see below)
- Refactor design system sidebar nav to work more smoothly
- Move theo processing inside webpack builds
- Add automatic component registration to bundled Vue library so consumers don't have to do that manually
- Refactor breakpoints.bootstrap-vue.config.js breakpoints definition to generate dynamically from tokens
- Refactor theme colours so they are auto generated and registered in bootstrap
- Refactor button and other components to use autogenerated theme colours

### To Add to readme

- SSR
- Linting
- Tests
- Tokens
- BootstrapVue
- Breakpoints
- Icons
- Colours and docs colours
