module.exports = {
    chainWebpack(config) {
        config.resolve.symlinks(false)
        config.resolve.alias.set( 'vue', path.resolve('./node_modules/vue'))    
    },
}