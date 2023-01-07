describe('User page is rendered with fake data', () => {
    const TEST_EMAIL = "some@email333.com";

    beforeEach(() => {
        cy.intercept({
            method: 'GET',
            pathname: ' /admin/api/user/search'
        }, {
            statusCode: 200,
            body: {
                "usersFound": [], "pageNumber": 1, "totalCount": 0
            }
        }).as('getSearch')

        // This will mock a login
        cy.fakeLogin();
        cy.wait('@getSearch')
    })

    it('loads fake users data', () => {
        cy.intercept({
            method: 'GET',
            pathname: `/admin/api/user/${encodeURIComponent(TEST_EMAIL)}`
        }, {
            statusCode: 200,
            body: {
                "email": TEST_EMAIL, "registrationDate": 1667675612463, "emailsSent": [], "preferences": { "headlineCount": 121, "sites": ["www.fox.com"], "daysToSendOn": ["Sunday"] }
            }
        }).as('getUser')

        cy.visit(`http://localhost:3000/users/${encodeURIComponent(TEST_EMAIL)}`)

        //cy.wait('@getUser')

        cy.get('[data-test-id=email]').should('have.text', TEST_EMAIL)
        cy.get('[data-test-id=registration_date]').should('have.text', 1667675612463)
        cy.get('[data-test-id=headline_count]').should('have.text', 121)
    })
});