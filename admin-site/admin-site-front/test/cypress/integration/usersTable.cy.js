
describe('User table page is rendered with fake data', () => {
    const TEST_EMAIL = "some@email333.com";

    beforeEach(() => {
        cy.intercept({
            method: 'GET',
            pathname: ' /admin/api/user/search'
        }, {
            statusCode: 200,
            body: {
                "usersFound": [{ "email": TEST_EMAIL, "registrationDate": 1667675612463, "emailsSent": [], "preferences": { "headlineCount": 121, "sites": ["www.fox.com"], "daysToSendOn": ["Sunday"] } }], "pageNumber": 1, "totalCount": 1
            }
        }).as('getSearch')

        // This will mock a login
        cy.fakeLogin();
        cy.wait('@getSearch')
    })

    it('displays users table', () => {
        cy.get('[data-test-id=user_table]').then((table) => {
            cy.get(table).should('be.visible')
        });
    })

    it('displays a search bar', () => {
        cy.get('[data-test-id=user_search_input]').then((searchBar) => {
            cy.get(searchBar).should('be.visible')
        })
    })

    it('searches users when entering some search term', () => {
        cy.get('[data-test-id=user_search_input]').type("email333");
        cy.wait('@getSearch')
    })

    it('displays empty table when no search results are found', () => {
        cy.intercept({
            method: 'GET',
            pathname: ' /admin/api/user/search'
        }, {
            statusCode: 200,
            body: {
                "usersFound": [], "pageNumber": 1, "totalCount": 0
            }
        }).as('getSearchEmpty')

        cy.get('[data-test-id=user_search_input]').type("N");
        cy.wait('@getSearchEmpty')
    })

    it('opens user details', () => {
        cy.get('[data-test-id=user_details_button]').click();
        cy.location().should((loc) => {
            expect(loc.hash).to.eq(`#users/${encodeURIComponent(TEST_EMAIL)}`);
        });
    })
})

describe('User table page is rendered with real data', () => {
    beforeEach(() => {
        cy.intercept({
            method: 'GET',
            pathname: ' /admin/api/user/search'
        }).as('getSearch')

        cy.realLogin();
        cy.wait('@getSearch')
    })

    it('displays users table', () => {
        cy.get('[data-test-id=user_table]').then((table) => {
            cy.get(table).should('be.visible')
        });

        cy.get('[data-test-id=user_table_body]').find('tr').then((rows) => {
            cy.get(rows).should('have.length.of.at.least', 1);
        });
    });
})