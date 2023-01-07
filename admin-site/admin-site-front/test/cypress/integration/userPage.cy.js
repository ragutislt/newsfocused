describe('User page is rendered with fake data', () => {
    const TEST_EMAIL = "some@email333.com";
    const TEST_REGISTRATION_DATE = 1667675612463;
    const TEST_HEADLINE_COUNT = 121;
    const TEST_SITES = ["www.fox.com","www.bbc.com"];
    const TEST_DAYS_TO_SEND_ON = ["Sunday", "Tuesday", "Friday"];

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

    it('renders fake users data', () => {
        cy.intercept({
            method: 'GET',
            pathname: `/admin/api/user/${encodeURIComponent(TEST_EMAIL)}`
        }, {
            statusCode: 200,
            body: {
                "email": TEST_EMAIL, "registrationDate": TEST_REGISTRATION_DATE, "emailsSent": [], "preferences": { "headlineCount": TEST_HEADLINE_COUNT, "sites": TEST_SITES, "daysToSendOn": TEST_DAYS_TO_SEND_ON }
            }
        }).as('getUser')

        cy.visit(`http://localhost:3000/users/${encodeURIComponent(TEST_EMAIL)}`)

        cy.get('[data-test-id=email_title]').should('have.text', TEST_EMAIL)
        cy.get('[data-test-id=email]').should('have.text', TEST_EMAIL)
        cy.get('[data-test-id=registration_date]').should('have.text', new Date(TEST_REGISTRATION_DATE).toUTCString())
        cy.get('[data-test-id=headline_count]').should('have.text', TEST_HEADLINE_COUNT)
        cy.get('[data-test-id=days_to_send_on]').should('have.text', TEST_DAYS_TO_SEND_ON.join(', '))
        cy.get('[data-test-id=sites]').should('have.text', TEST_SITES.join(', '))
    })
});
