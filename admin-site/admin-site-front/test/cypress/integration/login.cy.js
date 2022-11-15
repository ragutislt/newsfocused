
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

    it('does not render anymore login page on successful login', () => {
        cy.get('#username-input').type('username')
        cy.get('#password-input').type('password')

        cy.intercept({ // TODO - do not stub this call as we need to e2e-test the actual login
            method: 'POST',
            url: 'admin/api/auth'
        }, {
            statusCode: 200
        }).as('postLogin')

        cy.get('#login-button').click()

        cy.wait('@postLogin').should(({ request }) => {
            console.log(request.headers['authorization']);
            const authHeader = window.atob(request.headers['authorization'].replace("Basic ","")).split(":");
            expect(authHeader[0]).to.equal('username');
            expect(authHeader[1]).to.equal('password');
        })

        cy.get('#username-input').should('not.exist')
        cy.get('#password-input').should('not.exist')
    })

    it('displays error message on failed login', () => {
        cy.get('#username-input').type('username')
        cy.get('#password-input').type('password')

        cy.intercept({
            method: 'POST',
            url: 'admin/api/auth'
        }, {
            statusCode: 401
        }).as('postLogin')

        cy.get('#login-button').click()

        cy.wait('@postLogin')

        cy.get('#login-error').should('have.length', 1)
    })
})
