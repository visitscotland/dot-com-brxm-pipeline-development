/* eslint-disable no-underscore-dangle */
/* eslint-disable import/extensions */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/no-import-module-exports */
import path from 'path';
import { fileURLToPath } from 'url';

import MiniCssExtractPlugin from 'mini-css-extract-plugin';
import { VueLoaderPlugin } from 'vue-loader';
import pkg from 'eslint-webpack-plugin';
import buildMode from './base.build-mode.js';

const ESLintPlugin = pkg;
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

function resolve(dir) {
    return path.join(__dirname, '..', dir);
}

const baseWebpackConfig = {
    mode: buildMode,
    context: path.resolve(__dirname, '../'),
    entry: {
        app: './src/main.js',
    },
    devtool: process.env.NODE_ENV === 'development' ? 'source-map' : false,
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, '../dist/base'),
        publicPath:
            buildMode === 'development' ? '/' : '../'
    },
    resolve: {
        extensions: ['.cjs','.js', '.vue', '.json', '.scss', '.css'],
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
            // {
            //     enforce: 'pre',
            //     test: /src.*\.(js|vue)$/,
            //     exclude: ['/ssr/', '/src/components/patterns/header/components/Chart/'],
            //     loader: 'eslint-loader',
            //     options: {
            //         fix: true,
            //         emitError: true,
            //         emitWarning: true,
            //     },
            // },
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
                test: /\.scss$/,
                use: [
                    'vue-style-loader',
                    'css-loader',
                    'sass-loader'
                ]
            },
            {
                test: /\.(png|jpe?g|gif)(\?.*)?$/,
                type: 'asset',
                generator: {
                    filename: 'img/[name].[contenthash:7].[ext]',
                },
            },
            // media
            {
                test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
                type: 'asset',
                generator: { filename: 'media/[contenthash:7][ext][query]' },
            },
            // fonts
            {
                test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/i,
                type: 'asset',
                generator: { filename: 'fonts/[contenthash:7][ext][query]' },
            },
            {
                test: /\.svg$/,
                oneOf: [
                    {
                        resourceQuery: /optimise/,
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
                                            {
                                                inlineStyles: {
                                                    onlyMatchedOnce: false,
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
            filename: '[name].css',
            chunkFilename: '[id].css',
        }),
        new ESLintPlugin(),
    ],
    node: false,
};

export default baseWebpackConfig;
