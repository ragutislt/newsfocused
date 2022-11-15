const { defineConfig } = require('cypress');

module.exports = defineConfig({
  "fileServerFolder": "./test/cypress",
  "fixturesFolder": "./test/cypress/fixtures",
  "screenshotsFolder": "./test/cypress/screenshots",
  "videosFolder": "./test/cypress/videos",
  "downloadsFolder": "./test/cypress/downloads",
  e2e: {
    // We've imported your old cypress plugins here.
    // You may want to clean this up later by importing these.
    setupNodeEvents(on, config) {
      return require('./test/cypress/plugins/index.js')(on, config);
    },
    "specPattern": "./test/cypress/integration/**/*.cy.{js,jsx,ts,tsx}",
    "supportFile": "./test/cypress/support/index.js"
  }
});