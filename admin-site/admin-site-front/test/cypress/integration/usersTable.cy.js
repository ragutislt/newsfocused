
describe('See the user table page', () => {
    beforeEach(() => {
        cy.visit('http://localhost:3000')
        cy.login();
    })

    it('displays users table', () => {
        cy.get('#userTable').then((table) => {
            cy.get(table).should('be.visible')
        })
    })
})
