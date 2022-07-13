const path = require('path');

const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const { VueLoaderPlugin } = require('vue-loader');
const ESLintPlugin = require('eslint-webpack-plugin');

const buildMode = require('./base.build-mode');

const myESLintOptions = {
    extensions: ['js', 'jsx', 'ts'],
    exclude: ['node_modules', '/ssr/', '/src/components/patterns/header/components/Chart/'],
};

function resolve(dir) {
    return path.join(__dirname, '..', dir);
}

module.exports = {
    mode: buildMode,
    context: path.resolve(__dirname, '../'),
    entry: {
        app: './src/main.js',
    },
    devtool: process.env.NODE_ENV === 'development' ? 'source-map' : '',
    output: {
        path: path.resolve(__dirname, '../dist/base'),
        filename: '[name].js',
        publicPath: buildMode === 'development' ? '/' : '../',
    },
    resolve: {
        extensions: ['.js', '.vue', '.json'],
        alias: {
            vue$: 'vue/dist/vue.esm.js',
            'bootstrap-vue$': 'bootstrap-vue/src/index.js',
            '@': resolve('src'),
            '@components': resolve('src/components'),
            '@docs': resolve('docs'),
            '@images': resolve('docs/images'),
            '@cypress': resolve('cypress'),
        },
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                loader: 'vue-loader',
                options: {
                    cacheBusting: true,
                    transformAssetUrls: {
                        video: ['src', 'poster'],
                        source: 'src',
                        img: 'src',
                        image: 'xlink:href',
                    },
                },
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                include: [
                    resolve('docs'),
                    resolve('src'),
                    resolve('test'),
                    resolve('node_modules/webpack-dev-server/client'),
                ],
            },
            {
                test: /\.(png|jpe?g|gif)(\?.*)?$/,
                loader: 'file-loader',
                options: {
                    name: 'img/[name].[hash:7].[ext]',
                },
            },
            {
                test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
                loader: 'file-loader',
                options: {
                    name: 'media/[name].[hash:7].[ext]',
                },
            },
            {
                test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
                loader: 'file-loader',
                options: {
                    name: 'fonts/[name].[ext]',
                },
            },
            {
                test: /\.svg$/,
                oneOf: [
                    {
                        resourceQuery: /optimise/,
                        use: [
                            'file-loader',
                            {
                                loader: 'image-webpack-loader',
                                options: {
                                    svgo: {
                                        plugins: [
                                            {
                                                name: 'preset-default',
                                                params: {
                                                    overrides: {
                                                        // customize default plugin options
                                                        inlineStyles: {
                                                            onlyMatchedOnce: false,
                                                        },

                                                        // or disable plugins
                                                        removeDoctype: false,
                                                    },
                                                },
                                            },
                                        ],
                                    },
                                },
                            },
                        ],
                    },
                    {
                        options: {
                            name: 'img/[name].[hash:7].[ext]',
                        },
                        loader: 'file-loader',
                    },
                ],
            },
        ],
    },
    performance: {
        maxEntrypointSize: 750000,
    },
    stats: {
        entrypoints: false,
        children: false,
    },
    plugins: [
        new VueLoaderPlugin(),
        new MiniCssExtractPlugin({
            filename: 'style.css',
        }),
        new ESLintPlugin(myESLintOptions),
    ],
};
