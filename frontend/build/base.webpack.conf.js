const path = require('path');

const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const { VueLoaderPlugin } = require('vue-loader');

const buildMode = require('./base.build-mode');

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
        publicPath:
      buildMode === 'development' ? '/' : '../',
    },
    resolve: {
        extensions: ['.js', '.vue', '.json'],
        alias: {
            vue$: 'vue/dist/vue.esm.js',
            'bootstrap-vue$': 'bootstrap-vue/src/index.js',
            '@': resolve('src'),
            '@components': resolve('src/components'),
            '@docs': resolve('docs'),
            '@cypress': resolve('cypress'),
        },
    },
    module: {
        rules: [
            {
                enforce: 'pre',
                test: /src.*\.(js|vue)$/,
                exclude: ['/ssr/', '/src/components/patterns/header/components/Chart/'],
                loader: 'eslint-loader',
                options: {
                    failOnError: true,
                    failOnWarning: false,
                },
            },
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
                test: /\.svg$/,
                use: [
                    'html-loader',
                    {
                        loader: 'image-webpack-loader',
                        options: {
                            svgo: {
                                plugins: [
                                    {
                                        removeViewBox: false,
                                    },
                                ],
                            },
                        },
                    },
                ],
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
        new MiniCssExtractPlugin('style.css'),
    ],
    node: {
    // prevent webpack from injecting useless setImmediate polyfill because Vue
    // source contains it (although only uses it if it's native).
        setImmediate: false,
    },
};
