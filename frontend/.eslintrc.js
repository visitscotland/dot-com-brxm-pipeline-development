module.exports = {
    root: true,
    env: {
        // this section will be used to determine which APIs are available to us
        // (i.e are we running in a browser environment or a node.js env)
        node: true,
        browser: true,
        'jest/globals': true,
        'cypress/globals': true,
    },
    parserOptions: {
        parser: '@babel/eslint-parser',
        // specifying a module sourcetype prevent eslint from marking import statements as errors
        sourceType: 'module',
    },
    extends: [
        // use the recommended rule set for both plain javascript and vue
        'eslint:recommended',
        'airbnb-base',
        'plugin:vue/recommended',
        'plugin:vue/strongly-recommended',
        'plugin:jest/recommended',
        'plugin:cypress/recommended',
    ],
    plugins: ['jest', 'cypress'],
    rules: {
        indent: [
            'warn',
            4,
            {
                ignoredNodes: ['TemplateLiteral'],
            },
        ],
        'template-curly-spacing': 'off',
        'vue/attributes-order': 'off',
        'vue/no-unused-vars': 'error',
        'vue/order-in-components': 'error',
        'vue/this-in-template': 'error',
        'vue/component-name-in-template-casing': [
            'error',
            'PascalCase',
            {
                registeredComponentsOnly: false,
            },
        ],
        'vue/script-indent': ['error', 4],
        'vue/html-indent': ['error', 4],
        quotes: ['error', 'single'],
        'no-extra-semi': 'off',
        semi: ['error', 'always'],
        'semi-style': ['error', 'last'],
        'object-curly-newline': [
            'error',
            {
                ObjectExpression: 'always',
                ObjectPattern: { multiline: true },
                ImportDeclaration: { multiline: true, minProperties: 3 },
                ExportDeclaration: { multiline: true, minProperties: 3 },
            },
        ],
        'object-property-newline': [
            'error',
            {
                allowAllPropertiesOnSameLine: false,
            },
        ],
        'space-before-function-paren': ['error', 'never'],
        'comma-dangle': ['error', 'always-multiline'],
        'arrow-parens': ['error', 'always'],
        'no-plusplus': ['error', { allowForLoopAfterthoughts: true }],
        'vue/html-self-closing': [
            'error',
            {
                html: {
                    void: 'never',
                    normal: 'always',
                },
            },
        ],
        'vue/singleline-html-element-content-newline': [
            'error',
            {
                ignoreWhenNoAttributes: true,
                ignoreWhenEmpty: true,
            },
        ],
        'vue/no-v-html': 2,
        'import/extensions': [
            'error',
            'ignorePackages',
            {
                vue: 'never',
                js: 'never',
            },
        ],
        'no-debugger': 'warn',
        'no-underscore-dangle': 'off',
    },
    settings: {
        'import/resolver': {
            node: {},
            webpack: {
                config: './build/base.webpack.conf.js',
            },
            'import/extensions': ['.js', '.vue'],
        },
    },
    overrides: [
        // Modify rules for build scripts
        {
            files: ['./src/system.js', '{build,config,test}/**/*'],
            rules: {
                // Disable no-console rule for build scripts
                'no-console': 0,

                // Allow devDeps to be used without error
                'import/no-extraneous-dependencies': [
                    'error',
                    {
                        devDependencies: true,
                    },
                ],
            },
        },
        // Ignore jest rules for cypress tests
        {
            files: ['*.cypress.spec.js'],
            rules: {
                'jest/expect-expect': 'off',
                'jest/valid-expect': 'off',
                'jest/valid-expect-in-promise': 'off',
            },
        },
        // Various rules for uncompiled Node SSR app files
        {
            files: ['./ssr/*'],
            rules: {
                semi: ['error', 'always'],
                'no-console': 'off',
            },
        },
    ],
};
