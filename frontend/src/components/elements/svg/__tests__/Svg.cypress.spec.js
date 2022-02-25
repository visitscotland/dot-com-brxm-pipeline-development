const { vueHelper } = require('@cypress/helpers/index').default;
const VsSvg = require('../Svg').default;

describe('Svg component', () => {
    const props = {
        path: 'logo',
        height: null,
        width: null,
        fill: null,
    };

    vueHelper.init('vs-svg', VsSvg, {
        props,
    });

    it('renders the logo svg', () => {
        cy.get('svg').should('have.attr', 'xmlns', 'http://www.w3.org/2000/svg');
        // for some reason cypress cannot detect this viewBox attribute
        // .should("have.attr", "viewBox", "80 0 480 100")

        cy.get('svg>g')
            .first()
            .should('have.class', 'vs-logo vs-logo--scotland')
            .next()
            .should('have.class', 'vs-logo-visit')
            .next()
            .should('have.class', 'vs-logo-alba');

        cy.get('svg>rect').should('have.attr', 'x', '373.6');
    });

    it('renders the svg at specific height and width', () => {
        vueHelper.setProp('height', 100);
        vueHelper.setProp('width', 400);

        cy.get('svg').should('have.css', 'height', '100px');
        cy.get('svg').should('have.css', 'width', '400px');
    });

    it('renders the svg with a specific fill colour', () => {
        const colour = 'rgba(100, 170, 25, 0.5)';
        vueHelper.setProp('fill', colour);

        cy.get('svg').should('have.css', 'fill', colour);
    });
});
