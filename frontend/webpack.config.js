const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    entry: path.join(__dirname, './src/index.tsx'),
    output: {
        path: path.join(__dirname, 'dist'),
        filename: "bundle.js",
    },
    devServer: {
        host: 'localhost',
        port: 3000,
        hot: true,
        historyApiFallback: true,
    },
    resolve: {
        extensions: ['.js', '.jsx', '.ts', '.tsx'],
    },
    module: {
        rules: [
            {
                test: /\.(ts|tsx)$/,
                use: ['babel-loader'],
                exclude: /node_modules/,
            },
            {
                test: /\.(png|jpe?g|gif|svg)$/,
                type: "asset/resource"
            },
        ],
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: './public/index.html',
        }),
    ],
};
