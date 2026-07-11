// Skiko's JS-target renderer loads skiko.wasm via skiko.mjs (ES module with
// top-level await). Webpack 5 needs these two experiments enabled to handle it.
// WasmJS already sets these in its generated config; this override ensures the
// regular JS target gets them too.
config.experiments = Object.assign({}, config.experiments, {
    asyncWebAssembly: true,
    topLevelAwait: true,
});
