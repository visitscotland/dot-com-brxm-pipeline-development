const path = require("path")

module.exports = {
    rootDir: path.resolve(__dirname, "../../"),
    roots: ["<rootDir>/src"],
    testMatch: ["**/?(*.)jest.spec.js?(x)"],
    modulePaths: ["<rootDir>"],
    moduleFileExtensions: ["js", "json", "vue"],
    moduleNameMapper: {
        "^@/(.*)$": "<rootDir>/src/$1",
        "^@components/(.*)$": "<rootDir>/src/components/$1",
        "^@test/(.*)$": "<rootDir>/test/$1",
        "\\.(jpg|jpeg|png|gif|eot|otf|webp|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$":
            "<rootDir>/test/unit/mocks/fileMock.js",
        "\\.svg$": "<rootDir>/test/unit/mocks/svgMock.js",
    },
    transform: {
        "^.+\\.js$": "<rootDir>/node_modules/babel-jest",
        ".*\\.(vue)$": "<rootDir>/node_modules/vue-jest",
    },
    transformIgnorePatterns: ["<rootDir>/node_modules/(?!(bootstrap-vue)/)"],
    snapshotSerializers: ["<rootDir>/node_modules/jest-serializer-vue"],
    setupFiles: ["<rootDir>/test/unit/setup"],
    coverageDirectory: "<rootDir>/test/unit/coverage",
    collectCoverageFrom: [
        "<rootDir>/src/**/*.{js,vue}",
        "!<rootDir>/cypress/**/*.{js,vue}",
        "!<rootDir>/**/*.spec.js",
        "!<rootDir>/src/main.js",
        "!<rootDir>/node_modules/**",
        "!<rootDir>/src/system.js",
        "!<rootDir>/src/system-components.js",
        "!<rootDir>/docs/**/*",
    ],
}
