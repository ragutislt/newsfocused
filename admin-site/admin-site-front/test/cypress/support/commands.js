// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
Cypress.Commands.add('login', () => {
    cy.get('#username-input').type('username')
    cy.get('#password-input').type('password')

    cy.intercept({
        method: 'POST',
        url: 'admin/api/auth'
    }, {
        statusCode: 200
    }).as('postLogin')

    cy.get('#login-button').click()

    cy.get('#username-input', { timeout: 10000 }).should('not.exist');
});
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })
