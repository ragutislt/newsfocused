
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

    it('displays a search bar', () => {
        cy.get('#searchBar').then((searchBar) => {
            cy.get(searchBar).should('be.visible')
        })
    })

    it('gets all users at first load', () => {

    })

    it('searches users', () => {

    })

    it('opens user details', () => {

    })
})
