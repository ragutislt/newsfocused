
describe('login to the admin app', () => {
    beforeEach(() => {
        cy.visit('http://localhost:3000')
    })

    it('displays username input', () => {
        cy.get('#username-input').then((input) => {
            cy.get(input).should('have.length', 1)
            cy.get(input).should('not.be.disabled')
        })
    })

    it('displays password input', () => {
        cy.get('#password-input').then((input) => {
            cy.get(input).should('have.length', 1)
            cy.get(input).should('not.be.disabled')
        })
    })

    it('disables login button if credentials are empty', () => {
        cy.get('#login-button').then((button) => {
            cy.get(button).should('have.length', 1)
            cy.get(button).should('be.disabled')
        })
    })

    it('enables login button if credentials are not empty', () => {
        cy.get('#username-input').type('username')
        cy.get('#password-input').type('password')
        cy.get('#login-button').should('not.be.disabled')
    })

    it('calls login endpoint upon login button click', () => {
        cy.get('#username-input').type('username')
        cy.get('#password-input').type('password')

        cy.intercept({
            method: 'POST',
            url: '**/api/login'
        }, {
            statusCode: 200
        }).as('postLogin')

        cy.get('#login-button').click()

        cy.wait('@postLogin').should(({ request }) => {
            expect(request.body).property('username').to.equal('username');
            expect(request.body).property('password').to.equal('password');
        })

        cy.get('#username-input').should('have.length', 0)
    })
})
