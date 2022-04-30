
describe('login to the admin app', () => {
    beforeEach(() => {
        cy.visit('https://example.cypress.io/todo')
    })

    it('displays username input', () => {
        cy.get('#username').then(($input) => {
            cy.get($input).should('have.length', 1)
            cy.get($input).should('not.be.disabled')
        })
    })
})
